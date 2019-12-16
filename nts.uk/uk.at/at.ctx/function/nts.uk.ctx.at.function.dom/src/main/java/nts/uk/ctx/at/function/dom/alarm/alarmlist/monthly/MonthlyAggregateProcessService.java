package nts.uk.ctx.at.function.dom.alarm.alarmlist.monthly;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.ResponseImprovementAdapter;
import nts.uk.ctx.at.function.dom.adapter.checkresultmonthly.Check36AgreementValueImport;
import nts.uk.ctx.at.function.dom.adapter.checkresultmonthly.CheckResultMonthlyAdapter;
import nts.uk.ctx.at.function.dom.adapter.checkresultmonthly.MonthlyRecordValuesImport;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlAtdItemConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.holidaysremaining.AbsenceReruitmentManaAdapter;
import nts.uk.ctx.at.function.dom.adapter.holidaysremaining.StatusOfHolidayImported;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.ExtraResultMonthlyFunAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.FixedExtraMonFunAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.FixedExtraMonFunImport;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.checkremainnumber.CheckRemainNumberMonFunImport;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.checkremainnumber.CompareRangeImport;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.checkremainnumber.CompareSingleValueImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyremain.AnnualLeaveUsageImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyremain.CheckResultRemainMonthlyAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlyremain.DayoffCurrentMonthOfEmployeeImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyremain.GetConfirmMonthlyAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlyremain.ReserveLeaveUsageImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyremain.TypeCheckVacationImport;
import nts.uk.ctx.at.function.dom.adapter.periodofspecialleave.ComplileInPeriodOfSpecialLeaveAdapter;
import nts.uk.ctx.at.function.dom.adapter.periodofspecialleave.SpecialHolidayImported;
import nts.uk.ctx.at.function.dom.adapter.sysfixedcheckcondition.SysFixedCheckConMonAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.approvalmanagement.ApprovalProcessImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.ErAlWorkRecordCheckAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.RegulationInfoEmployeeResult;
import nts.uk.ctx.at.function.dom.adapter.workrecord.identificationstatus.identityconfirmprocess.IdentityConfirmProcessImport;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.ErAlConstant;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.monthly.MonAlarmCheckCon;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.monthly.dtoevent.ExtraResultMonthlyDomainEventDto;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.enums.TypeOfItem;
import nts.uk.ctx.at.function.dom.attendanceitemname.AttendanceItemName;
import nts.uk.ctx.at.function.dom.attendanceitemname.service.AttendanceItemNameDomainService;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.OccurrenceDigClass;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.DigestionAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngParam;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;
/**
 * 月次の集計処理
 * @author tutk
 *
 */
@Stateless
public class MonthlyAggregateProcessService {
	
	@Inject
	private AlarmCheckConditionByCategoryRepository alCheckConByCategoryRepo;
	
	@Inject
	private ExtraResultMonthlyFunAdapter extraResultMonthlyFunAdapter;
	
	@Inject
	private FixedExtraMonFunAdapter fixedExtraMonFunAdapter;
	
	@Inject
	private ResponseImprovementAdapter responseImprovementAdapter;
	
	@Inject 
	private SysFixedCheckConMonAdapter sysFixedCheckConMonAdapter;
	
	@Inject
	private CheckResultMonthlyAdapter checkResultMonthlyAdapter;
	
	@Inject
	private ClosureService closureService;
	
	@Inject
	private CompensLeaveComSetRepository compensLeaveComSetRepository;
	
	@Inject
	private AttendanceItemNameDomainService attdItemNameDomainService;
	
	@Inject
	private AbsenceReruitmentManaAdapter absenceReruitmentManaAdapter;
	
	@Inject
	private ComplileInPeriodOfSpecialLeaveAdapter complileInPeriodOfSpecialLeaveAdapter;
	
	@Inject
	private GetConfirmMonthlyAdapter getConfirmMonthlyAdapter;
		
	@Inject
	private CheckResultRemainMonthlyAdapter checkResultRemainMonthlyAdapter;
	
	@Inject 
	private BreakDayOffMngInPeriodQuery breakDayOffMngInPeriodQuery;

	@Inject
	private ErAlWorkRecordCheckAdapter erAlWorkRecordCheckAdapter;
	
	@Inject
	private ManagedParallelWithContext parallelManager;
	
