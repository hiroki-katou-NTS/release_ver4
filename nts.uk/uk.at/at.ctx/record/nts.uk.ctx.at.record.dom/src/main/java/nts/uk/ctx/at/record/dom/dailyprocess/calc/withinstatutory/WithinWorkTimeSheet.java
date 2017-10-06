package nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.daily.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.CalcAtrOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.WorkInformationOfDaily;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeSheetOfDaily;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ActualWorkTimeSheetAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateTimeSheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LeaveEarlyTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetOfDeductionItem;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.WithinStatutoryAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.CalculationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayAdditionAtr;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecifiedbonusPayTimeSheet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalculationCategoryOutsideHours;
import nts.uk.ctx.at.shared.dom.worktime.AmPmClassification;
import nts.uk.ctx.at.shared.dom.worktime.CommomSetting.PredetermineTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.WorkTimeCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.WorkTimeOfTimeSheetSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.WorkTimeOfTimeSheetSetList;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.FluidWorkSetting;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 就業時間内時間帯
 * @author keisuke_hoshina
 *
 */
@RequiredArgsConstructor
public class WithinWorkTimeSheet implements {

	//必要になったら追加
	//private WorkingHours
	//private RaisingSalaryTime
	
	private final List<WithinWorkTimeFrame> withinWorkTimeFrame;
	private final List<LeaveEarlyDecisionClock> leaveEarlyDecisionClock;
	private final List<LateDecisionClock> lateDecisionClock;
	
	/**
	 * 就業時間内時間帯の作成
	 * @param workType　勤務種類クラス
	 * @param predetermineTimeSet 所定時間設定クラス
	 * @param fixedWorkSetting  固定勤務設定クラス
	 * @return 就業時間内時間帯クラス
	 */
	public static WithinWorkTimeSheet createAsFixedWork(
			WorkType workType,
			PredetermineTimeSet predetermineTimeSet,
			FixedWorkSetting fixedWorkSetting,
			WorkTimeCommonSet workTimeCommonSet,
			DeductionTimeSheet deductionTimeSheet,
			BonusPaySetting bonusPaySetting
			) {
		

		predetermineTimeSet.getSpecifiedTimeSheet().correctPredetermineTimeSheet(workType.getDailyWork());

		//遅刻猶予時間の取得
		val lateGraceTime = workTimeCommonSet.getLateSetting().getGraceTimeSetting();//引数でworkTimeCommonSet毎渡すように修正予定
		//早退猶予時間の取得
		val leaveEarlyGraceTime = workTimeCommonSet.getLeaveEarlySetting().getGraceTimeSetting();
						
		val timeFrames = new ArrayList<WithinWorkTimeFrame>();
		WithinWorkTimeFrame timeFrame;
		val workingHourSet = createWorkingHourSet(workType, predetermineTimeSet, fixedWorkSetting);
		
		for (int frameNo = 0; frameNo < workingHourSet.toArray().length; frameNo++) {
			List<BonusPayTimesheet> bonusPayTimeSheet = new ArrayList<>();
			List<SpecifiedbonusPayTimeSheet> specifiedBonusPayTimeSheet = new ArrayList<>();
			Optional<MidNightTimeSheet> midNightTimeSheet;
			for(WorkTimeOfTimeSheetSet duplicateTimeSheet :workingHourSet) {
				timeFrame = new WithinWorkTimeFrame(frameNo, duplicateTimeSheet.getTimeSpan(),duplicateTimeSheet.getTimeSpan(),Collections.emptyList(),Collections.emptyList(),Optional.empty(),Collections.emptyList());
				/*控除時間を分割する
				 * */
				/*加給*/
				bonusPayTimeSheet = bonusPaySetting.createDuplicationBonusPayTimeSheet(duplicateTimeSheet.getTimeSpan());
				specifiedBonusPayTimeSheet = bonusPaySetting.createDuplicationSpecifyBonusPay(duplicateTimeSheet.getTimeSpan());
				/*深夜*/
				midNightTimeSheet = timeFrame.createMidNightTimeSheet();
				
				timeFrames.add(new WithinWorkTimeFrame(timeFrame.getWorkingHoursTimeNo(),timeFrame.getTimeSheet(),timeFrame.getCalcrange(),timeFrame.getDeductionTimeSheets(),bonusPayTimeSheet,midNightTimeSheet,specifiedBonusPayTimeSheet));
			}
		}
		
		/*所定内割増時間の時間帯作成*/
		
		return new WithinWorkTimeSheet(
				timeFrames,
				LeaveEarlyDecisionClock.createListOfAllWorks(predetermineTimeSet, deductionTimeSheet, leaveEarlyGraceTime),
				LateDecisionClock.createListOfAllWorks(predetermineTimeSet, deductionTimeSheet, lateGraceTime));
	}
	
