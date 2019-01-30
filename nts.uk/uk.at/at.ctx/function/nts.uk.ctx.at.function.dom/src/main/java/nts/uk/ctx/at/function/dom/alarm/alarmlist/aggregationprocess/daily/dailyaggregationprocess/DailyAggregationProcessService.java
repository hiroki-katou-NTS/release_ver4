package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.daily.dailyaggregationprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
//import nts.arc.error.BusinessException;
import nts.arc.task.AsyncTask;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.DailyAttendanceItemAdapter;
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
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.ErrorRecordImport;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.PeriodByAlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.daily.DailyAlarmCondition;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.DailyAttendanceItem;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.repository.DailyAttendanceItemNameDomainService;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

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
	//get all attendance ID
	@Inject
	private DailyAttendanceItemAdapter dailyAttendanceItemAdapter;

	
	public List<ValueExtractAlarm> dailyAggregationProcess(String comId, String checkConditionCode, PeriodByAlarmCategory period, List<EmployeeSearchDto> employees,
			DatePeriod datePeriod) {
		 List<String> employeeIds = employees.stream().map( e ->e.getId()).collect(Collectors.toList());
		
		List<ValueExtractAlarm> listValueExtractAlarm = Collections.synchronizedList(new ArrayList<>()); 		
		
		// ドメインモデル「カテゴリ別アラームチェック条件」を取得する
		alCheckConByCategoryRepo.find(comId, AlarmCategory.DAILY.value, checkConditionCode).ifPresent(alCheckConByCategory -> {
			// カテゴリアラームチェック条件．抽出条件を元に日次データをチェックする
			DailyAlarmCondition dailyAlarmCondition = (DailyAlarmCondition) alCheckConByCategory.getExtractionCondition();

			// tab2: 日別実績のエラーアラーム
			listValueExtractAlarm.addAll(this.extractDailyRecord(dailyAlarmCondition, datePeriod, employees, comId, employeeIds));
			
			// tab3: チェック条件
			listValueExtractAlarm.addAll(this.extractCheckCondition(dailyAlarmCondition, datePeriod, employees, comId, employeeIds));
			
			// tab4: 「システム固定のチェック項目」で実績をチェックする
			List<ValueExtractAlarm> fixed = Collections.synchronizedList(new ArrayList<>());
//					employees.stream().map(e -> this.extractFixedCondition(dailyAlarmCondition, period, e))
//					.flatMap(List::stream).collect(Collectors.toList());
			/** 並列処理、AsyncTask */
			// Create thread pool.
			ExecutorService executorService = Executors.newFixedThreadPool(5);
			CountDownLatch countDownLatch = new CountDownLatch(employees.size());
			
			employees.forEach(employee -> {
				AsyncTask task = AsyncTask.builder()
						.withContexts()
						.keepsTrack(true)
						.threadName(this.getClass().getName())
						.build(() -> {
							try {
								fixed.addAll(this.extractFixedCondition(dailyAlarmCondition, period, employee));
							} catch (Exception e2) {
								e2.printStackTrace();
								
							}						
							// Count down latch.
							countDownLatch.countDown();
						});
				executorService.submit(task);
			});
			// Wait for latch until finish.
			try {
				countDownLatch.await();
			} catch (InterruptedException ie) {
				throw new RuntimeException(ie);						
			} finally {
				// Force shut down executor services.
				executorService.shutdown();
				
			}
			listValueExtractAlarm.addAll(fixed);
		});
		
		return listValueExtractAlarm;
	}

	
	// tab2: 日別実績のエラーアラーム
	private List<ValueExtractAlarm> extractDailyRecord(DailyAlarmCondition dailyAlarmCondition,
			DatePeriod period, List<EmployeeSearchDto> employee, String companyID, List<String> employeeIds) {
		return dailyPerformanceService.aggregationProcess(dailyAlarmCondition, period, employeeIds, employee, companyID);
	}

	
	// tab3: チェック条件
	private List<ValueExtractAlarm> extractCheckCondition(DailyAlarmCondition dailyAlarmCondition, DatePeriod period, 
			List<EmployeeSearchDto> employee, String companyID, List<String> emIds) {
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>();
		List<WorkRecordExtraConAdapterDto> listWorkRecordExtraCon=  workRecordExtraConAdapter.getAllWorkRecordExtraConByListID(dailyAlarmCondition.getExtractConditionWorkRecord());
		Map<String, WorkRecordExtraConAdapterDto> mapWorkRecordExtraCon = listWorkRecordExtraCon.stream().collect(Collectors.toMap(WorkRecordExtraConAdapterDto::getErrorAlarmCheckID, x->x));
		List<ErrorRecordImport> listErrorRecord = erAlWorkRecordCheckAdapter.check(dailyAlarmCondition.getExtractConditionWorkRecord(), period, emIds);
		/**fix response*/
		//get nameAlarmItem by type
		List<AlarmItemName> listAlarmItemName = this.getAlarmItemByType();
		//get all attendance ID and all name
		List<Integer> listAttdID = dailyAttendanceItemAdapter.getDailyAttendanceItemList(companyID).stream()
				.map(c->c.getAttendanceItemId()).collect(Collectors.toList());
		List<DailyAttendanceItem> listAttenDanceItem = attendanceNameService.getNameOfDailyAttendanceItem(listAttdID);
		// Get map all name Attd Name
		Map<Integer,DailyAttendanceItem> mapAttdName = listAttenDanceItem.stream().collect(Collectors.toMap(DailyAttendanceItem::getAttendanceItemId, x -> x));
		for(ErrorRecordImport errorRecord : listErrorRecord) {
			if(errorRecord.isError()) {
				EmployeeSearchDto em = employee.stream().filter(e -> e.getId().equals(errorRecord.getEmployeeId())).findFirst().get();
				String alarmItem = "";
				TypeCheckWorkRecord checkItem = EnumAdaptor.valueOf(mapWorkRecordExtraCon.get(errorRecord.getErAlId()).getCheckItem(), TypeCheckWorkRecord.class);
				for(AlarmItemName alarmItemName : listAlarmItemName) {
					if(alarmItemName.getCheckItem() == checkItem) {
						alarmItem = alarmItemName.getNameAlarm();
						break;
					}
				}
				listValueExtractAlarm.add(this.checkConditionGenerateValue(em, errorRecord.getDate(), mapWorkRecordExtraCon.get(errorRecord.getErAlId()), companyID,alarmItem,listAttenDanceItem,mapAttdName));				
			}			
		}
		
		return listValueExtractAlarm;
	}
	
	private List<AlarmItemName> getAlarmItemByType() {
		List<AlarmItemName> listAlarmItemName = new ArrayList<>();
		listAlarmItemName.add(new AlarmItemName(TypeCheckWorkRecord.TIME,TextResource.localize("KAL010_47")));
		listAlarmItemName.add(new AlarmItemName(TypeCheckWorkRecord.TIMES,TextResource.localize("KAL010_50")));
		listAlarmItemName.add(new AlarmItemName(TypeCheckWorkRecord.AMOUNT_OF_MONEY,TextResource.localize("KAL010_51")));
		listAlarmItemName.add(new AlarmItemName(TypeCheckWorkRecord.TIME_OF_DAY,TextResource.localize("KAL010_52")));
		listAlarmItemName.add(new AlarmItemName(TypeCheckWorkRecord.CONTINUOUS_TIME,TextResource.localize("KAL010_53")));
		listAlarmItemName.add(new AlarmItemName(TypeCheckWorkRecord.CONTINUOUS_WORK,TextResource.localize("KAL010_56")));
		listAlarmItemName.add(new AlarmItemName(TypeCheckWorkRecord.CONTINUOUS_TIME_ZONE,TextResource.localize("KAL010_58")));
		listAlarmItemName.add(new AlarmItemName(TypeCheckWorkRecord.CONTINUOUS_CONDITION,TextResource.localize("KAL010_60")));
		return listAlarmItemName;
	}
	
	private ValueExtractAlarm checkConditionGenerateValue(EmployeeSearchDto employee, GeneralDate date, WorkRecordExtraConAdapterDto workRecordExtraCon,  String companyID,String alarmItem,List<DailyAttendanceItem> listAttenDanceItem ,Map<Integer,DailyAttendanceItem> mapAtdItemName) {
		String alarmContent = "";
		TypeCheckWorkRecord checkItem = EnumAdaptor.valueOf(workRecordExtraCon.getCheckItem(), TypeCheckWorkRecord.class);
//		switch (checkItem) {
//			case TIME:
//				alarmItem = TextResource.localize("KAL010_47");
//				break;
//			case TIMES:
//				alarmItem = TextResource.localize("KAL010_50");
//				break;
//			case AMOUNT_OF_MONEY:
//				alarmItem = TextResource.localize("KAL010_51");
//				break;
//			case TIME_OF_DAY:
//				alarmItem = TextResource.localize("KAL010_52");
//				break;
//			case CONTINUOUS_TIME:
//				alarmItem = TextResource.localize("KAL010_53");
//				break;				
//			case CONTINUOUS_WORK:
//				alarmItem = TextResource.localize("KAL010_56");
//				break;
//			case CONTINUOUS_TIME_ZONE:
//				alarmItem = TextResource.localize("KAL010_58"); 
//				break;
//			case CONTINUOUS_CONDITION:
//				alarmItem = TextResource.localize("KAL010_60"); 
//				break;
//			default:
//				break;
//		}
		
		alarmContent = checkConditionGenerateAlarmContent(checkItem, workRecordExtraCon , companyID, listAttenDanceItem, mapAtdItemName);		
		if(alarmContent.length()>100) {
			alarmContent = alarmContent.substring(0, 100);
		}
		ValueExtractAlarm result = new ValueExtractAlarm(employee.getWorkplaceId(), employee.getId(), date.toString(), TextResource.localize("KAL010_1"), alarmItem, alarmContent,
				workRecordExtraCon.getErrorAlarmCondition().getDisplayMessage());
		return result;
	}
	
	private String  checkConditionGenerateAlarmContent(TypeCheckWorkRecord checkItem,  WorkRecordExtraConAdapterDto workRecordExtraCon, String companyID,List<DailyAttendanceItem> listAttenDanceItem, Map<Integer,DailyAttendanceItem> mapAtdItemName) {
		
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
			attendanceText =  calculateAttendanceText(atdItemCon,listAttenDanceItem);
					
			if (!singleCompare(atdItemCon.getCompareOperator())) {
				
				if(betweenRange(atdItemCon.getCompareOperator())) {
					alarmContent = TextResource.localize("KAL010_49", wktypeText, this.formatHourData( atdItemCon.getCompareStartValue().toString(), checkItem), coupleOperator.getOperatorStart(), attendanceText,
							coupleOperator.getOperatorEnd(), this.formatHourData(atdItemCon.getCompareEndValue().toString(), checkItem));
				}else {
					alarmContent = TextResource.localize("KAL010_121", wktypeText, attendanceText, coupleOperator.getOperatorStart(), atdItemCon.getCompareStartValue().toString(),
							atdItemCon.getCompareEndValue().toString(), coupleOperator.getOperatorEnd(), attendanceText);
				}
				
			} else {
				if (atdItemCon.getConditionType() == ConditionType.FIXED_VALUE.value) {
					alarmContent = TextResource.localize("KAL010_48", wktypeText, attendanceText, coupleOperator.getOperatorStart(),this.formatHourData( atdItemCon.getCompareStartValue().toString(), checkItem));
				} else {
					alarmContent = TextResource.localize("KAL010_48", wktypeText, attendanceText, coupleOperator.getOperatorStart(),mapAtdItemName.containsKey(atdItemCon.getSingleAtdItem()) ? mapAtdItemName.get(atdItemCon.getSingleAtdItem()).getAttendanceItemName() : "");
				}
			}
			break;
		case CONTINUOUS_TIME:
			wktypeText = calculateWktypeText( workRecordExtraCon, companyID);
			attendanceText =  calculateAttendanceText( atdItemCon,listAttenDanceItem);
			
			if (!singleCompare(atdItemCon.getCompareOperator())) {
				if(betweenRange(atdItemCon.getCompareOperator())) {
					alarmContent = TextResource.localize("KAL010_55", wktypeText, this.formatHourData(atdItemCon.getCompareStartValue().toString(), checkItem), coupleOperator.getOperatorStart(), attendanceText,
							coupleOperator.getOperatorEnd(), this.formatHourData(atdItemCon.getCompareEndValue().toString(), checkItem), workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + "");
				}else {
					alarmContent = TextResource.localize("KAL010_122", wktypeText, attendanceText , coupleOperator.getOperatorStart(),  atdItemCon.getCompareStartValue().toString(), 
							 atdItemCon.getCompareEndValue().toString(),
							coupleOperator.getOperatorEnd(), attendanceText,  workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + "");
				}

			} else {
				if (atdItemCon.getConditionType() == ConditionType.FIXED_VALUE.value) {
					alarmContent = TextResource.localize("KAL010_54", wktypeText, attendanceText, coupleOperator.getOperatorStart(), this.formatHourData(atdItemCon.getCompareStartValue().toString(), checkItem),
							workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + "");
				} else {
					alarmContent = TextResource.localize("KAL010_54", wktypeText, attendanceText, coupleOperator.getOperatorStart(), mapAtdItemName.containsKey(atdItemCon.getSingleAtdItem()) ? mapAtdItemName.get(atdItemCon.getSingleAtdItem()).getAttendanceItemName() : "",
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
			wktimeText = calculateWkTimeText(workRecordExtraCon,listAttenDanceItem);
			
			alarmContent = TextResource.localize("KAL010_59", wktypeText, wktimeText ,  workRecordExtraCon.getErrorAlarmCondition().getContinuousPeriod() + ""); 
			break;
		case CONTINUOUS_CONDITION:
			String alarmGroup1= "";
			String alarmGroup2 ="";
			ErAlConAttendanceItemAdapterDto group1 = workRecordExtraCon.getErrorAlarmCondition().getAtdItemCondition().getGroup1();
			alarmGroup1 = generateAlarmGroup(group1,listAttenDanceItem,checkItem,mapAtdItemName);
			ErAlConAttendanceItemAdapterDto group2 = workRecordExtraCon.getErrorAlarmCondition().getAtdItemCondition().getGroup2();
			alarmGroup2 = generateAlarmGroup(group2,listAttenDanceItem,checkItem,mapAtdItemName);
			if(alarmGroup1.length()!=0 && alarmGroup2.length() !=0 ) {
				alarmContent =   alarmGroup1 + logicalOperator( workRecordExtraCon.getErrorAlarmCondition().getAtdItemCondition().getOperatorBetweenGroups()) +  alarmGroup2 ;
			}else {
				alarmContent = alarmGroup1+ alarmGroup2;
			}
			break;
		default:
			break;			 
		}

		return alarmContent;
		
	}
	
	private String formatHourData(String  minutes, TypeCheckWorkRecord checkItem) {
		if(checkItem ==TypeCheckWorkRecord.TIME || checkItem ==TypeCheckWorkRecord.TIME_OF_DAY) {
			String h="", m="";
			if(minutes !=null && !minutes.equals("")) {
				Integer hour = Integer.parseInt(minutes);
				h = hour.intValue()/60 +"";
				m = hour.intValue()%60 +"";
				if(h.length()<2) h ="0" +h;
				if(m.length()<2) m ="0" +m;
				
				return h+ ":"+ m;
			}else {
				return "";
			}
			
		}else {
			return minutes;
		}

	}
	
	private String formatHourDataByGroup(String  minutes, int conditionAtr) {
		//conditionAtr = 1 : 時間 ,conditionAtr = 2 : 時刻
		if(conditionAtr ==1 || conditionAtr ==2) {
			String h="", m="";
			if(minutes !=null && !minutes.equals("")) {
				Integer hour = Integer.parseInt(minutes);
				h = hour.intValue()/60 +"";
				m = hour.intValue()%60 +"";
				if(h.length()<2) h ="0" +h;
				if(m.length()<2) m ="0" +m;
				
				return h+ ":"+ m;
			}else {
				return "";
			}
			
		}else {
			return minutes;
		}

	}
	
	private String generateAlarmGroup(ErAlConAttendanceItemAdapterDto group,List<DailyAttendanceItem> listAttenDanceItem,TypeCheckWorkRecord checkItem , Map<Integer,DailyAttendanceItem> mapAtdItemName) {
		String alarmGroup= "";		
		
		if (!group.getLstErAlAtdItemCon().isEmpty()) {
			for (int i = 0; i < group.getLstErAlAtdItemCon().size(); i++) {
				ErAlAtdItemConAdapterDto itemCon = group.getLstErAlAtdItemCon().get(i);
				CoupleOperator coupleOperator = findOperator(itemCon.getCompareOperator());
				String alarm = "";
				if (singleCompare(itemCon.getCompareOperator())) {
					if (itemCon.getConditionType() == ConditionType.FIXED_VALUE.value) {
						alarm = "(式" + (i + 1) + " " + calculateAttendanceText(itemCon,listAttenDanceItem) 
						+ coupleOperator.getOperatorStart() 
						+ this.formatHourDataByGroup(String.valueOf(itemCon.getCompareStartValue()), itemCon.getConditionAtr()) + ")";
					} else {
						String attendenceItemName = mapAtdItemName.containsKey(itemCon.getSingleAtdItem()) ? mapAtdItemName.get(itemCon.getSingleAtdItem()).getAttendanceItemName() :"";
						alarm = "(式" + (i + 1) + " " + calculateAttendanceText(itemCon,listAttenDanceItem) 
						+ coupleOperator.getOperatorStart()
						+ attendenceItemName+ ")";
					}

				} else {
					if (betweenRange(itemCon.getCompareOperator())) {
						alarm = "(式" + (i + 1) 
								+" "
								+ this.formatHourDataByGroup(String.valueOf(itemCon.getCompareStartValue()), itemCon.getConditionAtr())
								+ coupleOperator.getOperatorStart()
								+ calculateAttendanceText(itemCon,listAttenDanceItem) 
								+ coupleOperator.getOperatorEnd()
								+ this.formatHourDataByGroup(String.valueOf(itemCon.getCompareEndValue()), itemCon.getConditionAtr())+ ")";
					} else {
						alarm = "(式" + (i + 1) + " " + calculateAttendanceText(itemCon,listAttenDanceItem) 
								+ coupleOperator.getOperatorStart() 
								+ this.formatHourDataByGroup(String.valueOf(itemCon.getCompareStartValue()), itemCon.getConditionAtr())
								+ ", " 
								+ this.formatHourDataByGroup(String.valueOf(itemCon.getCompareEndValue()), itemCon.getConditionAtr())
								+ coupleOperator.getOperatorEnd() 
								+ calculateAttendanceText(itemCon,listAttenDanceItem) + ")";
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
		if (workTypeCodes.isEmpty() || workTypeCodes == null)
			return "";
		List<String> workTypeNames = workTypeRepository.findNotDeprecatedByListCode(companyID, workTypeCodes).stream().map( x ->x.getName().toString()).collect(Collectors.toList());
		return workTypeNames.isEmpty()? "":  String.join(",", workTypeNames);
	}
	
	private String calculateAttendanceText(ErAlAtdItemConAdapterDto atdItemCon,List<DailyAttendanceItem> listAttenDanceItem) {
		String attendanceText = "";		
		
		if(atdItemCon.getConditionAtr()==ConditionAtr.TIME_WITH_DAY.value) {
			if(atdItemCon.getUncountableAtdItem()>0) {
				List<DailyAttendanceItem>  listAttendance = new ArrayList<>();
				
				for(DailyAttendanceItem dailyAttendanceItem : listAttenDanceItem ) {
					if(dailyAttendanceItem.getAttendanceItemId() == atdItemCon.getUncountableAtdItem() ) {
						listAttendance.add(dailyAttendanceItem);
						break;
					}
				}
				//List<DailyAttendanceItem>  listAttendance = attendanceNameService.getNameOfDailyAttendanceItem(Arrays.asList(atdItemCon.getUncountableAtdItem()));
				if(!listAttendance.isEmpty()) attendanceText = listAttendance.get(0).getAttendanceItemName();
			}
			
		}else {
			if(!atdItemCon.getCountableAddAtdItems().isEmpty()) {
				List<DailyAttendanceItem>  listAttendanceAdd = new ArrayList<>();
				for(int attendanceID : atdItemCon.getCountableAddAtdItems()) {
					for(DailyAttendanceItem dailyAttendanceItem : listAttenDanceItem ) {
						if(attendanceID == dailyAttendanceItem.getAttendanceItemId()) {
							listAttendanceAdd.add(dailyAttendanceItem);
							break;
						}
					}
				}
				//List<DailyAttendanceItem>  listAttendanceAdd = attendanceNameService.getNameOfDailyAttendanceItem(atdItemCon.getCountableAddAtdItems());
				if(!listAttendanceAdd.isEmpty()) {
					for( int i=0 ; i< listAttendanceAdd.size(); i++) {
						String operator = (i == (listAttendanceAdd.size() - 1)) ? "" : " + ";
						attendanceText += listAttendanceAdd.get(i).getAttendanceItemName() + operator;
					}
				}
				if(! atdItemCon.getCountableSubAtdItems().isEmpty()) {
					List<DailyAttendanceItem>  listAttendanceSub = new ArrayList<>();
					for(int attendanceID : atdItemCon.getCountableSubAtdItems()) {
						for(DailyAttendanceItem dailyAttendanceItem : listAttenDanceItem ) {
							if(attendanceID == dailyAttendanceItem.getAttendanceItemId()) {
								listAttendanceSub.add(dailyAttendanceItem);
								break;
							}
						}
					}
					//List<DailyAttendanceItem>  listAttendanceSub = attendanceNameService.getNameOfDailyAttendanceItem(atdItemCon.getCountableAddAtdItems());
					if(!listAttendanceSub.isEmpty()) {
						for(int i=0; i< listAttendanceSub.size(); i++) {
                            String operator = (i == (listAttendanceSub.size() - 1)) ? "" : " - ";
                            String beforeOperator = (i == 0) ? " - " : "";
                            attendanceText += beforeOperator + listAttendanceSub.get(i).getAttendanceItemName() + operator;
						}
					}
				}
			}else if(! atdItemCon.getCountableSubAtdItems().isEmpty()) {
				List<DailyAttendanceItem>  listAttendanceSub = new ArrayList<>();
				for(int attendanceID : atdItemCon.getCountableSubAtdItems()) {
					for(DailyAttendanceItem dailyAttendanceItem : listAttenDanceItem ) {
						if(attendanceID == dailyAttendanceItem.getAttendanceItemId()) {
							listAttendanceSub.add(dailyAttendanceItem);
							break;
						}
					}
				}
//				List<DailyAttendanceItem>  listAttendanceSub = attendanceNameService.getNameOfDailyAttendanceItem(atdItemCon.getCountableAddAtdItems());
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
	
	private String calculateWkTimeText( WorkRecordExtraConAdapterDto workRecordExtraCon,List<DailyAttendanceItem> listAttenDanceItem ) {
		
		List<Integer> listWorkTimeIds = workRecordExtraCon.getErrorAlarmCondition().getWorkTimeCondition().getPlanLstWorkTime().stream().map(e -> Integer.parseInt(e))
				.collect(Collectors.toList());
		List<DailyAttendanceItem> listAttdItem = new ArrayList<>();
		for(int attendanceID : listWorkTimeIds) {
			for(DailyAttendanceItem dailyAttendanceItem : listAttenDanceItem ) {
				if(attendanceID == dailyAttendanceItem.getAttendanceItemId()) {
					listAttdItem.add(dailyAttendanceItem);
					break;
				}
			}
		}
		List<String> workTimeNames = listAttdItem.stream().map( e-> e.getAttendanceItemName()).collect(Collectors.toList());
		//List<String> workTimeNames = attendanceNameService.getNameOfDailyAttendanceItem(listWorkTimeIds).stream().map( e-> e.getAttendanceItemName()).collect(Collectors.toList());
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
				return new CoupleOperator("＜", "＜");
			case BETWEEN_RANGE_CLOSED:
				return new CoupleOperator("≦", "≦");
			case OUTSIDE_RANGE_OPEN:
				return new CoupleOperator("＜", "＜");
			case OUTSIDE_RANGE_CLOSED:
				return new CoupleOperator("≦", "≦");
		}
		
		return null;
	}
	
	private boolean singleCompare(int compareOperator ) {
		return compareOperator <= CompareType.GREATER_THAN.value;
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
		List<FixedConWorkRecordAdapterDto> listFixed =  fixedConWorkRecordAdapter.getAllFixedConWorkRecordByID(dailyAlarmCondition.getDailyAlarmConID())
				.stream().sorted((x,y) -> x.getFixConWorkRecordNo() - y.getFixConWorkRecordNo()).collect(Collectors.toList());
		for(int i = 0;i < listFixed.size();i++) {
			if(listFixed.get(i).isUseAtr()) {
				FixedConWorkRecordAdapterDto fixedData = listFixed.get(i);
				switch(i) {
				case 0 :
					for(GeneralDate date : period.getListDate()) {
						String workType = null;
						Optional<RecordWorkInfoFunAdapterDto> recordWorkInfo = recordWorkInfoFunAdapter.getInfoCheckNotRegister(employee.getId(), date);
						if(recordWorkInfo.isPresent()) {
							workType = recordWorkInfo.get().getWorkTypeCode();
							if(workType == null || workType.isEmpty()) continue;
							Optional<ValueExtractAlarm> checkWorkType = fixedCheckItemAdapter.checkWorkTypeNotRegister(employee.getWorkplaceId(),employee.getId(), date, workType);
							if(checkWorkType.isPresent()) {
								ValueExtractAlarm tmp = checkWorkType.get();
								tmp.setComment(Optional.ofNullable(fixedData.getMessage()));
								listValueExtractAlarm.add(tmp);
							}
						}	
					}
					break;
				case 1 :
					for(GeneralDate date : period.getListDate()) {
						String workTime = null;
						Optional<RecordWorkInfoFunAdapterDto> recordWorkInfoFunAdapterDto = recordWorkInfoFunAdapter.getInfoCheckNotRegister(employee.getId(), date);
						if(recordWorkInfoFunAdapterDto.isPresent()) {
							workTime = recordWorkInfoFunAdapterDto.get().getWorkTimeCode();
							if(workTime == null || workTime.isEmpty()) continue;
							Optional<ValueExtractAlarm> checkWorkTime = fixedCheckItemAdapter.checkWorkTimeNotRegister(employee.getWorkplaceId(),employee.getId(), date, workTime);
							if(checkWorkTime.isPresent()) {
								ValueExtractAlarm tmp = checkWorkTime.get();
								tmp.setComment(Optional.ofNullable(fixedData.getMessage()));
								listValueExtractAlarm.add(tmp);
							}
						} 
					}
					break;
				case 2:
					List<ValueExtractAlarm> listCheckPrincipalUnconfirm = fixedCheckItemAdapter.checkPrincipalUnconfirm(
							employee.getWorkplaceId(), employee.getId(), period.getStartDate(), period.getEndDate());
					if (!listCheckPrincipalUnconfirm.isEmpty()) {
						for (ValueExtractAlarm tmp : listCheckPrincipalUnconfirm) {
							tmp.setComment(Optional.ofNullable(fixedData.getMessage()));
						}
						listValueExtractAlarm.addAll(listCheckPrincipalUnconfirm);
					}
					break;
				case 3:
				/*	No 113 old
					List<ValueExtractAlarm> listCheckAdminUnverified = fixedCheckItemAdapter.checkAdminUnverified(
							employee.getWorkplaceId(), employee.getId(), period.getStartDate(), period.getEndDate());*/
					
					List<ValueExtractAlarm> listCheckAdminUnverified = fixedCheckItemAdapter.checkAdminUnverified(
							employee.getWorkplaceId(), employee.getId(), new DatePeriod(period.getStartDate(),  period.getEndDate()));
					
					if (!listCheckAdminUnverified.isEmpty()) {
						for (ValueExtractAlarm tmp : listCheckAdminUnverified) {
							tmp.setComment(Optional.ofNullable(fixedData.getMessage()));
						}
						listValueExtractAlarm.addAll(listCheckAdminUnverified);
					}
					break;
				case 5:
					List<ValueExtractAlarm> listCheckVacation = fixedCheckItemAdapter.checkContinuousVacation(employee.getId(), new DatePeriod(period.getStartDate(),  period.getEndDate()));
					
					if (!listCheckVacation.isEmpty()) {
						for (ValueExtractAlarm tmp : listCheckVacation) {
							tmp.setComment(Optional.ofNullable(fixedData.getMessage()));
						}
						listValueExtractAlarm.addAll(listCheckVacation);
					}
					break;
				default:
					List<ValueExtractAlarm> listCheckingData = fixedCheckItemAdapter.checkingData(
							employee.getWorkplaceId(), employee.getId(), period.getStartDate(), period.getEndDate());
					if (!listCheckingData.isEmpty()) {
						for (ValueExtractAlarm tmp : listCheckingData) {
							tmp.setComment(Optional.ofNullable(fixedData.getMessage()));
						}
						List<String> listDate = listCheckingData.stream().map(u -> u.getAlarmValueDate()).collect(Collectors.toList());
						listValueExtractAlarm = listValueExtractAlarm.stream().filter(o -> !listDate.contains(o.getAlarmValueDate())).collect(Collectors.toList());
						listValueExtractAlarm.addAll(listCheckingData);
					}
					break;
				}//end switch
			}//end if
		}//end for
		return listValueExtractAlarm;
	}
}
