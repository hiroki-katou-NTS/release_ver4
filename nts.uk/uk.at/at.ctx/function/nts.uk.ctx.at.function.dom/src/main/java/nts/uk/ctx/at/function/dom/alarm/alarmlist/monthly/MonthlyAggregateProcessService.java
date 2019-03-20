package nts.uk.ctx.at.function.dom.alarm.alarmlist.monthly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
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
import nts.uk.ctx.at.function.dom.adapter.workrecord.identificationstatus.identityconfirmprocess.IdentityConfirmProcessImport;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
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
		listValueExtractAlarm.addAll(this.extractMonthlyFixed(listFixed, period, employeesDto, companyID, approvalProcessImport, identityConfirmProcessImport));
		//tab 3
		
		listValueExtractAlarm.addAll(this.extraResultMonthly(companyID, listExtra, period, employeesDto));
		
		return listValueExtractAlarm;
	}
	//tab 2
	private List<ValueExtractAlarm> extractMonthlyFixed(List<FixedExtraMonFunImport> listFixed,
			DatePeriod period, List<EmployeeSearchDto> employees, String companyID ,ApprovalProcessImport approvalProcessImport ,
			IdentityConfirmProcessImport identityConfirmProcessImport) {
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>();
		List<YearMonth> lstYearMonth = period.yearMonthsBetween();
		
		for(EmployeeSearchDto employee : employees) {
			// No 395 with FIX_EXTRA_ITEM_MON_NO 6
			for (FixedExtraMonFunImport fixedExtraMonFunImport : listFixed) {
				if(fixedExtraMonFunImport.getFixedExtraItemMonNo() == SysFixedMonPerEralEnum.CHECK_DEADLINE_HOLIDAY.value
						&& fixedExtraMonFunImport.isUseAtr()){
					Optional<List<ValueExtractAlarm>> valueExtractAlarms = extractErrorAlarmForHoliday(fixedExtraMonFunImport, employee, companyID,lstYearMonth.get(0));
					if (valueExtractAlarms.isPresent()) {
						listValueExtractAlarm.addAll(valueExtractAlarms.get());
					}
				}
			}
			
			for (YearMonth yearMonth : lstYearMonth) {
				for(FixedExtraMonFunImport fixedExtraMonFunImport : listFixed) {
					if(fixedExtraMonFunImport.isUseAtr()) {
						SysFixedMonPerEralEnum fixedExtraMon = EnumAdaptor.valueOf(fixedExtraMonFunImport.getFixedExtraItemMonNo(), SysFixedMonPerEralEnum.class);
						switch(fixedExtraMon) {
							case MON_UNCONFIRMED : // FIX_EXTRA_ITEM_MON_NO 1
								List<ValueExtractAlarm> unconfirmeds = sysFixedCheckConMonAdapter.checkMonthlyUnconfirmeds(employee.getId(), yearMonth.v().intValue(), identityConfirmProcessImport);
								if(!CollectionUtil.isEmpty(unconfirmeds)) {
									for (ValueExtractAlarm valueExtractAlarm : unconfirmeds) {
										if(valueExtractAlarm!=null){
											valueExtractAlarm.setComment(Optional.ofNullable(fixedExtraMonFunImport.getMessage()));
											valueExtractAlarm.setWorkplaceID(Optional.ofNullable(employee.getWorkplaceId()));
											String dateString = valueExtractAlarm.getAlarmValueDate().substring(0, 7);
											valueExtractAlarm.setAlarmValueDate(dateString);
											listValueExtractAlarm.add(valueExtractAlarm);
										}
									}
								}
							break;
							case WITH_MON_CORRECTION : // FIX_EXTRA_ITEM_MON_NO 2 
								List<ValueExtractAlarm> unconfirmedsAdmin = sysFixedCheckConMonAdapter.checkMonthlyUnconfirmedsAdmin(employee.getId(), yearMonth, approvalProcessImport);
								if(!CollectionUtil.isEmpty(unconfirmedsAdmin)) {
									for (ValueExtractAlarm valueExtractAlarm : unconfirmedsAdmin) {
										if(valueExtractAlarm!=null){
											valueExtractAlarm.setComment(Optional.ofNullable(fixedExtraMonFunImport.getMessage()));
											valueExtractAlarm.setWorkplaceID(Optional.ofNullable(employee.getWorkplaceId()));
											String dateString = valueExtractAlarm.getAlarmValueDate().substring(0, 7);
											valueExtractAlarm.setAlarmValueDate(dateString);
											listValueExtractAlarm.add(valueExtractAlarm);
										}
									}
								}
								break;
							case CHECK_DEADLINE_HOLIDAY :break;//FIX_EXTRA_ITEM_MON_NO 6 has been split flow above
							//case 3 :break;//chua co
							//case 4 :
								//ticket #100053
//								Optional<ValueExtractAlarm> agreement = sysFixedCheckConMonAdapter.checkAgreement(employee.getId(), yearMonth.v().intValue(),closureID.get(),closureDate.get());
//								if(agreement.isPresent()) {
//									agreement.get().setAlarmValueMessage(listFixed.get(i).getMessage());
//									agreement.get().setWorkplaceID(Optional.ofNullable(employee.getWorkplaceId()));
//									String dateAgreement = agreement.get().getAlarmValueDate().substring(0, 7);
//									agreement.get().setAlarmValueDate(dateAgreement);
//									listValueExtractAlarm.add(agreement.get());
//								}
							//break;
							default : break; 
						}//end switch
						
					}
						
				}
			}
			
		}
		
		return listValueExtractAlarm;
	}
	
	
	//tab 3
	private List<ValueExtractAlarm> extraResultMonthly(String companyId,List<ExtraResultMonthlyDomainEventDto> listExtra,
			DatePeriod period, List<EmployeeSearchDto> employees) {
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>(); 
		List<YearMonth> lstYearMonth = period.yearMonthsBetween();
		
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
					String alarmName = TextResource.localize("KAL010_100");
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
								String yearMonth = annualLeaveUsageImport.getYearMonth().year() + "/"+ (annualLeaveUsageImport.getYearMonth().month() < 10?"0"+annualLeaveUsageImport.getYearMonth().month() : annualLeaveUsageImport.getYearMonth().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										yearMonth,
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage()
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
								String yearMonth = dayoffCurrentMonthOfEmployeeImport.getYm().year() + "/"+ (dayoffCurrentMonthOfEmployeeImport.getYm().month() < 10?"0"+dayoffCurrentMonthOfEmployeeImport.getYm().month() : dayoffCurrentMonthOfEmployeeImport.getYm().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										yearMonth,
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage()
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
								String yearMonth = statusOfHolidayImported.getYm().year() + "/"+ (statusOfHolidayImported.getYm().month() < 10?"0"+statusOfHolidayImported.getYm().month() : statusOfHolidayImported.getYm().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										yearMonth,
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage()
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
								String yearMonth = reserveLeaveUsageImport.getYearMonth().year() + "/"+ (reserveLeaveUsageImport.getYearMonth().month() < 10?"0"+reserveLeaveUsageImport.getYearMonth().month() : reserveLeaveUsageImport.getYearMonth().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										yearMonth,
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage()
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
								String yearMonth = specialHolidayImported.getYm().year() + "/"+ (specialHolidayImported.getYm().month() < 10?"0"+specialHolidayImported.getYm().month() : specialHolidayImported.getYm().month());
								ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
										employee.getWorkplaceId(),
										employee.getId(),
										yearMonth,
										alarmName,
										itemName,
										alarmMessage,	
										extra.getDisplayMessage()
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
												this.yearmonthToString(yearMonth), TextResource.localize("KAL010_100"),
												TextResource.localize("KAL010_204"),
												TextResource.localize("KAL010_205",
														this.timeToString(check36AgreementValueImport.getErrorValue())),
												extra.getDisplayMessage());
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
												this.yearmonthToString(yearMonth), TextResource.localize("KAL010_100"),
												TextResource.localize("KAL010_206"),
												TextResource.localize("KAL010_207",
														this.timeToString(check36AgreementValueImport.getAlarmValue())),
												extra.getDisplayMessage());
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
										TextResource.localize("KAL010_100"),
										TextResource.localize("KAL010_60"),
										alarmDescriptionValue,	
										extra.getDisplayMessage()
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
										TextResource.localize("KAL010_100"),
										nameItem,
										//TODO : còn thiếu
										alarmDescription,//fix tạm
										
										extra.getDisplayMessage()
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

	
	//Added by HiepTH
	/**
	 * 代休の消化期限チェック
	 * @param listFixed
	 * @param employees
	 * @param companyID
	 * @return
	 */
	private Optional<List<ValueExtractAlarm>> extractErrorAlarmForHoliday(FixedExtraMonFunImport fixedExtraMonFunImport, EmployeeSearchDto employee, String companyID,YearMonth yearMonth) {
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>();
		
		GeneralDate today = GeneralDate.today();
		CompensatoryLeaveComSetting compensatoryLeaveComSetting = compensLeaveComSetRepository.find(companyID);
		int deadlCheckMonth = compensatoryLeaveComSetting.getCompensatoryAcquisitionUse().getDeadlCheckMonth().value + 1;
		Closure closure = closureService.getClosureDataByEmployee(employee.getId(), today);

		if (closure == null) {
			return Optional.empty();
		}
		//締めのアルゴリズム「当月の期間を算出する」を実行する
		DatePeriod periodCurrentMonth = closureService.getClosurePeriod(closure.getClosureId().value,
				closure.getClosureMonth().getProcessingYm());
		
		//代休期限アラーム基準日を決定する
		DatePeriod periodCheckDealMonth = closureService.getClosurePeriod(closure.getClosureId().value,
				getDeadlCheckMonth(periodCurrentMonth, deadlCheckMonth));

		//RequestList No.203 期間内の休出代休残数を取得する
		//集計開始日
		GeneralDate startPeriod = periodCurrentMonth.start();
		//集計終了日
		GeneralDate endPeriod = periodCurrentMonth.end().addYears(1);
		DatePeriod newPeriod = new DatePeriod(startPeriod, endPeriod);
		
		BreakDayOffRemainMngParam param = new BreakDayOffRemainMngParam(companyID, employee.getId(),
				newPeriod, false, periodCurrentMonth.end(), false, Collections.emptyList(),
				Collections.emptyList(), Collections.emptyList());
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
				for (BreakDayOffDetail breakDayOffDetail : lstExtractData) {
					ValueExtractAlarm valueExractAlarm = new ValueExtractAlarm();
					valueExractAlarm.setEmployeeID(employee.getId());
					valueExractAlarm.setWorkplaceID(Optional.ofNullable(employee.getWorkplaceId()));
					valueExractAlarm.setAlarmValueDate(yearmonthToString(yearMonth));
					valueExractAlarm.setClassification(TextResource.localize("KAL010_100"));
					valueExractAlarm.setAlarmItem(TextResource.localize("KAL010_278"));
					valueExractAlarm.setAlarmValueMessage(TextResource.localize("KAL010_279",
							String.valueOf(deadlCheckMonth), breakDayOffDetail.getYmdData().getDayoffDate().get().toString(),
							String.valueOf(breakDayOffDetail.getUnUserOfBreak().get().getUnUsedDays())));
					valueExractAlarm.setComment(Optional.ofNullable(fixedExtraMonFunImport.getMessage()));
					listValueExtractAlarm.add(valueExractAlarm);
				}
			}
		}
		return Optional.of(listValueExtractAlarm);
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