	/**
	 * 遅刻・早退時間を控除する
	 */
	public void deductLateAndLeaveEarly() {
		
	}
	
	/**
	 * 指定した枠番の就業時間内時間枠を返す
	 * @param frameNo
	 * @return
	 */
	public WithinWorkTimeFrame getFrameAt(int frameNo) {
		return this.withinWorkTimeFrame.get(frameNo);
	}

	/**
	 *  所定時間と重複している時間帯の判定
	 * @param workType　勤務種類クラス
	 * @param predetermineTimeSet 所定時間設定クラス
	 * @param fixedWorkSetting 固定勤務設定クラス
	 * @return 所定時間と重複している時間帯
	 */
	private static List<WorkTimeOfTimeSheetSet> createWorkingHourSet(WorkType workType, PredetermineTimeSet predetermineTimeSet,
			FixedWorkSetting fixedWorkSetting) {
		
		val attendanceHolidayAttr = workType.getAttendanceHolidayAttr();
		return getWorkingHourSetByAmPmClass(fixedWorkSetting, attendanceHolidayAttr).extractBetween(
				predetermineTimeSet.getDateStartTime(),
				new TimeWithDayAttr(predetermineTimeSet.getPredetermineEndTime()));
	}

	/**
	 * 平日出勤の出勤時間帯を取得
	 * @param fixedWorkSetting 固定勤務設定クラス
	 * @param attendanceHolidayAttr 出勤休日区分
	 * @return 出勤時間帯
	 */
	private static WorkTimeOfTimeSheetSetList getWorkingHourSetByAmPmClass(
			FixedWorkSetting fixedWorkSetting,
			AttendanceHolidayAttr attendanceHolidayAttr) {
		
		switch (attendanceHolidayAttr) {
		case FULL_TIME:
		case HOLIDAY:
			return fixedWorkSetting.getWorkingHourSet(AmPmClassification.ONE_DAY);
		case MORNING:
			return fixedWorkSetting.getWorkingHourSet(AmPmClassification.AM);
		case AFTERNOON:
			return fixedWorkSetting.getWorkingHourSet(AmPmClassification.PM);
		default:
			throw new RuntimeException("unknown attendanceHolidayAttr" + attendanceHolidayAttr);
		}
	}
	
	/**
	 * 引数のNoと一致する遅刻判断時刻を取得する
	 * @param workNo
	 * @return　遅刻判断時刻
	 */
	public LateDecisionClock getlateDecisionClock(int workNo) {
		List<LateDecisionClock> clockList = this.lateDecisionClock.stream().filter(tc -> tc.getWorkNo()==workNo).collect(Collectors.toList());
		if(clockList.size()>1) {
			throw new RuntimeException("Exist duplicate workNo : " + workNo);
		}
		return clockList.get(0);
	}
	
	/**
	 * 引数のNoと一致する早退判断時刻を取得する
	 * @param workNo
	 * @return　早退判断時刻
	 */
	public LeaveEarlyDecisionClock getleaveEarlyDecisionClock(int workNo) {
		List<LeaveEarlyDecisionClock> clockList = this.leaveEarlyDecisionClock.stream().filter(tc -> tc.getWorkNo()==workNo).collect(Collectors.toList());
		if(clockList.size()>1) {
			throw new RuntimeException("Exist duplicate workNo : " + workNo);
		}
		return clockList.get(0);
	}
	
	
	/**
	 * 日別計算の遅刻早退時間の計算
	 * @return
	 */
	public int calcLateLeaveEarlyinWithinWorkTime() {
		
	}
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
	//就業時間内時間帯クラスを作成　　（流動勤務）
	public WithinWorkTimeSheet createAsFluidWork(
			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			WorkType workType,
			WorkInformationOfDaily workInformationOfDaily,
			FluidWorkSetting fluidWorkSetting,
			DeductionTimeSheet deductionTimeSheet) {
		//開始時刻を取得
		TimeWithDayAttr startClock = getStartClock();
		//所定時間帯、残業開始を補正
		cllectPredetermineTimeAndOverWorkTimeStart();
		//残業開始となる経過時間を取得
		AttendanceTime elapsedTime = fluidWorkSetting.getWeekdayWorkTime().getWorkTimeSheet().getMatchWorkNoOverTimeWorkSheet(1).getFluidWorkTimeSetting().getElapsedTime();
		//経過時間から終了時刻を計算
		TimeWithDayAttr endClock = startClock.backByMinutes(elapsedTime.valueAsMinutes());
		//就業時間帯の作成（一時的に作成）
		TimeSpanForCalc workTimeSheet = new TimeSpanForCalc(startClock,endClock);
		//控除時間帯を取得 (控除時間帯分ループ）
		for(TimeSheetOfDeductionItem timeSheetOfDeductionItem : deductionTimeSheet.getForDeductionTimeZoneList()) {
			//就業時間帯に重複する控除時間を計算
			TimeSpanForCalc duplicateTime = workTimeSheet.getDuplicatedWith(timeSheetOfDeductionItem.getTimeSheet().getSpan()).orElse(null);
			//就業時間帯と控除時間帯が重複しているかチェック
			if(duplicateTime!=null) {
				//控除項目の時間帯に法定内区分をセット
				timeSheetOfDeductionItem = new TimeSheetOfDeductionItem(
						timeSheetOfDeductionItem.getTimeSheet().getSpan(),
						timeSheetOfDeductionItem.getGoOutReason(),
						timeSheetOfDeductionItem.getBreakAtr(),
						timeSheetOfDeductionItem.getDeductionAtr(),
						WithinStatutoryAtr.WithinStatutory);
				//控除時間分、終了時刻をズラす
				endClock.backByMinutes(duplicateTime.lengthAsMinutes());
				//休暇加算するかチェックしてズラす
				
			}		
		}
		//就業時間内時間帯クラスを作成
		
		
		
	}
	
