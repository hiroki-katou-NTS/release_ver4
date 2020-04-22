package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.actualworkinghours.SubHolOccurrenceInfo;
import nts.uk.ctx.at.record.dom.calculationattribute.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaisingSalaryTime;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSpanForDailyCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.ot.zerotime.ZeroTime;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OneDayTime;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowOTTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 残業時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public class OverTimeSheet {
	
	//加給時間
	private RaisingSalaryTime raisingsalyryTime;
	//残業枠時間帯
	private List<OverTimeFrameTimeSheetForCalc> frameTimeSheets;
	//代休発生情報
	private SubHolOccurrenceInfo subOccurrenceInfo;

	
	/**
	 * Constrctor
	 * @param subOccurrenceInfo
	 * @param frameTimeSheets
	 * @param raisingsalyryTime
	 */
	public OverTimeSheet(RaisingSalaryTime raisingsalyryTime, List<OverTimeFrameTimeSheetForCalc> frameTimeSheets,
							SubHolOccurrenceInfo subOccurrenceInfo) {
		super();
		
		this.raisingsalyryTime = raisingsalyryTime;
		this.frameTimeSheets = frameTimeSheets;
		this.subOccurrenceInfo = subOccurrenceInfo;
	}
	
	
	/**
	 * 分割後の残業時間枠時間帯を受け取り
	 * @param insertList　補正した時間帯
	 * @param originList　補正する前の時間帯
	 * @return　
	 */
	public static List<OverTimeFrameTimeSheet> correctTimeSpan(List<OverTimeFrameTimeSheet> insertList,List<OverTimeFrameTimeSheet> originList,int nowNumber){
		originList.remove(nowNumber);
		originList.addAll(insertList);
		return originList;
	}
	
	/**
	 * 残業時間枠時間帯をループさせ時間を計算する
	 * @param autoCalcSet 時間外時間の自動計算設定
	 * @param integrationOfDaily 
	 */
	public List<OverTimeFrameTime> collectOverTimeWorkTime(AutoCalOvertimeSetting autoCalcSet,
														   WorkType workType,
														   Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
														   Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet,
														   IntegrationOfDaily integrationOfDaily,
														   List<OverTimeFrameNo> statutoryFrameNoList) {
		
		Map<Integer,OverTimeFrameTime> overTimeFrameList = new HashMap<Integer, OverTimeFrameTime>();
		List<OverTimeFrameNo> numberOrder = new ArrayList<>();
		val sortedFrameTimeSheet = sortFrameTime(frameTimeSheets, workType, eachWorkTimeSet, eachCompanyTimeSet);

		//時間帯の計算
		for(OverTimeFrameTimeSheetForCalc overTimeFrameTime : sortedFrameTimeSheet) {
			val forceAtr = autoCalcSet.decisionUseCalcSetting(overTimeFrameTime.getWithinStatutryAtr(), overTimeFrameTime.isGoEarly()).getCalAtr();
			//残業時間　－　控除時間算出
			AttendanceTime calcDedTime = overTimeFrameTime.correctCalculationTime(Optional.empty(), autoCalcSet,DeductionAtr.Deduction).getTime();
			AttendanceTime calcRecTime = overTimeFrameTime.correctCalculationTime(Optional.empty(), autoCalcSet,DeductionAtr.Appropriate).getTime();
			if(!numberOrder.contains(overTimeFrameTime.getFrameTime().getOverWorkFrameNo()))
				numberOrder.add(overTimeFrameTime.getFrameTime().getOverWorkFrameNo());
			//加算だけ
			if(overTimeFrameList.containsKey(overTimeFrameTime.getFrameTime().getOverWorkFrameNo().v())) {
				val frame = overTimeFrameList.get(overTimeFrameTime.getFrameTime().getOverWorkFrameNo().v());
				val addFrame = frame.addOverTime(forceAtr.isCalculateEmbossing()?calcRecTime:new AttendanceTime(0),calcDedTime);
				overTimeFrameList.replace(overTimeFrameTime.getFrameTime().getOverWorkFrameNo().v(), addFrame);
			}
			//枠追加
			else {
				overTimeFrameList.put(overTimeFrameTime.getFrameTime().getOverWorkFrameNo().v(),
									  overTimeFrameTime.getFrameTime().addOverTime(forceAtr.isCalculateEmbossing()?calcRecTime:new AttendanceTime(0),calcDedTime));
			}
		}
		List<OverTimeFrameTime> calcOverTimeWorkTimeList = new ArrayList<>(overTimeFrameList.values()); 
		
		//staticがついていなので、4末緊急対応
		if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily() != null
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime() != null
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getActualTime() != null
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily() != null
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
			calcOverTimeWorkTimeList.forEach(ts -> {
				val wantAddTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getOverTimeWorkFrameTime()
									.stream()
									.filter(tc -> tc.getOverWorkFrameNo().equals(ts.getOverWorkFrameNo()))
									.findFirst();
				if(wantAddTime.isPresent())
					ts.addBeforeTime(wantAddTime.get().getBeforeApplicationTime());
							
			});
			List<OverTimeFrameTime> reOrderList = new ArrayList<>();
			for(OverTimeFrameNo no : numberOrder){
				val item = calcOverTimeWorkTimeList.stream().filter(tc -> tc.getOverWorkFrameNo().equals(no)).findFirst();
				item.ifPresent(tc -> reOrderList.add(tc));
			}
			calcOverTimeWorkTimeList = reOrderList;
		}
		//事前申請を上限とする制御
		val afterCalcUpperTimeList = afterUpperControl(calcOverTimeWorkTimeList,autoCalcSet,statutoryFrameNoList);
		//return afterCalcUpperTimeList; 
		//振替処理
		val aftertransTimeList = transProcess(workType,
											  afterCalcUpperTimeList,
											  eachWorkTimeSet,
											  eachCompanyTimeSet);
		return aftertransTimeList;
	}
	



	private List<OverTimeFrameTimeSheetForCalc> sortFrameTime(List<OverTimeFrameTimeSheetForCalc> frameTimeSheets, WorkType workType, Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet, Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		val useSetting = decisionUseSetting(workType, eachWorkTimeSet, eachCompanyTimeSet);
		if(!useSetting.isPresent())
			return frameTimeSheets;
		//指定した時間分振り替える
		//開始時刻のASC
		//&& 普通残業を優先するであれば普通残業、早出残業順になるようにする
		if(useSetting.get().getSubHolTransferSetAtr().isSpecifiedTimeSubHol()) {
			return frameTimeSheets.stream().sorted((first,second) -> first.timeSheet.getStart().compareTo(second.timeSheet.getStart())).collect(Collectors.toList());
		}
		//一定時間
		//開始時刻のDESC
		else {
			return frameTimeSheets.stream().sorted((first,second) -> second.timeSheet.getStart().compareTo(first.timeSheet.getStart())).collect(Collectors.toList());
		}
	}


	/**
	 * 事前申請上限制御処理
	 * @param calcOverTimeWorkTimeList 残業時間枠リスト
	 * @param autoCalcSet 残業時間の自動計算設定
	 */
	private List<OverTimeFrameTime> afterUpperControl(List<OverTimeFrameTime> calcOverTimeWorkTimeList,AutoCalOvertimeSetting autoCalcSet,List<OverTimeFrameNo> statutoryFrameNoList) {
		List<OverTimeFrameTime> returnList = new ArrayList<>();
		for(OverTimeFrameTime loopOverTimeFrame:calcOverTimeWorkTimeList) {
			
			TimeLimitUpperLimitSetting autoSet = autoCalcSet.getNormalOtTime().getUpLimitORtSet();
			if(statutoryFrameNoList != null && statutoryFrameNoList.contains(loopOverTimeFrame.getOverWorkFrameNo()))
					autoSet =  autoCalcSet.getLegalOtTime().getUpLimitORtSet();
													 
													
			//時間の上限時間算出
			AttendanceTime upperTime = desictionUseUppserTime(autoSet,loopOverTimeFrame, loopOverTimeFrame.getOverTimeWork().getTime());
			//計算時間の上限算出
//			AttendanceTime upperCalcTime = desictionUseUppserTime(autoSet,loopOverTimeFrame,  loopOverTimeFrame.getOverTimeWork().getCalcTime());
			//振替処理
			loopOverTimeFrame = loopOverTimeFrame.changeOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(upperTime.greaterThan(loopOverTimeFrame.getOverTimeWork().getTime())?loopOverTimeFrame.getOverTimeWork().getTime():upperTime,
//																														 upperCalcTime.greaterThan(loopOverTimeFrame.getOverTimeWork().getCalcTime())?loopOverTimeFrame.getOverTimeWork().getCalcTime():upperCalcTime)
																														 loopOverTimeFrame.getOverTimeWork().getCalcTime()));
			returnList.add(loopOverTimeFrame);
		}
		return returnList;
	}
	
	
	public AttendanceTime desictionUseUppserTime(TimeLimitUpperLimitSetting autoSet, OverTimeFrameTime loopOverTimeFrame, AttendanceTime attendanceTime) {
		switch(autoSet) {
		//上限なし
		case NOUPPERLIMIT:
			return attendanceTime;
		//指示時間を上限とする
		case INDICATEDYIMEUPPERLIMIT:
			return loopOverTimeFrame.getOrderTime();
		//事前申請を上限とする
		case LIMITNUMBERAPPLICATION:
			return loopOverTimeFrame.getBeforeApplicationTime();
		default:
			throw new RuntimeException("uknown AutoCalcAtr Over Time When Ot After Upper Control");
		}
	}


	/**
	 * 全枠の中に入っている控除時間(控除区分に従って)を合計する
	 * @return 控除合計時間
	 */
	public AttendanceTime calculationAllFrameDeductionTime(DeductionAtr dedAtr,ConditionAtr atr) {
		AttendanceTime totalTime = new AttendanceTime(0);
		for(OverTimeFrameTimeSheetForCalc frameTime : this.frameTimeSheets) {
			totalTime = totalTime.addMinutes(frameTime.forcs(atr,dedAtr).valueAsMinutes());
		}
		return totalTime;
	}
	
	/**
	 * 残業枠時間帯(WORK)を全て残業枠時間帯へ変換する
	 * @return　残業枠時間帯List
	 */
	public List<OverTimeFrameTimeSheet> changeOverTimeFrameTimeSheet(){
		return this.frameTimeSheets.stream().map(tc -> tc.changeNotWorkFrameTimeSheet())
											.sorted((first,second) -> first.getFrameNo().v().compareTo(second.getFrameNo().v()))
											.collect(Collectors.toList());
	}
	
	/**
	 * 残業時間帯に入っている加給時間の計算
	 */
	public List<BonusPayTime> calcBonusPayTimeInOverWorkTime(AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		ActualWorkTimeSheetAtr sheetAtr;
		for(OverTimeFrameTimeSheetForCalc timeFrame : frameTimeSheets) {
			sheetAtr = ActualWorkTimeSheetAtr.OverTimeWork;
			if(timeFrame.getWithinStatutryAtr().isStatutory()) {
				sheetAtr = ActualWorkTimeSheetAtr.StatutoryOverTimeWork;
				if(timeFrame.isGoEarly()) {
					sheetAtr = ActualWorkTimeSheetAtr.EarlyWork;
				}
			}
			bonusPayList.addAll(timeFrame.calcBonusPay(sheetAtr,raisingAutoCalcSet,bonusPayAutoCalcSet,calcAtrOfDaily,bonusPayAtr));
		}
		return sumBonusPayTime(bonusPayList);
	}
	
	/**
	 * 残業時間帯に入っている特定加給時間の計算
	 */
	public List<BonusPayTime> calcSpecBonusPayTimeInOverWorkTime(AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		ActualWorkTimeSheetAtr sheetAtr;
		for(OverTimeFrameTimeSheetForCalc timeFrame : frameTimeSheets) {
			sheetAtr = ActualWorkTimeSheetAtr.OverTimeWork;
			if(timeFrame.getWithinStatutryAtr().isStatutory()) {
				sheetAtr = ActualWorkTimeSheetAtr.StatutoryOverTimeWork;
				if(timeFrame.isGoEarly()) {
					sheetAtr = ActualWorkTimeSheetAtr.EarlyWork;
				}
			}
			bonusPayList.addAll(timeFrame.calcSpacifiedBonusPay(sheetAtr,raisingAutoCalcSet,bonusPayAutoCalcSet,calcAtrOfDaily,bonusPayAtr));
		}
		return sumBonusPayTime(bonusPayList);
	}
	
	/**
	 * 同じ加給時間Ｎｏを持つものを１つにまとめる
	 * @param bonusPayTime　加給時間
	 * @return　Noでユニークにした加給時間List
	 */
	private List<BonusPayTime> sumBonusPayTime(List<BonusPayTime> bonusPayTime){
		List<BonusPayTime> returnList = new ArrayList<>();
		List<BonusPayTime> refineList = new ArrayList<>();
		for(int bonusPayNo = 1 ; bonusPayNo <= 10 ; bonusPayNo++) {
			refineList = getByBonusPayNo(bonusPayTime, bonusPayNo);
			if(refineList.size()>0) {
				returnList.add(new BonusPayTime(bonusPayNo,
												new AttendanceTime(refineList.stream().map(tc -> tc.getBonusPayTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))),
												TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(refineList.stream().map(tc -> tc.getWithinBonusPay().getTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))),
																							  new AttendanceTime(refineList.stream().map(tc -> tc.getWithinBonusPay().getCalcTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)))),
												TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(refineList.stream().map(tc -> tc.getExcessBonusPayTime().getTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))),
																							  new AttendanceTime(refineList.stream().map(tc -> tc.getExcessBonusPayTime().getCalcTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))))
												));
			}
		}
		return returnList;
	}
	
	/**
	 * 受け取った加給時間Ｎｏを持つ加給時間を取得
	 * @param bonusPayTime 加給時間
	 * @param bonusPayNo　加給時間Ｎｏ
	 * @return　加給時間リスト
	 */
	private List<BonusPayTime> getByBonusPayNo(List<BonusPayTime> bonusPayTime,int bonusPayNo){
		return bonusPayTime.stream().filter(tc -> tc.getBonusPayTimeItemNo() == bonusPayNo).collect(Collectors.toList());
	}
	
	/**
	 * 深夜時間計算
	 * @return 計算時間get
	 */
	public AttendanceTime calcMidNightTime(AutoCalOvertimeSetting autoCalcSet) {
		
		AttendanceTime calcTime = new AttendanceTime(0);
		for(OverTimeFrameTimeSheetForCalc timeSheet:frameTimeSheets) {
			val calcSet = getCalcSetByAtr(autoCalcSet, timeSheet.getWithinStatutryAtr(),timeSheet.isGoEarly());
			if(timeSheet.getMidNightTimeSheet().isPresent()) {
				val calcValue = timeSheet.getMidNightTimeSheet().get().calcTotalTime();
				calcTime = calcTime.addMinutes(calcSet.getCalAtr().isCalculateEmbossing()?calcValue.valueAsMinutes():0);
			}
		}
		return calcTime;
	}
	
	/**
	 * 法定内区分、早出区分に従って計算区分の取得
	 * @param autoCalcSet 自動計算設定
	 * @param statutoryAtr　法定内区分
	 * @param goEarly　早出区分
	 * @return　自動計算設定
	 */
	private AutoCalSetting getCalcSetByAtr(AutoCalOvertimeSetting autoCalcSet,StatutoryAtr statutoryAtr, boolean goEarly) {
		//法内である
		if(statutoryAtr.isStatutory() ) {
			return autoCalcSet.getLegalMidOtTime();
		}
		else {
			//早出である
			if(goEarly) {
				return autoCalcSet.getEarlyMidOtTime();
			}
			else {
				return autoCalcSet.getNormalMidOtTime();
			}
		}
	}
	
	/**
	 * 代休の振替処理(残業用)
	 * @param workType　当日の勤務種類
	 * @param eachWorkTimeSet 就業時間帯別代休時間設定
	 * @param eachCompanyTimeSet 会社別代休時間設定
	 * 
	 */
	public List<OverTimeFrameTime> transProcess(WorkType workType, List<OverTimeFrameTime> afterCalcUpperTimeList,
												Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
												Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		
		val useSettingAtr = decisionUseSetting(workType, eachWorkTimeSet, eachCompanyTimeSet);
		
		if(!useSettingAtr.isPresent())
			return afterCalcUpperTimeList;
		//代休振替設定判定
		switch(useSettingAtr.get().getSubHolTransferSetAtr()) {
			//一定時間を超えたら代休とする
			case CERTAIN_TIME_EXC_SUB_HOL:
				return periodOfTimeTransfer(useSettingAtr.get().getCertainTime(),afterCalcUpperTimeList);
			//指定した時間を代休とする
			case SPECIFIED_TIME_SUB_HOL:
				return transAllTime(useSettingAtr.get().getDesignatedTime().getOneDayTime(),
								    useSettingAtr.get().getDesignatedTime().getHalfDayTime(),
									afterCalcUpperTimeList);
			default:
				throw new RuntimeException("unknown daikyuSet:");
		}
	}

	/**
	 * 代休の振替処理(残業用)
	 * @param workType　当日の勤務種類
	 * @param eachWorkTimeSet 就業時間帯別代休時間設定
	 * @param eachCompanyTimeSet 会社別代休時間設定
	 * 
	 */
	public Optional<SubHolTransferSet> decisionUseSetting(WorkType workType,
													  Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
													  Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		//平日ではない
		if(!workType.isWeekDayAttendance()) 
			return Optional.empty();
		val transSet = getTransSet(eachWorkTimeSet,eachCompanyTimeSet);
		//就業時間帯の代休設定取得できない
		if(!transSet.isPresent()||!transSet.get().isUseDivision()) {
			return Optional.empty();
		}
		else {
			//代休振替設定判定
			return transSet;
		}
	}
	
	/**
	 * 代休振替設定の取得
	 * @param eachWorkTimeSet 就業時間帯代休設定
	 * @param eachCompanyTimeSet　会社別代休設定
	 * @return　代休振替設定
	 */
	private Optional<SubHolTransferSet> getTransSet(Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
										  Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		if(eachWorkTimeSet.isPresent() && eachWorkTimeSet.get().getSubHolTimeSet().isUseDivision()) {
			return Optional.of(eachWorkTimeSet.get().getSubHolTimeSet());
		}
		else {
			if(eachCompanyTimeSet.isPresent())
				return Optional.of(eachCompanyTimeSet.get().getTransferSetting());
		}
		return Optional.empty();
	}


	/**
	 * 一定時間の振替処理
	 * @param 一定時間
	 */
	public List<OverTimeFrameTime> periodOfTimeTransfer(OneDayTime periodTime,List<OverTimeFrameTime> afterCalcUpperTimeList) {
		/*振替可能時間の計算*/
		AttendanceTime transAbleTime = calcTransferTimeOfPeriodTime(new AttendanceTime(periodTime.v()),afterCalcUpperTimeList,UseTimeAtr.TIME);
		AttendanceTime transAbleCalcTime = calcTransferTimeOfPeriodTime(new AttendanceTime(periodTime.v()),afterCalcUpperTimeList,UseTimeAtr.CALCTIME);
		/*振り替える*/
		val afterTransOverTime = trans(transAbleTime ,afterCalcUpperTimeList ,UseTimeAtr.TIME);
		val afterTransOverCalcTime = trans(transAbleCalcTime ,afterTransOverTime ,UseTimeAtr.CALCTIME);
		return afterTransOverCalcTime;
	}
	
	/**
	 * 代休の振替可能時間の計算
	 * @param periodTime 一定時間
	 * @param afterCalcUpperTimeList 残業時間枠リスト
	 * @param useTimeAtr 使用時間区分
	 * @return 振替可能時間
	 */
	private AttendanceTime calcTransferTimeOfPeriodTime(AttendanceTime periodTime,List<OverTimeFrameTime> afterCalcUpperTimeList, UseTimeAtr useTimeAtr) {
		int totalFrameTime =  useTimeAtr.isTime()
								?afterCalcUpperTimeList.stream().map(tc -> tc.getOverTimeWork().getTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))
								:afterCalcUpperTimeList.stream().map(tc -> tc.getOverTimeWork().getCalcTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc));
		if(totalFrameTime > periodTime.valueAsMinutes()) {
			return new AttendanceTime(totalFrameTime).minusMinutes(periodTime.valueAsMinutes());
		}
		else {
			return new AttendanceTime(0);
		}
	}

	
	public List<OverTimeFrameTime> trans(AttendanceTime restTransAbleTime, List<OverTimeFrameTime> afterCalcUpperTimeList,UseTimeAtr useTimeAtr) {
		List<OverTimeFrameTime> returnList = new ArrayList<>();
		//振替時間
		AttendanceTime transAbleTime = restTransAbleTime;
		//振替残時間
		AttendanceTime transRestAbleTime = restTransAbleTime;
		for(OverTimeFrameTime overTimeFrameTime : afterCalcUpperTimeList) {

			
			transAbleTime = calcTransferTime(useTimeAtr, overTimeFrameTime, transRestAbleTime);
			//振替
			val overTime = useTimeAtr.isTime()?overTimeFrameTime.getOverTimeWork().getTime().minusMinutes(transAbleTime.valueAsMinutes())
											  :overTimeFrameTime.getOverTimeWork().getCalcTime().minusMinutes(transAbleTime.valueAsMinutes());
			//振替可能時間から減算
			transRestAbleTime = transRestAbleTime.minusMinutes(transAbleTime.valueAsMinutes());
			returnList.add(calcTransTimeInFrame(useTimeAtr, overTimeFrameTime, overTime, transAbleTime));
		}
		
		return returnList;
	}
	
	/**
	 * 振替可能時間算出(振替処理前)
	 * @param useTimeAtr
	 * @param overTimeFrameTime
	 * @param transAbleTime
	 * @param transRestAbleTime
	 * @return
	 */
	private AttendanceTime calcTransferTime(UseTimeAtr useTimeAtr, OverTimeFrameTime overTimeFrameTime, AttendanceTime transRestAbleTime) {
		if(useTimeAtr.isTime()) {
			return overTimeFrameTime.getOverTimeWork().getTime().greaterThanOrEqualTo(transRestAbleTime)
																		  ?transRestAbleTime
																		  :overTimeFrameTime.getOverTimeWork().getTime();
		}
		else {
			return overTimeFrameTime.getOverTimeWork().getCalcTime().greaterThanOrEqualTo(transRestAbleTime)
					  													  ?transRestAbleTime
					  													  :overTimeFrameTime.getOverTimeWork().getCalcTime();
		}
	}
	
	
	/**
	 * 振替残時間(振替後)の算出
	 * @param useTimeAtr 使用する時間区分
	 * @param overTimeFrameTime　残業時間枠
	 * @param overTime　振替処理後の残業時間
	 * @param transAbleTime　振替時間
	 * @return 振替処理後の残業時間枠
	 */
	private OverTimeFrameTime calcTransTimeInFrame(UseTimeAtr useTimeAtr, OverTimeFrameTime overTimeFrameTime,AttendanceTime overTime, AttendanceTime transAbleTime){
		if(useTimeAtr.isTime()) {
			val changeOverTimeFrame = overTimeFrameTime.changeOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(overTime,
																															   overTimeFrameTime.getOverTimeWork().getCalcTime()));
			return changeOverTimeFrame.changeTransTime(TimeDivergenceWithCalculation.createTimeWithCalculation(transAbleTime, overTimeFrameTime.getTransferTime().getCalcTime()));
		}
		else {
			val changeOverTimeFrame = overTimeFrameTime.changeOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(overTimeFrameTime.getOverTimeWork().getTime(),
																															   overTime));
			return changeOverTimeFrame.changeTransTime(TimeDivergenceWithCalculation.createTimeWithCalculation(overTimeFrameTime.getTransferTime().getTime(), transAbleTime));
		}
	}
	
	/**
	 * 指定時間の振替処理
	 * @param prioritySet 優先設定
	 */
	public List<OverTimeFrameTime> transAllTime(OneDayTime oneDay,OneDayTime halfDay,List<OverTimeFrameTime> afterCalcUpperTimeList) {
		AttendanceTime transAbleTime = calsTransAllTime(oneDay,halfDay,afterCalcUpperTimeList,UseTimeAtr.TIME);
		AttendanceTime transAbleCalcTime = calsTransAllTime(oneDay,halfDay,afterCalcUpperTimeList,UseTimeAtr.CALCTIME);
		/*振り替える*/
		val afterTransOverTime = trans(transAbleTime ,afterCalcUpperTimeList ,UseTimeAtr.TIME);
		val afterTransOverCalcTime = trans(transAbleCalcTime ,afterTransOverTime ,UseTimeAtr.CALCTIME);
		return afterTransOverCalcTime;
	}
	
	/**
	 * 指定合計時間の計算
	 * @param 指定時間クラス 
	 */
	private AttendanceTime calsTransAllTime(OneDayTime oneDay,OneDayTime halfDay,List<OverTimeFrameTime> afterCalcUpperTimeList,UseTimeAtr useTimeAtr) {
		int totalFrameTime = useTimeAtr.isTime()
										?afterCalcUpperTimeList.stream().map(tc -> tc.getOverTimeWork().getTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))
										:afterCalcUpperTimeList.stream().map(tc -> tc.getOverTimeWork().getCalcTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc));
		if(totalFrameTime >= oneDay.valueAsMinutes()) {
			return  new AttendanceTime(oneDay.valueAsMinutes());
		}
		else {
			if(totalFrameTime >= halfDay.valueAsMinutes()) {
				return new AttendanceTime(halfDay.valueAsMinutes());
			}
			else {
				return new AttendanceTime(0);
			}
		}
	}
	/**
	 * 変形法定内残業時間の計算
	 * @return　変形法定内残業時間
	 */
	public AttendanceTime calcIrregularTime() {
		val irregularTimeSheetList = this.frameTimeSheets.stream().filter(tc -> tc.getWithinStatutryAtr().isDeformationCriterion()).collect(Collectors.toList());
		return new AttendanceTime(irregularTimeSheetList.stream().map(tc -> tc.overTimeCalculationByAdjustTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
	}
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

//	/**
//	 * 流動勤務（就外、平日）
//	 * @return
//	 */
//	public OverTimeWorkSheet createOverTimeWorkSheet(
//			TimeSpanForDailyCalc calcRange,/*1日の計算範囲の計算範囲*/
//			WithinWorkTimeSheet withinWorkTimeSheet,/*流動勤務（平日・就内）で作成した就業時間内時間帯*/
//			DeductionTimeSheet deductionTimeSheet,
//			FluidWorkTimeSetting fluidWorkTimeSetting
//			) {
//		
//		//計算範囲の取得
//		TimeSpanForDailyCalc timeSpan = new TimeSpanForDailyCalc(
//				withinWorkTimeSheet.getFrameAt(0).getTimeSheet().getEnd(),
//				calcRange.getEnd());
//		
//		//控除時間帯を取得　（保科くんが作ってくれた処理を呼ぶ）
//		
//		//残業枠の開始時刻
//		TimeWithDayAttr startClock = calcRange.getStart();
//		//残業枠設定分ループ
//		for(FluidOverTimeWorkSheet fluidOverTimeWorkSheet: fluidWorkTimeSetting.overTimeWorkSheet) {
//			//残業枠n+1の経過時間を取得
////			AttendanceTime nextElapsedTime = getnextElapsedTime(
////					fluidOverTimeWorkSheet,
////					fluidWorkTimeSetting,
////					new AttendanceTime(calcRange.lengthAsMinutes()));
//			//控除時間から残業時間帯を作成
//			OverTimeWorkFrameTimeSheet overTimeWorkFrameTimeSheet;
//			
//			
//			//次の残業枠の開始時刻に終了時刻を入れる。
//			startClock = overTimeWorkFrameTimeSheet.getTimeSheet().getEnd();
//		}
//		//時間休暇溢れ分の割り当て
//			
//		
//	}
	
	
	/**
	 * 残業枠ｎ+1．経過時間を取得する(流動勤務用)
	 * @param fluidOverTimeWorkSheet
	 * @param fluidWorkTimeSetting
	 * @param timeOfCalcRange
	 * @return
	 */
//	public AttendanceTime getnextElapsedTime(
//			FlowOTTimezone fluidOverTimeWorkSheet,
//			FlowWorkTimezoneSetting fluidWorkTimeSetting,
//			AttendanceTime timeOfCalcRange) {
//		int nextOverWorkTimeNo = fluidOverTimeWorkSheet.getWorktimeNo() + 1;
//		AttendanceTime nextlapsedTime;
//		Optional<FlowOTTimezone> nextFluidOverTimeWorkSheet = 
//				fluidWorkTimeSetting.getMatchWorkNoOverTimeWorkSheet(nextOverWorkTimeNo);
//		if(nextFluidOverTimeWorkSheet==null) {
//			nextlapsedTime = timeOfCalcRange;
//			return nextlapsedTime;
//		}
//		nextlapsedTime = nextFluidOverTimeWorkSheet.get().getFlowTimeSetting().getElapsedTime();
//		return nextlapsedTime;
//	}
	
	/**
	 * 流動勤務(平日・就外)
	 * @param personalInfo 日別計算用の個人情報
	 * @param flowWorkSetting 流動勤務設定
	 * @param predetermineTimeSetForCalc 所定時間設定(計算用クラス)
	 * @param timeSheetOfDeductionItems 控除項目の時間帯
	 * @param calcRange 残業開始終了時刻
	 * @param bonuspaySetting 加給設定
	 * @param integrationOfDaily 日別実績(Work)
	 * @param midNightTimeSheet 深夜時間帯
	 * @param overtimeSetting 残業時間の自動計算設定
	 * @param addSetting 加算設定
	 * @param timeVacationAdditionRemainingTime 休暇使用合計残時間未割当
	 * @param zeroTime 0時跨ぎ計算設定
	 * @param yesterdayInfo 勤務情報（前日）
	 * @param tommorowInfo 勤務情報（翌日）
	 * @param todayWorkType 勤務種類（当日）
	 * @param yesterdayWorkType 勤務種類（前日）
	 * @param tommorowWorkType 勤務種類（翌日）
	 * @param withinWorkTimeSheet 就業時間内時間帯
	 * @param personCommonSetting 毎日変更の可能性のあるマスタ管理クラス
	 * @param vacation 休暇クラス
	 * @param illegularAddSetting 変形労働勤務の加算設定
	 * @param flexAddSetting フレックス勤務の加算設定
	 * @param regularAddSetting 通常勤務の加算設定
	 * @param holidayAddtionSet 休暇加算時間設定
	 * @return 残業時間帯
	 */
	public static Optional<OverTimeSheet> createAsFlow(
			DailyCalculationPersonalInformation personalInfo,
			FlowWorkSetting flowWorkSetting,
			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			List<TimeSheetOfDeductionItem> timeSheetOfDeductionItems,
			TimeSpanForDailyCalc calcRange,
			Optional<BonusPaySetting> bonuspaySetting,
			IntegrationOfDaily integrationOfDaily,
			MidNightTimeSheet midNightTimeSheet,
			AddSetting addSetting,
			AttendanceTime timeVacationAdditionRemainingTime,
			ZeroTime zeroTime,
			WorkType todayWorkType,
			WorkType yesterdayWorkType,
			WorkType tommorowWorkType,
			//共通処理呼ぶ用
			Optional<WorkInformation> yesterdayInfo,
			Optional<WorkInformation> tommorowInfo,
			WithinWorkTimeSheet withinWorkTimeSheet,
			ManagePerPersonDailySet personCommonSetting,
			VacationClass vacation,
			WorkDeformedLaborAdditionSet illegularAddSetting,
			WorkFlexAdditionSet flexAddSetting,
			WorkRegularAdditionSet regularAddSetting,
			HolidayAddtionSet holidayAddtionSet) {
		
		//残業開始時刻
		TimeWithDayAttr overTimeStartTime = calcRange.getStart();
		
		//Listクラスへ変換
		TimeSheetOfDeductionItemList timeSheetOfDeductionItemList = new TimeSheetOfDeductionItemList(timeSheetOfDeductionItems);
		//重複している控除項目の時間帯
		List<TimeSheetOfDeductionItem> overlappingTimeSheets = timeSheetOfDeductionItemList.getOverlappingTimeSheets(calcRange);
		
		List<OverTimeFrameTimeSheetForCalc> overTimeframeTimeSheets = new ArrayList<>();
		
		for(FlowOTTimezone processingFlowOTTimezone : flowWorkSetting.getHalfDayWorkTimezoneLstOTTimezone()) {
			//残業時間帯の開始時刻を計算
			if(overTimeframeTimeSheets.size() != 0) {
				//枠Noの降順の1件目（前回作成した枠を取得する為）
				overTimeStartTime = overTimeframeTimeSheets.stream()
						.sorted((f,s) -> s.getOverTimeWorkSheetNo().compareTo(f.getOverTimeWorkSheetNo()))
						.map(frame -> frame.getTimeSheet().getEnd())
						.findFirst().get();
			}
			//控除時間から残業時間帯を作成
			overTimeframeTimeSheets.add(OverTimeFrameTimeSheetForCalc.createAsFlow(
					flowWorkSetting,
					processingFlowOTTimezone,
					flowWorkSetting.getHalfDayWorkTimezone().getWorkTimeZone().getLstOTTimezone(),
					overlappingTimeSheets,
					overTimeStartTime,
					calcRange.getEnd(),
					bonuspaySetting,
					integrationOfDaily.getSpecDateAttr(),
					midNightTimeSheet));
		}
		
		//時間休暇溢れ分の割り当て
		OverTimeSheet.allocateOverflowTimeVacation(
				flowWorkSetting,
				addSetting,
				timeVacationAdditionRemainingTime,
				overTimeStartTime,
				integrationOfDaily.getCalAttr().getOvertimeSetting(),
				overTimeframeTimeSheets);
		
		//流動勤務の「流動残業時間帯」を固定勤務の「残業時間の時間帯設定」へ変換
		List<OverTimeOfTimeZoneSet> overTimeHourSetList = flowWorkSetting.getHalfDayWorkTimezone().getWorkTimeZone().getLstOTTimezone().stream()
				.map(OTTimezone -> OverTimeOfTimeZoneSet.convertOverTimeOfTimeZoneSet(OTTimezone))
				.collect(Collectors.toList());
		
		//変形基準内残業を分割
		OverTimeFrameTimeSheetForCalc.dicisionCalcVariableWork(
				overTimeframeTimeSheets,
				predetermineTimeSetForCalc.getAdditionSet().getPredTime(),
				integrationOfDaily.getCalAttr().getOvertimeSetting(),
				personalInfo,
				true,
				overTimeHourSetList,//固定勤務の「残業時間の時間帯設定」、流動の「流動残業時間帯」から変換した。
				personCommonSetting.getDailyUnit(),
				nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet.convertHolidayCalcMethodSet(addSetting.getHolidayCalcMethodSet()));
		
		//法定内残業分割処理
		OverTimeFrameTimeSheetForCalc.diciaionCalcStatutory(
				flowWorkSetting.getLegalOTSetting(),
				new DailyTime(personCommonSetting.getDailyUnit().getDailyTime().valueAsMinutes()),
				overTimeframeTimeSheets,
				integrationOfDaily.getCalAttr().getOvertimeSetting(),
				predetermineTimeSetForCalc.getAdditionSet().getPredTime(),
				overTimeHourSetList,//固定勤務の「残業時間の時間帯設定」、流動の「流動残業時間帯」から変換した。
				personCommonSetting.getDailyUnit(),//何を渡せばいいのか不明
				nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet.convertHolidayCalcMethodSet(addSetting.getHolidayCalcMethodSet()),
				withinWorkTimeSheet,
				vacation,
				timeVacationAdditionRemainingTime, //timevacationUseTimeOfDaily？
				todayWorkType,
				predetermineTimeSetForCalc,
				Optional.of(flowWorkSetting.getWorkingCode()),
				integrationOfDaily.getCalAttr().getLeaveEarlySetting().isLate(),
				integrationOfDaily.getCalAttr().getLeaveEarlySetting().isLeaveEarly(),
				personCommonSetting.personInfo.get().getLaborSystem(),
				illegularAddSetting,
				flexAddSetting,
				regularAddSetting,
				holidayAddtionSet,
				Optional.of(flowWorkSetting.getCommonSetting()),
				personCommonSetting.personInfo.get(),
				Optional.empty(), //predetermineTimeSetByPersonInfo,
				Optional.empty(), //coreTimeSetting,
				WorkTimeDailyAtr.REGULAR_WORK);
		
		return Optional.of(new OverTimeSheet(new RaisingSalaryTime(), overTimeframeTimeSheets, new SubHolOccurrenceInfo()));
	}
	
	/**
	 * 時間休暇溢れ分を割り当て
	 * @param flowWorkSetting 流動勤務設定
	 * @param addSetting 加算設定 
	 * @param timeVacationAdditionRemainingTime 休暇使用合計残時間未割当
	 * @param overTimeStartTime 残業開始時刻
	 * @param autoCalcSet 残業時間の自動計算設定
	 * @param overTimeframeTimeSheets 残業枠時間帯(WORK)
	 */
	private static void allocateOverflowTimeVacation(
			FlowWorkSetting flowWorkSetting,
			AddSetting addSetting,
			AttendanceTime timeVacationAdditionRemainingTime,
			TimeWithDayAttr overTimeStartTime,
			AutoCalOvertimeSetting autoCalcSet,
			List<OverTimeFrameTimeSheetForCalc> overTimeframeTimeSheets) {
		
		//input.休暇使用合計残時間未割当のチェック
		if(timeVacationAdditionRemainingTime.lessThanOrEqualTo(AttendanceTime.ZERO)) return;
		//割増計算方法をチェック
		if(addSetting.getCalculationByActualTimeAtr(StatutoryDivision.Premium).isCalclationByActualTime()) return;
		
		if(!overTimeframeTimeSheets.isEmpty()){
			overTimeframeTimeSheets.sort((f,s) -> s.getOverTimeWorkSheetNo().compareTo(f.getOverTimeWorkSheetNo()));
			//勤務した残業時間帯に割り当てる
			timeVacationAdditionRemainingTime = overTimeframeTimeSheets.get(overTimeframeTimeSheets.size()-1).allocateToOverTimeFrame(
					timeVacationAdditionRemainingTime,
					flowWorkSetting.getHalfDayWorkTimezone().getWorkTimeZone(),
					autoCalcSet);
		}
		
		if(timeVacationAdditionRemainingTime.lessThanOrEqualTo(AttendanceTime.ZERO)) return;
		
		//勤務した残業時間帯以降に割り当てる
		overTimeframeTimeSheets.addAll(
				OverTimeSheet.createFrameWorkSheetAsNonWorkingHours(
					new EmTimezoneNo(overTimeframeTimeSheets.size()),
					timeVacationAdditionRemainingTime,
					overTimeStartTime,
					flowWorkSetting.getHalfDayWorkTimezone().getWorkTimeZone()));
	}
	
	/**
	 * 以降に割当（勤務していない残業枠時間帯を作成して時間休暇溢れ時間を持たせる）
	 * @param overTimeWorkSheetNo 就業時間帯NO
	 * @param timeVacationAdditionRemainingTime 休暇使用合計残時間未割当
	 * @param overTimeStartTime 残業開始時刻
	 * @param flowWorkTimezoneSetting 流動勤務設定
	 * @return 時間休暇溢れ時間を持たせた残業枠時間帯
	 */
	private static List<OverTimeFrameTimeSheetForCalc> createFrameWorkSheetAsNonWorkingHours(
			EmTimezoneNo overTimeWorkSheetNo,
			AttendanceTime timeVacationAdditionRemainingTime,
			TimeWithDayAttr overTimeStartTime,
			FlowWorkTimezoneSetting flowWorkTimezoneSetting){
		
		//残業枠を作成する
		List<OverTimeFrameTimeSheetForCalc> overTimeFrames = flowWorkTimezoneSetting.getLstOTTimezone().stream()
				.filter(timezone -> overTimeWorkSheetNo.lessThan(new EmTimezoneNo(timezone.getWorktimeNo())))
				.map(timezone -> OverTimeFrameTimeSheetForCalc.createEmpty(
						new TimeSpanForDailyCalc(overTimeStartTime, overTimeStartTime),
						timezone))
				.sorted((f,s) -> f.getOverTimeWorkSheetNo().compareTo(s.getOverTimeWorkSheetNo()))
				.collect(Collectors.toList());
		
		//残業枠へ割り当てる
		for(int i=0; i<overTimeFrames.size(); i++) {
			timeVacationAdditionRemainingTime = overTimeFrames.get(i).allocateOverflowTimeVacation(
					i == overTimeFrames.size()-1,
					timeVacationAdditionRemainingTime,
					flowWorkTimezoneSetting);
			
			if(timeVacationAdditionRemainingTime.lessThanOrEqualTo(AttendanceTime.ZERO)) break;
		}
		return overTimeFrames;
	}
	
}

class ReturnValueForCreateAsFlow {
	AttendanceTime timeVacationAdditionRemainingTime;
}