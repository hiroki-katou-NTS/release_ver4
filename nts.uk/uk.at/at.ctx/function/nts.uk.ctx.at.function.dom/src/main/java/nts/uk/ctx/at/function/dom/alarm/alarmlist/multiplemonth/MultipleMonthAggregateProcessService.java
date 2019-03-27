package nts.uk.ctx.at.function.dom.alarm.alarmlist.multiplemonth;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.ResponseImprovementAdapter;
import nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth.ActualMultipleMonthAdapter;
import nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth.CheckActualResultMulMonth;
import nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth.MonthlyRecordValueImport;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlAtdItemConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.multimonth.MultiMonthFucAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.ErAlWorkRecordCheckAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.RegulationInfoEmployeeResult;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.ErAlConstant;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.monthly.CompareOperatorText;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.multimonth.MulMonAlarmCond;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.multimonth.doevent.MulMonCheckCondDomainEventDto;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.enums.TypeOfItem;
import nts.uk.ctx.at.function.dom.attendanceitemname.AttendanceItemName;
import nts.uk.ctx.at.function.dom.attendanceitemname.service.AttendanceItemNameDomainService;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
public class MultipleMonthAggregateProcessService {

	@Inject
	private AlarmCheckConditionByCategoryRepository alCheckConByCategoryRepo;

	@Inject
	private ResponseImprovementAdapter responseImprovementAdapter;

	@Inject
	private ActualMultipleMonthAdapter actualMultipleMonthAdapter;

	@Inject
	private MultiMonthFucAdapter multiMonthFucAdapter;

	@Inject
	private AttendanceItemNameDomainService attdItemNameDomainService;
	
	@Inject
	private CheckActualResultMulMonth checkActualResultMulMonth;
	
	@Inject
	private ErAlWorkRecordCheckAdapter erAlWorkRecordCheckAdapter;
	
	@Inject
	private ManagedParallelWithContext parallelManager;
	