	/**
	 * 開始時刻を取得　　（流動勤務（平日・就内））
	 * @return
	 */
	public TimeWithDayAttr getStartClock() {
		
	}
	
	
	//所定時間帯、残業開始を補正
	public void cllectPredetermineTimeAndOverWorkTimeStart(
			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			WorkType workType,
			WorkInformationOfDaily workInformationOfDaily) {
		//所定時間帯を取得
		predetermineTimeSetForCalc.correctPredetermineTimeSheet(workType.getDailyWork());
		//予定所定時間が変更された場合に所定時間を変更するかチェック
		//勤務予定と勤務実績の勤怠情報を比較
		//勤務種類が休日出勤でないかチェック
		if(
				!workInformationOfDaily.isMatchWorkInfomation()||
				workType.getDailyWork().isHolidayWork()
				) {
			return;
		}
		//就業時間帯の所定時間と予定時間を比較
			
		//計算用所定時間設定を所定終了ずらす時間分ズラす
		
		//流動勤務時間帯設定の残業時間帯を所定終了ずらす時間分ズラす
		
	}
	
	
	/**
	 * 遅刻時間の計算　（遅刻時間帯の作成）
	 * 呼び出す時に勤務No分ループする前提で記載
	 * @return 日別実績の遅刻時間
	 */
	public LateTimeOfDaily calcLateTime(
			boolean clacification,/*遅刻早退の自動計算設定.遅刻　←　どこが持ってるか不明*/
			boolean deducttionClacification,/*控除設定　←　何を参照すればよいのか不明*/
			int workNo) {
		
		//勤務Noに一致する遅刻時間をListで取得する
		List<LateTimeSheet> lateTimeSheetList = getMatchWorkNoLateTimeSheetList(workNo).orElse(null);
		
		LateTimeSheet lateTimeSheet;
		//遅刻時間帯を１つの時間帯にする。
		if(lateTimeSheetList!=null) {
			//ここの処理で保科君が考えてくれた処理を組み込む
			lateTimeSheet = createBondLateTimeSheet(workNo,lateTimeSheetList);
		}

		//遅刻計上時間の計算  ←　1つのメソッドとして出すこと
		int calcTime = lateTimeSheet.getForRecordTimeSheet().get().calcTotalTime();
		TimeWithCalculation lateTime = calcClacificationjudge(clacification, calcTime);
		
		//遅刻控除時間の計算 ←　1つのメソッドとして出すこと
		TimeWithCalculation lateDeductionTime;
		if(deducttionClacification) {//控除する場合
			int calcTime2 = lateTimeSheet.getForDeducationTimeSheet().get().calcTotalTime();
			lateDeductionTime =  calcClacificationjudge(clacification, calcTime2);
		}else {//控除しない場合
			lateDeductionTime = TimeWithCalculation.of(new AttendanceTime(0));
		}
		
		//相殺時間の計算
		
		//計上用時間帯から相殺時間を控除する
		
		LateTimeOfDaily lateTimeOfDaily = new LateTimeOfDaily();
		return lateTimeOfDaily;
	}
	
