package nts.uk.ctx.at.record.dom.workrecord.erroralarm.multimonth.algorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMergeRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.CheckedCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.CompareSingleValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.CompareOperatorText;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConvertCompareTypeToText;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.TypeCheckWorkRecordMultipleMonth;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.mastercheck.algorithm.WorkPlaceHistImportAl;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.algorithm.MonthlyRecordValuesDto;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.checkremainnumber.CheckOperatorType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.multimonth.MulMonAlarmCheckCondRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.multimonth.MulMonthAlarmCheckCond;
import nts.uk.ctx.at.shared.dom.adapter.attendanceitemname.AttendanceItemNameAdapter;
import nts.uk.ctx.at.shared.dom.adapter.attendanceitemname.MonthlyAttendanceItemNameDto;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckType;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ExtractionAlarmPeriodDate;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ExtractionResultDetail;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ResultOfEachCondition;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.service.AttendanceItemConvertFactory;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.affiliation.AffiliationInfoOfMonthlyRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.anyitem.AnyItemOfMonthlyRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.absenceleave.AbsenceLeaveRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.absenceleave.AbsenceLeaveRemainDataRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnLeaRemNumEachMonthRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.dayoff.MonthlyDayoffRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.dayoff.MonthlyDayoffRemainDataRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.RsvLeaRemNumEachMonthRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialHolidayRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialHolidayRemainDataRepository;
import nts.uk.shr.com.i18n.TextResource;
@Stateless
public class MultiMonthlyExtractCheckServiceImpl<V> implements MultiMonthlyExtractCheckService{
	/** 月別実績の勤怠時間 */
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeRepo;
	/** 月別実績の所属情報 */
	@Inject
	private AffiliationInfoOfMonthlyRepository affiliationInfoRepo;
	/** 月別実績の任意項目 */
	@Inject
	private AnyItemOfMonthlyRepository anyItemRepo;
	/** 年休月別残数データ */
	@Inject
	private AnnLeaRemNumEachMonthRepository annualLeaveRepo;
	/** 積立年休月別残数データ */
	@Inject
	private RsvLeaRemNumEachMonthRepository reserveLeaveRepo;
	/** 振休月別残数データ */
	@Inject
	private AbsenceLeaveRemainDataRepository absLeaRemRepo;
	/** 代休月別残数データ */
	@Inject
	private MonthlyDayoffRemainDataRepository monDayoffRemRepo;
	/** 特別休暇月別残数データ */
	@Inject
	private SpecialHolidayRemainDataRepository spcLeaRemRepo;
	/** 勤怠項目変換 */
	@Inject
	private AttendanceItemConvertFactory attendanceItemConverterFact;
	
	@Inject
	private TimeOfMonthlyRepository timeRepo;
	
