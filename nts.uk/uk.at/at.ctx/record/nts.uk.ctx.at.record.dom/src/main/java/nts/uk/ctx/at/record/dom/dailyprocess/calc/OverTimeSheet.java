package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.SubHolOccurrenceInfo;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.ExcessOverTimeWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeFrame;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaisingSalaryTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameNo;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.RaisingSalaryCalcAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OneDayTime;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowOTTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

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
	
	
//	/**
//	 * 残業時間の計算(残業時間帯の合計の時間を取得し1日の範囲に返す)
//	 * @return
//	 */
//	public static OverTimeOfDaily calcOverTimeWork(AutoCalculationOfOverTimeWork autoCalcSet) {
//		ControlOverFrameTime returnClass = new ControlOverFrameTime(overWorkTimeOfDaily.collectOverTimeWorkTime(autoCalcSet));
//		
//		overWorkTimeOfDaily.addToList(returnClass);
//		
//		return  overWorkTimeOfDaily;
//	}
//	
	
	/**
	 * 残業時間枠時間帯をループさせ時間を計算する
	 * @param autoCalcSet 時間外時間の自動計算設定
	 */
	public List<OverTimeFrameTime> collectOverTimeWorkTime(AutoCalOvertimeSetting autoCalcSet,
														   WorkType workType,
														   Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
														   Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		List<OverTimeFrameTime> calcOverTimeWorkTimeList = new ArrayList<>();
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(1), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(2), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(3), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(4), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(5), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(6), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(7), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(8), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(9), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(10), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		
		val forceAtr = AutoCalAtrOvertime.CALCULATEMBOSS;
		
		//時間帯の計算
		for(OverTimeFrameTimeSheetForCalc overTimeFrameTime : frameTimeSheets) {
			AttendanceTime calcDedTime = overTimeFrameTime.correctCalculationTime(Optional.empty(), autoCalcSet,DeductionAtr.Deduction);
			OverTimeFrameTime getListItem = calcOverTimeWorkTimeList.get(overTimeFrameTime.getFrameTime().getOverWorkFrameNo().v().intValue() - 1);
			getListItem.addOverTime(forceAtr.isCalculateEmbossing()?calcDedTime:new AttendanceTime(0),calcDedTime);
			calcOverTimeWorkTimeList.set(overTimeFrameTime.getFrameTime().getOverWorkFrameNo().v().intValue() - 1, getListItem);
		}
		//事前申請を上限とする制御
		val afterCalcUpperTimeList = afterUpperControl(calcOverTimeWorkTimeList,autoCalcSet);
		//振替処理
		val aftertransTimeList = transProcess(workType,
											  afterCalcUpperTimeList,
											  eachWorkTimeSet,
											  eachCompanyTimeSet);
		return aftertransTimeList;
		
	}
	



	/**
	 * 事前申請上限制御処理
	 * @param calcOverTimeWorkTimeList 残業時間枠リスト
	 * @param autoCalcSet 残業時間の自動計算設定
	 */
	private List<OverTimeFrameTime> afterUpperControl(List<OverTimeFrameTime> calcOverTimeWorkTimeList,AutoCalOvertimeSetting autoCalcSet) {
		List<OverTimeFrameTime> returnList = new ArrayList<>();
		for(OverTimeFrameTime loopOverTimeFrame:calcOverTimeWorkTimeList) {
			AttendanceTime upperTime = new AttendanceTime(0);
			switch(autoCalcSet.decisionUseCalcSetting(StatutoryAtr.Excess,false).getUpLimitORtSet()) {
				//上限なし
				case NOUPPERLIMIT:
					upperTime = loopOverTimeFrame.getOverTimeWork().getCalcTime();
					break;
				//指示時間を上限とする
				case INDICATEDYIMEUPPERLIMIT:
					upperTime = loopOverTimeFrame.getOrderTime();
					break;
				//事前申請を上限とする
				case LIMITNUMBERAPPLICATION:
					upperTime = loopOverTimeFrame.getBeforeApplicationTime();
					break;
				default:
					throw new RuntimeException("uknown AutoCalcAtr Over Time When Ot After Upper Control");
			}
			//上限制御
			if(upperTime.lessThanOrEqualTo(loopOverTimeFrame.getOverTimeWork().getCalcTime())) 
				loopOverTimeFrame = loopOverTimeFrame.changeOverTime(TimeWithCalculation.sameTime(upperTime));
			
			returnList.add(loopOverTimeFrame);
		}
		return returnList;
	}


	/**
	 * 全枠の中に入っている控除時間(控除区分に従って)を合計する
	 * @return 控除合計時間
	 */
	public AttendanceTime calculationAllFrameDeductionTime(DeductionAtr dedAtr,ConditionAtr atr) {
		AttendanceTime totalTime = new AttendanceTime(0);
		List<TimeSheetOfDeductionItem> forcsList = new ArrayList<>(); 
		for(OverTimeFrameTimeSheetForCalc frameTime : this.frameTimeSheets) {
			totalTime = totalTime.addMinutes(frameTime.forcs(forcsList,atr,dedAtr).valueAsMinutes());
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
	public List<BonusPayTime> calcBonusPayTimeInOverWorkTime(RaisingSalaryCalcAtr raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
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
	public List<BonusPayTime> calcSpecBonusPayTimeInOverWorkTime(RaisingSalaryCalcAtr raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
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
				val breakValue = timeSheet.getDedTimeSheetByAtr(DeductionAtr.Appropriate, ConditionAtr.BREAK).stream().map(tc -> tc.calcrange.lengthAsMinutes()).collect(Collectors.summingInt(tc -> tc));
				calcTime = calcTime.addMinutes(timeSheet.calcMidNight(calcSet.getCalAtr()).valueAsMinutes() - breakValue);
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
		if(statutoryAtr.isStatutory() && !goEarly) {
			return autoCalcSet.getLegalOtTime();
		}
		else if(statutoryAtr.isStatutory() && goEarly) {
			return autoCalcSet.getEarlyOtTime();
		}
		else {
			return autoCalcSet.getNormalOtTime();
		}
		
	}
	
	
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

//	/**
//	 * 流動勤務（就外、平日）
//	 * @return
//	 */
//	public OverTimeWorkSheet createOverTimeWorkSheet(
//			TimeSpanForCalc calcRange,/*1日の計算範囲の計算範囲*/
//			WithinWorkTimeSheet withinWorkTimeSheet,/*流動勤務（平日・就内）で作成した就業時間内時間帯*/
//			DeductionTimeSheet deductionTimeSheet,
//			FluidWorkTimeSetting fluidWorkTimeSetting
//			) {
//		
//		//計算範囲の取得
//		TimeSpanForCalc timeSpan = new TimeSpanForCalc(
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
	 * 残業枠ｎ+1．経過時間を取得する
	 * @param fluidOverTimeWorkSheet
	 * @param fluidWorkTimeSetting
	 * @param timeOfCalcRange
	 * @return
	 */
	public AttendanceTime getnextElapsedTime(
			FlowOTTimezone fluidOverTimeWorkSheet,
			FlowWorkTimezoneSetting fluidWorkTimeSetting,
			AttendanceTime timeOfCalcRange) {
		int nextOverWorkTimeNo = fluidOverTimeWorkSheet.getWorktimeNo() + 1;
		AttendanceTime nextlapsedTime;
		Optional<FlowOTTimezone> nextFluidOverTimeWorkSheet = 
				fluidWorkTimeSetting.getMatchWorkNoOverTimeWorkSheet(nextOverWorkTimeNo);
		if(nextFluidOverTimeWorkSheet==null) {
			nextlapsedTime = timeOfCalcRange;
			return nextlapsedTime;
		}
		nextlapsedTime = nextFluidOverTimeWorkSheet.get().getFlowTimeSetting().getElapsedTime();
		return nextlapsedTime;
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
		//平日ではない
		if(!workType.isWeekDayAttendance()) 
			return afterCalcUpperTimeList;
		val transSet = getTransSet(eachWorkTimeSet,eachCompanyTimeSet);
		//就業時間帯の代休設定取得できない
		if(!transSet.isPresent()||transSet.get().isUseDivision()) {
			return afterCalcUpperTimeList;
		}
		else {
			//代休振替設定判定
			switch(transSet.get().getSubHolTransferSetAtr()) {
				case CERTAIN_TIME_EXC_SUB_HOL:
					return periodOfTimeTransfer(transSet.get().getCertainTime(),afterCalcUpperTimeList);
				case SPECIFIED_TIME_SUB_HOL:
					return transAllTime(transSet.get().getDesignatedTime().getOneDayTime(),
										transSet.get().getDesignatedTime().getHalfDayTime(),
										afterCalcUpperTimeList);
				default:
					throw new RuntimeException("unknown daikyuSet:");
			}
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
								?afterCalcUpperTimeList.stream().map(tc -> tc.getOverTimeWork().getTime().v()).collect(Collectors.summingInt(tc -> tc))
								:afterCalcUpperTimeList.stream().map(tc -> tc.getOverTimeWork().getCalcTime().v()).collect(Collectors.summingInt(tc -> tc));
		if(periodTime.greaterThanOrEqualTo(new AttendanceTime(totalFrameTime))) {
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

			
			transAbleTime = calcTransferTime(useTimeAtr, overTimeFrameTime, transAbleTime, transRestAbleTime);
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
	private AttendanceTime calcTransferTime(UseTimeAtr useTimeAtr, OverTimeFrameTime overTimeFrameTime, AttendanceTime transAbleTime, AttendanceTime transRestAbleTime) {
		if(useTimeAtr.isTime()) {
			return overTimeFrameTime.getOverTimeWork().getTime().greaterThanOrEqualTo(transRestAbleTime)
																		  ?transAbleTime
																		  :overTimeFrameTime.getOverTimeWork().getTime();
		}
		else {
			return transAbleTime = overTimeFrameTime.getOverTimeWork().getCalcTime().greaterThanOrEqualTo(transRestAbleTime)
					  													  ?transAbleTime
					  													  :overTimeFrameTime.getOverTimeWork().getCalcTime();
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
	private OverTimeFrameTime calcTransTimeInFrame(UseTimeAtr useTimeAtr, OverTimeFrameTime overTimeFrameTime,AttendanceTime overTime, AttendanceTime transAbleTime){
		if(useTimeAtr.isTime()) {
			val changeOverTimeFrame = overTimeFrameTime.changeOverTime(TimeWithCalculation.createTimeWithCalculation(overTime , overTimeFrameTime.getOverTimeWork().getTime()));
			return changeOverTimeFrame.changeTransTime(TimeWithCalculation.createTimeWithCalculation(overTimeFrameTime.getTransferTime().getTime(), transAbleTime));
		}
		else {
			val changeOverTimeFrame = overTimeFrameTime.changeOverTime(TimeWithCalculation.createTimeWithCalculation(overTime , overTimeFrameTime.getOverTimeWork().getCalcTime()));
			return changeOverTimeFrame.changeTransTime(TimeWithCalculation.createTimeWithCalculation(overTimeFrameTime.getTransferTime().getCalcTime(), transAbleTime));
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
										?afterCalcUpperTimeList.stream().map(tc -> tc.getOverTimeWork().getTime().v()).collect(Collectors.summingInt(tc -> tc))
										:afterCalcUpperTimeList.stream().map(tc -> tc.getOverTimeWork().getCalcTime().v()).collect(Collectors.summingInt(tc -> tc));
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
	/**
	 * 変形法定内残業時間の計算
	 * @return　変形法定内残業時間
	 */
	public AttendanceTime calcIrregularTime() {
		val irregularTimeSheetList = this.frameTimeSheets.stream().filter(tc -> tc.getWithinStatutryAtr().isDeformationCriterion()).collect(Collectors.toList());
		return new AttendanceTime(irregularTimeSheetList.stream().map(tc -> tc.overTimeCalculationByAdjustTime(DeductionAtr.Deduction).valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
	}
}
