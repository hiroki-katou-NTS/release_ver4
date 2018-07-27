package nts.uk.ctx.at.function.dom.alarm.alarmlist.monthly;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.adapter.ResponseImprovementAdapter;
import nts.uk.ctx.at.function.dom.adapter.checkresultmonthly.Check36AgreementValueImport;
import nts.uk.ctx.at.function.dom.adapter.checkresultmonthly.CheckResultMonthlyAdapter;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlAtdItemConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.ExtraResultMonthlyFunAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.FixedExtraMonFunAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.FixedExtraMonFunImport;
import nts.uk.ctx.at.function.dom.adapter.sysfixedcheckcondition.SysFixedCheckConMonAdapter;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.monthly.MonAlarmCheckCon;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.monthly.dtoevent.ExtraResultMonthlyDomainEventDto;
import nts.uk.ctx.at.function.dom.attendanceitemname.AttendanceItemName;
import nts.uk.ctx.at.function.dom.attendanceitemname.service.AttendanceItemNameDomainService;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
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
	private AttendanceItemNameDomainService attdItemNameDomainService;
	
	public List<ValueExtractAlarm> monthlyAggregateProcess(String companyID , String  checkConditionCode,DatePeriod period,List<EmployeeSearchDto> employees){
		
		
		
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
		listValueExtractAlarm.addAll(this.extractMonthlyFixed(listFixed, period, employeesDto));
		//tab 3
		
		listValueExtractAlarm.addAll(this.extraResultMonthly(companyID, listExtra, period, employeesDto));
		
		return listValueExtractAlarm;
	}
	//tab 2
	private List<ValueExtractAlarm> extractMonthlyFixed(List<FixedExtraMonFunImport> listFixed,
			DatePeriod period, List<EmployeeSearchDto> employees) {
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>();
		List<YearMonth> lstYearMonth = period.yearMonthsBetween();
		GeneralDate lastDateInPeriod = period.end();
		
		
		
		for(EmployeeSearchDto employee : employees) {
			//社員(list)に対応する処理締めを取得する(get closing xử lý đối ứng với employee (List))
			Closure closure = closureService.getClosureDataByEmployee(employee.getId(), GeneralDate.today());
			int closureID= closure.getClosureId().value;
			ClosureDate closureDate = null;
			for(ClosureHistory ClosureHistory :closure.getClosureHistories() ) {
				String endYM = StringUtils.leftPad(String.valueOf(ClosureHistory.getEndYearMonth().month()), 2, '0');
				GeneralDate endDateYearMonthly = GeneralDate.fromString(String.valueOf(ClosureHistory.getEndYearMonth().year()) + '-' 
						+ endYM + '-' + String.valueOf(ClosureHistory.getEndYearMonth().lastDateInMonth()), "yyyy-MM-dd");
				String startYM = StringUtils.leftPad(String.valueOf(ClosureHistory.getStartYearMonth().month()), 2, '0');
				GeneralDate startDateYearMonthly = GeneralDate.fromString(String.valueOf(ClosureHistory.getStartYearMonth().year()) + '-' 
						+ startYM + '-' +"01", "yyyy-MM-dd");
				if(lastDateInPeriod.beforeOrEquals(endDateYearMonthly) && lastDateInPeriod.afterOrEquals(startDateYearMonthly)){
					closureDate = ClosureHistory.getClosureDate();
					break;
				}
			}
			for (YearMonth yearMonth : lstYearMonth) {
				for(int i = 0;i<listFixed.size();i++) {
					if(listFixed.get(i).isUseAtr()) {
//						FixedExtraMonFunImport fixedExtraMon = listFixed.get(i);
						switch(i) {
							case 0 :
								Optional<ValueExtractAlarm> unconfirmed = sysFixedCheckConMonAdapter.checkMonthlyUnconfirmed(employee.getId(), yearMonth.v().intValue());
								if(unconfirmed.isPresent()) {
									unconfirmed.get().setAlarmValueMessage(listFixed.get(i).getMessage());
									unconfirmed.get().setWorkplaceID(Optional.ofNullable(employee.getWorkplaceId()));
									String dateString = unconfirmed.get().getAlarmValueDate().substring(0, 7);
									unconfirmed.get().setAlarmValueDate(dateString);
									listValueExtractAlarm.add(unconfirmed.get());
								}
								
							break;
							case 1 :break;//chua co
							case 2 :break;//chua co
							case 3 :break;//chua co
							case 4 :
								Optional<ValueExtractAlarm> agreement = sysFixedCheckConMonAdapter.checkAgreement(employee.getId(), yearMonth.v().intValue(),closureID,closureDate);
								if(agreement.isPresent()) {
									agreement.get().setAlarmValueMessage(listFixed.get(i).getMessage());
									agreement.get().setWorkplaceID(Optional.ofNullable(employee.getWorkplaceId()));
									String dateAgreement = agreement.get().getAlarmValueDate().substring(0, 7);
									agreement.get().setAlarmValueDate(dateAgreement);
									listValueExtractAlarm.add(agreement.get());
								}
							break;
							default : break; // so 6 : chua co
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
		GeneralDate lastDateInPeriod = period.end();
		
		
		
		for (ExtraResultMonthlyDomainEventDto extra : listExtra) {
			for (YearMonth yearMonth : lstYearMonth) {
				for (EmployeeSearchDto employee : employees) {
					
					//社員(list)に対応する処理締めを取得する(get closing xử lý đối ứng với employee (List))
					Closure closure = closureService.getClosureDataByEmployee(employee.getId(), GeneralDate.today());
					int closureID= closure.getClosureId().value;
					ClosureDate closureDate = null;
					for(ClosureHistory ClosureHistory :closure.getClosureHistories() ) {
						GeneralDate endDateYearMonthly = GeneralDate.fromString(String.valueOf(ClosureHistory.getEndYearMonth().year()) + '-' 
								+ StringUtils.leftPad(String.valueOf(ClosureHistory.getEndYearMonth().month()), 2, '0')  + '-' 
								+ String.valueOf(ClosureHistory.getEndYearMonth().lastDateInMonth()), "yyyy-MM-dd");
						GeneralDate startDateYearMonthly = GeneralDate.fromString(String.valueOf(ClosureHistory.getStartYearMonth().year()) + '-'
								+ StringUtils.leftPad(String.valueOf(ClosureHistory.getStartYearMonth().month()), 2, '0') + '-'
								+ "01", "yyyy-MM-dd");
						if(lastDateInPeriod.beforeOrEquals(endDateYearMonthly) && lastDateInPeriod.afterOrEquals(startDateYearMonthly)){
							closureDate = ClosureHistory.getClosureDate();
							break;
						}
					}
					
					
					
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
					case 1:
						Check36AgreementValueImport checkAgreementError = checkResultMonthlyAdapter.check36AgreementCondition(employee.getId(),
								yearMonth,closureID,closureDate,extra.getAgreementCheckCon36());
						
						if(checkAgreementError.isCheck36AgreementCon()) {
							ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
									employee.getWorkplaceId(),
									employee.getId(),
									this.yearmonthToString(yearMonth),
									TextResource.localize("KAL010_100"),
									TextResource.localize("KAL010_204"),
									TextResource.localize("KAL010_205",
									this.timeToString(checkAgreementError.getErrorValue())), 
									extra.getDisplayMessage()
									);
							listValueExtractAlarm.add(resultMonthlyValue);
						}
						break;
					case 2:
						Check36AgreementValueImport checkAgreementAlarm = checkResultMonthlyAdapter.check36AgreementCondition(employee.getId(),
								yearMonth,closureID,closureDate,extra.getAgreementCheckCon36());
						if(checkAgreementAlarm.isCheck36AgreementCon()) {
							ValueExtractAlarm resultMonthlyValue = new ValueExtractAlarm(
									employee.getWorkplaceId(),
									employee.getId(),
									this.yearmonthToString(yearMonth),
									TextResource.localize("KAL010_100"),
									TextResource.localize("KAL010_206"),
									TextResource.localize("KAL010_207",
									this.timeToString(checkAgreementAlarm.getAlarmValue())),
									extra.getDisplayMessage()
									);
							listValueExtractAlarm.add(resultMonthlyValue);
						}
						if(true) {
							
						}
						break;
					case 3 :
						break;
					default:
						boolean checkPerTimeMonActualResult = checkResultMonthlyAdapter.checkPerTimeMonActualResult(
								yearMonth, closureID,closureDate, employee.getId(), extra.getCheckConMonthly());
						if(checkPerTimeMonActualResult) {
							if(extra.getTypeCheckItem() ==8) {
								String alarmDescription1 = "";
								String alarmDescription2 = "";
								List<ErAlAtdItemConAdapterDto> listErAlAtdItemCon = extra.getCheckConMonthly().getGroup1().getLstErAlAtdItemCon();
								
								//group 1 
								for(ErAlAtdItemConAdapterDto erAlAtdItemCon : listErAlAtdItemCon ) {
									int compare = erAlAtdItemCon.getCompareOperator();
									String startValue = String.valueOf(erAlAtdItemCon.getCompareStartValue());
									String endValue= "";
									String nameErrorAlarm = "";
									//get name attdanceName 
									if(!erAlAtdItemCon.getCountableAddAtdItems().isEmpty()) {
										List<AttendanceItemName> listAttdName =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemCon.getCountableAddAtdItems(), 0);
										nameErrorAlarm = listAttdName.get(0).getAttendanceItemName();
									}else {
										List<AttendanceItemName> listAttdName =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemCon.getCountableSubAtdItems(), 0);
										nameErrorAlarm = listAttdName.get(0).getAttendanceItemName();
									}
									//if type = time
									if(erAlAtdItemCon.getConditionAtr() == 1) {
										startValue =this.timeToString(erAlAtdItemCon.getCompareStartValue().intValue());
									}
									CompareOperatorText compareOperatorText = convertCompareType(compare);
									//0 : AND, 1 : OR
									String compareAndOr = "";
									if(extra.getCheckConMonthly().getGroup1().getConditionOperator() == 0) {
										compareAndOr = "AND";
									}else {
										compareAndOr = "OR";
									}
									if(!alarmDescription1.equals("")) {
										alarmDescription1 = compareAndOr +" "+ alarmDescription1;
									}
									if(compare<=5) {
										alarmDescription1 =  nameErrorAlarm + " " + compareOperatorText.getCompareLeft()+" "+ startValue+" ";
//												startValueTime,nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueTime;
									}else {
										endValue = String.valueOf(erAlAtdItemCon.getCompareEndValue().intValue());
										if(erAlAtdItemCon.getConditionAtr() == 1) {
											endValue =  this.timeToString(erAlAtdItemCon.getCompareEndValue().intValue()); 
										}
										if(compare>5 && compare<=7) {
											alarmDescription1 = startValue +" "+
													compareOperatorText.getCompareLeft()+ " "+
													nameErrorAlarm+ " "+
													compareOperatorText.getCompareright()+ " "+
													endValue+ " ";	
										}else {
											alarmDescription1 = nameErrorAlarm + " "+
													compareOperatorText.getCompareLeft()+ " "+
													startValue + ","+endValue+ " "+
													compareOperatorText.getCompareright()+ " "+
													nameErrorAlarm+ " " ;
										}
									}
								}
								if(!alarmDescription1.equals(""))
									alarmDescription1 = "("+alarmDescription1+")";
								
								if(extra.getCheckConMonthly().isGroup2UseAtr()) {
									List<ErAlAtdItemConAdapterDto> listErAlAtdItemCon2 = extra.getCheckConMonthly().getGroup1().getLstErAlAtdItemCon();
									//group 2 
									for(ErAlAtdItemConAdapterDto erAlAtdItemCon2 : listErAlAtdItemCon2 ) {
										int compare = erAlAtdItemCon2.getCompareOperator();
										String startValue = String.valueOf(erAlAtdItemCon2.getCompareStartValue());
										String endValue= "";
										String nameErrorAlarm = "";
										//get name attdanceName 
										if(!erAlAtdItemCon2.getCountableAddAtdItems().isEmpty()) {
											List<AttendanceItemName> listAttdName =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemCon2.getCountableAddAtdItems(), 0);
											nameErrorAlarm = listAttdName.get(0).getAttendanceItemName();
										}else {
											List<AttendanceItemName> listAttdName =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemCon2.getCountableSubAtdItems(), 0);
											nameErrorAlarm = listAttdName.get(0).getAttendanceItemName();
										}
										//if type = time
										if(erAlAtdItemCon2.getConditionAtr() == 1) {
											startValue = this.timeToString(erAlAtdItemCon2.getCompareStartValue().intValue());
										}
										CompareOperatorText compareOperatorText = convertCompareType(compare);
										//0 : AND, 1 : OR
										String compareAndOr = "";
										if(extra.getCheckConMonthly().getGroup1().getConditionOperator() == 0) {
											compareAndOr = "AND";
										}else {
											compareAndOr = "OR";
										}
										if(!alarmDescription2.equals("")) {
											alarmDescription2 = compareAndOr +" "+ alarmDescription2;
										}
										if(compare<=5) {
											alarmDescription2 =  nameErrorAlarm + " " + compareOperatorText.getCompareLeft()+" "+ startValue+" ";
//													startValueTime,nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueTime;
										}else {
											endValue = String.valueOf(erAlAtdItemCon2.getCompareEndValue().intValue());
											if(erAlAtdItemCon2.getConditionAtr() == 1) {
												endValue = this.timeToString(erAlAtdItemCon2.getCompareEndValue().intValue());
											}
											if(compare>5 && compare<=7) {
												alarmDescription2 = startValue +" "+
														compareOperatorText.getCompareLeft()+ " "+
														nameErrorAlarm+ " "+
														compareOperatorText.getCompareright()+ " "+
														endValue+ " ";	
											}else {
												alarmDescription2 = nameErrorAlarm + " "+
														compareOperatorText.getCompareLeft()+ " "+
														startValue + ","+endValue+ " "+
														compareOperatorText.getCompareright()+ " "+
														nameErrorAlarm+ " " ;
											}
										}
									}//end for
									
									if(!alarmDescription2.equals(""))
										alarmDescription2 = "("+alarmDescription2+")";
								}
								String alarmDescriptionValue= "";
								if(extra.getCheckConMonthly().getOperatorBetweenGroups() ==0) {//AND
									alarmDescriptionValue = "("+alarmDescription1+") AND ("+alarmDescription2+")";
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
								BigDecimal startValue = erAlAtdItemConAdapterDto.getCompareStartValue();
								BigDecimal endValue = erAlAtdItemConAdapterDto.getCompareEndValue();
								CompareOperatorText compareOperatorText = convertCompareType(compare);
								String nameErrorAlarm = "";
								//0 is monthly,1 is dayly
								if(!erAlAtdItemConAdapterDto.getCountableAddAtdItems().isEmpty()) {
									List<AttendanceItemName> listAttdName =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemConAdapterDto.getCountableAddAtdItems(), 0);
									nameErrorAlarm = listAttdName.get(0).getAttendanceItemName();
								}else {
									List<AttendanceItemName> listAttdName =  attdItemNameDomainService.getNameOfAttendanceItem(erAlAtdItemConAdapterDto.getCountableSubAtdItems(), 0);
									nameErrorAlarm = listAttdName.get(0).getAttendanceItemName();
								}
								
								
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
													compareOperatorText.getCompareright(),
													endValueTime
													);	
										}else {
											alarmDescription = TextResource.localize("KAL010_277",
													nameErrorAlarm,
													compareOperatorText.getCompareLeft(),
													startValueTime + ","+endValueTime,
													compareOperatorText.getCompareright(),
													nameErrorAlarm
													);
										}
									}
									
									break;
								case 5 ://日数
									nameItem = TextResource.localize("KAL010_113");
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
									nameItem = TextResource.localize("KAL010_50");
									String startValueTimes = String.valueOf(startValue.intValue());
									String endValueTimes = "";
									if(compare<=5) {
										alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueTimes);
									}else {
										endValueDays = String.valueOf(endValue.intValue());
										if(compare>5 && compare<=7) {
											alarmDescription = TextResource.localize("KAL010_277",startValueTimes,
													compareOperatorText.getCompareLeft(),
													nameErrorAlarm,
													compareOperatorText.getCompareright(),
													endValueTimes
													);	
										}else {
											alarmDescription = TextResource.localize("KAL010_277",
													nameErrorAlarm,
													compareOperatorText.getCompareLeft(),
													startValueTimes + "," + endValueTimes,
													compareOperatorText.getCompareright(),
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
										alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueMoney+".00");
									}else {
										endValueDays = String.valueOf(endValue.intValue());
										if(compare>5 && compare<=7) {
											alarmDescription = TextResource.localize("KAL010_277",startValueMoney+".00",
													compareOperatorText.getCompareLeft(),
													nameErrorAlarm,
													compareOperatorText.getCompareright(),
													endValueMoney+".00"
													);	
										}else {
											alarmDescription = TextResource.localize("KAL010_277",
													nameErrorAlarm,
													compareOperatorText.getCompareLeft(),
													startValueMoney + ".00," + endValueMoney+".00",
													compareOperatorText.getCompareright(),
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
		case 0 :/* 等しい（＝） */
			compare.setCompareLeft("＝");
			compare.setCompareright("");
			break; 
		case 1 :/* 等しくない（≠） */
			compare.setCompareLeft("≠");
			compare.setCompareright("");
			break; 
		case 2 :/* より大きい（＞） */
			compare.setCompareLeft("＞");
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
		case 5 :/* 以下（≦） */
			compare.setCompareLeft("≦");
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
			compare.setCompareLeft("＞");
			compare.setCompareright("＞");
			break;
		
		default :/* 範囲の外（境界値を含む）（≧≦） */
			compare.setCompareLeft("≧");
			compare.setCompareright("≧");
			break; 
		}
		
		return compare;
	}
		

}