	public List<ValueExtractAlarm> monthlyAggregateProcess(String companyID , String  checkConditionCode,DatePeriod period,List<EmployeeSearchDto> employees, ApprovalProcessImport approvalProcessImport, IdentityConfirmProcessImport identityConfirmProcessImport){
		
		List<String> employeeIds = employees.stream().map( e ->e.getId()).collect(Collectors.toList());
		
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>(); 	
		
		
		Optional<AlarmCheckConditionByCategory> alCheckConByCategory =   alCheckConByCategoryRepo.find(companyID, AlarmCategory.MONTHLY.value, checkConditionCode);
		if(!alCheckConByCategory.isPresent()) {
			return Collections.emptyList();
		}
				
		MonAlarmCheckCon monAlarmCheckCon = (MonAlarmCheckCon) alCheckConByCategory.get().getExtractionCondition();
		List<FixedExtraMonFunImport> listFixed = fixedExtraMonFunAdapter.getByEralCheckID(monAlarmCheckCon.getMonAlarmCheckConID());
		List<ExtraResultMonthlyDomainEventDto> listExtra = extraResultMonthlyFunAdapter.getListExtraResultMonByListEralID(monAlarmCheckCon.getArbExtraCon());
		
		//対象者を絞り込む
		DatePeriod endDatePerior = new DatePeriod(period.end(),period.end());
		List<String> listEmployeeID = responseImprovementAdapter.reduceTargetResponseImprovement(employeeIds, endDatePerior, alCheckConByCategory.get().getExtractTargetCondition());
		
		
		//対象者の件数をチェック : 対象者　≦　0
		if(listEmployeeID.isEmpty()) {
			return Collections.emptyList();
		}
		List<EmployeeSearchDto> employeesDto = employees.stream().filter(c-> listEmployeeID.contains(c.getId())).collect(Collectors.toList());
		//tab 2
		listValueExtractAlarm.addAll(this.extractMonthlyFixed(listFixed, period, employeesDto, companyID));
		//tab 3
		
		listValueExtractAlarm.addAll(this.extraResultMonthly(companyID, listExtra, period, employeesDto));
		
		return listValueExtractAlarm;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ValueExtractAlarm> monthlyAggregateProcess(String companyID , List<AlarmCheckConditionByCategory> monthlyErAl, DatePeriod period, List<EmployeeSearchDto> employees, 
			Consumer<Integer> counter, Supplier<Boolean> shouldStop){

		List<ValueExtractAlarm> listValueExtractAlarm = Collections.synchronizedList(new ArrayList<>());
		
		parallelManager.forEach(CollectionUtil.partitionBySize(employees, 100), emps -> {
			List<String> employeeIds = emps.stream().map( e ->e.getId()).collect(Collectors.toList());
			 	
			Map<String, List<RegulationInfoEmployeeResult>> listTargetMap = erAlWorkRecordCheckAdapter.filterEmployees(period, 
																														employeeIds, 
																														monthlyErAl.stream().map(c -> c.getExtractTargetCondition()).collect(Collectors.toList()));
			monthlyErAl.stream().forEach(eral -> {
				synchronized (this) {
					if(shouldStop.get()) {
						return;
					}
				}
				//対象者を絞り込む
				List<String> listEmployeeID = listTargetMap.get(eral.getExtractTargetCondition().getId()).stream().map(c -> c.getEmployeeId()).collect(Collectors.toList());
				
				//対象者の件数をチェック : 対象者　≦　0
				if(!listEmployeeID.isEmpty()) {
					MonAlarmCheckCon monAlarmCheckCon = (MonAlarmCheckCon) eral.getExtractionCondition();
					
					List<FixedExtraMonFunImport> listFixed = fixedExtraMonFunAdapter.getByEralCheckID(monAlarmCheckCon.getMonAlarmCheckConID());
					List<ExtraResultMonthlyDomainEventDto> listExtra = extraResultMonthlyFunAdapter.getListExtraResultMonByListEralID(monAlarmCheckCon.getArbExtraCon());
					
					List<EmployeeSearchDto> employeesDto = emps.stream().filter(c-> listEmployeeID.contains(c.getId())).collect(Collectors.toList());
					//tab 2
					/***/
					listValueExtractAlarm.addAll(this.extractMonthlyFixed(listFixed, period, employeesDto, companyID));
					//tab 3
					/***/
					listValueExtractAlarm.addAll(this.extraResultMonthlyV2(companyID, listExtra, period, employeesDto));
				}
				
				synchronized (this) {
					counter.accept(emps.size());
				}
			});
		});
		
		return listValueExtractAlarm;
	}

	//tab 2
	private List<ValueExtractAlarm> extractMonthlyFixed(List<FixedExtraMonFunImport> listFixed,
			DatePeriod period, List<EmployeeSearchDto> employees, String companyID) {
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>();
		List<YearMonth> lstYearMonth = period.yearMonthsBetween();
		List<String> empIds = employees.stream().map(c -> c.getId()).collect(Collectors.toList());
		
		List<FixedExtraMonFunImport> monUnconfirm = listFixed.stream().filter(f -> f.isUseAtr() && f.getFixedExtraItemMonNo() == SysFixedMonPerEralEnum.MON_UNCONFIRMED.value)
				.collect(Collectors.toList());
		// FIX_EXTRA_ITEM_MON_NO
		if(!monUnconfirm.isEmpty()) {
			//月次未確認
			String checkedValue = TextResource.localize("KAL010_130");
			List<ValueExtractAlarm> unconfirmeds = sysFixedCheckConMonAdapter.checkMonthlyUnconfirmeds(empIds,  lstYearMonth);
			for(ValueExtractAlarm item : unconfirmeds) {
				item.setCheckedValue(Optional.ofNullable(checkedValue));
			}
			processFixedCheckResult(employees, listValueExtractAlarm, monUnconfirm, unconfirmeds);
			
		}
		
		List<FixedExtraMonFunImport> monCorrection = listFixed.stream().filter(f -> f.isUseAtr() && f.getFixedExtraItemMonNo() == SysFixedMonPerEralEnum.WITH_MON_CORRECTION.value)
				.collect(Collectors.toList());
		// FIX_EXTRA_ITEM_MON_NO
		if(!monCorrection.isEmpty()) {
			//// 2:管理者未承認
			String checkedValue = TextResource.localize("KAL010_131");
			List<ValueExtractAlarm> corrections = sysFixedCheckConMonAdapter.checkMonthlyUnconfirmedsAdmin(empIds,  lstYearMonth);
			for(ValueExtractAlarm item : corrections) {
				item.setCheckedValue(Optional.ofNullable(checkedValue));
			}
			processFixedCheckResult(employees, listValueExtractAlarm, monCorrection, corrections);
			
		}
		
		List<FixedExtraMonFunImport> deadline = listFixed.stream().filter(f -> f.isUseAtr() && f.getFixedExtraItemMonNo() == SysFixedMonPerEralEnum.CHECK_DEADLINE_HOLIDAY.value)
				.collect(Collectors.toList());
		if(!deadline.isEmpty()) {
			//代休の消化期限チェック
			listValueExtractAlarm.addAll(extractErrorAlarmForHoliday(deadline, employees, companyID, empIds));
		}
		
		return listValueExtractAlarm;
	}
	
	/**
	 * 代休の消化期限チェック
	 * @param listFixed
	 * @param employees
	 * @param companyID
	 * @return
	 */
	private List<ValueExtractAlarm> extractErrorAlarmForHoliday(List<FixedExtraMonFunImport> fixedExtraMonFunImport, List<EmployeeSearchDto> employee, String companyID, List<String> empIDs) {
		List<ValueExtractAlarm> listValueExtractAlarm = Collections.synchronizedList(new ArrayList<>());

		String KAL010_278 = TextResource.localize("KAL010_278");
		String KAL010_100 = TextResource.localize("KAL010_100");

		GeneralDate today = GeneralDate.today();
		CompensatoryLeaveComSetting compensatoryLeaveComSetting = compensLeaveComSetRepository.find(companyID);
		int deadlCheckMonth = compensatoryLeaveComSetting.getCompensatoryAcquisitionUse().getDeadlCheckMonth().value + 1;
		Map<String, Closure> closureMap = closureService.getClosureByEmployees(empIDs, today);

		if (closureMap.isEmpty()) {
			return listValueExtractAlarm;
		}
		employee.stream().forEach(emp -> {
			Closure closure = closureMap.get(emp.getId());
			if(closure == null){
				return;
			}
			val closureOpt = Optional.ofNullable(closure);
			//締めのアルゴリズム「当月の期間を算出する」を実行する
			DatePeriod periodCurrentMonth = closureService.getClosurePeriod(closure.getClosureId().value,
					closure.getClosureMonth().getProcessingYm(), closureOpt);
			
			//代休期限アラーム基準日を決定する
			DatePeriod periodCheckDealMonth = closureService.getClosurePeriod(closure.getClosureId().value,
					getDeadlCheckMonth(periodCurrentMonth, deadlCheckMonth), closureOpt);

			//RequestList No.203 期間内の休出代休残数を取得する
			//集計開始日
			//集計終了日
			DatePeriod newPeriod = new DatePeriod(periodCurrentMonth.start(), periodCurrentMonth.end().addYears(1));
			
			BreakDayOffRemainMngParam param = new BreakDayOffRemainMngParam(companyID, emp.getId(),
					newPeriod, false, periodCurrentMonth.end(), false, Collections.emptyList(),
					Collections.emptyList(), Collections.emptyList(), Optional.empty(), Optional.empty(), Optional.empty());
			BreakDayOffRemainMngOfInPeriod breakDayOffRemainMngOfInPeriod = breakDayOffMngInPeriodQuery
					.getBreakDayOffMngInPeriod(param);
			List<BreakDayOffDetail> lstDetailData = breakDayOffRemainMngOfInPeriod.getLstDetailData();

			List<BreakDayOffDetail> lstExtractData = new ArrayList<>();
			if (!CollectionUtil.isEmpty(lstDetailData)) {
				//代休期限アラーム基準日以前に発生した未使用の休出を抽出する
				lstExtractData = lstDetailData.stream().filter(detail -> {
					return ((detail.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE)
							&& (detail.getUnUserOfBreak().isPresent()
									&& detail.getUnUserOfBreak().get().getDigestionAtr() == DigestionAtr.UNUSED)
							&& (detail.getYmdData().getDayoffDate().isPresent()
									&& detail.getYmdData().getDayoffDate().get().beforeOrEquals(periodCheckDealMonth.end())));
				}).collect(Collectors.toList());

				if (!CollectionUtil.isEmpty(lstExtractData)) {
					//アラームメッセージを生成する
					lstExtractData.stream().forEach(breakDayOffDetail -> {
						fixedExtraMonFunImport.stream().forEach(fix -> {
							ValueExtractAlarm valueExractAlarm = new ValueExtractAlarm();
							valueExractAlarm.setEmployeeID(emp.getId());
							valueExractAlarm.setWorkplaceID(Optional.ofNullable(emp.getWorkplaceId()));
							//valueExractAlarm.setAlarmValueDate(GeneralDate.today().toString(ErAlConstant.YM_FORMAT));
							valueExractAlarm.setAlarmValueDate(yearmonthToString(periodCurrentMonth.start().yearMonth()));
							valueExractAlarm.setClassification(KAL010_100);
							valueExractAlarm.setAlarmItem(KAL010_278);
							String checkedValue = TextResource.localize("KAL010_305",
									breakDayOffDetail.getYmdData().getDayoffDate().get().toString(),
									String.valueOf(breakDayOffDetail.getUnUserOfBreak().get().getUnUsedDays()));
							valueExractAlarm.setAlarmValueMessage(TextResource.localize("KAL010_279",
									String.valueOf(deadlCheckMonth), breakDayOffDetail.getYmdData().getDayoffDate().get().toString(),
									String.valueOf(breakDayOffDetail.getUnUserOfBreak().get().getUnUsedDays())));
							valueExractAlarm.setComment(Optional.ofNullable(fix.getMessage()));
							valueExractAlarm.setCheckedValue(Optional.ofNullable(checkedValue));
							listValueExtractAlarm.add(valueExractAlarm);
						});
					});
//=======
//		for(EmployeeSearchDto employee : employees) {
//			// No 395 with FIX_EXTRA_ITEM_MON_NO 6
//			for (FixedExtraMonFunImport fixedExtraMonFunImport : listFixed) {
//				if(fixedExtraMonFunImport.getFixedExtraItemMonNo() == SysFixedMonPerEralEnum.CHECK_DEADLINE_HOLIDAY.value
//						&& fixedExtraMonFunImport.isUseAtr()){
//					Optional<List<ValueExtractAlarm>> valueExtractAlarms = extractErrorAlarmForHoliday(fixedExtraMonFunImport, employee, companyID,lstYearMonth.get(0));
//					if (valueExtractAlarms.isPresent()) {
//						listValueExtractAlarm.addAll(valueExtractAlarms.get());
//					}
//>>>>>>> delivery/release_ootsuka
				}
			}
		});
		
		return listValueExtractAlarm;
	}

	private void processFixedCheckResult(List<EmployeeSearchDto> employees,
			List<ValueExtractAlarm> listValueExtractAlarm, List<FixedExtraMonFunImport> monUnconfirm,
			List<ValueExtractAlarm> unconfirmeds) {
		if(!CollectionUtil.isEmpty(unconfirmeds)) {
			Map<String, List<ValueExtractAlarm>> eral = unconfirmeds.stream().collect(Collectors.groupingBy(c -> c.getEmployeeID(), Collectors.toList()));
			monUnconfirm.stream().forEach(fix -> {
				employees.stream().forEach(emp -> {
					List<ValueExtractAlarm> empEral = eral.get(emp.getId());
					if(empEral != null) {
						empEral.stream().filter(c -> c != null).forEach(er -> {
							listValueExtractAlarm.add(new ValueExtractAlarm(emp.getWorkplaceId(), er.getEmployeeID(), 
																			er.getAlarmValueDate(), er.getClassification(), 
																			er.getAlarmItem(), er.getAlarmValueMessage(), 
																			fix.getMessage(),er.getCheckedValue().get()));
						});
					}
				});
			});
		}
	}
	
	//tab 3
		private List<ValueExtractAlarm> extraResultMonthlyV2(String companyId,List<ExtraResultMonthlyDomainEventDto> listExtra,
				DatePeriod period, List<EmployeeSearchDto> employees) {
			String KAL010_206 = TextResource.localize("KAL010_206");
			String KAL010_204 = TextResource.localize("KAL010_204");
			String KAL010_100 = TextResource.localize("KAL010_100");
			List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>(); 
			
			YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(period.start().yearMonth(), period.end().yearMonth());
			
			List<Integer> itemIds = Arrays.asList(202,203,204,205,206);
			
			List<ExtraResultMonthlyDomainEventDto> checkRemain = listExtra.stream().filter(c -> c.isUseAtr() && c.getTypeCheckItem() == TypeMonCheckItemImport.CHECK_REMAIN_NUMBER.value).collect(Collectors.toList());
			if(!checkRemain.isEmpty()){
				String alarmName = KAL010_100;
				
				checkRemain.stream().forEach(extra -> {
					String itemName = extra.getNameAlarmExtraCon();
					CheckRemainNumberMonFunImport checkRemainNumberMonFunImport = extra.getCheckRemainNumberMon();
					CompareSingleValueImport compareSingleValueImport = checkRemainNumberMonFunImport.getCompareSingleValueEx();
					CompareRangeImport compareRangeImport = checkRemainNumberMonFunImport.getCompareRangeEx();
					
					int typeOperator = checkRemainNumberMonFunImport.getCheckOperatorType();
					TypeCheckVacationImport typeCheckVacation = EnumAdaptor.valueOf(checkRemainNumberMonFunImport.getCheckVacation(), TypeCheckVacationImport.class);
					String daysSingle = "";
					String daysRangeStart = "";
					String daysRangeEnd = "";
					CompareOperatorText compareOperatorText = new CompareOperatorText();
					int compareSingle = -1;
					int compareRange = -1;
					//0 - singlee -----1-range
					if (typeOperator == 0) {
						daysSingle = compareSingleValueImport.getValue().getDaysValue().toString();
						compareSingle = compareSingleValueImport.getCompareOperator();
						compareOperatorText = convertCompareType(compareSingle);
					}
					if (typeOperator == 1) {
						daysRangeStart = compareRangeImport.getStartValue().getDaysValue().toString();
						daysRangeEnd = compareRangeImport.getEndValue().getDaysValue().toString();
						compareRange = compareRangeImport.getCompareOperator();
						compareOperatorText = convertCompareType(compareRange);
					}
					
					//HoiDD #1000436
					for (EmployeeSearchDto employee : employees) {
						String sid = employee.getId();
						String checkerValue = null;
						switch (typeCheckVacation) {

							//ANNUAL_PAID_LEAVE
						case ANNUAL_PAID_LEAVE:
							List<AnnualLeaveUsageImport> annualLeaveUsageImports = getConfirmMonthlyAdapter.getListAnnualLeaveUsageImport(sid, yearMonthPeriod);
							for (AnnualLeaveUsageImport annualLeaveUsageImport : annualLeaveUsageImports) {
								boolean check = false;
								String alarmMessage = "";
//								String itemName = TextResource.localize("KAL010_123");
								check = checkResultRemainMonthlyAdapter.checkAnnualLeaveUsage(checkRemainNumberMonFunImport, annualLeaveUsageImport);
								if(check){
									String str = "";
									checkerValue = TextResource.localize("KAL010_306",TextResource.localize("KAL010_123"), annualLeaveUsageImport.getRemainingDays().toString());
									if(typeOperator == 0){
//										alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
										str = TextResource.localize("KAL010_984",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysSingle);
									}else {
										if(compareRange <=7 ){
//											alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
//													compareOperatorText.getCompareright()+daysRangeEnd;
											str = TextResource.localize("KAL010_985",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}else {
//											alarmMessage = itemName+
//													compareOperatorText.getCompareLeft()+
//													daysRangeStart+", "+
//													daysRangeEnd+
//													compareOperatorText.getCompareright()+
//													itemName;
											str = TextResource.localize("KAL010_986",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}
									}
									alarmMessage = TextResource.localize("KAL010_110",str,checkerValue);
									//add to list
									ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
											employee.getWorkplaceId(),
											employee.getId(),
											toYMString(annualLeaveUsageImport.getYearMonth()),
											alarmName,
											itemName,
											alarmMessage,	
											extra.getDisplayMessage(),
											checkerValue
											);
									listValueExtractAlarm.add(resultCheckRemain);
								}
							}
							break;
							
							//SUB_HOLIDAY
						case SUB_HOLIDAY:
							List<DayoffCurrentMonthOfEmployeeImport> dayoffCurrentMonthOfEmployeeImports = getConfirmMonthlyAdapter.lstDayoffCurrentMonthOfEmployee(sid, yearMonthPeriod.start(), yearMonthPeriod.end());
							for (DayoffCurrentMonthOfEmployeeImport dayoffCurrentMonthOfEmployeeImport : dayoffCurrentMonthOfEmployeeImports) {
								boolean check = false;
								String alarmMessage = "";
//								String itemName = TextResource.localize("KAL010_124");
								check = checkResultRemainMonthlyAdapter.checkDayoffCurrentMonth(checkRemainNumberMonFunImport, dayoffCurrentMonthOfEmployeeImport);
								if(check){
									String str = "";
									checkerValue = TextResource.localize("KAL010_306",TextResource.localize("KAL010_124"), dayoffCurrentMonthOfEmployeeImport.getRemainingDays().toString());
									if(typeOperator == 0){
										//alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
										str = TextResource.localize("KAL010_984",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysSingle);
									}else {
										if(compareRange <=7 ){
//										alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
//												compareOperatorText.getCompareright()+daysRangeEnd;
										str = TextResource.localize("KAL010_985",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}else {
//											alarmMessage = itemName+
//													compareOperatorText.getCompareLeft()+
//													daysRangeStart+", "+
//													daysRangeEnd+
//													compareOperatorText.getCompareright()+
//													itemName;
											str = TextResource.localize("KAL010_986",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}
									}
									alarmMessage = TextResource.localize("KAL010_110",str,checkerValue);
									//add to list
									ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
											employee.getWorkplaceId(),
											employee.getId(),
											toYMString(dayoffCurrentMonthOfEmployeeImport.getYm()),
											alarmName,
											itemName,
											alarmMessage,	
											extra.getDisplayMessage(),
											checkerValue
											);
									listValueExtractAlarm.add(resultCheckRemain);
								}
							}
							break;
							
							//PAUSE
						case PAUSE:
							List<StatusOfHolidayImported> statusOfHolidayImporteds = absenceReruitmentManaAdapter.getDataCurrentMonthOfEmployee(sid, yearMonthPeriod.start(), yearMonthPeriod.end());
							for (StatusOfHolidayImported statusOfHolidayImported : statusOfHolidayImporteds) {
								boolean check = false;
								String alarmMessage = "";
//								String itemName = TextResource.localize("KAL010_125");
								check = checkResultRemainMonthlyAdapter.checkStatusOfHoliday(checkRemainNumberMonFunImport, statusOfHolidayImported);
								if(check){
									String str = "";
									checkerValue = TextResource.localize("KAL010_306",TextResource.localize("KAL010_125"), statusOfHolidayImported.getRemainingDays().toString());
									if(typeOperator == 0){
//										alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
										str = TextResource.localize("KAL010_984",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysSingle);
									}else {
										if(compareRange <=7 ){
//										alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
//												compareOperatorText.getCompareright()+daysRangeEnd;
										str = TextResource.localize("KAL010_985",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}else {
//											alarmMessage = itemName+
//													compareOperatorText.getCompareLeft()+
//													daysRangeStart+", "+
//													daysRangeEnd+
//													compareOperatorText.getCompareright()+
//													itemName;
											str = TextResource.localize("KAL010_986",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}
									}
									alarmMessage = TextResource.localize("KAL010_110",str,checkerValue);
									//add to list
									ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
											employee.getWorkplaceId(),
											employee.getId(),
											toYMString(statusOfHolidayImported.getYm()),
											alarmName,
											itemName,
											alarmMessage,	
											extra.getDisplayMessage(),
											checkerValue);
									listValueExtractAlarm.add(resultCheckRemain);
								}
							}
							
							break;
							
							//YEARLY_RESERVED
						case YEARLY_RESERVED:
							List<ReserveLeaveUsageImport> reserveLeaveUsageImports = getConfirmMonthlyAdapter.getListReserveLeaveUsageImport(sid, yearMonthPeriod);
							for (ReserveLeaveUsageImport reserveLeaveUsageImport : reserveLeaveUsageImports) {
								boolean check = false;
								String alarmMessage = "";
//								String itemName = TextResource.localize("KAL010_126");
								check = checkResultRemainMonthlyAdapter.checkReserveLeaveUsage(checkRemainNumberMonFunImport, reserveLeaveUsageImport);
								if(check){
									String str = "";
									checkerValue = TextResource.localize("KAL010_306",TextResource.localize("KAL010_126"), reserveLeaveUsageImport.getRemainingDays().toString());
									if(typeOperator == 0){
//										alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
										str = TextResource.localize("KAL010_984",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysSingle);
									}else {
										if(compareRange <=7 ){
//										alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
//												compareOperatorText.getCompareright()+daysRangeEnd;
										str = TextResource.localize("KAL010_985",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}else {
//											alarmMessage = itemName+
//													compareOperatorText.getCompareLeft()+
//													daysRangeStart+", "+
//													daysRangeEnd+
//													compareOperatorText.getCompareright()+
//													itemName;
											str = TextResource.localize("KAL010_986",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}
									}
									alarmMessage = TextResource.localize("KAL010_110",str,checkerValue);
									//add to list
									ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
											employee.getWorkplaceId(),
											employee.getId(),
											toYMString(reserveLeaveUsageImport.getYearMonth()),
											alarmName,
											itemName,
											alarmMessage,	
											extra.getDisplayMessage(),
											checkerValue
											);
									listValueExtractAlarm.add(resultCheckRemain);
								}
							}
							break;	
							
							//SPECIAL_HOLIDAY
						case SPECIAL_HOLIDAY:
							List<Integer> listSpeCode = checkRemainNumberMonFunImport.getListItemID();
							List<SpecialHolidayImported> specialHolidayImporteds= complileInPeriodOfSpecialLeaveAdapter.getSpeHoliOfConfirmedMonthly(sid, yearMonthPeriod.start(), yearMonthPeriod.end(), listSpeCode);
							for (SpecialHolidayImported specialHolidayImported : specialHolidayImporteds) {
								boolean check = false;
								String alarmMessage = "";
//								String itemName = TextResource.localize("KAL010_115");
								check = checkResultRemainMonthlyAdapter.checkSpecialHoliday(checkRemainNumberMonFunImport, specialHolidayImported);
								if(check){
									String str = "";
									checkerValue = TextResource.localize("KAL010_306",TextResource.localize("KAL010_115"), specialHolidayImported.getRemainDays().toString()); 
									if(typeOperator == 0){
										alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
										str = TextResource.localize("KAL010_984",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysSingle);
									}else {
										if(compareRange <=7 ){
//										alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
//												compareOperatorText.getCompareright()+daysRangeEnd;
										str = TextResource.localize("KAL010_985",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}else {
//											alarmMessage = itemName+
//													compareOperatorText.getCompareLeft()+
//													daysRangeStart+", "+
//													daysRangeEnd+
//													compareOperatorText.getCompareright()+
//													itemName;
											str = TextResource.localize("KAL010_986",typeCheckVacation.nameId,compareOperatorText.getCompareLeft(),daysRangeStart,daysRangeEnd);
										}
									}
									alarmMessage = TextResource.localize("KAL010_110",str,checkerValue);
									//add to list
									ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
											employee.getWorkplaceId(),
											employee.getId(),
											toYMString(specialHolidayImported.getYm()),
											alarmName,
											itemName,
											alarmMessage,	
											extra.getDisplayMessage(),
											checkerValue
											);
									listValueExtractAlarm.add(resultCheckRemain);
								}
							}
							
							break;
							
							default:
								break;
							}
						}
					
				});
			}
			
			// 36協定エラー時間  && 36協定アラーム時間 
			List<String> empIds = employees.stream().map(c -> c.getId()).collect(Collectors.toList());
			Map<String, List<MonthlyRecordValuesImport>> monthlyRecords = checkResultMonthlyAdapter.getListMonthlyRecords(empIds, yearMonthPeriod, itemIds);
			listExtra.stream().filter(c -> c.isUseAtr() && (c.getTypeCheckItem() == 1 || c.getTypeCheckItem() == 2)).forEach(extra -> {
				//End HoiDD #1000436
				for (EmployeeSearchDto employee : employees) {
					// Call RQ 436
					List<MonthlyRecordValuesImport> empMonthlyRecords = monthlyRecords.get(employee.getId());
					if (!CollectionUtil.isEmpty(empMonthlyRecords)) {
						List<Check36AgreementValueImport> lstReturnStatus = checkResultMonthlyAdapter.check36AgreementConditions(employee.getId(), empMonthlyRecords,
										extra.getAgreementCheckCon36(), Optional.empty());
						if (!CollectionUtil.isEmpty(lstReturnStatus)) {
							for (Check36AgreementValueImport check36AgreementValueImport : lstReturnStatus) {
								if (check36AgreementValueImport.isCheck36AgreementCon()) {
									ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
											employee.getWorkplaceId(), employee.getId(),
											this.toYMString(check36AgreementValueImport.getYm()), KAL010_100,
											extra.getTypeCheckItem() == 1 ? KAL010_204 : KAL010_206,
											TextResource.localize(extra.getTypeCheckItem() == 1 ? "KAL010_205" : "KAL010_207",
													this.timeToString(check36AgreementValueImport.getErrorValue())),
											extra.getDisplayMessage(),null);
									listValueExtractAlarm.add(resultMonthlyValue);
								}
							}
						}
					}
				}
			});
			List<ExtraResultMonthlyDomainEventDto> over3 = listExtra.stream().filter(c -> c.isUseAtr() && c.getTypeCheckItem() > 3).collect(Collectors.toList());
			if(!over3.isEmpty()){
				Map<String, Map<YearMonth, Map<String,String>>> resultsData = new HashMap<>();
				//No 257
				Map<String, Map<YearMonth, Map<String,Integer>>> checkPerTimeMonActualResults = checkResultMonthlyAdapter.checkPerTimeMonActualResult(
						yearMonthPeriod, empIds, 
						over3.stream().collect(Collectors.toMap(c -> c.getErrorAlarmCheckID(), c -> c.getCheckConMonthly())),
						resultsData);
				
				List<AttendanceItemName> listAttdNameAdds =  attdItemNameDomainService.getNameOfAttendanceItem(TypeOfItem.Monthly);
				
				//End HoiDD #1000436
				over3.stream().forEach(extra -> {
					for (EmployeeSearchDto employee : employees) {
						for (YearMonth yearMonth : yearMonthPeriod.yearMonthsBetween()) {
							if(isError(checkPerTimeMonActualResults, extra.getErrorAlarmCheckID(), employee.getId(), yearMonth)) {
								if(extra.getTypeCheckItem() == 8) {
									processType8(listValueExtractAlarm, listAttdNameAdds, extra, employee, yearMonth,resultsData);
								} else {
									processOtherType(listValueExtractAlarm, listAttdNameAdds, extra, employee, yearMonth,resultsData);
								}
							}
						}
					}
				});
			}
			
			return listValueExtractAlarm;
		}

		private String toYMString(YearMonth ym) {
			return GeneralDate.ymd(ym.year(), ym.month(), 1).toString(ErAlConstant.YM_FORMAT);
		}

		private void processOtherType(List<ValueExtractAlarm> listValueExtractAlarm,
				List<AttendanceItemName> listAttdNameAdds, ExtraResultMonthlyDomainEventDto extra,
				EmployeeSearchDto employee, YearMonth yearMonth,Map<String, Map<YearMonth, Map<String,String>>> resultsData) {
			String checkedValue = resultsData.get(employee.id).get(yearMonth).get(extra.getErrorAlarmCheckID());
			String KAL010_100 = TextResource.localize("KAL010_100");
			
			ErAlAtdItemConAdapterDto erAlAtdItemConAdapterDto = extra.getCheckConMonthly().getGroup1().getLstErAlAtdItemCon().get(0);
			int compare = erAlAtdItemConAdapterDto.getCompareOperator();
			BigDecimal startValue = erAlAtdItemConAdapterDto.getCompareStartValue();
			BigDecimal endValue = erAlAtdItemConAdapterDto.getCompareEndValue();
			CompareOperatorText compareOperatorText = convertCompareType(compare);
			String nameErrorAlarm = "";
			//0 is monthly,1 is dayly
			List<AttendanceItemName> listAttdNameAdd =  listAttdNameAdds.stream().filter(c -> erAlAtdItemConAdapterDto.getCountableAddAtdItems().contains(c.getAttendanceItemId())).collect(Collectors.toList());
			nameErrorAlarm = getNameErrorAlarm(listAttdNameAdd,0,nameErrorAlarm);//0 add atd item
			List<AttendanceItemName> listAttdNameSub =  listAttdNameAdds.stream().filter(c -> erAlAtdItemConAdapterDto.getCountableSubAtdItems().contains(c.getAttendanceItemId())).collect(Collectors.toList());
			nameErrorAlarm = getNameErrorAlarm(listAttdNameSub,1,nameErrorAlarm);//1 sub atd item
			
			String nameItem = extra.getNameAlarmExtraCon();
			String alarmDescription = "";
			switch(extra.getTypeCheckItem()) {
			case 4 ://時間
				//nameItem = TextResource.localize("KAL010_47");
				String startValueTime = this.timeToString(startValue.intValue());
				String endValueTime = "";
				if(compare<=5) {
					alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueTime);
				}else {
					endValueTime = this.timeToString(endValue.intValue());
					if(compare>5 && compare<=7) {
						alarmDescription = TextResource.localize("KAL010_277",startValueTime,
								compareOperatorText.getCompareLeft(),
								nameErrorAlarm,
								compareOperatorText.getCompareright()+
								endValueTime
								);	
					}else {
						alarmDescription = TextResource.localize("KAL010_277",
								nameErrorAlarm,
								compareOperatorText.getCompareLeft(),
								startValueTime + ","+endValueTime,
								compareOperatorText.getCompareright()+
								nameErrorAlarm
								);
					}
				}
				checkedValue = this.timeToString(Double.valueOf(checkedValue).intValue());
				break;
				case 5 ://日数
					//nameItem = TextResource.localize("KAL010_113");
					String startValueDays = String.valueOf(startValue.intValue());
					String endValueDays = "";
					if(compare<=5) {
						alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueDays);
					}else {
						endValueDays = String.valueOf(endValue.intValue());
						if(compare>5 && compare<=7) {
							alarmDescription = TextResource.localize("KAL010_277",startValueDays,
									compareOperatorText.getCompareLeft(),
									nameErrorAlarm,
									compareOperatorText.getCompareright(),
									endValueDays
									);	
						}else {
							alarmDescription = TextResource.localize("KAL010_277",
									nameErrorAlarm,
									compareOperatorText.getCompareLeft(),
									startValueDays + "," + endValueDays,
									compareOperatorText.getCompareright(),
									nameErrorAlarm
									);
						}
					}
					break;
				case 6 ://回数
					//nameItem = TextResource.localize("KAL010_50");
					String startValueTimes = String.valueOf(startValue.intValue());
					String endValueTimes = "";
					if(compare<=5) {
						alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueTimes);
					}else {
						endValueTimes = String.valueOf(endValue.intValue());
						if(compare>5 && compare<=7) {
							alarmDescription = TextResource.localize("KAL010_277",startValueTimes,
									compareOperatorText.getCompareLeft(),
									nameErrorAlarm,
									compareOperatorText.getCompareright()+
									endValueTimes
									);	
						}else {
							alarmDescription = TextResource.localize("KAL010_277",
									nameErrorAlarm,
									compareOperatorText.getCompareLeft(),
									startValueTimes + "," + endValueTimes,
									compareOperatorText.getCompareright()+
									nameErrorAlarm
									);
						}
					}
					break;
				case 7 ://金額 money
					//nameItem = TextResource.localize("KAL010_51");
					String startValueMoney = String.valueOf(startValue.intValue());
					String endValueMoney = "";
					if(compare<=5) {
						alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueMoney);
					}else {
						endValueMoney = String.valueOf(endValue.intValue());
						if(compare>5 && compare<=7) {
							alarmDescription = TextResource.localize("KAL010_277",startValueMoney,
									compareOperatorText.getCompareLeft(),
									nameErrorAlarm,
									compareOperatorText.getCompareright()+
									endValueMoney
									);	
						}else {
							alarmDescription = TextResource.localize("KAL010_277",
									nameErrorAlarm,
									compareOperatorText.getCompareLeft(),
									startValueMoney + "," + endValueMoney,
									compareOperatorText.getCompareright()+
									nameErrorAlarm
									);
						}
					}
					break;
				default : break;
			}
			
			ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
					employee.getWorkplaceId(),
					employee.getId(),
					this.toYMString(yearMonth),
					KAL010_100,
					nameItem,
					//TODO : còn thiếu
					alarmDescription,//fix tạm
					
					extra.getDisplayMessage(),
					checkedValue
					);
			listValueExtractAlarm.add(resultMonthlyValue);
		}

		private void processType8(List<ValueExtractAlarm> listValueExtractAlarm,
				List<AttendanceItemName> listAttdNameAdds, ExtraResultMonthlyDomainEventDto extra,
				EmployeeSearchDto employee, YearMonth yearMonth,Map<String, Map<YearMonth, Map<String,String>>> resultsData) {
			String checkedValue = resultsData.get(employee.id).get(yearMonth).get(extra.getErrorAlarmCheckID());//chưa có case nên để tạm
			String KAL010_100 = TextResource.localize("KAL010_100");
			
			String alarmDescription2 = "";
			List<ErAlAtdItemConAdapterDto> listErAlAtdItemCon = extra.getCheckConMonthly().getGroup1().getLstErAlAtdItemCon();
			
			//group 1 
			String alarmDescription1 = getDesGroup(listAttdNameAdds, extra, listErAlAtdItemCon);
//										if(listErAlAtdItemCon.size()>1)
//											alarmDescription1 = "("+alarmDescription1+")";
			
			if(extra.getCheckConMonthly().isGroup2UseAtr()) {
				List<ErAlAtdItemConAdapterDto> listErAlAtdItemCon2 = extra.getCheckConMonthly().getGroup2().getLstErAlAtdItemCon();
				//group 2 
				alarmDescription2 = getDesGroup(listAttdNameAdds, extra, listErAlAtdItemCon2);
			}
			String alarmDescriptionValue= "";
			if(extra.getCheckConMonthly().getOperatorBetweenGroups() ==0) {//AND
				if(!alarmDescription2.equals("")) {
					alarmDescriptionValue = "("+alarmDescription1+") AND ("+alarmDescription2+")";
				}else {
					alarmDescriptionValue = alarmDescription1;
				}
			}else{
				if(!alarmDescription2.equals("")) {
					alarmDescriptionValue = "("+alarmDescription1+") OR ("+alarmDescription2+")";
				}else {
					alarmDescriptionValue = alarmDescription1;
			}
			}
				
				
			ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
					employee.getWorkplaceId(),
					employee.getId(),
					this.toYMString(yearMonth),
					KAL010_100,
					extra.getNameAlarmExtraCon(),
					alarmDescriptionValue,	
					extra.getDisplayMessage(),TextResource.localize("KAL010_998"));
			listValueExtractAlarm.add(resultMonthlyValue);
		}

	private String getDesGroup(List<AttendanceItemName> listAttdNameAdds, ExtraResultMonthlyDomainEventDto extra, List<ErAlAtdItemConAdapterDto> listErAlAtdItemCon) {
		String alarmDescription = "";
		for(ErAlAtdItemConAdapterDto erAlAtdItemCon : listErAlAtdItemCon ) {
			int compare = erAlAtdItemCon.getCompareOperator();
		    String startValue ="";
			String endValue= "";
			String nameErrorAlarm = "";	
			List<AttendanceItemName> listAttdNameAddCompare = new ArrayList<>();
			//get name attdanceName 										
			if(erAlAtdItemCon.getConditionType()==0){
				 startValue = String.valueOf(erAlAtdItemCon.getCompareStartValue().intValue());											
				//if type = time
				if(erAlAtdItemCon.getConditionAtr() == 1) {
					startValue =this.timeToString(erAlAtdItemCon.getCompareStartValue().intValue());
				}	
			}else{
				Integer singleAtdItem[] = {Integer.valueOf(erAlAtdItemCon.getSingleAtdItem()) };												
				List<Integer> singleAtdItemList= Arrays.asList(singleAtdItem);
				listAttdNameAddCompare =  listAttdNameAdds.stream().filter(ati -> singleAtdItemList.contains(ati.getAttendanceItemId())).collect(Collectors.toList());
				startValue = getNameErrorAlarm(listAttdNameAddCompare,0,nameErrorAlarm);										 
			}
		
			List<AttendanceItemName> listAttdNameAdd =  listAttdNameAdds.stream().filter(ati -> erAlAtdItemCon.getCountableAddAtdItems().contains(ati.getAttendanceItemId())).collect(Collectors.toList());
			nameErrorAlarm = getNameErrorAlarm(listAttdNameAdd,0,nameErrorAlarm);//0 add atd item
			List<AttendanceItemName> listAttdNameSub =  listAttdNameAdds.stream().filter(ati -> erAlAtdItemCon.getCountableSubAtdItems().contains(ati.getAttendanceItemId())).collect(Collectors.toList());
			nameErrorAlarm = getNameErrorAlarm(listAttdNameSub,1,nameErrorAlarm);//1 sub atd item

			CompareOperatorText compareOperatorText = convertCompareType(compare);
			//0 : AND, 1 : OR
			String compareAndOr = "";
			if(extra.getCheckConMonthly().getGroup1().getConditionOperator() == 0) {
				compareAndOr = "AND";
			}else {
				compareAndOr = "OR";
			}
			if(!alarmDescription.equals("")) {
				alarmDescription += compareAndOr +" ";
			}
			if(compare<=5) {
					alarmDescription +=  nameErrorAlarm + " " + compareOperatorText.getCompareLeft()+" "+ startValue+" ";											
																										
			}else {
				endValue = String.valueOf(erAlAtdItemCon.getCompareEndValue().intValue());
				if(erAlAtdItemCon.getConditionAtr() == 1) {
					endValue =  this.timeToString(erAlAtdItemCon.getCompareEndValue().intValue()); 
				}
				if(compare>5 && compare<=7) {
					alarmDescription += startValue +" "+
							compareOperatorText.getCompareLeft()+ " "+
							nameErrorAlarm+ " "+
							compareOperatorText.getCompareright()+ " "+
							endValue+ " ";	
				}else {
					alarmDescription += nameErrorAlarm + " "+
							compareOperatorText.getCompareLeft()+ " "+
							startValue + ","+endValue+ " "+
							compareOperatorText.getCompareright()+ " "+
							nameErrorAlarm+ " " ;
				}
			}

					
		}
		return alarmDescription;
	}

		private boolean isError(Map<String, Map<YearMonth, Map<String, Integer>>> checkPerTimeMonActualResults,
				String eralId, String empId, YearMonth yearMonth) {
			if(checkPerTimeMonActualResults.containsKey(empId)){
				if(checkPerTimeMonActualResults.get(empId).containsKey(yearMonth)) {
					if(checkPerTimeMonActualResults.get(empId).get(yearMonth).containsKey(eralId)) {
						return checkPerTimeMonActualResults.get(empId).get(yearMonth).get(eralId) == 1;
					}
				}
			}
			return false;
		}
	
	//tab 3
	private List<ValueExtractAlarm> extraResultMonthly(String companyId,List<ExtraResultMonthlyDomainEventDto> listExtra,
			DatePeriod period, List<EmployeeSearchDto> employees) {
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>(); 
		List<YearMonth> lstYearMonth = period.yearMonthsBetween();
		
		String KAL010_206 = TextResource.localize("KAL010_206");
		String KAL010_204 = TextResource.localize("KAL010_204");
		String KAL010_100 = TextResource.localize("KAL010_100");
		GeneralDate tempStart = period.start();
		GeneralDate tempEnd = period.end();
		YearMonth startYearMonth = tempStart.yearMonth();
		YearMonth endYearMonth = tempEnd.yearMonth();
		YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(startYearMonth, endYearMonth);
		List<MonthlyRecordValuesImport> monthlyRecords;
		List<Integer> itemIds = Arrays.asList(202,203,204,205,206);
		
		for (ExtraResultMonthlyDomainEventDto extra : listExtra) {
			if(!extra.isUseAtr())
				continue;
			//HoiDD #1000436
			for (EmployeeSearchDto employee : employees) {
				if(extra.getTypeCheckItem() == TypeMonCheckItemImport.CHECK_REMAIN_NUMBER.value ){
					CheckRemainNumberMonFunImport checkRemainNumberMonFunImport = extra.getCheckRemainNumberMon();
					CompareSingleValueImport compareSingleValueImport = checkRemainNumberMonFunImport.getCompareSingleValueEx();
					CompareRangeImport compareRangeImport = checkRemainNumberMonFunImport.getCompareRangeEx();
					
					int typeOperator = checkRemainNumberMonFunImport.getCheckOperatorType();
					TypeCheckVacationImport typeCheckVacation = EnumAdaptor.valueOf(checkRemainNumberMonFunImport.getCheckVacation(), TypeCheckVacationImport.class);
					String alarmName = KAL010_100;
					String daysSingle = "";
					String daysRangeStart = "";
					String daysRangeEnd = "";
					CompareOperatorText compareOperatorText = new CompareOperatorText();
					int compareSingle = -1;
					int compareRange = -1;
					//0 - singlee -----1-range
					if (typeOperator == 0) {
						daysSingle = compareSingleValueImport.getValue().getDaysValue().toString();
						compareSingle = compareSingleValueImport.getCompareOperator();
						compareOperatorText = convertCompareType(compareSingle);
					}
					if (typeOperator == 1) {
						daysRangeStart = compareRangeImport.getStartValue().getDaysValue().toString();
						daysRangeEnd = compareRangeImport.getEndValue().getDaysValue().toString();
						compareRange = compareRangeImport.getCompareOperator();
						compareOperatorText = convertCompareType(compareRange);
					}


					String sid = employee.getId();
					
					switch (typeCheckVacation) {

						//ANNUAL_PAID_LEAVE
					case ANNUAL_PAID_LEAVE:
						List<AnnualLeaveUsageImport> annualLeaveUsageImports = getConfirmMonthlyAdapter.getListAnnualLeaveUsageImport(sid, yearMonthPeriod);
						for (AnnualLeaveUsageImport annualLeaveUsageImport : annualLeaveUsageImports) {
							boolean check = false;
							String alarmMessage = "";
							String itemName = TextResource.localize("KAL010_123");

							check = checkResultRemainMonthlyAdapter.checkAnnualLeaveUsage(checkRemainNumberMonFunImport, annualLeaveUsageImport);
							if(check){
								if(typeOperator == 0){
									alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
								}else {
									if(compareRange <=7 ){
										alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
												compareOperatorText.getCompareright()+daysRangeEnd;
									}else {
										alarmMessage = itemName+
												compareOperatorText.getCompareLeft()+
												daysRangeStart+", "+
												daysRangeEnd+
												compareOperatorText.getCompareright()+
												itemName;
									}
								}
								//add to list
//								String yearMonth = annualLeaveUsageImport.getYearMonth().year() + "/"+ (annualLeaveUsageImport.getYearMonth().month() < 10?"0"+annualLeaveUsageImport.getYearMonth().month() : annualLeaveUsageImport.getYearMonth().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										toYMString(annualLeaveUsageImport.getYearMonth()),
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage(),null
										);
								listValueExtractAlarm.add(resultCheckRemain);
							}
						}
						break;
						
						//SUB_HOLIDAY
					case SUB_HOLIDAY:
						List<DayoffCurrentMonthOfEmployeeImport> dayoffCurrentMonthOfEmployeeImports = getConfirmMonthlyAdapter.lstDayoffCurrentMonthOfEmployee(sid, startYearMonth, endYearMonth);
						for (DayoffCurrentMonthOfEmployeeImport dayoffCurrentMonthOfEmployeeImport : dayoffCurrentMonthOfEmployeeImports) {
							boolean check = false;
							String alarmMessage = "";
							String itemName = TextResource.localize("KAL010_124");
							check = checkResultRemainMonthlyAdapter.checkDayoffCurrentMonth(checkRemainNumberMonFunImport, dayoffCurrentMonthOfEmployeeImport);
							if(check){
								if(typeOperator == 0){
									alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
								}else {
									if(compareRange <=7 ){
									alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
											compareOperatorText.getCompareright()+daysRangeEnd;
									}else {
										alarmMessage = itemName+
												compareOperatorText.getCompareLeft()+
												daysRangeStart+", "+
												daysRangeEnd+
												compareOperatorText.getCompareright()+
												itemName;
									}
								}
								//add to list
//								String yearMonth = dayoffCurrentMonthOfEmployeeImport.getYm().year() + "/"+ (dayoffCurrentMonthOfEmployeeImport.getYm().month() < 10?"0"+dayoffCurrentMonthOfEmployeeImport.getYm().month() : dayoffCurrentMonthOfEmployeeImport.getYm().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										toYMString(dayoffCurrentMonthOfEmployeeImport.getYm()),
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage(),null
										);
								listValueExtractAlarm.add(resultCheckRemain);
							}
						}
						break;
						
						//PAUSE
					case PAUSE:
						List<StatusOfHolidayImported> statusOfHolidayImporteds = absenceReruitmentManaAdapter.getDataCurrentMonthOfEmployee(sid, startYearMonth, endYearMonth);
						for (StatusOfHolidayImported statusOfHolidayImported : statusOfHolidayImporteds) {
							boolean check = false;
							String alarmMessage = "";
							String itemName = TextResource.localize("KAL010_125");
							check = checkResultRemainMonthlyAdapter.checkStatusOfHoliday(checkRemainNumberMonFunImport, statusOfHolidayImported);
							if(check){
								if(typeOperator == 0){
									alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
								}else {
									if(compareRange <=7 ){
									alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
											compareOperatorText.getCompareright()+daysRangeEnd;
									}else {
										alarmMessage = itemName+
												compareOperatorText.getCompareLeft()+
												daysRangeStart+", "+
												daysRangeEnd+
												compareOperatorText.getCompareright()+
												itemName;
									}
								}
								//add to list
//								String yearMonth = statusOfHolidayImported.getYm().year() + "/"+ (statusOfHolidayImported.getYm().month() < 10?"0"+statusOfHolidayImported.getYm().month() : statusOfHolidayImported.getYm().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										toYMString(statusOfHolidayImported.getYm()),
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage(),null
										);
								listValueExtractAlarm.add(resultCheckRemain);
							}
						}
						
						break;
						
						//YEARLY_RESERVED
					case YEARLY_RESERVED:
						List<ReserveLeaveUsageImport> reserveLeaveUsageImports = getConfirmMonthlyAdapter.getListReserveLeaveUsageImport(sid, yearMonthPeriod);
						for (ReserveLeaveUsageImport reserveLeaveUsageImport : reserveLeaveUsageImports) {
							boolean check = false;
							String alarmMessage = "";
							String itemName = TextResource.localize("KAL010_126");
							check = checkResultRemainMonthlyAdapter.checkReserveLeaveUsage(checkRemainNumberMonFunImport, reserveLeaveUsageImport);
							if(check){
								if(typeOperator == 0){
									alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
								}else {
									if(compareRange <=7 ){
									alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
											compareOperatorText.getCompareright()+daysRangeEnd;
									}else {
										alarmMessage = itemName+
												compareOperatorText.getCompareLeft()+
												daysRangeStart+", "+
												daysRangeEnd+
												compareOperatorText.getCompareright()+
												itemName;
									}
								}
								//add to list
//								String yearMonth = reserveLeaveUsageImport.getYearMonth().year() + "/"+ (reserveLeaveUsageImport.getYearMonth().month() < 10?"0"+reserveLeaveUsageImport.getYearMonth().month() : reserveLeaveUsageImport.getYearMonth().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										toYMString(reserveLeaveUsageImport.getYearMonth()),
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage(),null
										);
								listValueExtractAlarm.add(resultCheckRemain);
							}
						}
						break;	
						
						//SPECIAL_HOLIDAY
					case SPECIAL_HOLIDAY:
						List<Integer> listSpeCode = checkRemainNumberMonFunImport.getListItemID();
						List<SpecialHolidayImported> specialHolidayImporteds= complileInPeriodOfSpecialLeaveAdapter.getSpeHoliOfConfirmedMonthly(sid, startYearMonth, endYearMonth, listSpeCode);
						for (SpecialHolidayImported specialHolidayImported : specialHolidayImporteds) {
							boolean check = false;
							String alarmMessage = "";
							String itemName = TextResource.localize("KAL010_115");
							check = checkResultRemainMonthlyAdapter.checkSpecialHoliday(checkRemainNumberMonFunImport, specialHolidayImported);
							if(check){
								if(typeOperator == 0){
									alarmMessage = itemName+compareOperatorText.getCompareLeft()+daysSingle;
								}else {
									if(compareRange <=7 ){
									alarmMessage = daysRangeStart+compareOperatorText.getCompareLeft()+itemName+
											compareOperatorText.getCompareright()+daysRangeEnd;
									}else {
										alarmMessage = itemName+
												compareOperatorText.getCompareLeft()+
												daysRangeStart+", "+
												daysRangeEnd+
												compareOperatorText.getCompareright()+
												itemName;
									}
								}
								//add to list
//								String yearMonth = specialHolidayImported.getYm().year() + "/"+ (specialHolidayImported.getYm().month() < 10?"0"+specialHolidayImported.getYm().month() : specialHolidayImported.getYm().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										toYMString(specialHolidayImported.getYm()),
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage(),null
										);
								listValueExtractAlarm.add(resultCheckRemain);
							}
						}
						
						break;
					
					default:
						break;
					}
				}
			}
			//End HoiDD #1000436
			
			for (YearMonth yearMonth : lstYearMonth) {
				for (EmployeeSearchDto employee : employees) {
					
					switch (extra.getTypeCheckItem()) {
					case 0:
//						boolean checkPublicHoliday = checkResultMonthlyAdapter.checkPublicHoliday(companyId, employee.getCode(), employee.getId(),
//								employee.getWorkplaceId(), true, yearMonth, extra.getSpecHolidayCheckCon());
//						if(checkPublicHoliday) {
//							ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
//									employee.getWorkplaceId(),
//									employee.getId(),
//									this.yearmonthToString(yearMonth),
//									TextResource.localize("KAL010_100"),
//									TextResource.localize("KAL010_209"),
//									TextResource.localize("KAL010_210"),
//									extra.getDisplayMessage()
//									);
//							listValueExtractAlarm.add(resultMonthlyValue);
//						}
						break;
					case 1: // 36協定エラー時間  
						// Call RQ 436
						monthlyRecords = checkResultMonthlyAdapter.getListMonthlyRecords(employee.getId(), yearMonthPeriod, itemIds);
						if (!CollectionUtil.isEmpty(monthlyRecords)) {
							List<Check36AgreementValueImport> lstReturnStatus = checkResultMonthlyAdapter
									.check36AgreementConditions(employee.getId(), monthlyRecords,
											extra.getAgreementCheckCon36(), Optional.empty());
							if (!CollectionUtil.isEmpty(lstReturnStatus)) {
								for (Check36AgreementValueImport check36AgreementValueImport : lstReturnStatus) {
									if (check36AgreementValueImport.isCheck36AgreementCon()) {
										ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
												employee.getWorkplaceId(), employee.getId(),
												this.yearmonthToString(yearMonth), KAL010_100,
												KAL010_204,
												TextResource.localize("KAL010_205",
														this.timeToString(check36AgreementValueImport.getErrorValue())),
												extra.getDisplayMessage(),null);
										listValueExtractAlarm.add(resultMonthlyValue);
									}
								}
							}
						}
						break;
					case 2: // 36協定アラーム時間 
						// Call RQ 436
						monthlyRecords = checkResultMonthlyAdapter.getListMonthlyRecords(employee.getId(), yearMonthPeriod, itemIds);
						if (!CollectionUtil.isEmpty(monthlyRecords)) {
							List<Check36AgreementValueImport> lstReturnStatus = checkResultMonthlyAdapter
									.check36AgreementConditions(employee.getId(), monthlyRecords,
											extra.getAgreementCheckCon36(), Optional.empty());
							if (!CollectionUtil.isEmpty(lstReturnStatus)) {
								for (Check36AgreementValueImport check36AgreementValueImport : lstReturnStatus) {
									if (check36AgreementValueImport.isCheck36AgreementCon()) {
										ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
												employee.getWorkplaceId(), employee.getId(),
												this.yearmonthToString(yearMonth), KAL010_100,
												KAL010_206,
												TextResource.localize("KAL010_207",
														this.timeToString(check36AgreementValueImport.getAlarmValue())),
												extra.getDisplayMessage(),null);
										listValueExtractAlarm.add(resultMonthlyValue);
									}
								}
							}
						}
						break;
					case 3 :
						break;
					default:
					//No 257
						Map<String, Integer> checkPerTimeMonActualResults = checkResultMonthlyAdapter.checkPerTimeMonActualResult(
								yearMonth, employee.getId(), extra.getCheckConMonthly());
						// Key of MAP : employeeID+yearMonth.toString()+closureID.toString()
						//ValueMap 0 = false, 1= true
//						String key =employee.getId().toString()+yearMonth.toString()+closureID.toString();
						//true
						checkPerTimeMonActualResults.forEach((k,v)->{
						if(v==1) {
							if(extra.getTypeCheckItem() ==8) {
								String alarmDescription1 = "";
								String alarmDescription2 = "";
								List<ErAlAtdItemConAdapterDto> listErAlAtdItemCon = extra.getCheckConMonthly().getGroup1().getLstErAlAtdItemCon();
								
								//group 1 
								for(ErAlAtdItemConAdapterDto erAlAtdItemCon : listErAlAtdItemCon ) {
									 
											int compare = erAlAtdItemCon.getCompareOperator();
										    String startValue ="";
											String endValue= "";
											String nameErrorAlarm = "";	
											List<AttendanceItemName> listAttdNameAddCompare = new ArrayList<>();
											//get name attdanceName 										
											if(erAlAtdItemCon.getConditionType()==0){
												 startValue = String.valueOf(erAlAtdItemCon.getCompareStartValue());											
												//if type = time
												if(erAlAtdItemCon.getConditionAtr() == 1) {
													startValue =this.timeToString(erAlAtdItemCon.getCompareStartValue().intValue());
												}	
											}else{
												Integer singleAtdItem[] = {Integer.valueOf(erAlAtdItemCon.getSingleAtdItem()) };												
												List<Integer> singleAtdItemList= Arrays.asList(singleAtdItem);
												listAttdNameAddCompare =  attdItemNameDomainService.getNameOfAttendanceItem(singleAtdItemList 
														, TypeOfItem.Monthly.value);
												startValue = getNameErrorAlarm(listAttdNameAddCompare,0,nameErrorAlarm);										 
											}
										
											List<AttendanceItemName> listAttdNameAdd =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemCon.getCountableAddAtdItems(), TypeOfItem.Monthly.value);
											nameErrorAlarm = getNameErrorAlarm(listAttdNameAdd,0,nameErrorAlarm);//0 add atd item
											List<AttendanceItemName> listAttdNameSub =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemCon.getCountableSubAtdItems(), TypeOfItem.Monthly.value);
											nameErrorAlarm = getNameErrorAlarm(listAttdNameSub,1,nameErrorAlarm);//1 sub atd item
							
											CompareOperatorText compareOperatorText = convertCompareType(compare);
											//0 : AND, 1 : OR
											String compareAndOr = "";
											if(extra.getCheckConMonthly().getGroup1().getConditionOperator() == 0) {
												compareAndOr = "AND";
											}else {
												compareAndOr = "OR";
											}
											if(!alarmDescription1.equals("")) {
												alarmDescription1 += compareAndOr +" ";
											}
											if(compare<=5) {
													alarmDescription1 +=  nameErrorAlarm + " " + compareOperatorText.getCompareLeft()+" "+ startValue+" ";											
																																		
											}else {
												endValue = String.valueOf(erAlAtdItemCon.getCompareEndValue());
												if(erAlAtdItemCon.getConditionAtr() == 1) {
													endValue =  this.timeToString(erAlAtdItemCon.getCompareEndValue().intValue()); 
												}
												if(compare>5 && compare<=7) {
													alarmDescription1 += startValue +" "+
															compareOperatorText.getCompareLeft()+ " "+
															nameErrorAlarm+ " "+
															compareOperatorText.getCompareright()+ " "+
															endValue+ " ";	
												}else {
													alarmDescription1 += nameErrorAlarm + " "+
															compareOperatorText.getCompareLeft()+ " "+
															startValue + ","+endValue+ " "+
															compareOperatorText.getCompareright()+ " "+
															nameErrorAlarm+ " " ;
												}
											}

											
								}
//								if(listErAlAtdItemCon.size()>1)
//									alarmDescription1 = "("+alarmDescription1+")";
								
								if(extra.getCheckConMonthly().isGroup2UseAtr()) {
									List<ErAlAtdItemConAdapterDto> listErAlAtdItemCon2 = extra.getCheckConMonthly().getGroup2().getLstErAlAtdItemCon();
									//group 2 
									for(ErAlAtdItemConAdapterDto erAlAtdItemCon2 : listErAlAtdItemCon2 ) {
										int compare = erAlAtdItemCon2.getCompareOperator();
										String startValue ="";
										String endValue= "";
										String nameErrorAlarm = "";
										List<AttendanceItemName> listAttdNameAddCompare = new ArrayList<>();
										//get name attdanceName 
										if(erAlAtdItemCon2.getConditionType()==0){
											//if type = time
											 startValue = String.valueOf(erAlAtdItemCon2.getCompareStartValue());
											if(erAlAtdItemCon2.getConditionAtr() == 1) {
											startValue = this.timeToString(erAlAtdItemCon2.getCompareStartValue().intValue());
											}
										}else{
											Integer singleAtdItem[] = {Integer.valueOf(erAlAtdItemCon2.getSingleAtdItem()) };												
											List<Integer> singleAtdItemList= Arrays.asList(singleAtdItem);
											listAttdNameAddCompare =  attdItemNameDomainService.getNameOfAttendanceItem(singleAtdItemList 
													, TypeOfItem.Monthly.value);
											startValue = getNameErrorAlarm(listAttdNameAddCompare,0,nameErrorAlarm);
											
										}
										
										List<AttendanceItemName> listAttdNameAdd =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemCon2.getCountableAddAtdItems(), TypeOfItem.Monthly.value);
										nameErrorAlarm = getNameErrorAlarm(listAttdNameAdd,0,nameErrorAlarm);//0 add atd item
										List<AttendanceItemName> listAttdNameSub =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemCon2.getCountableSubAtdItems(), TypeOfItem.Monthly.value);
										nameErrorAlarm = getNameErrorAlarm(listAttdNameSub,1,nameErrorAlarm);//1 sub atd item
										
										CompareOperatorText compareOperatorText = convertCompareType(compare);
										//0 : AND, 1 : OR
										String compareAndOr = "";
										if(extra.getCheckConMonthly().getGroup2().getConditionOperator() == 0) {
											compareAndOr = "AND";
										}else {
											compareAndOr = "OR";
										}
										if(!alarmDescription2.equals("")) {
											alarmDescription2 += compareAndOr +" ";
										}
										if(compare<=5) {
											alarmDescription2 +=  nameErrorAlarm + " " + compareOperatorText.getCompareLeft()+" "+ startValue+" ";
										}else {
											endValue = String.valueOf(erAlAtdItemCon2.getCompareEndValue());
											if(erAlAtdItemCon2.getConditionAtr() == 1) {
												endValue = this.timeToString(erAlAtdItemCon2.getCompareEndValue().intValue());
											}
											if(compare>5 && compare<=7) {
												alarmDescription2 += startValue +" "+
														compareOperatorText.getCompareLeft()+ " "+
														nameErrorAlarm+ " "+
														compareOperatorText.getCompareright()+ " "+
														endValue+ " ";	
											}else {
												alarmDescription2 += nameErrorAlarm + " "+
														compareOperatorText.getCompareLeft()+ " "+
														startValue + ","+endValue+ " "+
														compareOperatorText.getCompareright()+ " "+
														nameErrorAlarm+ " " ;
											}
										}
									}//end for
									
									
									
								}
								String alarmDescriptionValue= "";
								if(extra.getCheckConMonthly().getOperatorBetweenGroups() ==0) {//AND
									if(!alarmDescription2.equals("")) {
										alarmDescriptionValue = "("+alarmDescription1+") AND ("+alarmDescription2+")";
									}else {
										alarmDescriptionValue = alarmDescription1;
									}
								}else{
									if(!alarmDescription2.equals("")) {
										alarmDescriptionValue = "("+alarmDescription1+") OR ("+alarmDescription2+")";
									}else {
										alarmDescriptionValue = alarmDescription1;
								}
								}
									
									
								ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										this.yearmonthToString(yearMonth),
										KAL010_100,
										TextResource.localize("KAL010_60"),
										alarmDescriptionValue,	
										extra.getDisplayMessage(),null
										);
								listValueExtractAlarm.add(resultMonthlyValue);
							}else {
								ErAlAtdItemConAdapterDto erAlAtdItemConAdapterDto = extra.getCheckConMonthly().getGroup1().getLstErAlAtdItemCon().get(0);
								int compare = erAlAtdItemConAdapterDto.getCompareOperator();
								Double startValue = erAlAtdItemConAdapterDto.getCompareStartValue().doubleValue();
								Double endValue = erAlAtdItemConAdapterDto.getCompareEndValue().doubleValue();
								CompareOperatorText compareOperatorText = convertCompareType(compare);
								String nameErrorAlarm = "";
								//0 is monthly,1 is dayly
								List<AttendanceItemName> listAttdNameAdd =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemConAdapterDto.getCountableAddAtdItems(), TypeOfItem.Monthly.value);
								nameErrorAlarm = getNameErrorAlarm(listAttdNameAdd,0,nameErrorAlarm);//0 add atd item
								List<AttendanceItemName> listAttdNameSub =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemConAdapterDto.getCountableSubAtdItems(), TypeOfItem.Monthly.value);
								nameErrorAlarm = getNameErrorAlarm(listAttdNameSub,1,nameErrorAlarm);//1 sub atd item
								
								String nameItem = "";
								String alarmDescription = "";
								switch(extra.getTypeCheckItem()) {
								case 4 ://時間
									nameItem = TextResource.localize("KAL010_47");
									String startValueTime = this.timeToString(startValue.intValue());
									String endValueTime = "";
									if(compare<=5) {
										alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueTime);
									}else {
										endValueTime = this.timeToString(endValue.intValue());
										if(compare>5 && compare<=7) {
											alarmDescription = TextResource.localize("KAL010_277",startValueTime,
													compareOperatorText.getCompareLeft(),
													nameErrorAlarm,
													compareOperatorText.getCompareright()+
													endValueTime
													);	
										}else {
											alarmDescription = TextResource.localize("KAL010_277",
													nameErrorAlarm,
													compareOperatorText.getCompareLeft(),
													startValueTime + ","+endValueTime,
													compareOperatorText.getCompareright()+
													nameErrorAlarm
													);
										}
									}
									
									break;
								case 5 ://日数
									nameItem = TextResource.localize("KAL010_113");
									String startValueDays = String.valueOf(startValue);
									String endValueDays = "";
									if(compare<=5) {
										alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueDays);
									}else {
										endValueDays = String.valueOf(endValue);
										if(compare>5 && compare<=7) {
											alarmDescription = TextResource.localize("KAL010_277",startValueDays,
													compareOperatorText.getCompareLeft(),
													nameErrorAlarm,
													compareOperatorText.getCompareright(),
													endValueDays
													);	
										}else {
											alarmDescription = TextResource.localize("KAL010_277",
													nameErrorAlarm,
													compareOperatorText.getCompareLeft(),
													startValueDays + "," + endValueDays,
													compareOperatorText.getCompareright(),
													nameErrorAlarm
													);
										}
									}
									break;
								case 6 ://回数
									nameItem = TextResource.localize("KAL010_50");
									String startValueTimes = String.valueOf(startValue.intValue());
									String endValueTimes = "";
									if(compare<=5) {
										alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueTimes);
									}else {
										endValueTimes = String.valueOf(endValue.intValue());
										if(compare>5 && compare<=7) {
											alarmDescription = TextResource.localize("KAL010_277",startValueTimes,
													compareOperatorText.getCompareLeft(),
													nameErrorAlarm,
													compareOperatorText.getCompareright()+
													endValueTimes
													);	
										}else {
											alarmDescription = TextResource.localize("KAL010_277",
													nameErrorAlarm,
													compareOperatorText.getCompareLeft(),
													startValueTimes + "," + endValueTimes,
													compareOperatorText.getCompareright()+
													nameErrorAlarm
													);
										}
									}
									break;
								case 7 ://金額 money
									nameItem = TextResource.localize("KAL010_51");
									String startValueMoney = String.valueOf(startValue.intValue());
									String endValueMoney = "";
									if(compare<=5) {
										alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueMoney);
									}else {
										endValueMoney = String.valueOf(endValue.intValue());
										if(compare>5 && compare<=7) {
											alarmDescription = TextResource.localize("KAL010_277",startValueMoney,
													compareOperatorText.getCompareLeft(),
													nameErrorAlarm,
													compareOperatorText.getCompareright()+
													endValueMoney
													);	
										}else {
											alarmDescription = TextResource.localize("KAL010_277",
													nameErrorAlarm,
													compareOperatorText.getCompareLeft(),
													startValueMoney + "," + endValueMoney,
													compareOperatorText.getCompareright()+
													nameErrorAlarm
													);
										}
									}
									break;
								default : break;
								}
								
								
								
								ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										this.yearmonthToString(yearMonth),
										KAL010_100,
										nameItem,
										//TODO : còn thiếu
										alarmDescription,//fix tạm
										
										extra.getDisplayMessage(),null
										);
								listValueExtractAlarm.add(resultMonthlyValue);
							}
						}
						});// close for Map
						break;
					}
					
					//this.checkPublicHoliday(companyId,employee.getWorkplaceCode(), employee.getId(),employee.getWorkplaceId(), true, yearMonth);
				}
			}
		}
		
		return listValueExtractAlarm;
	}
	
	private String timeToString(int value ){
		if(value%60<10){
			return  String.valueOf(value/60)+":0"+  String.valueOf(value%60);
		}
		return String.valueOf(value/60)+":"+  String.valueOf(value%60);
	}
	
	private String yearmonthToString(YearMonth yearMonth){
		if(yearMonth.month()<10){
			return  String.valueOf(yearMonth.year())+"/0"+  String.valueOf(yearMonth.month());
		}
		return String.valueOf(yearMonth.year())+"/"+  String.valueOf(yearMonth.month());
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

	
	/**
	 * get period of check unused holiday
	 * @param currentPeriod
	 * @param deadlCheckMonth
	 * @return
	 */
	private YearMonth getDeadlCheckMonth(DatePeriod currentPeriod, int deadlCheckMonth) {
		GeneralDate endCurrentDate = currentPeriod.end();
		int currentMonth = endCurrentDate.month();
		int currentYear = endCurrentDate.year();
		
		int monthCheck = currentMonth - deadlCheckMonth;
		
		if (monthCheck <= 0) {
			monthCheck = monthCheck + 12; 
			currentYear = currentYear - 1;
		}
		
		return YearMonth.of(currentYear, monthCheck);
	}
	//End HiepTH
}