	/***
	 * 勤務Noに一致する遅刻時間をListで取得する
	 * @return
	 */
	public Optional<List<LateTimeSheet>> getMatchWorkNoLateTimeSheetList(int workNo){
		//<<interface>>遅刻早退管理時間帯が持っているはずの遅刻時間帯<List>
		List<LateTimeSheet> oldlateTimeSheetList;
		//遅刻時間帯を１つの時間帯にする。
		List<LateTimeSheet> lateTimeSheetList = oldlateTimeSheetList.stream().filter(ts -> ts.getWorkNo()==workNo).collect(Collectors.toList());
		if(lateTimeSheetList==null) {
			return Optional.empty();
		}
		return Optional.of(lateTimeSheetList);
	}
	
	/**
	 * 遅刻時間帯を１つの時間帯にする。
	 * @param workNo
	 * @return
	 */
	public LateTimeSheet createBondLateTimeSheet(
			int workNo,
			List<LateTimeSheet> lateTimeSheetList) {
		//計上用時間帯のみのリストを作成
		List<TimeSpanForCalc> forRecordTimeSheetList = 
				lateTimeSheetList.stream().map(ts -> ts.getForRecordTimeSheet().get()).collect(Collectors.toList());
		//1つの時間帯に結合
		TimeSpanForCalc forRecordTimeSheet = bondTimeSpan(forRecordTimeSheetList);
		
		//控除用時間帯のみのリストを作成
		List<TimeSpanForCalc> forDeductionTimeSheetList = 
				lateTimeSheetList.stream().map(ts -> ts.getForDeducationTimeSheet().get()).collect(Collectors.toList());
		//1つの時間帯に結合
		TimeSpanForCalc forDeductionTimeSheet = bondTimeSpan(forRecordTimeSheetList);
		
		return LateTimeSheet.createAsLate(
				forRecordTimeSheet,
				forDeductionTimeSheet,
				workNo,
				Optional.empty(),
				Optional.empty());
	}
	
	/**
	 * 渡した時間帯(List)を1つの時間帯に結合する
	 * @param list
	 * @return
	 */
	public TimeSpanForCalc bondTimeSpan(List<TimeSpanForCalc> list) {
		TimeWithDayAttr start = list.stream().map(ts -> ts.getStart()).min(Comparator.naturalOrder()).get();
		TimeWithDayAttr end =  list.stream().map(ts -> ts.getEnd()).max(Comparator.naturalOrder()).get();
		TimeSpanForCalc bondTimeSpan = new TimeSpanForCalc(start, end);
		return bondTimeSpan;
	}
	
	/**
	 * 指定された計算区分を基に計算付き時間帯を作成する
	 * @return
	 */
	public TimeWithCalculation calcClacificationjudge(boolean clacification , int calcTime) {
		if(clacification) {
			return TimeWithCalculation.of(new AttendanceTime(calcTime));
		}else {
			return TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(0),new AttendanceTime(calcTime));
		}
	}
	
	
	
	
	/**
	 * 就業時間内時間帯に入っている加給時間の計算
	 */
	public List<BonusPayTime> calcBonusPayTimeInWithinWorkTime(BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalcAtrOfDaily calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(WithinWorkTimeFrame timeFrame : withinWorkTimeFrame) {
			bonusPayList.addAll(timeFrame.calcBonusPay(ActualWorkTimeSheetAtr.WithinWorkTime,bonusPayAutoCalcSet, calcAtrOfDaily));
		}
		return bonusPayList;
	}
	/**
	 * 就業時間内時間帯に入っている特定加給時間の計算
	 */
	public List<BonusPayTime> calcSpecifiedBonusPayTimeInWithinWorkTime(BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalcAtrOfDaily calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(WithinWorkTimeFrame timeFrame : withinWorkTimeFrame) {
			bonusPayList.addAll(timeFrame.calcBonusPay(ActualWorkTimeSheetAtr.WithinWorkTime,bonusPayAutoCalcSet, calcAtrOfDaily));
		}
		return bonusPayList;
	}
	/**
	 * 法定内深夜時間の計算
	 * @return　法定内深夜時間
	 */
	public WithinStatutoryMidNightTime calcMidNightTime(AutoCalculationCategoryOutsideHours autoCalcSet) {
		int totalMidNightTime = 0;
		totalMidNightTime = withinWorkTimeFrame.stream()
											   .filter(tg -> tg.getMidNightTimeSheet().isPresent())
											   .map(ts -> ts.getMidNightTimeSheet().get().calcMidNight(autoCalcSet))
											   .collect(Collectors.summingInt(tc -> tc));
		return new WithinStatutoryMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(totalMidNightTime)));
	}
}
