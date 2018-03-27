package nts.uk.ctx.at.record.dom.daily.overtimework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.AutoCalOverTimeAttr;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOverTimeWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculationMinusExist;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationRangeOfOneDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ControlOverFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.FlexWithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LeaveEarlyTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethod;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfEachPremiumHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.calculation.holiday.time.OverTimeFrame;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfIrregularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfRegularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.VacationAddTimeSet;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 日別実績の残業時間
 * @author keisuke_hoshina
 *
 */
@Getter
public class OverTimeOfDaily {
	//残業枠時間帯
	private List<OverTimeFrameTimeSheet> overTimeWorkFrameTimeSheet;
	//残業枠時間
	private List<OverTimeFrameTime> overTimeWorkFrameTime;
	//法定外深夜時間
	private Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTime; 
	//残業拘束時間
	private AttendanceTime overTimeWorkSpentAtWork = new AttendanceTime(0);
	//変形法定内残業
	private AttendanceTime irregularWithinPrescribedOverTimeWork = new AttendanceTime(0);
	//フレックス時間
	@Setter
	private FlexTime flexTime = new FlexTime(TimeWithCalculationMinusExist.sameTime(new AttendanceTimeOfExistMinus(0)),new AttendanceTime(0));
	
	public OverTimeOfDaily(List<OverTimeFrameTimeSheet> frameTimeSheetList, List<OverTimeFrameTime> frameTimeList
							   ,Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTime) {
		this.overTimeWorkFrameTimeSheet = frameTimeSheetList;
		this.overTimeWorkFrameTime = frameTimeList;
		this.excessOverTimeWorkMidNightTime = excessOverTimeWorkMidNightTime;
	}
	
	public OverTimeOfDaily(List<OverTimeFrameTimeSheet> frameTimeSheetList,
							List<OverTimeFrameTime> frameTimeList,
						    Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTime,
						    AttendanceTime irregularTime,
						    FlexTime flexTime,
						    AttendanceTime overTimeWork
						    ) {
		this.overTimeWorkFrameTimeSheet = frameTimeSheetList;
		this.overTimeWorkFrameTime = frameTimeList;
		this.excessOverTimeWorkMidNightTime = excessOverTimeWorkMidNightTime;
		this.irregularWithinPrescribedOverTimeWork = irregularTime;
		this.flexTime = flexTime;
		this.overTimeWorkSpentAtWork = overTimeWork;
	}

	
	/**
	 * 勤務回数を見て開始時刻が正しいか判定する
	 * @param startTime
	 * @param workNo
	 * @param attendanceTime
	 * @return
	 */
	public static boolean startDicision(TimeWithDayAttr startTime, int workNo, TimeWithDayAttr attendanceTime) {
		if(workNo == 0) {
			return (startTime.v() < attendanceTime.v());
		}
		else{
			return (startTime.v() >= attendanceTime.v());
		}
	}
	

	
	/**
	 * 残業枠時間へ残業時間の集計結果を追加する
	 * @param hasAddListClass 残業時間帯の集計を行った後の残業枠時間クラス
	 */
	public void addToList(ControlOverFrameTime hasAddListClass) {
		this.overTimeWorkFrameTime.addAll(hasAddListClass.getOverTimeWorkFrameTime());
	}
	
	/**
	 * 残業時間が含んでいる加給時間の計算
	 * @return 加給時間リスト
	 */
	public List<BonusPayTime> calcBonusPay(BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(OverTimeFrameTimeSheetWork frameTimeSheet : overTimeWorkFrameTimeSheet) {
//			bonusPayList.addAll(frameTimeSheet.calcBonusPay(ActualWorkTimeSheetAtr.OverTimeWork,bonusPayAutoCalcSet, calcAtrOfDaily));
//		}
		return bonusPayList;
	}
	
	/**
	 * 残業時間が含んでいる特定日加給時間の計算
	 * @return 加給時間リスト
	 */
	public List<BonusPayTime> calcSpecifiedBonusPay(BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(OverTimeFrameTimeSheetWork frameTimeSheet : overTimeWorkFrameTimeSheet) {
//			bonusPayList.addAll(frameTimeSheet.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.OverTimeWork,bonusPayAutoCalcSet, calcAtrOfDaily));
//		}
		return bonusPayList;
	}
	/**
	 * 残業時間が含んでいる深夜時間の算出
	 * @return 日別実績の深夜時間帯クラス
	 */
	public ExcessOverTimeWorkMidNightTime calcMidNightTimeIncludeOverTimeWork(AutoCalOvertimeSetting autoCalcSet) {
		int totalTime = 0;
//		for(OverTimeFrameTimeSheetWork frameTime : overTimeWorkFrameTimeSheet) {
//			/*↓分岐の条件が明確になったら記述*/
//			AutoCalcSet setting;
//			if(frameTime.getWithinStatutoryAtr().isStatutory()) {
//				setting = autoCalcSet.getLegalOvertimeHours();
//			}
//			else if(frameTime.isGoEarly()) {
//				setting = autoCalcSet.getEarlyOvertimeHours();
//			}
//			else {
//				setting = autoCalcSet.getNormalOvertimeHours();
//			}
//			totalTime += frameTime.calcMidNight(setting.getCalculationClassification());
//		}
		return new ExcessOverTimeWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(totalTime)));
	}
	