	public List<ValueExtractAlarm> multimonthAggregateProcess(String companyID, String checkConditionCode,
			DatePeriod period, List<EmployeeSearchDto> employees) {

		List<String> employeeIds = employees.stream().map(e -> e.getId()).collect(Collectors.toList());

		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>();

		Optional<AlarmCheckConditionByCategory> alCheckConByCategory = alCheckConByCategoryRepo.find(companyID,
				AlarmCategory.MULTIPLE_MONTH.value, checkConditionCode);
		if (!alCheckConByCategory.isPresent()) {
			return Collections.emptyList();
		}
		// list alarmPartem
		MulMonAlarmCond mulMonAlarmCond = (MulMonAlarmCond) alCheckConByCategory.get().getExtractionCondition();
		List<MulMonCheckCondDomainEventDto> listExtra = multiMonthFucAdapter
				.getListMultiMonCondByListEralID(mulMonAlarmCond.getErrorAlarmCondIds());

		// 対象者を絞り込む
		DatePeriod endDatePerior = new DatePeriod(period.end(), period.start());
		GeneralDate tempStart = period.start();
		GeneralDate tempEnd = period.end();
		YearMonth startYearMonth = tempStart.yearMonth();
		YearMonth endYearMonth = tempEnd.yearMonth();
		YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(startYearMonth, endYearMonth);
		List<String> listEmployeeID = responseImprovementAdapter.reduceTargetResponseImprovement(employeeIds,
				endDatePerior, alCheckConByCategory.get().getExtractTargetCondition());

		// 対象者の件数をチェック : 対象者 ≦ 0
		if (listEmployeeID.isEmpty()) {
			return Collections.emptyList();
		}
		List<EmployeeSearchDto> employeesDto = employees.stream().filter(c -> listEmployeeID.contains(c.getId()))
				.collect(Collectors.toList());


			// tab1
			listValueExtractAlarm
					.addAll(this.extraResultMulMon(companyID, listExtra, period, employeesDto, listEmployeeID, yearMonthPeriod));

		return listValueExtractAlarm;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ValueExtractAlarm> multimonthAggregateProcess(String companyID, List<AlarmCheckConditionByCategory> multiMonthErAl,
			DatePeriod period, List<EmployeeSearchDto> employees, Consumer<Integer> counter, Supplier<Boolean> shouldStop) {

		List<ValueExtractAlarm> listValueExtractAlarm = Collections.synchronizedList(new ArrayList<>());
		
		parallelManager.forEach(CollectionUtil.partitionBySize(employees, 100), emps -> {
			List<String> employeeIds = emps.stream().map(e -> e.getId()).collect(Collectors.toList());

			Map<String, List<RegulationInfoEmployeeResult>> listTargetMap = erAlWorkRecordCheckAdapter.filterEmployees(new DatePeriod(period.end(),period.end()), 
																														employeeIds, 
																														multiMonthErAl.stream().map(c -> c.getExtractTargetCondition()).collect(Collectors.toList()));
			
			multiMonthErAl.stream().forEach(eral -> {
				synchronized (this) {
					if(shouldStop.get()){
						return;
					}
				}
				List<RegulationInfoEmployeeResult> targetEmps = listTargetMap.get(eral.getExtractTargetCondition().getId());

				// 対象者の件数をチェック : 対象者 ≦ 0
				if (!targetEmps.isEmpty()) {
					// list alarmPartem
					MulMonAlarmCond mulMonAlarmCond = (MulMonAlarmCond) eral.getExtractionCondition();
					List<MulMonCheckCondDomainEventDto> listExtra = multiMonthFucAdapter
							.getListMultiMonCondByListEralID(mulMonAlarmCond.getErrorAlarmCondIds());
					
					YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(period.start().yearMonth(), period.end().yearMonth());

					// tab1
					listValueExtractAlarm.addAll(this.extraResultMulMon2(companyID, listExtra, period, targetEmps, yearMonthPeriod));
				}
				synchronized (this) {
					counter.accept(targetEmps.size());
				}
			});
		});
		

		return listValueExtractAlarm;
	}

	// tab1
	private List<ValueExtractAlarm> extraResultMulMon2(String companyId, List<MulMonCheckCondDomainEventDto> listExtra,
			DatePeriod period, List<RegulationInfoEmployeeResult> employees, YearMonthPeriod yearMonthPeriod) {

		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>();
		// convert date to String
		GeneralDate tempStart = period.start();
		GeneralDate tempEnd = period.end();
		String periodYearMonth = tempStart.toString(ErAlConstant.YM_FORMAT) + ErAlConstant.PERIOD_SEPERATOR
				+ tempEnd.toString(ErAlConstant.YM_FORMAT);
		// save moths of NumberMonth
		for (MulMonCheckCondDomainEventDto extra : listExtra) {
			if(!extra.isUseAtr())
				continue;
			ErAlAtdItemConAdapterDto erAlAtdItemConAdapterDto = extra.getErAlAtdItem();
			if(erAlAtdItemConAdapterDto == null) continue;
			int typeCheckItem = extra.getTypeCheckItem();
			TypeCheckWorkRecordMultipleMonthImport checkItem = EnumAdaptor.valueOf(typeCheckItem,
					TypeCheckWorkRecordMultipleMonthImport.class);
			List<Integer> tmp = extra.getErAlAtdItem().getCountableAddAtdItems();
			List<Integer> tmp2 = extra.getErAlAtdItem().getCountableSubAtdItems();
			int compare = erAlAtdItemConAdapterDto.getCompareOperator();
			CompareOperatorText compareOperatorText = convertCompareType(compare);
			BigDecimal startValue = erAlAtdItemConAdapterDto.getCompareStartValue();
			BigDecimal endValue = erAlAtdItemConAdapterDto.getCompareEndValue();
			String nameErrorAlarm = "";
			List<Integer> listAttendanceItemIds = new ArrayList<>();
			if (!CollectionUtil.isEmpty(tmp)) {
				List<AttendanceItemName> listAttdName = attdItemNameDomainService.getNameOfAttendanceItem(tmp,
						TypeOfItem.Monthly.value);
				listAttendanceItemIds = listAttdName.stream().map(AttendanceItemName::getAttendanceItemId)
						.collect(Collectors.toList());
					nameErrorAlarm = getNameErrorAlarm(listAttdName,0,nameErrorAlarm);
			}

			if (!CollectionUtil.isEmpty(tmp2)) {
				List<AttendanceItemName> listAttdName = attdItemNameDomainService.getNameOfAttendanceItem(tmp2,
						TypeOfItem.Monthly.value);
				listAttendanceItemIds.addAll(listAttdName.stream().map(AttendanceItemName::getAttendanceItemId)
						.collect(Collectors.toList()));
					nameErrorAlarm = getNameErrorAlarm(listAttdName,1,nameErrorAlarm);
			}
			
			// 月別実績を取得する
			Map<String, List<MonthlyRecordValueImport>> resultActuals = actualMultipleMonthAdapter.getActualMultipleMonth(
																										employees.stream().map(e -> e.getEmployeeId()).collect(Collectors.toList()), 
																										yearMonthPeriod, listAttendanceItemIds);
			if(resultActuals.isEmpty()){
				continue;
			}
			
			String alarmDescription = "";
			for (RegulationInfoEmployeeResult employee : employees) {
				boolean checkAddAlarm = false;
				List<MonthlyRecordValueImport> result = resultActuals.get(employee.getEmployeeId());
				if (CollectionUtil.isEmpty(result)) continue;
				switch (checkItem) {
				
				case TIME:
					if (checkActualResultMulMonth.checkMulMonthCheckCond(period,companyId,employee.getEmployeeId(),result,extra)) {

						checkAddAlarm = true;
						String startValueTime = timeToString(startValue.intValue());
						String endValueTime = "";
						if (compare <= 5) {
							alarmDescription = TextResource.localize("KAL010_254", periodYearMonth, nameErrorAlarm,
									compareOperatorText.getCompareLeft(), startValueTime);
						} else {
							endValueTime = timeToString(endValue.intValue());
							if (compare > 5 && compare <= 7) {
								alarmDescription = TextResource.localize("KAL010_255", periodYearMonth, startValueTime,
										compareOperatorText.getCompareLeft(), nameErrorAlarm,
										compareOperatorText.getCompareright(), endValueTime);
							} else {
								alarmDescription = TextResource.localize("KAL010_256", periodYearMonth, startValueTime,
										compareOperatorText.getCompareLeft(), nameErrorAlarm, nameErrorAlarm,
										compareOperatorText.getCompareright(), endValueTime);
							}
						}
					}
					break;
					
				case TIMES:
				case AMOUNT:
				case DAYS:
					if (checkActualResultMulMonth.checkMulMonthCheckCond(period,companyId,employee.getEmployeeId(),result,extra)) {

						checkAddAlarm = true;
						String startValueTime = String.valueOf(startValue.intValue());
						String endValueTime = "";
						if (compare <= 5) {
							alarmDescription = TextResource.localize("KAL010_254", periodYearMonth, nameErrorAlarm,
									compareOperatorText.getCompareLeft(), startValueTime);
						} else {
							endValueTime = String.valueOf(endValue.intValue());
							if (compare > 5 && compare <= 7) {
								alarmDescription = TextResource.localize("KAL010_255", periodYearMonth, startValueTime,
										compareOperatorText.getCompareLeft(), nameErrorAlarm,
										compareOperatorText.getCompareright(), endValueTime);
							} else {
								alarmDescription = TextResource.localize("KAL010_256", periodYearMonth, startValueTime,
										compareOperatorText.getCompareLeft(), nameErrorAlarm, nameErrorAlarm,
										compareOperatorText.getCompareright(), endValueTime);
							}
						}
					}
					break;
					
				case AVERAGE_TIME:
					if (checkActualResultMulMonth.checkMulMonthCheckCondAverage(period,companyId,employee.getEmployeeId(),result,extra)) {
						checkAddAlarm = true;
						String startValueTime = timeToString(startValue.intValue());
						String endValueTime = "";
						if (compare <= 5) {
							alarmDescription = TextResource.localize("KAL010_264", periodYearMonth, nameErrorAlarm,
									compareOperatorText.getCompareLeft(), startValueTime);
						} else {
							endValueTime = timeToString(endValue.intValue());
							if (compare > 5 && compare <= 7) {
								alarmDescription = TextResource.localize("KAL010_265", periodYearMonth, startValueTime,
										compareOperatorText.getCompareLeft(), nameErrorAlarm,
										compareOperatorText.getCompareright(), endValueTime);
							} else {
								alarmDescription = TextResource.localize("KAL010_266", periodYearMonth, startValueTime,
										compareOperatorText.getCompareLeft(), nameErrorAlarm, nameErrorAlarm,
										compareOperatorText.getCompareright(), endValueTime);
							}
						}
					}
					break;
					
				case AVERAGE_TIMES:
				case AVERAGE_AMOUNT:
				case AVERAGE_DAYS:
					if (checkActualResultMulMonth.checkMulMonthCheckCondAverage(period,companyId,employee.getEmployeeId(),result,extra)) {
						checkAddAlarm = true;
						String startValueTime = String.valueOf(startValue.intValue());
						String endValueTime = "";
						if (compare <= 5) {
							alarmDescription = TextResource.localize("KAL010_264", periodYearMonth, nameErrorAlarm,
									compareOperatorText.getCompareLeft(), startValueTime);
						} else {
							endValueTime = String.valueOf(endValue.intValue());
							if (compare > 5 && compare <= 7) {
								alarmDescription = TextResource.localize("KAL010_265", periodYearMonth, startValueTime,
										compareOperatorText.getCompareLeft(), nameErrorAlarm,
										compareOperatorText.getCompareright(), endValueTime);
							} else {
								alarmDescription = TextResource.localize("KAL010_266", periodYearMonth, startValueTime,
										compareOperatorText.getCompareLeft(), nameErrorAlarm, nameErrorAlarm,
										compareOperatorText.getCompareright(), endValueTime);
							}
						}
					}
					break;
					
				case CONTINUOUS_TIME:
					if (checkActualResultMulMonth.checkMulMonthCheckCondContinue(period,companyId,employee.getEmployeeId(),result,extra)) {
						checkAddAlarm = true;
						String startValueTime = timeToString(startValue.intValue());
						alarmDescription = TextResource.localize("KAL010_260", periodYearMonth, nameErrorAlarm,
								compareOperatorText.getCompareLeft(), startValueTime,
								String.valueOf(extra.getContinuousMonths()));
					}
					break;
					
				case CONTINUOUS_TIMES:
				case CONTINUOUS_AMOUNT:
				case CONTINUOUS_DAYS:
					if (checkActualResultMulMonth.checkMulMonthCheckCondContinue(period,companyId,employee.getEmployeeId(),result,extra)) {
						checkAddAlarm = true;
						String startValueTime = String.valueOf(startValue.intValue());
						alarmDescription = TextResource.localize("KAL010_260", periodYearMonth, nameErrorAlarm,
								compareOperatorText.getCompareLeft(), startValueTime,
								String.valueOf(extra.getContinuousMonths()));
					}
					break;
				
				case NUMBER_TIME:
					ArrayList<Integer> listMonthNumberTime = checkActualResultMulMonth.checkMulMonthCheckCondCosp(period,companyId,employee.getEmployeeId(),result,extra) ;
					if (!CollectionUtil.isEmpty(listMonthNumberTime)) {
						checkAddAlarm = true;
						String startValueTime = timeToString(startValue.intValue());
						alarmDescription = TextResource.localize("KAL010_270", periodYearMonth, nameErrorAlarm,
								convertCompareType(extra.getCompareOperator()).getCompareLeft(), startValueTime,
								listMonthNumberTime.toString(), String.valueOf(extra.getTimes()));

					}
					break;
					
				case NUMBER_TIMES:
				case NUMBER_AMOUNT:
				case NUMBER_DAYS:
					ArrayList<Integer> listMonthNumber = checkActualResultMulMonth.checkMulMonthCheckCondCosp(period,companyId,employee.getEmployeeId(),result,extra) ;
					if (!CollectionUtil.isEmpty(listMonthNumber)) {
						checkAddAlarm = true;
						String startValueTime = String.valueOf(startValue.intValue());
						alarmDescription = TextResource.localize("KAL010_270", periodYearMonth, nameErrorAlarm,
								convertCompareType(extra.getCompareOperator()).getCompareLeft(), startValueTime,
								listMonthNumber.toString(), String.valueOf(extra.getTimes()));

					}
					break;
				default:
					break;
				}//end switch check item
				if (checkAddAlarm) {
					ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(employee.getWorkplaceId(),
							employee.getEmployeeId(), tempStart.toString(ErAlConstant.YM_FORMAT), TextResource.localize("KAL010_250"),
							checkItem.nameId, alarmDescription, extra.getDisplayMessage());
					listValueExtractAlarm.add(resultMonthlyValue);
				}
			}

		}
		return listValueExtractAlarm;
		
	}
	
	// tab1
		private List<ValueExtractAlarm> extraResultMulMon(String companyId, List<MulMonCheckCondDomainEventDto> listExtra,
				DatePeriod period, List<EmployeeSearchDto> employees,List<String> listEmployeeID, YearMonthPeriod yearMonthPeriod) {

			List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>();
			// convert date to String
			GeneralDate tempStart = period.start();
			GeneralDate tempEnd = period.end();
			String periodYearMonth = tempStart.toString("yyyy/MM") + "~" + tempEnd.toString("yyyy/MM");
			// save moths of NumberMonth
			for (MulMonCheckCondDomainEventDto extra : listExtra) {
				if(!extra.isUseAtr())
					continue;
				ErAlAtdItemConAdapterDto erAlAtdItemConAdapterDto = extra.getErAlAtdItem();
				if(erAlAtdItemConAdapterDto == null) continue;
				int typeCheckItem = extra.getTypeCheckItem();
				TypeCheckWorkRecordMultipleMonthImport checkItem = EnumAdaptor.valueOf(typeCheckItem,
						TypeCheckWorkRecordMultipleMonthImport.class);
				List<Integer> tmp = extra.getErAlAtdItem().getCountableAddAtdItems();
				List<Integer> tmp2 = extra.getErAlAtdItem().getCountableSubAtdItems();
				int compare = erAlAtdItemConAdapterDto.getCompareOperator();
				CompareOperatorText compareOperatorText = convertCompareType(compare);
				BigDecimal startValue = erAlAtdItemConAdapterDto.getCompareStartValue();
				BigDecimal endValue = erAlAtdItemConAdapterDto.getCompareEndValue();
				String nameErrorAlarm = "";
				List<Integer> listAttendanceItemIds = new ArrayList<>();
				if (!CollectionUtil.isEmpty(tmp)) {
					List<AttendanceItemName> listAttdName = attdItemNameDomainService.getNameOfAttendanceItem(tmp,
							TypeOfItem.Monthly.value);
					listAttendanceItemIds = listAttdName.stream().map(AttendanceItemName::getAttendanceItemId)
							.collect(Collectors.toList());
						nameErrorAlarm = getNameErrorAlarm(listAttdName,0,nameErrorAlarm);
				}

				if (!CollectionUtil.isEmpty(tmp2)) {
					List<AttendanceItemName> listAttdName = attdItemNameDomainService.getNameOfAttendanceItem(tmp2,
							TypeOfItem.Monthly.value);
					listAttendanceItemIds.addAll(listAttdName.stream().map(AttendanceItemName::getAttendanceItemId)
							.collect(Collectors.toList()));
						nameErrorAlarm = getNameErrorAlarm(listAttdName,1,nameErrorAlarm);
				}
				
				// 月別実績を取得する
				Map<String, List<MonthlyRecordValueImport>> resultActuals = actualMultipleMonthAdapter
						.getActualMultipleMonth(listEmployeeID, yearMonthPeriod, listAttendanceItemIds);
				if(resultActuals.isEmpty()){
					continue;
				}
				
				String alarmDescription = "";
				for (EmployeeSearchDto employee : employees) {
					boolean checkAddAlarm = false;
					List<MonthlyRecordValueImport> result = resultActuals.get(employee.getId());
					if (CollectionUtil.isEmpty(result)) continue;
					switch (checkItem) {
					
					case TIME:
						if (checkActualResultMulMonth.checkMulMonthCheckCond(period,companyId,employee.getId(),result,extra)) {

							checkAddAlarm = true;
							String startValueTime = timeToString(startValue.intValue());
							String endValueTime = "";
							if (compare <= 5) {
								alarmDescription = TextResource.localize("KAL010_254", periodYearMonth, nameErrorAlarm,
										compareOperatorText.getCompareLeft(), startValueTime);
							} else {
								endValueTime = timeToString(endValue.intValue());
								if (compare > 5 && compare <= 7) {
									alarmDescription = TextResource.localize("KAL010_255", periodYearMonth, startValueTime,
											compareOperatorText.getCompareLeft(), nameErrorAlarm,
											compareOperatorText.getCompareright(), endValueTime);
								} else {
									alarmDescription = TextResource.localize("KAL010_256", periodYearMonth, startValueTime,
											compareOperatorText.getCompareLeft(), nameErrorAlarm, nameErrorAlarm,
											compareOperatorText.getCompareright(), endValueTime);
								}
							}
						}
						break;
						
					case TIMES:
					case AMOUNT:
					case DAYS:
						if (checkActualResultMulMonth.checkMulMonthCheckCond(period,companyId,employee.getId(),result,extra)) {

							checkAddAlarm = true;
							String startValueTime = String.valueOf(startValue.intValue());
							String endValueTime = "";
							if (compare <= 5) {
								alarmDescription = TextResource.localize("KAL010_254", periodYearMonth, nameErrorAlarm,
										compareOperatorText.getCompareLeft(), startValueTime);
							} else {
								endValueTime = String.valueOf(endValue.intValue());
								if (compare > 5 && compare <= 7) {
									alarmDescription = TextResource.localize("KAL010_255", periodYearMonth, startValueTime,
											compareOperatorText.getCompareLeft(), nameErrorAlarm,
											compareOperatorText.getCompareright(), endValueTime);
								} else {
									alarmDescription = TextResource.localize("KAL010_256", periodYearMonth, startValueTime,
											compareOperatorText.getCompareLeft(), nameErrorAlarm, nameErrorAlarm,
											compareOperatorText.getCompareright(), endValueTime);
								}
							}
						}
						break;
						
					case AVERAGE_TIME:
						if (checkActualResultMulMonth.checkMulMonthCheckCondAverage(period,companyId,employee.getId(),result,extra)) {
							checkAddAlarm = true;
							String startValueTime = timeToString(startValue.intValue());
							String endValueTime = "";
							if (compare <= 5) {
								alarmDescription = TextResource.localize("KAL010_264", periodYearMonth, nameErrorAlarm,
										compareOperatorText.getCompareLeft(), startValueTime);
							} else {
								endValueTime = timeToString(endValue.intValue());
								if (compare > 5 && compare <= 7) {
									alarmDescription = TextResource.localize("KAL010_265", periodYearMonth, startValueTime,
											compareOperatorText.getCompareLeft(), nameErrorAlarm,
											compareOperatorText.getCompareright(), endValueTime);
								} else {
									alarmDescription = TextResource.localize("KAL010_266", periodYearMonth, startValueTime,
											compareOperatorText.getCompareLeft(), nameErrorAlarm, nameErrorAlarm,
											compareOperatorText.getCompareright(), endValueTime);
								}
							}
						}
						break;
						
					case AVERAGE_TIMES:
					case AVERAGE_AMOUNT:
					case AVERAGE_DAYS:
						if (checkActualResultMulMonth.checkMulMonthCheckCondAverage(period,companyId,employee.getId(),result,extra)) {
							checkAddAlarm = true;
							String startValueTime = String.valueOf(startValue.intValue());
							String endValueTime = "";
							if (compare <= 5) {
								alarmDescription = TextResource.localize("KAL010_264", periodYearMonth, nameErrorAlarm,
										compareOperatorText.getCompareLeft(), startValueTime);
							} else {
								endValueTime = String.valueOf(endValue.intValue());
								if (compare > 5 && compare <= 7) {
									alarmDescription = TextResource.localize("KAL010_265", periodYearMonth, startValueTime,
											compareOperatorText.getCompareLeft(), nameErrorAlarm,
											compareOperatorText.getCompareright(), endValueTime);
								} else {
									alarmDescription = TextResource.localize("KAL010_266", periodYearMonth, startValueTime,
											compareOperatorText.getCompareLeft(), nameErrorAlarm, nameErrorAlarm,
											compareOperatorText.getCompareright(), endValueTime);
								}
							}
						}
						break;
						
					case CONTINUOUS_TIME:
						if (checkActualResultMulMonth.checkMulMonthCheckCondContinue(period,companyId,employee.getId(),result,extra)) {
							checkAddAlarm = true;
							String startValueTime = timeToString(startValue.intValue());
							alarmDescription = TextResource.localize("KAL010_260", periodYearMonth, nameErrorAlarm,
									compareOperatorText.getCompareLeft(), startValueTime,
									String.valueOf(extra.getContinuousMonths()));
						}
						break;
						
					case CONTINUOUS_TIMES:
					case CONTINUOUS_AMOUNT:
					case CONTINUOUS_DAYS:
						if (checkActualResultMulMonth.checkMulMonthCheckCondContinue(period,companyId,employee.getId(),result,extra)) {
							checkAddAlarm = true;
							String startValueTime = String.valueOf(startValue.intValue());
							alarmDescription = TextResource.localize("KAL010_260", periodYearMonth, nameErrorAlarm,
									compareOperatorText.getCompareLeft(), startValueTime,
									String.valueOf(extra.getContinuousMonths()));
						}
						break;
					
					case NUMBER_TIME:
						ArrayList<Integer> listMonthNumberTime = checkActualResultMulMonth.checkMulMonthCheckCondCosp(period,companyId,employee.getId(),result,extra) ;
						if (!CollectionUtil.isEmpty(listMonthNumberTime)) {
							checkAddAlarm = true;
							String startValueTime = timeToString(startValue.intValue());
							alarmDescription = TextResource.localize("KAL010_270", periodYearMonth, nameErrorAlarm,
									convertCompareType(extra.getCompareOperator()).getCompareLeft(), startValueTime,
									listMonthNumberTime.toString(), String.valueOf(extra.getTimes()));

						}
						break;
						
					case NUMBER_TIMES:
					case NUMBER_AMOUNT:
					case NUMBER_DAYS:
						ArrayList<Integer> listMonthNumber = checkActualResultMulMonth.checkMulMonthCheckCondCosp(period,companyId,employee.getId(),result,extra) ;
						if (!CollectionUtil.isEmpty(listMonthNumber)) {
							checkAddAlarm = true;
							String startValueTime = String.valueOf(startValue.intValue());
							alarmDescription = TextResource.localize("KAL010_270", periodYearMonth, nameErrorAlarm,
									convertCompareType(extra.getCompareOperator()).getCompareLeft(), startValueTime,
									listMonthNumber.toString(), String.valueOf(extra.getTimes()));

						}
						break;
					default:
						break;
					}//end switch check item
					if (checkAddAlarm) {
						ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(employee.getWorkplaceId(),
								employee.getId(), tempStart.toString("yyyy/MM"), TextResource.localize("KAL010_250"),
								checkItem.nameId, alarmDescription, extra.getDisplayMessage());
						listValueExtractAlarm.add(resultMonthlyValue);
					}
				}

			}
			return listValueExtractAlarm;
			
		}


	private CompareOperatorText convertCompareType(int compareOperator) {
		CompareOperatorText compare = new CompareOperatorText();
		switch(compareOperator) {
		case 0 :/* 等しくない（≠） */
			compare.setCompareLeft("≠");
			compare.setCompareright("");
			break; 
		case 1 :/* 等しい（＝） */
			compare.setCompareLeft("＝");
			compare.setCompareright("");
			break; 
		case 2 :/* 以下（≦） */
			compare.setCompareLeft("≦");
			compare.setCompareright("");
			break;
		case 3 :/* 以上（≧） */
			compare.setCompareLeft("≧");
			compare.setCompareright("");
			break;
		case 4 :/* より小さい（＜） */
			compare.setCompareLeft("＜");
			compare.setCompareright("");
			break;
		case 5 :/* より大きい（＞） */
			compare.setCompareLeft("＞");
			compare.setCompareright("");
			break;
		case 6 :/* 範囲の間（境界値を含まない）（＜＞） */
			compare.setCompareLeft("＜");
			compare.setCompareright("＜");
			break;
		case 7 :/* 範囲の間（境界値を含む）（≦≧） */
			compare.setCompareLeft("≦");
			compare.setCompareright("≦");
			break;
		case 8 :/* 範囲の外（境界値を含まない）（＞＜） */
			compare.setCompareLeft("＜");
			compare.setCompareright("＜");
			break;
		
		default :/* 範囲の外（境界値を含む）（≧≦） */
			compare.setCompareLeft("≦");
			compare.setCompareright("≦");
			break; 
		}

		return compare;
	}
	/*
	 * get name error Alarm
	 * @param attendanceItemNames : list attendance item name
	 * @param type : 0 add/1 sub
	 * @param nameErrorAlarm : String input to join
	 * @return string
	 */
	private String getNameErrorAlarm(List<AttendanceItemName> attendanceItemNames ,int type,String nameErrorAlarm){
		if(!CollectionUtil.isEmpty(attendanceItemNames)) {
			
			for(int i=0; i< attendanceItemNames.size(); i++) {
				String beforeOperator = "";
				String operator = (i == (attendanceItemNames.size() - 1)) ? "" : type == 1 ? " - " : " + ";
				
				if (!"".equals(nameErrorAlarm) || type == 1) {
					beforeOperator = (i == 0) ? type == 1 ? " - " : " + " : "";
				}
                nameErrorAlarm += beforeOperator + attendanceItemNames.get(i).getAttendanceItemName() + operator;
			}
		}		
		return nameErrorAlarm;
	}
	private String timeToString(int value ){
		if(value%60<10){
			return  String.valueOf(value/60)+":0"+  String.valueOf(value%60);
		}
		return String.valueOf(value/60)+":"+  String.valueOf(value%60);
	}
}
