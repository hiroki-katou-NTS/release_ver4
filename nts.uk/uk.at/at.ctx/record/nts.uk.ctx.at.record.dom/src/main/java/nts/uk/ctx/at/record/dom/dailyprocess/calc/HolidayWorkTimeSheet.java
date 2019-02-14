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
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaisingSalaryTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OneDayTime;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 休日出勤時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public class HolidayWorkTimeSheet{
	//加給時間
	private RaisingSalaryTime raisingSalaryTime;
	//休出枠時間帯
	private List<HolidayWorkFrameTimeSheetForCalc> workHolidayTime;
	//代休発生情報
	private SubHolOccurrenceInfo subOccurrenceInfo;
	

	/**
	* Constructor 
 	*/
	public HolidayWorkTimeSheet(RaisingSalaryTime raisingSalaryTime, List<HolidayWorkFrameTimeSheetForCalc> workHolidayTime,
								SubHolOccurrenceInfo subOccurrenceInfo) {
		super();
		this.raisingSalaryTime = raisingSalaryTime;
		this.workHolidayTime = workHolidayTime;
		this.subOccurrenceInfo = subOccurrenceInfo;
	}
		
	/**
	 * 休出枠時間帯をループさせ時間計算をする
	 * @param integrationOfDaily 
	 * @return
	 */
	public List<HolidayWorkFrameTime> collectHolidayWorkTime(AutoCalSetting holidayAutoCalcSetting,
															 WorkType workType,
															 Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
															 Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet, IntegrationOfDaily integrationOfDaily){
		Map<Integer,HolidayWorkFrameTime> holidayTimeFrameList = new HashMap<Integer, HolidayWorkFrameTime>();
		//強制区分
		val forceAtr = holidayAutoCalcSetting.getCalAtr();
		//枠時間のソート
		val sortedFrameTimeSheet = sortFrameTime(workHolidayTime, workType, eachWorkTimeSet, eachCompanyTimeSet);
		
		List<HolidayWorkFrameNo> numberOrder = new ArrayList<>();
		
		for(HolidayWorkFrameTimeSheetForCalc holidayWorkFrameTime:sortedFrameTimeSheet) {
			AttendanceTime calcDedTime = holidayWorkFrameTime.correctCalculationTime(holidayAutoCalcSetting,DeductionAtr.Deduction);
			AttendanceTime calcRecTime = holidayWorkFrameTime.correctCalculationTime(holidayAutoCalcSetting,DeductionAtr.Appropriate);
			if(!numberOrder.contains(holidayWorkFrameTime.getFrameTime().getHolidayFrameNo()))
				numberOrder.add(holidayWorkFrameTime.getFrameTime().getHolidayFrameNo());
			//加算だけ
			if(holidayTimeFrameList.containsKey(holidayWorkFrameTime.getFrameTime().getHolidayFrameNo().v())) {
				val frame = holidayTimeFrameList.get(holidayWorkFrameTime.getFrameTime().getHolidayFrameNo().v());
				val addFrame = frame.addHolidayTimeExistReturn(forceAtr.isCalculateEmbossing()?calcRecTime:new AttendanceTime(0),calcDedTime);
				holidayTimeFrameList.replace(holidayWorkFrameTime.getFrameTime().getHolidayFrameNo().v(), addFrame);
			}
			//枠追加
			else {
				
				holidayTimeFrameList.put(holidayWorkFrameTime.getFrameTime().getHolidayFrameNo().v(),
										 holidayWorkFrameTime.getFrameTime().addHolidayTimeExistReturn(forceAtr.isCalculateEmbossing()?calcRecTime:new AttendanceTime(0),calcDedTime)
										 );
			}
		}
		//Map→List変換
		List<HolidayWorkFrameTime> calcHolidayTimeWorkTimeList = new ArrayList<>(holidayTimeFrameList.values()); 
		//staticがついていなので、4末緊急対応
		if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily() != null
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime() != null
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getActualTime() != null
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily() != null
			&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().isPresent()) {
			calcHolidayTimeWorkTimeList.forEach(ts -> {
				val wantAddTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().getHolidayWorkFrameTime()
									.stream()
									.filter(tc -> tc.getHolidayFrameNo().equals(ts.getHolidayFrameNo()))
									.findFirst();
				if(wantAddTime.isPresent())
					ts.addBeforeTime(wantAddTime.get().getBeforeApplicationTime().isPresent()?wantAddTime.get().getBeforeApplicationTime().get():new AttendanceTime(0));
							
			});
			List<HolidayWorkFrameTime> reOrderList = new ArrayList<>();
			for(HolidayWorkFrameNo no : numberOrder){
				val item = calcHolidayTimeWorkTimeList.stream().filter(tc -> tc.getHolidayFrameNo().equals(no)).findFirst();
				item.ifPresent(tc -> reOrderList.add(tc));
			}
			calcHolidayTimeWorkTimeList = reOrderList;
		}
		//staticがついていなので、4末緊急対応	
		//事前申請を上限とする制御
		val afterCalcUpperTimeList = afterUpperControl(calcHolidayTimeWorkTimeList,holidayAutoCalcSetting);
		//振替処理
		val aftertransTimeList = transProcess(workType,
											  afterCalcUpperTimeList,
											  eachWorkTimeSet,
											  eachCompanyTimeSet);
		return aftertransTimeList;
	}
	
	private List<HolidayWorkFrameTimeSheetForCalc> sortFrameTime(List<HolidayWorkFrameTimeSheetForCalc> frameTimeSheets, WorkType workType, Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet, Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
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
		if(!workType.getDailyWork().isHolidayWork() || !workType.isGenSubHolidayForHolidayWork()) 
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
	 * 休出枠時間帯(WORK)を全て休出枠時間帯へ変換する
	 * @return　休出枠時間帯List
	 */
	public List<HolidayWorkFrameTimeSheet> changeHolidayWorkTimeFrameTimeSheet(){
		return this.workHolidayTime.stream().map(tc -> tc.changeNotWorkFrameTimeSheet())
											.sorted((first,second) -> first.getHolidayWorkTimeSheetNo().v().compareTo(second.getHolidayWorkTimeSheetNo().v()))
											.collect(Collectors.toList());
	}
	
	/**
	 * 全枠の中に入っている控除時間(控除区分に従って)を合計する
	 * @return 控除合計時間
	 */
	public AttendanceTime calculationAllFrameDeductionTime(DeductionAtr dedAtr,ConditionAtr atr) {
		AttendanceTime totalTime = new AttendanceTime(0);
		for(HolidayWorkFrameTimeSheetForCalc frameTime : this.workHolidayTime) {
			totalTime = totalTime.addMinutes(frameTime.forcs(atr,dedAtr).valueAsMinutes());
		}
		return totalTime;
	}

	
	/**
	 * 休出時間帯に入っている加給時間の計算
	 */
	public List<BonusPayTime> calcBonusPayTimeInHolidayWorkTime(AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(HolidayWorkFrameTimeSheetForCalc timeFrame : workHolidayTime) {
			bonusPayList.addAll(timeFrame.calcBonusPay(ActualWorkTimeSheetAtr.HolidayWork,raisingAutoCalcSet,bonusPayAutoCalcSet,calcAtrOfDaily,bonusPayAtr));
		}
		//同じNo同士はここで加算し、Listのサイズを減らす
		return sumBonusPayTime(bonusPayList);
	}
	/**
	 * 休出時間帯に入っている特定加給時間の計算
	 */
	public List<BonusPayTime> calcSpecBonusPayTimeInHolidayWorkTime(AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(HolidayWorkFrameTimeSheetForCalc timeFrame : workHolidayTime) {
			bonusPayList.addAll(timeFrame.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.HolidayWork,raisingAutoCalcSet,bonusPayAutoCalcSet,calcAtrOfDaily,bonusPayAtr));
		}
		//同じNo同士はここで加算し、Listのサイズを減らす
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
	 * 事前申請上限制御処理
	 * @param calcHolidayTimeWorkTimeList 残業時間枠リスト
	 * @param autoCalcSet 残業時間の自動計算設定
	 */
	private List<HolidayWorkFrameTime> afterUpperControl(List<HolidayWorkFrameTime> calcHolidayTimeWorkTimeList,AutoCalSetting autoCalcSet) {
		List<HolidayWorkFrameTime> returnList = new ArrayList<>();
		for(HolidayWorkFrameTime loopHolidayTimeFrame:calcHolidayTimeWorkTimeList) {
			
			//時間の上限時間算出
			AttendanceTime upperTime = desictionUseUppserTime(autoCalcSet, loopHolidayTimeFrame,loopHolidayTimeFrame.getHolidayWorkTime().get().getTime());
			//計算時間の上限算出
//			AttendanceTime upperCalcTime = desictionUseUppserTime(autoCalcSet,  loopHolidayTimeFrame,loopHolidayTimeFrame.getHolidayWorkTime().get().getCalcTime());
			//振替処理
			loopHolidayTimeFrame = loopHolidayTimeFrame.changeOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(upperTime.greaterThan(loopHolidayTimeFrame.getHolidayWorkTime().get().getTime())?loopHolidayTimeFrame.getHolidayWorkTime().get().getTime():upperTime,
//																														 upperCalcTime.greaterThan(loopHolidayTimeFrame.getHolidayWorkTime().get().getCalcTime())?loopHolidayTimeFrame.getHolidayWorkTime().get().getCalcTime():upperCalcTime)
																															   loopHolidayTimeFrame.getHolidayWorkTime().get().getCalcTime()));
			
			returnList.add(loopHolidayTimeFrame);
		}
		return returnList;
	}
	
	public AttendanceTime desictionUseUppserTime(AutoCalSetting autoCalcSet, HolidayWorkFrameTime loopHolidayTimeFrame,AttendanceTime attendanceTime) {
		switch(autoCalcSet.getUpLimitORtSet()) {
		//上限なし
		case NOUPPERLIMIT:
			return attendanceTime;
		//指示時間を上限とする
		case INDICATEDYIMEUPPERLIMIT:
		//事前申請を上限とする
		case LIMITNUMBERAPPLICATION:
			return loopHolidayTimeFrame.getBeforeApplicationTime().get();
		default:
			throw new RuntimeException("uknown AutoCalcAtr Over Time When Ot After Upper Control");
		}
	}
	
	/**
	 * 代休の振替処理(残業用)
	 * @param workType　当日の勤務種類
	 * @param eachWorkTimeSet 就業時間帯別代休時間設定
	 * @param eachCompanyTimeSet 会社別代休時間設定
	 * 
	 */
	public List<HolidayWorkFrameTime> transProcess(WorkType workType, List<HolidayWorkFrameTime> afterCalcUpperTimeList,
												Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
												Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		val useSettingAtr = decisionUseSetting(workType, eachWorkTimeSet, eachCompanyTimeSet);
		
		if(!useSettingAtr.isPresent())
			return afterCalcUpperTimeList;
		//代休振替設定判定
		switch(useSettingAtr.get().getSubHolTransferSetAtr()) {
			case CERTAIN_TIME_EXC_SUB_HOL:
				return periodOfTimeTransfer(useSettingAtr.get().getCertainTime(),afterCalcUpperTimeList);
			case SPECIFIED_TIME_SUB_HOL:
				return transAllTime(useSettingAtr.get().getDesignatedTime().getOneDayTime(),
								    useSettingAtr.get().getDesignatedTime().getHalfDayTime(),
									afterCalcUpperTimeList);
			default:
				throw new RuntimeException("unknown daikyuSet:");
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
			if(eachCompanyTimeSet.isPresent()) {
				if(eachCompanyTimeSet.get().getTransferSetting().isUseDivision()) {
					return Optional.of(eachCompanyTimeSet.get().getTransferSetting());
				}
				else {
					return Optional.empty();
				}
			}
		}
		return Optional.empty();
	}


	/**
	 * 一定時間の振替処理
	 * @param 一定時間
	 */
	public List<HolidayWorkFrameTime> periodOfTimeTransfer(OneDayTime periodTime,List<HolidayWorkFrameTime> afterCalcUpperTimeList) {
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
	private AttendanceTime calcTransferTimeOfPeriodTime(AttendanceTime periodTime,List<HolidayWorkFrameTime> afterCalcUpperTimeList, UseTimeAtr useTimeAtr) {
		int totalFrameTime =  useTimeAtr.isTime()
								?afterCalcUpperTimeList.stream().map(tc -> tc.getHolidayWorkTime().get().getTime().v()).collect(Collectors.summingInt(tc -> tc))
								:afterCalcUpperTimeList.stream().map(tc -> tc.getHolidayWorkTime().get().getCalcTime().v()).collect(Collectors.summingInt(tc -> tc));
		if(new AttendanceTime(totalFrameTime).greaterThanOrEqualTo(periodTime)) {
			return new AttendanceTime(totalFrameTime).minusMinutes(periodTime.valueAsMinutes());
		}
		else {
			return new AttendanceTime(0);
		}
	}

	
	public List<HolidayWorkFrameTime> trans(AttendanceTime restTransAbleTime, List<HolidayWorkFrameTime> afterCalcUpperTimeList,UseTimeAtr useTimeAtr) {
		List<HolidayWorkFrameTime> returnList = new ArrayList<>();
		//振替時間
		AttendanceTime transAbleTime = restTransAbleTime;
		//振替残時間
		AttendanceTime transRestAbleTime = restTransAbleTime;
		for(HolidayWorkFrameTime holidayWorkFrameTime : afterCalcUpperTimeList) {

			transAbleTime = calcTransferTime(useTimeAtr, holidayWorkFrameTime, transRestAbleTime);
			//振替
			val overTime = useTimeAtr.isTime()?holidayWorkFrameTime.getHolidayWorkTime().get().getTime().minusMinutes(transAbleTime.valueAsMinutes())
											  :holidayWorkFrameTime.getHolidayWorkTime().get().getCalcTime().minusMinutes(transAbleTime.valueAsMinutes());
			//振替可能時間から減算
			transRestAbleTime = transRestAbleTime.minusMinutes(transAbleTime.valueAsMinutes());
			returnList.add(calcTransTimeInFrame(useTimeAtr, holidayWorkFrameTime, overTime, transAbleTime));
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
	private AttendanceTime calcTransferTime(UseTimeAtr useTimeAtr, HolidayWorkFrameTime holidayWorkFrameTime,AttendanceTime transRestAbleTime) {
		if(useTimeAtr.isTime()) {
			return holidayWorkFrameTime.getHolidayWorkTime().get().getTime().greaterThanOrEqualTo(transRestAbleTime)
																		  ?transRestAbleTime
																		  :holidayWorkFrameTime.getHolidayWorkTime().get().getTime();
		}
		else {
			return holidayWorkFrameTime.getHolidayWorkTime().get().getCalcTime().greaterThanOrEqualTo(transRestAbleTime)
					  													  ?transRestAbleTime
					  													  :holidayWorkFrameTime.getHolidayWorkTime().get().getCalcTime();
		}
	}
	
	
	/**
	 * 振替残時間(振替後)の算出
	 * @param useTimeAtr 使用する時間区分
	 * @param overTimeFrameTime　残業時間枠
	 * @param overTime　残業時間
	 * @param transAbleTime　振替時間
	 * @return 振替処理後の残業時間枠
	 */
	private HolidayWorkFrameTime calcTransTimeInFrame(UseTimeAtr useTimeAtr, HolidayWorkFrameTime holidayWorkTimeFrame,AttendanceTime holidayWorkTime, AttendanceTime transAbleTime){
		if(useTimeAtr.isTime()) {
			val changeOverTimeFrame = holidayWorkTimeFrame.changeOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(holidayWorkTime , 
																																  holidayWorkTimeFrame.getHolidayWorkTime().get().getCalcTime()));
			return changeOverTimeFrame.changeTransTime(TimeDivergenceWithCalculation.createTimeWithCalculation(transAbleTime,
																											   holidayWorkTimeFrame.getTransferTime().get().getCalcTime()));
		}
		else {
			val changeOverTimeFrame = holidayWorkTimeFrame.changeOverTime(TimeDivergenceWithCalculation.createTimeWithCalculation(holidayWorkTimeFrame.getHolidayWorkTime().get().getTime(),
																																  holidayWorkTime));
			
			return changeOverTimeFrame.changeTransTime(TimeDivergenceWithCalculation.createTimeWithCalculation(holidayWorkTimeFrame.getTransferTime().get().getTime(),
																											   transAbleTime));
		}
	}
	
	/**
	 * 指定時間の振替処理
	 * @param prioritySet 優先設定
	 */
	public List<HolidayWorkFrameTime> transAllTime(OneDayTime oneDay,OneDayTime halfDay,List<HolidayWorkFrameTime> afterCalcUpperTimeList) {
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
	private AttendanceTime calsTransAllTime(OneDayTime oneDay,OneDayTime halfDay,List<HolidayWorkFrameTime> afterCalcUpperTimeList,UseTimeAtr useTimeAtr) {
		int totalFrameTime = useTimeAtr.isTime()
										?afterCalcUpperTimeList.stream().map(tc -> tc.getHolidayWorkTime().get().getTime().v()).collect(Collectors.summingInt(tc -> tc))
										:afterCalcUpperTimeList.stream().map(tc -> tc.getHolidayWorkTime().get().getCalcTime().v()).collect(Collectors.summingInt(tc -> tc));
		if(totalFrameTime >= oneDay.valueAsMinutes()) {
			return  new AttendanceTime(oneDay.v());
		}
		else {
			if(totalFrameTime >= halfDay.valueAsMinutes()) {
				return new AttendanceTime(halfDay.v());
			}
			else {
				return new AttendanceTime(0);
			}
		}
	}

	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
//	/**
//	 * 流動勤務（休日出勤）
//	 */
//	public HolidayWorkTimeSheet createholidayWorkTimeSheet(
//			AttendanceLeavingWorkOfDaily attendanceLeavingWork,
//			List<FluWorkHolidayTimeSheet> workingTimes,
//			DeductionTimeSheet deductionTimeSheet,
//			WorkType workType,
//			HolidayWorkTimeOfDaily holidayWorkTimeOfDaily,
//			TimeSpanForCalc calcRange/*1日の計算範囲*/
//			) {
//		//出退勤時間帯を作成
//		TimeSpanForCalc attendanceLeavingWorkTimeSpan = new TimeSpanForCalc(
//				attendanceLeavingWork.getAttendanceLeavingWork(1).getAttendance().getEngrave().getTimesOfDay(),
//				attendanceLeavingWork.getLeavingWork().getEngrave().getTimesOfDay());
//		//出退勤時間帯と１日の計算範囲の重複部分を計算範囲とする
//		TimeSpanForCalc collectCalcRange = calcRange.getDuplicatedWith(attendanceLeavingWorkTimeSpan).orElse(null);
//		//休出枠時間帯を入れるListを作成
//		List<HolidayWorkFrameTimeSheet> holidayWorkFrameTimeSheetList = new ArrayList<>();
//		//前回のループの経過時間保持用(時間は0：00で作成)
//		AttendanceTime previousElapsedTime = new AttendanceTime(0);
//		//全ての休出枠設定分ループ
//		for(FluWorkHolidayTimeSheet fluWorkHolidayTimeSheet : workingTimes) {
//			//控除時間から休出時間帯を作成
//			HolidayWorkFrameTimeSheet holidayWorkFrameTimeSheet;
//			holidayWorkFrameTimeSheet.collectHolidayWorkFrameTimeSheet(fluWorkHolidayTimeSheet, workType, deductionTimeSheet, collectCalcRange, previousElapsedTime);
//			holidayWorkFrameTimeSheetList.add(holidayWorkFrameTimeSheet);
//			//次のループでの休出枠時間計算用に今回の経過時間を退避
//			previousElapsedTime = fluWorkHolidayTimeSheet.getFluidTimeSetting().getElapsedTime();
//		}
//		HolidayWorkTimeSheet holidayWorkTimeSheet = new HolidayWorkTimeSheet();
//		return  holidayWorkTimeSheet;
//	}
	
	
}
