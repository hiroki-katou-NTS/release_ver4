package nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ActualWorkTimeSheetAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CommonFixedWorkTimezoneSet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ConditionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionClassification;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionOffSetTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.FlexWithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateLeaveEarlyManagementTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LeaveEarlyTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PremiumAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.SpecBonusPayTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetOfDeductionItem;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationAddTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfIrregularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfRegularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.CalculationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayAdditionAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.VacationAddTimeSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.RaisingSalaryCalcAtr;
import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixHalfDayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;import nts.uk.ctx.at.shared.dom.worktime.flexset.TimeSheet;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 就業時間内時間帯
 * @author keisuke_hoshina
 *
 */
@AllArgsConstructor
@Getter
public class WithinWorkTimeSheet implements LateLeaveEarlyManagementTimeSheet{

	//必要になったら追加
	//private WorkingHours
	//private RaisingSalaryTime
	//private Optional<> flexTimeSheet;
	private final List<WithinWorkTimeFrame> withinWorkTimeFrame;
//	private final List<LeaveEarlyDecisionClock> leaveEarlyDecisionClock;
//	private final List<LateDecisionClock> lateDecisionClock;
//	private List<LateTimeOfDaily> lateTimeOfDaily;
//	private final FlexWithinWorkTimeSheet flexTimeSheet;
	
	
	
	/**
	 * 就業時間内時間帯
	 * 
	 * @param workType 勤務種類クラス
	 * @param predetermineTimeSet 所定時間設定クラス
	 * @param fixedWorkSetting 固定勤務設定クラス
	 * @param workTimeCommonSet 就業時間帯の共通設定
	 * @param deductionTimeSheet 控除時間帯
	 * @param bonusPaySetting 加給設定
	 * @return 就業時間内時間帯
	 */
	public static WithinWorkTimeSheet createAsFixed(TimeLeavingWork timeLeavingWork,
													WorkType workType,
													PredetermineTimeSetForCalc predetermineTimeSetForCalc,
													CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,
													WorkTimezoneCommonSet workTimeCommonSet,
													DeductionTimeSheet deductionTimeSheet,
													BonusPaySetting bonusPaySetting,
													MidNightTimeSheet midNightTimeSheet,
													int workNo) {
		
		List<WithinWorkTimeFrame> timeFrames = new ArrayList<>();
		//List<LateDecisionClock> lateDesClock = new ArrayList<>();
		if(workType.isWeekDayAttendance()) {
			//lateDesClock = 
			timeFrames = isWeekDayProcess(timeLeavingWork,workType,predetermineTimeSetForCalc,lstHalfDayWorkTimezone,workTimeCommonSet
									 							,deductionTimeSheet,bonusPaySetting,midNightTimeSheet,workNo);
		}
		return new WithinWorkTimeSheet(timeFrames);
	}

	/**
	 * 就業時間内時間帯の作成
	 * @param workType　勤務種類クラス
	 * @param predetermineTimeSet 所定時間設定クラス
	 * @param fixedWorkSetting  固定勤務設定クラス
	 * @return 就業時間内時間帯クラス
	 */
	private static List<WithinWorkTimeFrame> isWeekDayProcess(
			TimeLeavingWork timeLeavingWork,
			WorkType workType,
			PredetermineTimeSetForCalc predetermineTimeForSet,
			CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,
			WorkTimezoneCommonSet workTimeCommonSet,
			DeductionTimeSheet deductionTimeSheet,
			BonusPaySetting bonusPaySetting,
			MidNightTimeSheet midNightTimeSheet,
			int workNo
			) {

		//遅刻猶予時間の取得
		//val lateGraceTime = fixedWorkSetting.getworkTimeCommonSet.getLateSetting().getGraceTimeSetting();//引数でworkTimeCommonSet毎渡すように修正予定
		//早退猶予時間の取得
		//val leaveEarlyGraceTime = fixedWorkSetting.getworkTimeCommonSet.getLeaveEarlySetting().getGraceTimeSetting();
						
		val timeFrames = new ArrayList<WithinWorkTimeFrame>();
		//所定時間と就業時間帯の重複部分取得
		List<EmTimeZoneSet> workingHourSet = createWorkingHourSet(workType, predetermineTimeForSet , lstHalfDayWorkTimezone, workNo);
		//出退勤時刻と↑の重複時間帯と重複部分取得
		workingHourSet = duplicatedByStamp(workingHourSet,timeLeavingWork);
		
		for(EmTimeZoneSet duplicateTimeSheet :workingHourSet) {
			//就業時間内時間枠の作成
			timeFrames.add(WithinWorkTimeFrame.createWithinWorkTimeFrame(duplicateTimeSheet, deductionTimeSheet, bonusPaySetting, midNightTimeSheet));
		}
		/*所定内割増時間の時間帯作成*/
		
		return timeFrames;
	}
	


