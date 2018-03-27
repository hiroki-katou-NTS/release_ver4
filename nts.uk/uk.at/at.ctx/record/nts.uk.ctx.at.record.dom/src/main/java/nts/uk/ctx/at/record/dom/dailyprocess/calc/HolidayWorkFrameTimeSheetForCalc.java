package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.BreakdownItemNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSetOfHolidayWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.BreakFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixOffdayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkHolidayTimeZone;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 計算用休出枠時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public class HolidayWorkFrameTimeSheetForCalc extends CalculationTimeSheet{
	
	private HolidayWorkFrameTime frameTime;
	
	private boolean TreatAsTimeSpentAtWork;
	
	private EmTimezoneNo HolidayWorkTimeSheetNo; 
	
	private Finally<StaturoryAtrOfHolidayWork> statutoryAtr;
	
	/**
	 * constructor
	 * @param timeSheet 時間帯(丸め付き)
	 * @param calculationTimeSheet 計算範囲
	 * @param deductionTimeSheets 控除項目の時間帯
	 * @param bonusPayTimeSheet 加給時間帯
	 * @param midNighttimeSheet 
	 * @param frameTime
	 * @param treatAsTimeSpentAtWork
	 * @param holidayWorkTimeSheetNo
	 */
	public HolidayWorkFrameTimeSheetForCalc(TimeZoneRounding timeSheet, TimeSpanForCalc calcrange,
			List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
			List<TimeSheetOfDeductionItem> deductionTimeSheets, List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,
			List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet, Optional<MidNightTimeSheetForCalc> midNighttimeSheet,
			HolidayWorkFrameTime frameTime, boolean treatAsTimeSpentAtWork, EmTimezoneNo holidayWorkTimeSheetNo,
			Finally<StaturoryAtrOfHolidayWork> statutoryAtr) {
		super(timeSheet, calcrange, recorddeductionTimeSheets, deductionTimeSheets, bonusPayTimeSheet, specifiedBonusPayTimeSheet,
				midNighttimeSheet);
		this.frameTime = frameTime;
		TreatAsTimeSpentAtWork = treatAsTimeSpentAtWork;
		HolidayWorkTimeSheetNo = holidayWorkTimeSheetNo;
		this.statutoryAtr = statutoryAtr;
	}
	
	
	/**
	 * 計算用休出枠時間帯リストの作成
	 * @return
	 */
	public static List<HolidayWorkFrameTimeSheetForCalc> createHolidayTimeWorkFrame(TimeLeavingWork attendanceLeave,List<HDWorkTimeSheetSetting> holidayWorkTimeList,WorkType todayWorkType
																					,BonusPaySetting bonusPaySetting,MidNightTimeSheet midNightTimeSheet,DeductionTimeSheet deductionTimeSheet) {
		List<HolidayWorkFrameTimeSheetForCalc> returnList = new ArrayList<>();
		for(HDWorkTimeSheetSetting holidayWorkTime:holidayWorkTimeList) {
			val duplicateTimeSpan = holidayWorkTime.getTimezone().timeSpan().getDuplicatedWith(attendanceLeave.getTimespan()); 
			if(duplicateTimeSpan.isPresent()) {
				returnList.add(createHolidayTimeWorkFrameTimeSheet(duplicateTimeSpan.get(),holidayWorkTime,todayWorkType,bonusPaySetting,midNightTimeSheet,deductionTimeSheet));
			}
		}
		return returnList;
	}
	
	/**
	 * 計算用休出枠時間帯から休出枠時間帯へ変換する
	 * @return　残業枠時間帯
	 */
	public HolidayWorkFrameTimeSheet changeNotWorkFrameTimeSheet() {
		return new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(this.HolidayWorkTimeSheetNo.v().intValue()),this.calcrange);
	}
	
	/**
	 * 計算用休出枠ｔ時間帯(WORK)の作成
	 * @param holidayWorkFrameTimeSheet
	 * @param today
	 * @return
	 */
	public static HolidayWorkFrameTimeSheetForCalc createHolidayTimeWorkFrameTimeSheet(TimeSpanForCalc timeSpan,HDWorkTimeSheetSetting holidayWorkFrameTimeSheet,WorkType today
																					  ,BonusPaySetting bonusPaySetting,MidNightTimeSheet midNightTimeSheet,DeductionTimeSheet deductionTimeSheet) {

		//時間帯跨いだ控除時間帯分割
		List<TimeSheetOfDeductionItem> dedTimeSheet = deductionTimeSheet.getDupliRangeTimeSheet(timeSpan, DeductionAtr.Deduction);
		List<TimeSheetOfDeductionItem> recordTimeSheet = deductionTimeSheet.getDupliRangeTimeSheet(timeSpan, DeductionAtr.Appropriate);
		//控除時間帯を保持させる(継承先に)
		//控除の丸め
		//休出枠No
		BreakFrameNo breakFrameNo = holidayWorkFrameTimeSheet.decisionBreakFrameNoByHolidayAtr(today.getWorkTypeSetList().get(0).getHolidayAtr());
		/*加給*/
		/*加給*/
		val duplibonusPayTimeSheet = getDuplicatedBonusPay(bonusPaySetting.getLstBonusPayTimesheet().stream().map(tc ->BonusPayTimeSheetForCalc.convertForCalc(tc)).collect(Collectors.toList()),
													  	   timeSpan);
											 
		/*特定日*/
		val duplispecifiedBonusPayTimeSheet = getDuplicatedSpecBonusPay(bonusPaySetting.getLstSpecBonusPayTimesheet().stream().map(tc -> SpecBonusPayTimeSheetForCalc.convertForCalc(tc)).collect(Collectors.toList()),
																   		timeSpan);
		/*深夜*/
		val duplicatemidNightTimeSheet = getDuplicateMidNight(midNightTimeSheet,
														 	  timeSpan);
		
		return new HolidayWorkFrameTimeSheetForCalc(new TimeZoneRounding(timeSpan.getStart(),timeSpan.getEnd(),holidayWorkFrameTimeSheet.getTimezone().getRounding()),
													timeSpan,
													dedTimeSheet.stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
													recordTimeSheet.stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
													duplibonusPayTimeSheet,
													duplispecifiedBonusPayTimeSheet,
													duplicatemidNightTimeSheet,
													new HolidayWorkFrameTime(new HolidayWorkFrameNo(breakFrameNo.v().intValue()),
										  					Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),
										  					Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),
										  					Finally.of(new AttendanceTime(0))),
													false,
													new EmTimezoneNo(holidayWorkFrameTimeSheet.getWorkTimeNo()),
													Finally.of(StaturoryAtrOfHolidayWork.deicisionAtrByHolidayAtr(today.getWorkTypeSetList().get(0).getHolidayAtr())));
	}
	/**
	 * 残業時間帯時間枠に残業時間を埋める
	 * @param autoCalcSet 時間外の自動計算区分
	 * @return 残業時間枠時間帯クラス
	 */
	public HolidayWorkFrameTime calcOverTimeWorkTime(AutoCalcSetOfHolidayWorkTime autoCalcSet) {
		AttendanceTime holidayWorkTime;
		if(autoCalcSet.getLateNightTime().getCalculationClassification().isCalculateEmbossing()) {
			holidayWorkTime = new AttendanceTime(0);
		}
		else {
			holidayWorkTime = this.calcTotalTime();
		}
		return  new HolidayWorkFrameTime(this.frameTime.getHolidayFrameNo()
				,this.frameTime.getTransferTime()
				,Finally.of(TimeWithCalculation.sameTime(holidayWorkTime))
				,this.frameTime.getBeforeApplicationTime());
	}
	
	/**
	 * 計算処理
	 * 休出時間の計算
	 * @param forceCalcTime 強制時間区分
	 * @param autoCalcSet 
	 */
	public AttendanceTime correctCalculationTime(AutoCalSetting autoCalcSet,DeductionAtr dedAtr) {
		
		//区分をみて、計算設定を設定
		//一旦、打刻から計算する場合　を入れとく
		val forceAtr = AutoCalAtrOvertime.CALCULATEMBOSS;
		
		AttendanceTime calcTime = holidayWorkTimeCalculationByAdjustTime(dedAtr);
		
		if(!forceAtr.isCalculateEmbossing()) {
			calcTime = new AttendanceTime(0);
		}
		return calcTime;
	}
	
	/**
	 * 調整時間を考慮した時間の計算
	 * @param autoCalcSet 時間外の自動計算区分
	 * @return 残業時間枠時間帯クラス
	 */
	public AttendanceTime holidayWorkTimeCalculationByAdjustTime(DeductionAtr dedAtr) {
		//休憩時間
		AttendanceTime calcBreakTime = calcDedTimeByAtr(dedAtr,ConditionAtr.BREAK);
		//組合外出時間
		AttendanceTime calcUnionGoOutTime = calcDedTimeByAtr(dedAtr,ConditionAtr.UnionGoOut);
		//私用外出時間
		AttendanceTime calcPrivateGoOutTime = calcDedTimeByAtr(dedAtr,ConditionAtr.PrivateGoOut);
		//介護
		AttendanceTime calcCareTime = calcDedTimeByAtr(dedAtr,ConditionAtr.Care);
		//育児時間
		AttendanceTime calcChildTime = calcDedTimeByAtr(dedAtr,ConditionAtr.Child);
		return new AttendanceTime(this.calcrange.lengthAsMinutes()
								 -calcBreakTime.valueAsMinutes()
								 -calcUnionGoOutTime.valueAsMinutes()
								 -calcPrivateGoOutTime.valueAsMinutes()
								 -calcCareTime.valueAsMinutes()
								 -calcChildTime.valueAsMinutes());
	}
	
	
	/**
	 *　指定条件の控除項目だけの控除時間
	 * @param forcsList
	 * @param atr
	 * @return
	 */
	public AttendanceTime forcs(List<TimeSheetOfDeductionItem> forcsList,ConditionAtr atr,DeductionAtr dedAtr){
		AttendanceTime dedTotalTime = new AttendanceTime(0);
		val loopList = this.getDedTimeSheetByAtr(dedAtr, atr);
		for(TimeSheetOfDeductionItem deduTimeSheet: loopList) {
			if(deduTimeSheet.checkIncludeCalculation(atr)) {
				dedTotalTime = dedTotalTime.addMinutes(deduTimeSheet.calcTotalTime().valueAsMinutes());
			}
		}
		return dedTotalTime;
	}
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
	/**
	 * 控除時間を考慮して終了時刻を求める
	 * @return
	 */
	public HolidayWorkFrameTimeSheet collectHolidayWorkFrameTimeSheet(
			FlowWorkHolidayTimeZone fluWorkHolidayTimeSheet,
			WorkType workType,
			DeductionTimeSheet deductionTimeSheet,/*事前処理で作成した控除時間帯で良い？*/
			TimeSpanForCalc collectCalcRange,/*計算範囲*/
			AttendanceTime previousElapsedTime/*前回の経過時間*/
			) {
		//今回の処理の経過時間
		AttendanceTime elapsedTime = fluWorkHolidayTimeSheet.getFlowTimeSetting().getElapsedTime();
		//休出枠の時間を計算する (今回処理する経過時間-前回の経過時間)
		AttendanceTime holidayWorkFrameTime =  new AttendanceTime(elapsedTime.valueAsMinutes()-previousElapsedTime.valueAsMinutes());
		//休出枠時間から終了時刻を計算する
		TimeWithDayAttr endClock = collectCalcRange.getStart().backByMinutes(elapsedTime.valueAsMinutes());
		//休出枠時間帯　（一時的に作成）
		TimeSpanForCalc holidayWorkFrameTimeSheet = new TimeSpanForCalc(collectCalcRange.getStart(),endClock);
		//控除時間帯分ループ
		for(TimeSheetOfDeductionItem timeSheetOfDeductionItem : deductionTimeSheet.getForDeductionTimeZoneList()) {
			TimeSpanForCalc duplicateTime = holidayWorkFrameTimeSheet.getDuplicatedWith(timeSheetOfDeductionItem.getTimeSheet().timeSpan()).orElse(null);
			if(duplicateTime!=null) {//重複している場合の処理
				//控除項目の時間帯に法定内区分をセット
				//TODO: createBreakTimeSheetAsFixed
//				timeSheetOfDeductionItem = timeSheetOfDeductionItem.createBreakTimeSheetAsFixed(
//						timeSheetOfDeductionItem.getTimeSheet().getSpan(),
//						timeSheetOfDeductionItem.getGoOutReason(),
//						timeSheetOfDeductionItem.getBreakAtr(),
//						timeSheetOfDeductionItem.getDeductionAtr(),
//						WithinStatutoryAtr.WithinStatutory);
				//控除時間分、終了時刻を遅くする
				TimeSpanForCalc collectTimeSheet = this.timeSheet.timeSpan().shiftEndBack(duplicateTime.lengthAsMinutes());
				TimeZoneRounding newTimeSheet = this.timeSheet;
				// ここはベトナムへ連絡後コメントアウトを外すnewTimeSheet.newTimeSpan(collectTimeSheet);
			}	
		}
		
		//休出枠NOを判断
		
		//控除時間帯に丸め設定を付与

		//加給時間帯を作成
		
		//深夜時間帯を作成
		
		//TODO: return HolidayWorkFrameTimeSheet
//		HolidayWorkFrameTimeSheet holidayWorkFrameTimeSheet2 =  new HolidayWorkFrameTimeSheet();
		return null;
	}

	
}