	/**
	 * 全枠の残業時間の合計の算出
	 * @return　残業時間
	 */
	public int calcTotalFrameTime() {
		int totalTime = 0;
		for(OverTimeFrameTime overTimeWorkFrameTime :overTimeWorkFrameTime) {
			totalTime += overTimeWorkFrameTime.getOverTimeWork().getTime().valueAsMinutes();
		}
		return totalTime;
	}
	
	/**
	 * 全枠の振替残業時間の合計の算出
	 * @return　振替残業時間
	 */
	public int calcTransTotalFrameTime() {
		int transTotalTime = 0;
		for(OverTimeFrameTime overTimeWorkFrameTime :overTimeWorkFrameTime) {
			transTotalTime += overTimeWorkFrameTime.getTransferTime().getTime().valueAsMinutes();
		}
		return transTotalTime;
	}
	
	/**
	 * 早出・普通の設定(優先順位)を見て並び替える
	 * @param overTimeWorkFrameTimeSheetList
	 * @param prioritySet
	 * @return
	 */
	public static List<OverTimeFrameTimeSheet> sortedByPriority(List<OverTimeFrameTimeSheet> overTimeWorkFrameTimeSheetList,StatutoryPrioritySet prioritySet){
		List<OverTimeFrameTimeSheet> copyList = new ArrayList<>();
		if(prioritySet.isPriorityNormal()) {
			/*普通を優先*/
			//copyList.addAll(overTimeWorkFrameTimeSheetList.stream().filter(tc -> !tc.isGoEarly()).collect(Collectors.toList()));
			//copyList.addAll(overTimeWorkFrameTimeSheetList.stream().filter(tc -> tc.isGoEarly()).collect(Collectors.toList()));
		}else {
			/*早出を優先*/
			//copyList.addAll(overTimeWorkFrameTimeSheetList.stream().filter(tc -> tc.isGoEarly()).collect(Collectors.toList()));
			//copyList.addAll(overTimeWorkFrameTimeSheetList.stream().filter(tc -> !tc.isGoEarly()).collect(Collectors.toList()));
		}
		return copyList;
	}
	
	
	/**
	 * 指定時間の振替処理から呼ばれた振替処理
	 * @param hurikaeAbleTime 振替可能時間
	 * @param prioritySet 振替可能時間
	 */
	public void hurikakesyori(AttendanceTime hurikaeAbleTime,StatutoryPrioritySet prioritySet) {
//		List<OverTimeFrameTimeSheetWork> hurikae = sortedByPriority(overTimeWorkFrameTimeSheet,prioritySet);
//		AttendanceTime ableTransTime = new AttendanceTime(0);
//		for(OverTimeFrameTimeSheetWork overTimeFrameTimeSheet : hurikae) {
////			if(/*Not 振替大将*/) {
////				continue;
////			}
//			//残業時間 >= 振替可能時間
//			if(overTimeFrameTimeSheet.getOverWorkFrameTime().getOverTimeWork().getCalcTime().greaterThanOrEqualTo(hurikaeAbleTime.valueAsMinutes())) {
//				ableTransTime = hurikaeAbleTime;
//			}
//			//残業時間 < 振替可能時間
//			else {
//				ableTransTime = overTimeFrameTimeSheet.getOverWorkFrameTime().getOverTimeWork().getCalcTime(); 
//			}
//			overTimeWorkFrameTime.stream().sorted((first,second) -> first.getOverWorkFrameNo().compareTo(second.getOverWorkFrameNo()));
//			//残業枠時間帯に対する加算
//			overTimeFrameTimeSheet.getOverWorkFrameTime().getOverTimeWork().addMinutes(ableTransTime, ableTransTime);
//			overTimeFrameTimeSheet.getOverWorkFrameTime().getTransferTime().addMinutes(ableTransTime, ableTransTime);
//			//日別実績の～～が持ってる枠に対する加算
//			overTimeWorkFrameTime.get(overTimeFrameTimeSheet.getFrameNo().v()).getOverTimeWork().addMinutes(ableTransTime, ableTransTime);
//			overTimeWorkFrameTime.get(overTimeFrameTimeSheet.getFrameNo().v()).getTransferTime().addMinutes(ableTransTime, ableTransTime);
//			
//			hurikaeAbleTime.minusMinutes(ableTransTime.valueAsMinutes());
//		}
	}
	
	
	public OverTimeOfDaily createFromJavaType(List<OverTimeFrameTime> frameTimeList,
											  ExcessOverTimeWorkMidNightTime midNightTime,
											  AttendanceTime irregularTime,
											  FlexTime flexTime,
											  AttendanceTime overTimeWork) {
		return new OverTimeOfDaily(Collections.emptyList(),
								   frameTimeList,
								   Finally.of(midNightTime),
								   irregularTime,
								   flexTime,
								   overTimeWork
								   );
	}
	
