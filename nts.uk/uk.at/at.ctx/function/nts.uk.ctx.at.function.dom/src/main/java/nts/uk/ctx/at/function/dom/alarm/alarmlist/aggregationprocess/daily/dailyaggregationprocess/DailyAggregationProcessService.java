package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.daily.dailyaggregationprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.FixedConWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.adapter.FixedConWorkRecordAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.WorkRecordExtraConAdapter;
import nts.uk.ctx.at.function.dom.adapter.WorkRecordExtraConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlAtdItemConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlConAttendanceItemAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.fixedcheckitem.FixedCheckItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.worklocation.RecordWorkInfoFunAdapter;
import nts.uk.ctx.at.function.dom.adapter.worklocation.RecordWorkInfoFunAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.ErAlWorkRecordCheckAdapter;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.PeriodByAlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.daily.DailyAlarmCondition;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.DailyAttendanceItem;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.repository.DailyAttendanceItemNameDomainService;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class DailyAggregationProcessService {

	@Inject
	private AlarmCheckConditionByCategoryRepository alCheckConByCategoryRepo;

	@Inject
	private FixedConWorkRecordAdapter fixedConWorkRecordAdapter;
	
	@Inject
	private FixedCheckItemAdapter fixedCheckItemAdapter;
	
	@Inject
	private RecordWorkInfoFunAdapter recordWorkInfoFunAdapter;
	
	@Inject
	private DailyPerformanceService dailyPerformanceService;
	
	@Inject
	private ErAlWorkRecordCheckAdapter erAlWorkRecordCheckAdapter;
	
	@Inject
	private WorkRecordExtraConAdapter workRecordExtraConAdapter;
	
	// Get work type name
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	// Get attendance name
	@Inject	
	private DailyAttendanceItemNameDomainService attendanceNameService;


	public List<ValueExtractAlarm> dailyAggregationProcess(String checkConditionCode, PeriodByAlarmCategory period,
			EmployeeSearchDto employee) {
		
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>(); 		
		String companyID = AppContexts.user().companyId();
		
		// ドメインモデル「カテゴリ別アラームチェック条件」を取得する
		Optional<AlarmCheckConditionByCategory> alCheckConByCategory = alCheckConByCategoryRepo.find(companyID,
				AlarmCategory.DAILY.value, checkConditionCode);

		// カテゴリアラームチェック条件．抽出条件を元に日次データをチェックする
		DailyAlarmCondition dailyAlarmCondition = (DailyAlarmCondition) alCheckConByCategory.get()
				.getExtractionCondition();

		// tab2: 日別実績のエラーアラーム
		List<ValueExtractAlarm> extractDailyRecord = this.extractDailyRecord(dailyAlarmCondition, period, employee, companyID);
		listValueExtractAlarm.addAll(extractDailyRecord);
		
		// tab3: チェック条件
		List<ValueExtractAlarm> extractCheckCondition =this.extractCheckCondition(dailyAlarmCondition, period, employee, companyID);
		listValueExtractAlarm.addAll(extractCheckCondition);
		
		// tab4: 「システム固定のチェック項目」で実績をチェックする			
		List<ValueExtractAlarm> extractFixedCondition = this.extractFixedCondition(dailyAlarmCondition, period, employee);
		listValueExtractAlarm.addAll(extractFixedCondition);
		
		return listValueExtractAlarm;
	}
	
	// tab2: 日別実績のエラーアラーム
	private List<ValueExtractAlarm> extractDailyRecord(DailyAlarmCondition dailyAlarmCondition,
			PeriodByAlarmCategory period, EmployeeSearchDto employee, String companyID) {
		return dailyPerformanceService.aggregationProcess(dailyAlarmCondition, period, employee, companyID);
	}
	
	// tab3: チェック条件
	private List<ValueExtractAlarm> extractCheckCondition(DailyAlarmCondition dailyAlarmCondition, PeriodByAlarmCategory period, EmployeeSearchDto employee, String companyID) {
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>();
		List<WorkRecordExtraConAdapterDto> listWorkRecordExtraCon=  workRecordExtraConAdapter.getAllWorkRecordExtraConByListID(dailyAlarmCondition.getExtractConditionWorkRecord());
		Map<String, WorkRecordExtraConAdapterDto> mapWorkRecordExtraCon = listWorkRecordExtraCon.stream().collect(Collectors.toMap(WorkRecordExtraConAdapterDto::getErrorAlarmCheckID, x->x));
		
		for(GeneralDate date : period.getListDate()) {
			
			Map<String, Map<String, Boolean>> mapErAlCheckIdANDEmployee =  erAlWorkRecordCheckAdapter.check(date, Arrays.asList(employee.getId()), dailyAlarmCondition.getExtractConditionWorkRecord());
	
			mapErAlCheckIdANDEmployee.entrySet().stream().forEach(e ->{
				if(e.getValue().get(employee.getId())) {
					listValueExtractAlarm.add(checkConditionGenerateValue(employee, date, mapWorkRecordExtraCon.get(e.getKey()), companyID));
				} 
				
			});
		}
		return listValueExtractAlarm;
	}
	
	private ValueExtractAlarm checkConditionGenerateValue(EmployeeSearchDto employee, GeneralDate date, WorkRecordExtraConAdapterDto workRecordExtraCon,  String companyID) {
		String alarmItem = "";
		String alarmContent = "";
		TypeCheckWorkRecord checkItem = EnumAdaptor.valueOf(workRecordExtraCon.getCheckItem(), TypeCheckWorkRecord.class);
		switch (checkItem) {
			case TIME:
				alarmItem = TextResource.localize("KAL010_47");
				break;
			case TIMES:
				alarmItem = TextResource.localize("KAL010_50");
				break;
			case AMOUNT_OF_MONEY:
				alarmItem = TextResource.localize("KAL010_51");
				break;
			case TIME_OF_DAY:
				alarmItem = TextResource.localize("KAL010_52");
				break;
			case CONTINUOUS_TIME:
				alarmItem = TextResource.localize("KAL010_53");
				break;				
			case CONTINUOUS_WORK:
				alarmItem = TextResource.localize("KAL010_56");
				break;
			case CONTINUOUS_TIME_ZONE:
				alarmItem = TextResource.localize("KAL010_58"); 
				break;
			case CONTINUOUS_CONDITION:
				alarmItem = TextResource.localize("KAL010_60"); 
				break;
			default:
				break;
		}
		
		alarmContent = checkConditionGenerateAlarmContent(checkItem, workRecordExtraCon , companyID);		
		
		ValueExtractAlarm result = new ValueExtractAlarm(employee.getWorkplaceId(), employee.getId(), date.toString(), TextResource.localize("KAL010_1"), alarmItem, alarmContent,
				workRecordExtraCon.getErrorAlarmCondition().getDisplayMessage());
		return result;
	}
	
	private String  checkConditionGenerateAlarmContent(TypeCheckWorkRecord checkItem,  WorkRecordExtraConAdapterDto workRecordExtraCon, String companyID) {
		
		if(workRecordExtraCon.getErrorAlarmCondition().getAtdItemCondition().getGroup1().getLstErAlAtdItemCon().isEmpty()) return "";			
		
		String alarmContent ="";
		String wktypeText="";
		String attendanceText ="";
		String wktimeText ="";	
		
		ErAlAtdItemConAdapterDto atdItemCon = workRecordExtraCon.getErrorAlarmCondition().getAtdItemCondition().getGroup1().getLstErAlAtdItemCon().get(0);		
		
		CoupleOperator coupleOperator= findOperator(atdItemCon.getCompareOperator());
		
		switch (checkItem) {
		case TIME:
		case TIMES:
		case AMOUNT_OF_MONEY:
		case TIME_OF_DAY:	
			wktypeText = calculateWktypeText(workRecordExtraCon, companyID);
			attendanceText =  calculateAttendanceText(atdItemCon);
					
			if (!singleCompare(atdItemCon.getCompareOperator())) {
				
				if(betweenRange(atdItemCon.getCompareOperator())) {
					alarmContent = TextResource.localize("KAL010_49", wktypeText, atdItemCon.getCompareStartValue().toString(), coupleOperator.getOperatorStart(), attendanceText,
							coupleOperator.getOperatorEnd(), atdItemCon.getCompareEndValue().toString());
				}else {
					alarmContent = TextResource.localize("KAL010_121", wktypeText, attendanceText, coupleOperator.getOperatorStart(), atdItemCon.getCompareStartValue().toString(),
							atdItemCon.getCompareEndValue().toString(), coupleOperator.getOperatorEnd(), attendanceText);
				}
				
			} else {
				if (atdItemCon.getConditionType() == ConditionType.FIXED_VALUE.value) {
					alarmContent = TextResource.localize("KAL010_48", wktypeText, attendanceText, coupleOperator.getOperatorStart(), atdItemCon.getCompareStartValue().toString());
				} else {
					alarmContent = TextResource.localize("KAL010_48", wktypeText, attendanceText, coupleOperator.getOperatorStart(), atdItemCon.getSingleAtdItem() + "");
				}
			}
			break;
		case CONTINUOUS_TIME:
			wktypeText = calculateWktypeText( workRecordExtraCon, companyID);
			attendanceText =  calculateAttendanceText( atdItemCon);
			
			if (!singleCompare(atdItemCon.getCompareOperator())) {
				if(betweenRange(atdItemCon.getCompareOperator())) {
					alarmContent = TextResource.localize("KAL010_55", wktypeText, atdItemCon.getCompareStartValue().toString(), coupleOperator.getOperatorStart(), attendanceText,
							coupleOperator.getOperatorEnd(), atdItemCon.getCompareEndValue().toString(), workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + "");
				}else {
					alarmContent = TextResource.localize("KAL010_122", wktypeText, attendanceText , coupleOperator.getOperatorStart(),  atdItemCon.getCompareStartValue().toString(), 
							 atdItemCon.getCompareEndValue().toString(),
							coupleOperator.getOperatorEnd(), attendanceText,  workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + "");
				}

			} else {
				if (atdItemCon.getConditionType() == ConditionType.FIXED_VALUE.value) {
					alarmContent = TextResource.localize("KAL010_54", wktypeText, attendanceText, coupleOperator.getOperatorStart(), atdItemCon.getCompareStartValue().toString(),
							workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + "");
				} else {
					alarmContent = TextResource.localize("KAL010_54", wktypeText, attendanceText, coupleOperator.getOperatorStart(), atdItemCon.getSingleAtdItem() + "",
							workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + "");
				}
			} 
			break;
		case CONTINUOUS_WORK: 
			wktypeText = calculateWktypeText( workRecordExtraCon, companyID);
			
			alarmContent = TextResource.localize("KAL010_57", wktypeText, workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + ""); 
			break;
		case CONTINUOUS_TIME_ZONE: 
			wktypeText = calculateWktypeText(workRecordExtraCon, companyID);
			wktimeText = calculateWkTimeText(workRecordExtraCon);
			
			alarmContent = TextResource.localize("KAL010_59", wktypeText, wktimeText ,  workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + ""); 
			break;
		case CONTINUOUS_CONDITION:
			String alarmGroup1= "";
			String alarmGroup2 ="";
			ErAlConAttendanceItemAdapterDto group1 = workRecordExtraCon.getErrorAlarmCondition().getAtdItemCondition().getGroup1();
			alarmGroup1 = generateAlarmGroup(group1);
			ErAlConAttendanceItemAdapterDto group2 = workRecordExtraCon.getErrorAlarmCondition().getAtdItemCondition().getGroup1();
			alarmGroup2 = generateAlarmGroup(group2);
			if(alarmGroup1.length()!=0 && alarmGroup2.length() !=0 ) {
				alarmContent = "(" + alarmGroup1 + ")" + logicalOperator( workRecordExtraCon.getErrorAlarmCondition().getAtdItemCondition().getOperatorBetweenGroups()) + "(" + alarmGroup2 + ")";
			}else {
				alarmContent = alarmGroup1+ alarmGroup2;
			}
			break;
		default:
			break;			 
		}

		return alarmContent;
		
	}
	
	private String generateAlarmGroup(ErAlConAttendanceItemAdapterDto group ) {
		String alarmGroup= "";		
		
		if (!group.getLstErAlAtdItemCon().isEmpty()) {
			for (int i = 0; i < group.getLstErAlAtdItemCon().size(); i++) {
				ErAlAtdItemConAdapterDto itemCon = group.getLstErAlAtdItemCon().get(i);
				CoupleOperator coupleOperator = findOperator(itemCon.getCompareOperator());
				
				String alarm = "";
				if (singleCompare(itemCon.getCompareOperator())) {
					if (itemCon.getConditionType() == ConditionType.FIXED_VALUE.value) {
						alarm = "(式" + (i + 1) + calculateAttendanceText(itemCon) + coupleOperator.getOperatorStart() + itemCon.getCompareStartValue() + ")";
					} else {
						alarm = "(式" + (i + 1) + calculateAttendanceText(itemCon) + coupleOperator.getOperatorStart() + itemCon.getSingleAtdItem() + ")";
					}

				} else {
					if (betweenRange(itemCon.getCompareOperator())) {
						alarm = "(式" + (i + 1) + itemCon.getCompareStartValue() + coupleOperator.getOperatorStart() + calculateAttendanceText(itemCon) + coupleOperator.getOperatorEnd()
								+ itemCon.getCompareEndValue() + ")";
					} else {
						alarm = "(式" + (i + 1) + calculateAttendanceText(itemCon) + coupleOperator.getOperatorStart() + itemCon.getCompareStartValue() + ", " + itemCon.getCompareEndValue()
								+ coupleOperator.getOperatorEnd() + calculateAttendanceText(itemCon) + ")";
					}
				}

				if (alarmGroup.length() == 0) {
					alarmGroup += alarm;
				} else {
					alarmGroup +=logicalOperator(group.getConditionOperator()) + alarm;
				}
			}
		}

		return alarmGroup;
	}
	
	private String calculateWktypeText( WorkRecordExtraConAdapterDto workRecordExtraCon,String companyID) {
		
		List<String> workTypeCodes =  workRecordExtraCon.getErrorAlarmCondition().getWorkTypeCondition().getPlanLstWorkType();
		List<String> workTypeNames = workTypeRepository.findNotDeprecatedByListCode(companyID, workTypeCodes).stream().map( x ->x.getName().toString()).collect(Collectors.toList());
		return workTypeNames.isEmpty()? "":  String.join(",", workTypeNames);
	}
	
	private String calculateAttendanceText(ErAlAtdItemConAdapterDto atdItemCon) {
		String attendanceText = "";		
		
		if(atdItemCon.getConditionAtr()==ConditionAtr.TIME_WITH_DAY.value) {
			if(atdItemCon.getUncountableAtdItem()>0) {
				List<DailyAttendanceItem>  listAttendance =attendanceNameService.getNameOfDailyAttendanceItem(Arrays.asList(atdItemCon.getUncountableAtdItem()));
				if(!listAttendance.isEmpty()) attendanceText = listAttendance.get(0).getAttendanceItemName();
			}
			
		}else {
			if(!atdItemCon.getCountableAddAtdItems().isEmpty()) {
				List<DailyAttendanceItem>  listAttendanceAdd = attendanceNameService.getNameOfDailyAttendanceItem(atdItemCon.getCountableAddAtdItems());
				if(!listAttendanceAdd.isEmpty()) {
					for( int i=0 ; i< listAttendanceAdd.size(); i++) {
						String operator = (i == (listAttendanceAdd.size() - 1)) ? "" : " + ";
						attendanceText += listAttendanceAdd.get(i).getAttendanceItemName() + operator;
					}
				}
				if(! atdItemCon.getCountableSubAtdItems().isEmpty()) {
					List<DailyAttendanceItem>  listAttendanceSub = attendanceNameService.getNameOfDailyAttendanceItem(atdItemCon.getCountableAddAtdItems());
					if(!listAttendanceSub.isEmpty()) {
						for(int i=0; i< listAttendanceSub.size(); i++) {
                            String operator = (i == (listAttendanceSub.size() - 1)) ? "" : " - ";
                            String beforeOperator = (i == 0) ? " - " : "";
                            attendanceText += beforeOperator + listAttendanceSub.get(i).getAttendanceItemName() + operator;
						}
					}
				}
			}else if(! atdItemCon.getCountableSubAtdItems().isEmpty()) {
				List<DailyAttendanceItem>  listAttendanceSub = attendanceNameService.getNameOfDailyAttendanceItem(atdItemCon.getCountableAddAtdItems());
				if(!listAttendanceSub.isEmpty()) {
					for(int i=0; i< listAttendanceSub.size(); i++) {
                        String operator = (i == (listAttendanceSub.size() - 1)) ? "" : " - ";
                        String beforeOperator = (i == 0) ? " - " : "";
                        attendanceText += beforeOperator + listAttendanceSub.get(i).getAttendanceItemName() + operator;
					}
				}				
			}
		}
		return attendanceText;
	}
	
	private String calculateWkTimeText( WorkRecordExtraConAdapterDto workRecordExtraCon) {
		
		List<Integer> listWorkTimeIds = workRecordExtraCon.getErrorAlarmCondition().getWorkTimeCondition().getPlanLstWorkTime().stream().map(e -> Integer.parseInt(e))
				.collect(Collectors.toList());
		List<String> workTimeNames = attendanceNameService.getNameOfDailyAttendanceItem(listWorkTimeIds).stream().map( e-> e.getAttendanceItemName()).collect(Collectors.toList());
		return  workTimeNames.isEmpty()? "":  String.join(",", workTimeNames);
		
	}
	
	private CoupleOperator findOperator(int compareOperator) {
		CompareType compareType = EnumAdaptor.valueOf(compareOperator, CompareType.class);
		
		switch (compareType) {
			case EQUAL:
				return new CoupleOperator("＝", "");
			case NOT_EQUAL:
				return new CoupleOperator("≠", "");
			case GREATER_THAN:
				return new CoupleOperator("＞", "");
			case GREATER_OR_EQUAL:
				return new CoupleOperator("≧", "");
			case LESS_THAN:
				return new CoupleOperator("＜", "");
			case LESS_OR_EQUAL:
				return new CoupleOperator("≦", "");
			case BETWEEN_RANGE_OPEN:
				return new CoupleOperator("＜", "＞");
			case BETWEEN_RANGE_CLOSED:
				return new CoupleOperator("≦", "≧");
			case OUTSIDE_RANGE_OPEN:
				return new CoupleOperator("＞", "＜");
			case OUTSIDE_RANGE_CLOSED:
				return new CoupleOperator("≧", "≦");
		}
		
		return null;
	}
	
	private boolean singleCompare(int compareOperator ) {
		return compareOperator <= CompareType.LESS_OR_EQUAL.value;
	}
	private boolean betweenRange(int compareOperator ) {
		return compareOperator == CompareType.BETWEEN_RANGE_OPEN.value || compareOperator == CompareType.BETWEEN_RANGE_CLOSED.value;
	}
	
	
	private String logicalOperator(int logicalOperator) {
		if (logicalOperator == LogicalOperator.AND.value)
			return " AND ";
		else
			return " OR ";
	}
	
	// tab4: 「システム固定のチェック項目」で実績をチェックする	
	private List<ValueExtractAlarm> extractFixedCondition(DailyAlarmCondition dailyAlarmCondition, PeriodByAlarmCategory period, EmployeeSearchDto employee){
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>(); 
		
		//get data by dailyAlarmCondition
		List<FixedConWorkRecordAdapterDto> listFixed =  fixedConWorkRecordAdapter.getAllFixedConWorkRecordByID(dailyAlarmCondition.getDailyAlarmConID());
		for(int i = 0;i < listFixed.size();i++) {
			if(listFixed.get(i).isUseAtr()) {
				switch(i) {
				case 0 :
					for(GeneralDate date : period.getListDate()) {
						String workType = null;
						Optional<RecordWorkInfoFunAdapterDto> recordWorkInfo = recordWorkInfoFunAdapter.getInfoCheckNotRegister(employee.getId(), date);
						if(recordWorkInfo.isPresent()) {
							workType = recordWorkInfo.get().getWorkTypeCode();
						}
						
						Optional<ValueExtractAlarm> checkWorkType = fixedCheckItemAdapter.checkWorkTypeNotRegister(employee.getWorkplaceId(),employee.getId(), date, workType);
						if(checkWorkType.isPresent()) {
							listValueExtractAlarm.add(checkWorkType.get());
						}
						
					}
					break;
				case 1 :
					for(GeneralDate date : period.getListDate()) {
						String workTime = null;
						Optional<RecordWorkInfoFunAdapterDto> recordWorkInfoFunAdapterDto = recordWorkInfoFunAdapter.getInfoCheckNotRegister(employee.getId(), date);
						if(recordWorkInfoFunAdapterDto.isPresent()) {
							workTime = recordWorkInfoFunAdapterDto.get().getWorkTimeCode();
						}
						Optional<ValueExtractAlarm> checkWorkTime = fixedCheckItemAdapter.checkWorkTimeNotRegister(employee.getWorkplaceId(),employee.getId(), date, workTime);
						if(checkWorkTime.isPresent()) {
							listValueExtractAlarm.add(checkWorkTime.get());
						} 
					}
					break;
				case 2 : 
					 List<ValueExtractAlarm> listCheckPrincipalUnconfirm = fixedCheckItemAdapter.checkPrincipalUnconfirm(employee.getWorkplaceId(), employee.getId(), period.getStartDate(), period.getEndDate());
					 if(!listCheckPrincipalUnconfirm.isEmpty()) {
						 listValueExtractAlarm.addAll(listCheckPrincipalUnconfirm);
					 }
					break;
				case 3 :
					List<ValueExtractAlarm> listCheckAdminUnverified = fixedCheckItemAdapter.checkAdminUnverified(employee.getWorkplaceId(), employee.getId(), period.getStartDate(), period.getEndDate());
					if(!listCheckAdminUnverified.isEmpty()) {
						 listValueExtractAlarm.addAll(listCheckAdminUnverified);
					 }
					break;
				default :
					List<ValueExtractAlarm> listCheckingData = fixedCheckItemAdapter.checkingData(employee.getWorkplaceId(),employee.getId(), period.getStartDate(), period.getEndDate());
					if(!listCheckingData.isEmpty()) {
						 listValueExtractAlarm.addAll(listCheckingData);
					 }
					break;
				}//end switch
			}//end if
		}//end for
		return listValueExtractAlarm;
	}
}