	/***
	 * 出勤、退勤時刻との重複部分を調べる
	 * @param workingHourSet 就業時間枠の時間帯
	 * @param timeLeavingWork　出退勤
	 * @return　時間枠の時間帯と出退勤の重複時間
	 */
	private static List<EmTimeZoneSet> duplicatedByStamp(List<EmTimeZoneSet> workingHourSet,
			TimeLeavingWork timeLeavingWork) {
		List<EmTimeZoneSet> returnList = new ArrayList<>();
		Optional<TimeSpanForCalc> duplicatedRange = Optional.empty(); 
		for(EmTimeZoneSet timeZone:workingHourSet) {
			duplicatedRange = timeZone.getTimezone().getDuplicatedWith(timeLeavingWork.getTimespan());
			if(duplicatedRange.isPresent())
				returnList.add(new EmTimeZoneSet(timeZone.getEmploymentTimeFrameNo(),
												 new TimeZoneRounding(duplicatedRange.get().getStart(),
														 			  duplicatedRange.get().getEnd(),
														 			  timeZone.getTimezone().getRounding())));
			
		}
		return returnList;
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
	public static List<EmTimeZoneSet> createWorkingHourSet(WorkType workType, PredetermineTimeSetForCalc predetermineTimeSet,
			CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,int workNo) {
		
		val attendanceHolidayAttr = workType.getAttendanceHolidayAttr();
		val emTimeZoneSet = getWorkingHourSetByAmPmClass(lstHalfDayWorkTimezone, attendanceHolidayAttr);
		val predTimeSheet = predetermineTimeSet.getTimeSheets().stream().filter(tc -> tc.getWorkNo() == workNo).findFirst(); 
		return extractBetween(
				emTimeZoneSet,
				predTimeSheet.isPresent()?predTimeSheet.get().getStart():new TimeWithDayAttr(0),
				predTimeSheet.isPresent()?predTimeSheet.get().getEnd():new TimeWithDayAttr(0));
	}

	/**
	 * 所定時間帯と重複している就業時間帯設定時間を取り出す。
	 * @param start 開始時刻
	 * @param end　終了時刻
	 */
	private static List<EmTimeZoneSet> extractBetween(List<EmTimeZoneSet> timeZoneList,TimeWithDayAttr start,TimeWithDayAttr end){
		List<EmTimeZoneSet> returnList = new ArrayList<>();
		timeZoneList.forEach(source ->{ source.getTimezone().timeSpan().getDuplicatedWith(new TimeSpanForCalc(start, end)).ifPresent(duplicated -> {
											returnList.add(source.newSpanWith(duplicated.getStart(), duplicated.getEnd()));
										});
									});
		return returnList;
	}
	/**
	 * 平日出勤の出勤時間帯を取得
	 * @param fixedWorkSetting 固定勤務設定クラス
	 * @param attendanceHolidayAttr 出勤休日区分
	 * @return 出勤時間帯
	 */
	private static List<EmTimeZoneSet> getWorkingHourSetByAmPmClass(
			CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,
			AttendanceHolidayAttr attendanceHolidayAttr) {
		
		switch (attendanceHolidayAttr) {
		case FULL_TIME:
		case HOLIDAY:
			return lstHalfDayWorkTimezone.getFixedWorkTimezoneMap().get(AmPmAtr.ONE_DAY).getLstWorkingTimezone();
		case MORNING:
			return lstHalfDayWorkTimezone.getFixedWorkTimezoneMap().get(AmPmAtr.AM).getLstWorkingTimezone();
		case AFTERNOON:
			return lstHalfDayWorkTimezone.getFixedWorkTimezoneMap().get(AmPmAtr.PM).getLstWorkingTimezone();
		default:
			throw new RuntimeException("unknown attendanceHolidayAttr" + attendanceHolidayAttr);
		}
	}
	
//	/**
//	 * 引数のNoと一致する遅刻判断時刻を取得する
//	 * @param workNo
//	 * @return　遅刻判断時刻
//	 */
//	public LateDecisionClock getlateDecisionClock(int workNo) {
//		List<LateDecisionClock> clockList = this.lateDecisionClock.stream().filter(tc -> tc.getWorkNo()==workNo).collect(Collectors.toList());
//		if(clockList.size()>1) {
//			throw new RuntimeException("Exist duplicate workNo : " + workNo);
//		}
//		return clockList.get(0);
//	}
//	
//	/**
//	 * 引数のNoと一致する早退判断時刻を取得する
//	 * @param workNo
//	 * @return　早退判断時刻
//	 */
//	public LeaveEarlyDecisionClock getleaveEarlyDecisionClock(int workNo) {
//		List<LeaveEarlyDecisionClock> clockList = this.leaveEarlyDecisionClock.stream().filter(tc -> tc.getWorkNo()==workNo).collect(Collectors.toList());
//		if(clockList.size()>1) {
//			throw new RuntimeException("Exist duplicate workNo : " + workNo);
//		}
//		return clockList.get(0);
//	}
	
	/**
	 * コアタイムのセット
	 * @param coreTimeSetting コアタイム時間設定
	 */
	public WithinWorkTimeSheet createWithinFlexTimeSheet(CoreTimeSetting coreTimeSetting) {
		List<TimeSpanForCalc> duplicateCoreTimeList = new ArrayList<>();
		for(WithinWorkTimeFrame workTimeFrame : this.withinWorkTimeFrame) {
			Optional<TimeSpanForCalc> duplicateSpan = workTimeFrame.getCalcrange().getDuplicatedWith(new TimeSpanForCalc(coreTimeSetting.getCoreTimeSheet().getStartTime(),
																									 					 coreTimeSetting.getCoreTimeSheet().getEndTime())); 
			if(duplicateSpan.isPresent()) {
				duplicateCoreTimeList.add(duplicateSpan.get());
			}
		}
		TimeWithDayAttr startTime = new TimeWithDayAttr(0);
		TimeWithDayAttr endTime = new TimeWithDayAttr(0);
		if(!duplicateCoreTimeList.isEmpty()) {
			startTime = duplicateCoreTimeList.stream().sorted((first,second)-> first.getStart().compareTo(second.getStart())).collect(Collectors.toList()).get(0).getStart();
			endTime = duplicateCoreTimeList.stream().sorted((first,second)-> first.getStart().compareTo(second.getStart())).collect(Collectors.toList()).get(duplicateCoreTimeList.size() - 1).getEnd();
			/*フレックス時間帯に入れる*/
			return new FlexWithinWorkTimeSheet(this.withinWorkTimeFrame,new TimeSpanForCalc(startTime, endTime));
		}
		else {
			return this;
		}
	}
	
	
	/**
	 * 就業時間(法定内用)の計算
	 * @param calcActualTime 実働のみで計算する
	 * @param dedTimeSheet　控除時間帯
	 * @return 就業時間の計算結果
	 */
	public AttendanceTime calcWorkTimeForStatutory(PremiumAtr premiumAtr,CalculationByActualTimeAtr calcActualTime,
			   TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
			   VacationClass vacationClass,
			   StatutoryDivision statutoryDivision,
			   WorkType workType,
			   PredetermineTimeSetForCalc predetermineTimeSet,
			   Optional<WorkTimeCode> siftCode,
			   Optional<PersonalLaborCondition> personalCondition, 
			   LateTimeSheet lateTimeSheet,
			   LeaveEarlyTimeSheet leaveEarlyTimeSheet,
			   LateTimeOfDaily lateTimeOfDaily,
			   LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily,
			   boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
			   boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
			   WorkingSystem workingSystem,
			   AddSettingOfIrregularWork addSettingOfIrregularWork,
			   AddSettingOfFlexWork addSettingOfFlexWork,
			   AddSettingOfRegularWork addSettingOfRegularWork,
			   VacationAddTimeSet vacationAddTimeSet) {
		return calcWorkTime(
					premiumAtr,
					calcActualTime,
				    vacationClass,
				    timevacationUseTimeOfDaily,
				    statutoryDivision,
				    workType,
				    predetermineTimeSet,
				   siftCode,
				    personalCondition, 
				    lateTimeSheet,
				    leaveEarlyTimeSheet,
				    lateTimeOfDaily,
				    leaveEarlyTimeOfDaily,
				    late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
				    leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
				    workingSystem,
				    addSettingOfIrregularWork,
				    addSettingOfFlexWork,
				    addSettingOfRegularWork,
				    vacationAddTimeSet);
	}
	
	
	/**
	 * 就業時間の計算(控除時間差し引いた後)
	 * @return 就業時間
	 */
	public AttendanceTime calcWorkTime(PremiumAtr premiumAtr, CalculationByActualTimeAtr calcActualTime,VacationClass vacationClass,TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
									   StatutoryDivision statutoryDivision,
									   WorkType workType,
									   PredetermineTimeSetForCalc predetermineTimeSet,
									   Optional<WorkTimeCode> siftCode,
									   Optional<PersonalLaborCondition> personalCondition, 
									   LateTimeSheet lateTimeSheet,
									   LeaveEarlyTimeSheet leaveEarlyTimeSheet,
									   LateTimeOfDaily lateTimeOfDaily,
									   LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily,
									   boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
									   boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
									   WorkingSystem workingSystem,
									   AddSettingOfIrregularWork addSettingOfIrregularWork,
									   AddSettingOfFlexWork addSettingOfFlexWork,
									   AddSettingOfRegularWork addSettingOfRegularWork,
									   VacationAddTimeSet vacationAddTimeSet) {
		
		HolidayAdditionAtr holidayAddition = HolidayAdditionAtr.HolidayAddition.convertFromCalcByActualTimeToHolidayAdditionAtr(calcActualTime);
		
		AttendanceTime workTime = calcWorkTimeBeforeDeductPremium(holidayAddition ,timevacationUseTimeOfDaily,
																  workingSystem,addSettingOfRegularWork,addSettingOfIrregularWork, 
																  addSettingOfFlexWork,lateTimeSheet,leaveEarlyTimeSheet,
																  lateTimeOfDaily,leaveEarlyTimeOfDaily,vacationAddTimeSet,
																  late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
																  leaveEarly  //日別実績の計算区分.遅刻早退の自動計算設定.早退
																  );
		
		if(holidayAddition.isHolidayAddition()) {
			//休暇加算時間を計算
			VacationAddTime vacationAddTime = vacationClass.calcVacationAddTime(statutoryDivision,
																				workingSystem, 
																				addSettingOfRegularWork, 
																				vacationAddTimeSet, 
																				workType, 
																				predetermineTimeSet, 
																				siftCode, 
																				personalCondition, 
																				addSettingOfIrregularWork, 
																				addSettingOfFlexWork);
			//休暇加算時間を加算
			workTime = workTime.addMinutes(vacationAddTime.calcTotaladdVacationAddTime());
		}
		return workTime;
	}
	

	
	/**
	 * 就業時間内時間枠の全枠分の就業時間を算出する
	 * (所定内割増時間を差し引く前)
	 * @return 就業時間
	 */
	public AttendanceTime calcWorkTimeBeforeDeductPremium(HolidayAdditionAtr holidayAdditionAtr,
														  TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
														  WorkingSystem workingSystem,
														  AddSettingOfRegularWork addSettingOfRegularWork,
														  AddSettingOfIrregularWork addSettingOfIrregularWork, 
														  AddSettingOfFlexWork addSettingOfFlexWork,
														  LateTimeSheet lateTimeSheet,
														  LeaveEarlyTimeSheet leaveEarlyTimeSheet,
														  LateTimeOfDaily lateTimeOfDaily,
														  LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily,
														  VacationAddTimeSet vacationAddTimeSet,
														  boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
														  boolean leaveEarly  //日別実績の計算区分.遅刻早退の自動計算設定.早退
														  ) {
		AttendanceTime workTime = new AttendanceTime(0);
		for(WithinWorkTimeFrame copyItem: withinWorkTimeFrame) {
			//workTime.addMinutes(copyItem.calcActualWorkTimeAndWorkTime(holidayAdditionAtr,dedTimeSheet).v());
			workTime = new AttendanceTime(workTime.v()+copyItem.calcActualWorkTimeAndWorkTime(holidayAdditionAtr,
																							  timevacationUseTimeOfDaily,
																							  workingSystem,
																							  addSettingOfRegularWork,
																							  addSettingOfIrregularWork, 
																							  addSettingOfFlexWork,
																							  lateTimeSheet,
																							  leaveEarlyTimeSheet,
																							  lateTimeOfDaily,
																							  leaveEarlyTimeOfDaily,
																							  vacationAddTimeSet,
																							  late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
																							  leaveEarly  //日別実績の計算区分.遅刻早退の自動計算設定.早退
																							  ).v());
		}
		return workTime;
	}
	
	
//	/**
//	 * 日別計算の遅刻早退時間の計算
//	 * @return
//	 */
//	public int calcLateLeaveEarlyinWithinWorkTime() {
//		for(WithinWorkTimeFrame workTimeFrame : withinWorkTimeFrame) {
//			workTimeFrame.correctTimeSheet(dailyWork, timeSheet, predetermineTimeSetForCalc);
//		}
//	}
	
//	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
//	
//	//就業時間内時間帯クラスを作成　　（流動勤務）
//	public WithinWorkTimeSheet createAsFluidWork(
//			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
//			WorkType workType,
//			WorkInfoOfDailyPerformance workInformationOfDaily,
//			FluidWorkSetting fluidWorkSetting,
//			DeductionTimeSheet deductionTimeSheet) {
//		//開始時刻を取得
//		TimeWithDayAttr startClock = getStartClock();
//		//所定時間帯、残業開始を補正
//		cllectPredetermineTimeAndOverWorkTimeStart();
//		//残業開始となる経過時間を取得
//		AttendanceTime elapsedTime = fluidWorkSetting.getWeekdayWorkTime().getWorkTimeSheet().getMatchWorkNoOverTimeWorkSheet(1).getFluidWorkTimeSetting().getElapsedTime();
//		//経過時間から終了時刻を計算
//		TimeWithDayAttr endClock = startClock.backByMinutes(elapsedTime.valueAsMinutes());
//		//就業時間帯の作成（一時的に作成）
//		TimeSpanForCalc workTimeSheet = new TimeSpanForCalc(startClock,endClock);
//		//控除時間帯を取得 (控除時間帯分ループ）
//		for(TimeSheetOfDeductionItem timeSheetOfDeductionItem : deductionTimeSheet.getForDeductionTimeZoneList()) {
//			//就業時間帯に重複する控除時間を計算
//			TimeSpanForCalc duplicateTime = workTimeSheet.getDuplicatedWith(timeSheetOfDeductionItem.getTimeSheet().getSpan()).orElse(null);
//			//就業時間帯と控除時間帯が重複しているかチェック
//			if(duplicateTime!=null) {
//				//控除項目の時間帯に法定内区分をセット
//				timeSheetOfDeductionItem = new TimeSheetOfDeductionItem(
//						timeSheetOfDeductionItem.getTimeSheet().getSpan(),
//						timeSheetOfDeductionItem.getGoOutReason(),
//						timeSheetOfDeductionItem.getBreakAtr(),
//						timeSheetOfDeductionItem.getDeductionAtr(),
//						WithinStatutoryAtr.WithinStatutory);
//				//控除時間分、終了時刻をズラす
//				endClock.backByMinutes(duplicateTime.lengthAsMinutes());
//				//休暇加算するかチェックしてズラす
//				
//			}		
//		}
//		//就業時間内時間帯クラスを作成
//		
//		
//		
//	}
//	
//	/**
//	 * 開始時刻を取得　　（流動勤務（平日・就内））
//	 * @return
//	 */
//	public TimeWithDayAttr getStartClock() {
//		
//	}
//	
//	
//	//所定時間帯、残業開始を補正
//	public void cllectPredetermineTimeAndOverWorkTimeStart(
//			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
//			WorkType workType,
//			WorkInfoOfDailyPerformance workInformationOfDaily) {
//		//所定時間帯を取得
//		predetermineTimeSetForCalc.correctPredetermineTimeSheet(workType.getDailyWork());
//		//予定所定時間が変更された場合に所定時間を変更するかチェック
//		//勤務予定と勤務実績の勤怠情報を比較
//		//勤務種類が休日出勤でないかチェック
//		if(
//				!workInformationOfDaily.isMatchWorkInfomation()||
//				workType.getDailyWork().isHolidayWork()
//				) {
//			return;
//		}
//		//就業時間帯の所定時間と予定時間を比較
//			
//		//計算用所定時間設定を所定終了ずらす時間分ズラす
//		
//		//流動勤務時間帯設定の残業時間帯を所定終了ずらす時間分ズラす
//		
//	}
//	
//	
//	/**
//	 * 遅刻時間の計算　（遅刻時間帯の作成）
//	 * 呼び出す時に勤務No分ループする前提で記載
//	 * @return 日別実績の遅刻時間
//	 */
//	public LateTimeOfDaily calcLateTime(
//			boolean clacification,/*遅刻早退の自動計算設定.遅刻　←　どこが持ってるか不明*/
//			boolean deducttionClacification,/*控除設定　←　何を参照すればよいのか不明*/
//			int workNo) {
//		
//		//勤務Noに一致する遅刻時間をListで取得する
//		List<LateTimeSheet> lateTimeSheetList = getMatchWorkNoLateTimeSheetList(workNo).orElse(null);
//		
//		LateTimeSheet lateTimeSheet;
//		//遅刻時間帯を１つの時間帯にする。
//		if(lateTimeSheetList!=null) {
//			//ここの処理で保科君が考えてくれた処理を組み込む
//			lateTimeSheet = createBondLateTimeSheet(workNo,lateTimeSheetList);
//		}
//
//		//遅刻計上時間の計算  ←　1つのメソッドとして出すこと
//		int calcTime = lateTimeSheet.getForRecordTimeSheet().get().calcTotalTime();
//		TimeWithCalculation lateTime = calcClacificationjudge(clacification, calcTime);
//		
//		//遅刻控除時間の計算 ←　1つのメソッドとして出すこと
//		TimeWithCalculation lateDeductionTime;
//		if(deducttionClacification) {//控除する場合
//			int calcTime2 = lateTimeSheet.getForDeducationTimeSheet().get().calcTotalTime();
//			lateDeductionTime =  calcClacificationjudge(clacification, calcTime2);
//		}else {//控除しない場合
//			lateDeductionTime = TimeWithCalculation.sameTime(new AttendanceTime(0));
//		}
//		
//		//相殺時間の計算
//		
//		//計上用時間帯から相殺時間を控除する
//		
//		LateTimeOfDaily lateTimeOfDaily = new LateTimeOfDaily();
//		return lateTimeOfDaily;
//	}
//	
//	/***
//	 * 勤務Noに一致する遅刻時間をListで取得する
//	 * @return
//	 */
//	public Optional<List<LateTimeSheet>> getMatchWorkNoLateTimeSheetList(int workNo){
//		//<<interface>>遅刻早退管理時間帯が持っているはずの遅刻時間帯<List>
//		List<LateTimeSheet> oldlateTimeSheetList;
//		//遅刻時間帯を１つの時間帯にする。
//		List<LateTimeSheet> lateTimeSheetList = oldlateTimeSheetList.stream().filter(ts -> ts.getWorkNo()==workNo).collect(Collectors.toList());
//		if(lateTimeSheetList==null) {
//			return Optional.empty();
//		}
//		return Optional.of(lateTimeSheetList);
//	}
//	
//	/**
//	 * 遅刻時間帯を１つの時間帯にする。
//	 * @param workNo
//	 * @return
//	 */
//	public LateTimeSheet createBondLateTimeSheet(
//			int workNo,
//			List<LateTimeSheet> lateTimeSheetList) {
//		//計上用時間帯のみのリストを作成
//		List<TimeSpanForCalc> forRecordTimeSheetList = 
//				lateTimeSheetList.stream().map(ts -> ts.getForRecordTimeSheet().get()).collect(Collectors.toList());
//		//1つの時間帯に結合
//		TimeSpanForCalc forRecordTimeSheet = bondTimeSpan(forRecordTimeSheetList);
//		
//		//控除用時間帯のみのリストを作成
//		List<TimeSpanForCalc> forDeductionTimeSheetList = 
//				lateTimeSheetList.stream().map(ts -> ts.getForDeducationTimeSheet().get()).collect(Collectors.toList());
//		//1つの時間帯に結合
//		TimeSpanForCalc forDeductionTimeSheet = bondTimeSpan(forRecordTimeSheetList);
//		
//		return LateTimeSheet.createAsLate(
//				forRecordTimeSheet,
//				forDeductionTimeSheet,
//				workNo,
//				Optional.empty(),
//				Optional.empty());
//	}
//	
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
			return TimeWithCalculation.sameTime(new AttendanceTime(calcTime));
		}else {
			return TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(0),new AttendanceTime(calcTime));
		}
	}
	