	/**
	 * メンバー変数の時間計算を指示するクラス
	 * @return 計算結果
	 */
	public static OverTimeOfDaily calculationTime(OverTimeSheet overTimeSheet,AutoCalOvertimeSetting overTimeAutoCalcSet,WithinWorkTimeSheet withinWorkTimeSheetList,CalcMethodOfNoWorkingDay calcMethod,
												  HolidayCalcMethodSet holidayCalcMethodSet,AutoCalOverTimeAttr autoCalcAtr,WorkType workType,
												  Optional<SettingOfFlexWork> flexCalcMethod,PredetermineTimeSetForCalc predetermineTimeSet,
												  Optional<DeductionTimeSheet> tempDedTimeSheet,VacationClass vacationClass,TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
												  StatutoryDivision statutoryDivision,Optional<WorkTimeCode> siftCode,
												  Optional<PersonalLaborCondition> personalCondition, LateTimeSheet lateTimeSheet,LeaveEarlyTimeSheet leaveEarlyTimeSheet,LateTimeOfDaily lateTimeOfDaily,
												  LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily,boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
												  boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
												  WorkingSystem workingSystem,AddSettingOfIrregularWork addSettingOfIrregularWork,AddSettingOfFlexWork addSettingOfFlexWork,AddSettingOfRegularWork addSettingOfRegularWork,
												  VacationAddTimeSet vacationAddTimeSet,WorkTimeDailyAtr workTimeDailyAtr,
												  Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
												  Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		//枠時間帯入れる
		val overTimeFrameTimeSheet = overTimeSheet.changeOverTimeFrameTimeSheet();
		//枠時間計算
		val overTimeFrame = overTimeSheet.collectOverTimeWorkTime(overTimeAutoCalcSet,workType,eachWorkTimeSet,eachCompanyTimeSet);
		//残業内の深夜時間計算
		val excessOverTimeWorkMidNightTime = Finally.of(calcExcessMidNightTime(overTimeSheet,overTimeAutoCalcSet));
		//変形法定内残業時間計算
		val irregularTime = overTimeSheet.calcIrregularTime();
		//フレックス時間
		FlexTime flexTime = new FlexTime(TimeWithCalculationMinusExist.sameTime(new AttendanceTimeOfExistMinus(0)),new AttendanceTime(0));
		//フレ時間の計算に挑戦
		if(workTimeDailyAtr.isFlex() && withinWorkTimeSheetList != null) {
			val changeVariant = ((FlexWithinWorkTimeSheet)withinWorkTimeSheetList);
			flexTime =  changeVariant.createWithinWorkTimeSheetAsFlex(calcMethod,holidayCalcMethodSet,autoCalcAtr,workType,
					//flexCalcMethod.get(),
					new SettingOfFlexWork(new FlexCalcMethodOfHalfWork(new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.Half, FlexCalcMethod.Half),
																	   new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.Half, FlexCalcMethod.Half))),
					predetermineTimeSet,
					tempDedTimeSheet,vacationClass,timevacationUseTimeOfDaily,
					statutoryDivision,siftCode,
					personalCondition,lateTimeSheet,leaveEarlyTimeSheet,lateTimeOfDaily,
					leaveEarlyTimeOfDaily,late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
					leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
					workingSystem,addSettingOfIrregularWork,addSettingOfFlexWork,addSettingOfRegularWork,
					vacationAddTimeSet,TimeLimitUpperLimitSetting.NOUPPERLIMIT);
		}

		val overTimeWork = new AttendanceTime(0);
		return new OverTimeOfDaily(overTimeFrameTimeSheet,
								   overTimeFrame,
								   excessOverTimeWorkMidNightTime,
								   irregularTime,
								   flexTime,
								   overTimeWork);
		
	}
	/**
	 * 所定外深夜時間の計算
	 * @param oneDay
	 * @return　所定外深夜時間
	 */
	private static ExcessOverTimeWorkMidNightTime calcExcessMidNightTime(OverTimeSheet overTimeSheet,AutoCalOvertimeSetting autoCalcSet) {
		
		AttendanceTime calcTime = overTimeSheet.calcMidNightTime(autoCalcSet);
		return new ExcessOverTimeWorkMidNightTime(TimeWithCalculation.sameTime(calcTime));
	}
	
	/**
	 * 残業時間実績超過
	 */
	public void overRecord() {
		
	}
}