	@Inject
	private RemainMergeRepository remainRepo;
	@Inject
	private AttendanceItemNameAdapter attendanceAdapter;
	@Inject
	private MulMonAlarmCheckCondRepository mulMunCondRepos;
	@Inject
	private ConvertCompareTypeToText convertComparaToText;
	@Override
	public void extractMultiMonthlyAlarm(String cid, List<String> lstSid, YearMonthPeriod mPeriod,
			List<String> lstAnyConID, List<WorkPlaceHistImportAl> getWplByListSidAndPeriod,
			List<ResultOfEachCondition> lstResultCondition, List<AlarmListCheckInfor> lstCheckType) {
		DataCheck data = new DataCheck(cid, lstSid, mPeriod, lstAnyConID);
		if(data.lstAnyCondCheck.isEmpty()) return;
		
		for(MulMonthAlarmCheckCond anyCond : data.lstAnyCondCheck) {
			lstCheckType.add(new AlarmListCheckInfor(String.valueOf(anyCond.getCondNo()), AlarmListCheckType.FreeCheck));
			ErAlAttendanceItemCondition<?> erCondition = anyCond.getErAlAttendanceItemCondition();
			if(erCondition == null) continue;
			
			//比較演算子
			int compare = erCondition.getCompareSingleValue() == null ?
					erCondition.getCompareRange().getCompareOperator().value :
						erCondition.getCompareSingleValue().getCompareOpertor().value;
			//複数月のチェック条件(平均)

			for(String sid : lstSid) {
				String startValue = erCondition.getCompareSingleValue() == null ?
						erCondition.getCompareRange().getStartValue().toString() :
						erCondition.getCompareSingleValue().getValue().toString();					
				String endValue = erCondition.getCompareRange() == null ? null :
						erCondition.getCompareRange().getEndValue().toString();
				float avg = 0;
				//複数月のチェック条件(連続)
				//int countTmp = 0;
				//複数月のチェック条件(該当月数)
				List<String> lstYM = new ArrayList<>();
				List<MonthlyRecordValuesDto> lstSidOfMonthData = data.lstMonthData.get(sid);
				//チェック対象値
				double checkedValue = 0;
				String checkValue = "";
				boolean checkAddAlarm = false;
				if(lstSidOfMonthData == null || lstSidOfMonthData.isEmpty()) continue;
				switch (anyCond.getTypeCheckItem()) {
				//時間、回数、金額、日数
				case TIME:
				case TIMES:
				case AMOUNT:
				case DAYS:
					List<ItemValue> itemValues = new ArrayList<ItemValue>();
					for(MonthlyRecordValuesDto result :lstSidOfMonthData ){
						itemValues.addAll(result.getItemValues());
						checkAddAlarm = erCondition.checkTarget(item->{
								if (item.isEmpty()) {
									return new ArrayList<>();
								}
								return itemValues.stream().map(iv -> getValue(iv))
										.collect(Collectors.toList());
							});
						checkedValue = erCondition.calculateTargetValue(item ->{
							if(item.isEmpty()) {
								return new ArrayList<>();
							}
							return itemValues.stream().map(iv -> getValue(iv))
									.collect(Collectors.toList());
						});
					}
					break;
				//連続時間、連続回数、連続金額、連続日数
				case CONTINUOUS_TIME:
				case CONTINUOUS_TIMES:
				case CONTINUOUS_AMOUNT:
				case CONTINUOUS_DAYS:
					//Count連続月数
					int countContinus = 0;
					
					for(YearMonth ym : mPeriod.yearMonthsBetween()) {
						for(MonthlyRecordValuesDto result :lstSidOfMonthData ){
							if(result.getYearMonth().equals(ym)) {
								boolean checkPerMonth = false;
								checkPerMonth = erCondition.checkTarget(item -> {
											if (item.isEmpty()) {
												return new ArrayList<>();
											}
											return result.getItemValues().stream().map(iv -> getValue(iv))
													.collect(Collectors.toList());
										});
								if(countContinus >= anyCond.getContinuousMonths().get()){
									checkAddAlarm = true;
									checkedValue = countContinus;
								}
								countContinus = checkPerMonth ? countContinus++ : 0; 
							}
						}
					}
					
					break;
				//平均時間、平均回数、平均金額、平均日数
				case AVERAGE_TIME:
				case AVERAGE_TIMES:
				case AVERAGE_AMOUNT:
				case AVERAGE_DAYS:
					float sum = 0 ;
					
					for(MonthlyRecordValuesDto result :lstSidOfMonthData ){
						List<ItemValue> listValue = result.getItemValues();
						for (ItemValue itemValue : listValue) {
							sum +=getValue(itemValue);
						}
						
					}	
					avg = sum/mPeriod.yearMonthsBetween().size();
					double av = avg;
					BigDecimal bdAVG = new BigDecimal(avg);
					bdAVG.setScale(2, RoundingMode.HALF_UP);
					/*checkAddAlarm = CompareDouble(bdAVG, new BigDecimal(startValue),
							new BigDecimal(endValue),
							compare);*/
					List<Double> lstT = new ArrayList<>();
					checkAddAlarm = erCondition.checkTarget(item -> {
						if(item.isEmpty()) {
							return new ArrayList<>();
						}
						lstT.add(av);
						return lstT;
					});
					
					break;
				//該当月数時間、該当月数回数、該当月数金額、該当月数日数
				case NUMBER_TIME:
				case NUMBER_TIMES:
				case NUMBER_AMOUNT:
				case NUMBER_DAYS:
					double countCosp = 0 ;
					
					for(MonthlyRecordValuesDto result :lstSidOfMonthData ){
						boolean checkPerMonth = false;
						checkPerMonth = erCondition.checkTarget(item->{
							if (item.isEmpty()) {
								return new ArrayList<>();
							}
							return result.getItemValues().stream().map(iv -> getValue(iv))
									.collect(Collectors.toList());
						});
						if(checkPerMonth) {
							countCosp += 1;
							lstYM.add(result.getYearMonth().lastGeneralDate().toString("yyyy/MM"));
						}
					}	
					CompareSingleValue<Integer> checkgaitou = new CompareSingleValue<>(anyCond.getCompaOperator().get(), ConditionType.FIXED_VALUE);
					checkgaitou.setValue(anyCond.getNumbers().get());
					checkAddAlarm = checkgaitou.checkWithFixedValue(countCosp,  c -> Double.valueOf(c));
					if(checkAddAlarm) checkedValue = countCosp;
					break;
					
					default:
						break;
					
				} 
				
				if(checkAddAlarm) {
					if(anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.TIME
							|| anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.AVERAGE_TIME
							|| anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.CONTINUOUS_TIME
							|| anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.NUMBER_TIME) {
						startValue = timeToString(Integer.valueOf(startValue));
						endValue = endValue == null ? null : timeToString(Integer.valueOf(endValue));
					}
					
					List<Integer> lstAddSub = erCondition.getCountableTarget().getAddSubAttendanceItems().getAdditionAttendanceItems();
					List<Integer> lstSubStr = erCondition.getCountableTarget().getAddSubAttendanceItems().getSubstractionAttendanceItems();
					
					List<MonthlyAttendanceItemNameDto> addSubName = data.lstItemMond.stream().filter(x -> lstAddSub.contains(x.getAttendanceItemId()))
							.collect(Collectors.toList());
					List<MonthlyAttendanceItemNameDto> subStrName = data.lstItemMond.stream().filter(x -> lstSubStr.contains(x.getAttendanceItemId()))
							.collect(Collectors.toList());
					
					String nameItem = getNameErrorAlarm(addSubName,	1, "");
					nameItem = getNameErrorAlarm(subStrName, 0, nameItem);
					String alarmDescription = "";
					CompareOperatorText compareOperatorText = convertComparaToText.convertCompareType(
							erCondition.getCompareSingleValue() != null 
									? erCondition.getCompareSingleValue().getConditionType().value
									: erCondition.getCompareRange().getCompareOperator().value);
					
					String periodYearMonth = mPeriod.start().lastGeneralDate().toString("yyyy/MM") + "～" + mPeriod.end().lastGeneralDate().toString("yyyy/MM");
					if(anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.CONTINUOUS_AMOUNT
							|| anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.CONTINUOUS_DAYS
							|| anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.CONTINUOUS_TIME
							|| anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.CONTINUOUS_TIMES) {
						alarmDescription = TextResource.localize("KAL010_260", periodYearMonth, nameItem,
								compareOperatorText.getCompareLeft(), startValue,
								String.valueOf(anyCond.getContinuousMonths().get()));
						checkValue = TextResource.localize("KAL010_289", nameItem, String.valueOf(checkedValue));
					} else if (anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.NUMBER_AMOUNT
							|| anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.NUMBER_DAYS
							|| anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.NUMBER_TIME
							|| anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.NUMBER_TIMES) {
						alarmDescription = TextResource.localize("KAL010_270", periodYearMonth, nameItem,
								compareOperatorText.getCompareLeft(), startValue,
								lstYM.toString(), String.valueOf(anyCond.getNumbers().get()));
						checkValue = TextResource.localize("KAL010_290", lstYM.toString(), String.valueOf(checkedValue));
					} else {
						if(compare <= 5) {
							alarmDescription = TextResource.localize("KAL010_254", periodYearMonth, nameItem,
									compareOperatorText.getCompareLeft(), startValue);
						} else {
							if (compare > 5 && compare <= 7) {
								alarmDescription = TextResource.localize("KAL010_255", periodYearMonth, startValue,
										compareOperatorText.getCompareLeft(), nameItem,
										compareOperatorText.getCompareright(), endValue);
							} else {
								alarmDescription = TextResource.localize("KAL010_256", periodYearMonth, startValue,
										compareOperatorText.getCompareLeft(), nameItem, nameItem,
										compareOperatorText.getCompareright(), endValue);
							}
						}
						checkValue = TextResource.localize("KAL010_284", nameItem, String.valueOf(checkedValue));
						if(anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.TIME) checkValue = checkValue + TextResource.localize("KAL010_285");
						if(anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.TIMES) checkValue = checkValue + TextResource.localize("KAL010_286");
						if(anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.AMOUNT) checkValue = checkValue + TextResource.localize("KAL010_287");
						if(anyCond.getTypeCheckItem() == TypeCheckWorkRecordMultipleMonth.DAYS) checkValue = checkValue + TextResource.localize("KAL010_288");
					}
					GeneralDate startDate = GeneralDate.ymd(mPeriod.start().year() , mPeriod.start().month(), 1);
					YearMonth endMonthTemp = mPeriod.end().addMonths(1);
					GeneralDate endDateTemp = GeneralDate.ymd(endMonthTemp.year(), endMonthTemp.month(), 1);
					GeneralDate enDate = endDateTemp.addDays(-1);
					ExtractionAlarmPeriodDate pDate = new ExtractionAlarmPeriodDate(Optional.ofNullable(startDate), Optional.empty());
					String workplaceId = getWplByListSidAndPeriod.stream().filter(x -> x.getEmployeeId().equals(sid))
							.collect(Collectors.toList()).get(0)
							.getLstWkpIdAndPeriod().stream().filter(x -> x.getDatePeriod().start().beforeOrEquals(enDate) 
									&& x.getDatePeriod().end().afterOrEquals(startDate))
							.collect(Collectors.toList()).get(0).getWorkplaceId();
							
					ExtractionResultDetail detail = new ExtractionResultDetail(sid, 
							pDate,
							anyCond.getNameAlarmCon().v(),
							alarmDescription, 
							GeneralDateTime.now(),
							Optional.ofNullable(workplaceId),
							Optional.ofNullable(anyCond.getDisplayMessage().isPresent() ? anyCond.getDisplayMessage().get().v() : null),
							Optional.ofNullable(checkValue)); 
					List<ResultOfEachCondition> result = lstResultCondition.stream()
							.filter(x -> x.getCheckType() == AlarmListCheckType.FreeCheck && x.getNo().equals(String.valueOf(anyCond.getCondNo())))
							.collect(Collectors.toList());
					if(result.isEmpty()) {
						ResultOfEachCondition resultCon = new ResultOfEachCondition(AlarmListCheckType.FixCheck,
								String.valueOf(anyCond.getCondNo()),
								new ArrayList<>());
						resultCon.getLstResultDetail().add(detail);
						lstResultCondition.add(resultCon);
					} else {
						ResultOfEachCondition ex = result.get(0);
						lstResultCondition.remove(ex);
						ex.getLstResultDetail().add(detail);
						lstResultCondition.add(ex);
					}
				}
			}
		}
		
	}
	private String getNameErrorAlarm(List<MonthlyAttendanceItemNameDto> attendanceItemNames ,int type,String nameErrorAlarm){
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
	private boolean CompareDouble(BigDecimal value, BigDecimal valueAgreementStart, BigDecimal valueAgreementEnd,
			int compare) {
		boolean check = false;
		switch (compare) {
		/* 範囲の間（境界値を含まない）（＜＞） */
		case 6:
			if (value.compareTo(valueAgreementStart) > 0 && value.compareTo(valueAgreementEnd) < 0) {
				check = true;
			}
			break;
		/* 範囲の間（境界値を含む）（≦≧） */
		case 7:
			if (value.compareTo(valueAgreementStart) >= 0 && value.compareTo(valueAgreementEnd) <= 0) {
				check = true;
			}
			break;
		/* 範囲の外（境界値を含まない）（＞＜） */
		case 8:
			if (value.compareTo(valueAgreementStart) < 0 || value.compareTo(valueAgreementEnd) > 0) {
				check = true;
			}
			break;
		/* 範囲の外（境界値を含む）（≧≦） */
		default:
			if (value.compareTo(valueAgreementStart) <= 0 || value.compareTo(valueAgreementEnd) >= 0) {
				check = true;
			}
			break;
		}
		return check;
	}
	private String timeToString(int value ){
		if(value%60<10){
			return  String.valueOf(value/60)+":0"+  String.valueOf(value%60);
		}
		return String.valueOf(value/60)+":"+  String.valueOf(value%60);
	}
	
	private Double getValue(ItemValue value) {
		if(value.getValueType()==ValueType.DATE){
			return 0d;
		}
		if (value.value() == null) {
			return 0d;
		}
		else if (value.getValueType().isDouble()||value.getValueType().isInteger()) {
			return value.getValueType().isDouble() ? ((Double) value.value()) : Double.valueOf((Integer) value.value());
		}
		return 0d;
	}
	public class DataCheck {
		/**	月次の勤怠項目 */
		private List<MonthlyAttendanceItemNameDto> lstItemMond;
		/**月別実績(Work)	 */
		private Map<String, List<MonthlyRecordValuesDto>> lstMonthData;
		/**複数月のアラームチェック条件 */
		private List<MulMonthAlarmCheckCond>  lstAnyCondCheck;
		
		public DataCheck(String cid, List<String> lstSid, YearMonthPeriod mPeriod, List<String> lstAnyConId) {
		
			//ドメインモデル「複数月のアラームチェック条件」を取得する
			lstAnyCondCheck = mulMunCondRepos.getMulCondByUseAtr(lstAnyConId, true);
			List<Integer> lstAttendancCheck = new ArrayList<>();
			lstAnyCondCheck.stream().forEach(x -> {
				lstAttendancCheck.addAll(x.getErAlAttendanceItemCondition().getCountableTarget().getAddSubAttendanceItems().getAdditionAttendanceItems());
				lstAttendancCheck.addAll(x.getErAlAttendanceItemCondition().getCountableTarget().getAddSubAttendanceItems().getSubstractionAttendanceItems());
			});
			//社員ID（List）、期間を設定して月別実績を取得する
			this.lstMonthData = getMonthData(lstSid, mPeriod,lstAttendancCheck.stream().distinct().collect(Collectors.toList()));
			//月次の勤怠項目を取得する
			this.lstItemMond = attendanceAdapter.getMonthlyAttendanceItem(2);
		}
		/**
		 * 月別実績データを取得する
		 * @param lstSid
		 * @param mPeriod
		 */
		private Map<String, List<MonthlyRecordValuesDto>> getMonthData(List<String> employeeIds,
				YearMonthPeriod period, List<Integer> itemIds) {
			Map<String, List<MonthlyRecordValuesDto>> results = new HashMap<>();

			// 検索キーを準備する
			val yearMonths = period.yearMonthsBetween();
			
			// 月別実績の勤怠時間を取得する
			val attendanceTimes = attendanceTimeRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
			Map<String, Map<YearMonth, AttendanceTimeOfMonthly>> attendanceTimeMap = new HashMap<>();
			for (val attendanceTime : attendanceTimes){
				val employeeId = attendanceTime.getEmployeeId();
				val yearMonth = attendanceTime.getYearMonth();
				attendanceTimeMap.putIfAbsent(employeeId, new HashMap<>());
				if (attendanceTimeMap.get(employeeId).containsKey(yearMonth)){
					attendanceTime.sum(attendanceTimeMap.get(employeeId).get(yearMonth));
					attendanceTimeMap.get(employeeId).put(yearMonth, attendanceTime);
					continue;
				}
				attendanceTimeMap.get(employeeId).putIfAbsent(yearMonth, attendanceTime);
			}
			
			// 月別実績の所属情報を取得する
			val affiliationInfos = affiliationInfoRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
			Map<String, Map<YearMonth, AffiliationInfoOfMonthly>> affiliationInfoMap = new HashMap<>();
			for (val affiliationInfo : affiliationInfos){
				val employeeId = affiliationInfo.getEmployeeId();
				val yearMonth = affiliationInfo.getYearMonth();
				affiliationInfoMap.putIfAbsent(employeeId, new HashMap<>());
				affiliationInfoMap.get(employeeId).putIfAbsent(yearMonth, affiliationInfo);
			}
			
			// 月別実績の任意項目を取得する
			val anyItems = anyItemRepo.findBySidsAndMonths(employeeIds, yearMonths);
			Map<String, Map<YearMonth, List<AnyItemOfMonthly>>> anyItemMap = new HashMap<>();
			for (val anyItem : anyItems){
				val employeeId = anyItem.getEmployeeId();
				val yearMonth = anyItem.getYearMonth();
				anyItemMap.putIfAbsent(employeeId, new HashMap<>());
				anyItemMap.get(employeeId).putIfAbsent(yearMonth, new ArrayList<>());
				ListIterator<AnyItemOfMonthly> itrAnyItem = anyItemMap.get(employeeId).get(yearMonth).listIterator();
				boolean isSum = false;
				while (itrAnyItem.hasNext()){
					val outAnyItem = itrAnyItem.next();
					if (outAnyItem.getAnyItemId() == anyItem.getAnyItemId()){
						outAnyItem.sum(anyItem);
						itrAnyItem.set(outAnyItem);
						isSum = true;
						break;
					}
				}
				if (!isSum) anyItemMap.get(employeeId).get(yearMonth).add(anyItem);
			}
			
			// 年休月別残数データを取得する
			val annualLeaves = annualLeaveRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
			Map<String, Map<YearMonth, AnnLeaRemNumEachMonth>> annualLeaveMap = new HashMap<>();
			for (val annualLeave : annualLeaves){
				val employeeId = annualLeave.getEmployeeId();
				val yearMonth = annualLeave.getYearMonth();
				annualLeaveMap.putIfAbsent(employeeId, new HashMap<>());
				annualLeaveMap.get(employeeId).put(yearMonth, annualLeave);
			}
			
			// 積立年休月別残数データを取得する
			val reserveLeaves = reserveLeaveRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
			Map<String, Map<YearMonth, RsvLeaRemNumEachMonth>> reserveLeaveMap = new HashMap<>();
			for (val reserveLeave : reserveLeaves){
				val employeeId = reserveLeave.getEmployeeId();
				val yearMonth = reserveLeave.getYearMonth();
				reserveLeaveMap.putIfAbsent(employeeId, new HashMap<>());
				reserveLeaveMap.get(employeeId).put(yearMonth, reserveLeave);
			}
			
			// 振休月別残数データを取得する
			val absenceLeaves = absLeaRemRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
			Map<String, Map<YearMonth, AbsenceLeaveRemainData>> absenceLeaveMap = new HashMap<>();
			for (val absenceLeave : absenceLeaves){
				val employeeId = absenceLeave.getSId();
				val yearMonth = absenceLeave.getYm();
				absenceLeaveMap.putIfAbsent(employeeId, new HashMap<>());
				absenceLeaveMap.get(employeeId).put(yearMonth, absenceLeave);
			}
			
			// 代休月別残数データを取得する
			val monDayoffs = monDayoffRemRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
			Map<String, Map<YearMonth, MonthlyDayoffRemainData>> monDayoffMap = new HashMap<>();
			for (val monDayoff : monDayoffs){
				val employeeId = monDayoff.getSId();
				val yearMonth = monDayoff.getYm();
				monDayoffMap.putIfAbsent(employeeId, new HashMap<>());
				monDayoffMap.get(employeeId).put(yearMonth, monDayoff);
			}
			
			// 特別休暇月別残数データを取得する
			val specialLeaves = spcLeaRemRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
			Map<String, Map<YearMonth, List<SpecialHolidayRemainData>>> specialLeaveMap = new HashMap<>();
			for (val specialLeave : specialLeaves){
				val employeeId = specialLeave.getSid();
				val yearMonth = specialLeave.getYm();
				specialLeaveMap.putIfAbsent(employeeId, new HashMap<>());
				specialLeaveMap.get(employeeId).putIfAbsent(yearMonth, new ArrayList<>());
				ListIterator<SpecialHolidayRemainData> itrSpecialLeave =
						specialLeaveMap.get(employeeId).get(yearMonth).listIterator();
				boolean isNotExist = false;
				while (itrSpecialLeave.hasNext()){
					val outSpecialLeave = itrSpecialLeave.next();
					if (outSpecialLeave.getSpecialHolidayCd() == specialLeave.getSpecialHolidayCd()){
						itrSpecialLeave.set(outSpecialLeave);
						isNotExist = true;
						break;
					}
				}
				if (!isNotExist) specialLeaveMap.get(employeeId).get(yearMonth).add(specialLeave);
			}
			
			for (val employeeId : employeeIds){
				for (val yearMonth : yearMonths){
					if (!attendanceTimeMap.containsKey(employeeId)) continue;
					if (!attendanceTimeMap.get(employeeId).containsKey(yearMonth)) continue;
					val attendanceTime = attendanceTimeMap.get(employeeId).get(yearMonth);

					// 勤怠項目値リストに変換する準備をする
					val monthlyConverter = attendanceItemConverterFact.createMonthlyConverter();
					monthlyConverter.withAttendanceTime(attendanceTime);
					if (affiliationInfoMap.containsKey(employeeId)){
						if (affiliationInfoMap.get(employeeId).containsKey(yearMonth)){
							monthlyConverter.withAffiliation(affiliationInfoMap.get(employeeId).get(yearMonth));
						}
					}
					if (anyItemMap.containsKey(employeeId)){
						if (anyItemMap.get(employeeId).containsKey(yearMonth)){
							monthlyConverter.withAnyItem(anyItemMap.get(employeeId).get(yearMonth));
						}
					}
					if (annualLeaveMap.containsKey(employeeId)){
						if (annualLeaveMap.get(employeeId).containsKey(yearMonth)){
							monthlyConverter.withAnnLeave(annualLeaveMap.get(employeeId).get(yearMonth));
						}
					}
					if (reserveLeaveMap.containsKey(employeeId)){
						if (reserveLeaveMap.get(employeeId).containsKey(yearMonth)){
							monthlyConverter.withRsvLeave(reserveLeaveMap.get(employeeId).get(yearMonth));
						}
					}
					if (absenceLeaveMap.containsKey(employeeId)){
						if (absenceLeaveMap.get(employeeId).containsKey(yearMonth)){
							monthlyConverter.withAbsenceLeave(absenceLeaveMap.get(employeeId).get(yearMonth));
						}
					}
					if (monDayoffMap.containsKey(employeeId)){
						if (monDayoffMap.get(employeeId).containsKey(yearMonth)){
							monthlyConverter.withDayOff(monDayoffMap.get(employeeId).get(yearMonth));
						}
					}
					if (specialLeaveMap.containsKey(employeeId)){
						if (specialLeaveMap.get(employeeId).containsKey(yearMonth)){
							monthlyConverter.withSpecialLeave(specialLeaveMap.get(employeeId).get(yearMonth));
						}
					}
					
					// 月別実績データ値リストに追加する
					results.putIfAbsent(employeeId, new ArrayList<>());
					results.get(employeeId).add(MonthlyRecordValuesDto.of(
							yearMonth,
							attendanceTime.getClosureId(),
							attendanceTime.getClosureDate(),
							monthlyConverter.convert(itemIds)));
				}
			}
			
			return results;
		}
	}
}