//	//遅刻早退時間帯（List）を1つの遅刻早退時間帯に結合する
//	public LateLeaveEarlyTimeSheet connect(List<LateLeaveEarlyTimeSheet> timeSheetList){
//		 return new LeaveEarlyTimeSheet(createMinStartMaxEndSpanForTimeSheet(timeSheetList),
//		 								createMinStartMaxEndSpanForCalcRange(timeSheetList),
//		 								extract(List<LeaveEarlyTimeSheet> timeSheetList),
//		 								Optional.empty(),
//		 								Optional.empty(),
//		 								Optional.empty());
//	}
//	//遅刻早退時間帯（List）の時間帯（丸め付）を1つの時間帯（丸め付）にする
//	public TimeSpanWithRounding createMinStartMaxEndSpanForTimeSheet(List<LateLeaveEarlyTimeSheet> timeSheetList){
//		TimeWithDayAttr start = timeSheetList.stream().map(ts -> ts.getTimeSheet().getStart()).min(Comparator.naturalOrder()).get();
//		TimeWithDayAttr end =  timeSheetList.stream().map(ts -> ts.getTimeSheet().getEnd()).max(Comparator.naturalOrder()).get();
//		return new TimeSpanWithRounding(start, end, rounding);//丸めはどうする？
//	}
//	//遅刻早退時間帯（List）の計算範囲を1つの計算範囲にする
//	public TimeSpanForCalc createMinStartMaxEndSpanForCalcRange(List<LateLeaveEarlyTimeSheet> timeSheetList){
//		TimeWithDayAttr start = timeSheetList.stream().map(ts -> ts.getCalcrange().getStart()).min(Comparator.naturalOrder()).get();
//		TimeWithDayAttr end =  timeSheetList.stream().map(ts -> ts.getCalcrange().getEnd()).max(Comparator.naturalOrder()).get();
//		return new TimeSpanForCalc(start,end);
//	}
//	//遅刻早退時間帯（List）の控除項目の時間帯（List）を1つの控除項目の時間帯（List）にする
//	public List<TimeSheetOfDeductionItem> extract(List<LateLeaveEarlyTimeSheet> timeSheetList){
//		return timeSheetList.stream().map(tc -> tc.getDeductionTimeSheets()).collect(Collectors.toList());
//	}
	
	
//	/**
//	 * 遅刻時間の休暇時間相殺
//	 * @return
//	 */
//	public DeductionOffSetTime calcDeductionOffSetTime(
//			TimevacationUseTimeOfDaily TimeVacationAdditionRemainingTime,//時間休暇使用残時間を取得する
//			LateTimeSheet lateTimeSheet,
//			DeductionAtr deductionAtr) {
//		TimeSpanForCalc calcRange;
//		//計算範囲の取得
//		if(deductionAtr.isDeduction()) {//パラメータが控除の場合
//			calcRange = lateTimeSheet.getForDeducationTimeSheet().get().getTimeSheet().getSpan();
//		}else {//パラメータが計上の場合
//			calcRange = lateTimeSheet.getForRecordTimeSheet().get().getTimeSheet().getSpan();
//		}
//		//遅刻時間を求める
//		int lateRemainingTime = calcRange.lengthAsMinutes();
//		//時間休暇相殺を利用して相殺した各時間を求める
//		DeductionOffSetTime deductionOffSetTime = createDeductionOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime);
//		
//		return 	deductionOffSetTime;
//	}
	
	
	/**
	 * 時間休暇相殺を利用して相殺した各時間を求める  （一時的に作成）
	 * @return
	 */
	public DeductionOffSetTime createDeductionOffSetTime(int lateRemainingTime,TimevacationUseTimeOfDaily TimeVacationAdditionRemainingTime) {
		
		AttendanceTime timeAnnualLeaveUseTime = calcOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime.getTimeAnnualLeaveUseTime());
		lateRemainingTime -= timeAnnualLeaveUseTime.valueAsMinutes();

		AttendanceTime timeCompensatoryLeaveUseTime = new AttendanceTime(0);
		AttendanceTime sixtyHourExcessHolidayUseTime = new AttendanceTime(0);
		AttendanceTime timeSpecialHolidayUseTime = new AttendanceTime(0);
		
		if(lateRemainingTime > 0) {
			timeCompensatoryLeaveUseTime = calcOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime.getTimeCompensatoryLeaveUseTime());
			lateRemainingTime -= timeCompensatoryLeaveUseTime.valueAsMinutes();
		}
		
		if(lateRemainingTime > 0) {
			sixtyHourExcessHolidayUseTime = calcOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime.getSixtyHourExcessHolidayUseTime());
			lateRemainingTime -= sixtyHourExcessHolidayUseTime.valueAsMinutes();
		}
		
		if(lateRemainingTime > 0) {
			timeSpecialHolidayUseTime = calcOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime.getTimeSpecialHolidayUseTime());
			lateRemainingTime -= timeSpecialHolidayUseTime.valueAsMinutes();
		}
				
		return new DeductionOffSetTime(
				timeAnnualLeaveUseTime,
				timeCompensatoryLeaveUseTime,
				sixtyHourExcessHolidayUseTime,
				timeSpecialHolidayUseTime);
	}

	
	/**
	 * 
	 * @param lateRemainingTime 遅刻残数
	 * @param timeVacationUseTime　時間休暇使用時間
	 * @return
	 */
	public AttendanceTime calcOffSetTime(int lateRemainingTime,AttendanceTime timeVacationUseTime) {
		int offSetTime;
		//相殺する時間を計算（比較）する
		if(timeVacationUseTime.lessThanOrEqualTo(lateRemainingTime)) {
			offSetTime = timeVacationUseTime.valueAsMinutes();
		}else {
			offSetTime = lateRemainingTime;
		}
		return new AttendanceTime(offSetTime);
	}

		
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
	/**
	 * 就業時間内時間帯に入っている加給時間の計算
	 */
	public List<BonusPayTime> calcBonusPayTimeInWithinWorkTime(RaisingSalaryCalcAtr raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(WithinWorkTimeFrame timeFrame : withinWorkTimeFrame) {
			bonusPayList.addAll(timeFrame.calcBonusPay(ActualWorkTimeSheetAtr.WithinWorkTime,raisingAutoCalcSet,bonusPayAutoCalcSet,calcAtrOfDaily,bonusPayAtr));
		}
		//同じNo同士はここで加算し、Listのサイズを減らす
		return sumBonusPayTime(bonusPayList);
	}
	/**
	 * 就業時間内時間帯に入っている特定加給時間の計算
	 */
	public List<BonusPayTime> calcSpecifiedBonusPayTimeInWithinWorkTime(RaisingSalaryCalcAtr raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(WithinWorkTimeFrame timeFrame : withinWorkTimeFrame) {
			bonusPayList.addAll(timeFrame.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.WithinWorkTime,raisingAutoCalcSet,bonusPayAutoCalcSet, calcAtrOfDaily,bonusPayAtr));
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
	 * 法定内深夜時間の計算
	 * @return　法定内深夜時間
	 */
	public AttendanceTime calcMidNightTime(AutoCalAtrOvertime autoCalcSet) {
		int totalMidNightTime = 0;
		int totalDedTime = 0;
		totalMidNightTime = withinWorkTimeFrame.stream()
											   .filter(tc -> tc.getMidNightTimeSheet().isPresent())
											   .map(ts -> ts.getMidNightTimeSheet().get().calcTotalTime().v())
											   .collect(Collectors.summingInt(tc -> tc));
		
		for(WithinWorkTimeFrame frametime : withinWorkTimeFrame) {
			val a = frametime.getDedTimeSheetByAtr(DeductionAtr.Deduction, ConditionAtr.BREAK);
			if(frametime.getMidNightTimeSheet().isPresent()) {
				totalDedTime += a.stream().filter(tc -> tc.getCalcrange().getDuplicatedWith(frametime.getMidNightTimeSheet().get().getCalcrange()).isPresent())
										 .map(tc -> tc.getCalcrange().getDuplicatedWith(frametime.getMidNightTimeSheet().get().getCalcrange()).get().lengthAsMinutes())
										 .collect(Collectors.summingInt(tc->tc));
			}
		}
		
		totalMidNightTime -= totalDedTime;
		return new AttendanceTime(totalMidNightTime);
	}
	
	/**
	 * 全枠の中に入っている控除時間(控除区分に従って)を合計する
	 * @return 控除合計時間
	 */
	public AttendanceTime calculationAllFrameDeductionTime(DeductionAtr dedAtr,ConditionAtr atr) {
		AttendanceTime totalTime = new AttendanceTime(0);
		for(WithinWorkTimeFrame frameTime : this.withinWorkTimeFrame) {
			val addTime = frameTime.forcs(atr,dedAtr).valueAsMinutes();
			totalTime = totalTime.addMinutes(addTime);
		}
		return totalTime;
	}
	
//	/**
//	 * 控除区分に従って該当のリストを取得(現時点では休憩のみしか取得できない)
//	 * @param dedAtr
//	 * @param conAtr
//	 * @return
//	 */
//	public List<TimeSheetOfDeductionItem> getDedTimeSheetByDedAtr(DeductionAtr dedAtr,ConditionAtr conAtr){
//		List<TimeSheetOfDeductionItem> returnList = new ArrayList<>();
//		for(WithinWorkTimeFrame timeSheet : this.getWithinWorkTimeFrame()) {
//			returnList.addAll(timeSheet.getDedTimeSheetByDedAtr(dedAtr, conAtr));
//		}
//		return returnList;
//	}

}
