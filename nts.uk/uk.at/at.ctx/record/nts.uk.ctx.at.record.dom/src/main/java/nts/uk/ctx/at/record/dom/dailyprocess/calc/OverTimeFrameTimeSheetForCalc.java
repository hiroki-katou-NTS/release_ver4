package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDay;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethod;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfEachPremiumHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.PremiumAtr;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.DeductLeaveEarly;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
//import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.PremiumHolidayCalcMethod;
//import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.WorkTimeHolidayCalcMethod;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.ENUM.CalcurationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayAdditionAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
//import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.HolidayCalculation;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.SettlementOrder;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
//import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 残業枠時間帯(WORK)
 * @author keisuke_hoshina
 *
 */
@Getter
public class OverTimeFrameTimeSheetForCalc extends CalculationTimeSheet{
	
	//枠時間
	private OverTimeFrameTime frameTime;
	
	//法定内区分
	private  StatutoryAtr withinStatutryAtr;
	
	//早出区分
	private boolean goEarly;
	
	//残業時間帯No
	private EmTimezoneNo overTimeWorkSheetNo;
	
	//拘束時間として扱う
	private boolean asTreatBindTime;
	
	//精算順序
	private Optional<SettlementOrder> payOrder;

	//調整時間
	private Optional<AttendanceTime> adjustTime;

	
	
	/**
	 * Constrcotr
	 * @param timeSheet
	 * @param calcrange
	 * @param deductionTimeSheets
	 * @param bonusPayTimeSheet
	 * @param specifiedBonusPayTimeSheet
	 * @param midNighttimeSheet
	 * @param frameTime
	 * @param withinStatutryAtr
	 * @param goEarly
	 * @param overTimeWorkSheetNo
	 * @param asTreatBindTime
	 * @param adjustTime
	 */
	public OverTimeFrameTimeSheetForCalc(TimeZoneRounding timeSheet, TimeSpanForCalc calcrange,
			List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
			List<TimeSheetOfDeductionItem> deductionTimeSheets, List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,
			List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet,
			Optional<MidNightTimeSheetForCalc> midNighttimeSheet, OverTimeFrameTime frameTime,
			StatutoryAtr withinStatutryAtr, boolean goEarly, EmTimezoneNo overTimeWorkSheetNo, boolean asTreatBindTime,
			Optional<SettlementOrder> payOrder, Optional<AttendanceTime> adjustTime) {
		super(timeSheet, calcrange, recorddeductionTimeSheets, deductionTimeSheets, bonusPayTimeSheet,
				specifiedBonusPayTimeSheet, midNighttimeSheet);
		this.frameTime = frameTime;
		this.withinStatutryAtr = withinStatutryAtr;
		this.goEarly = goEarly;
		this.overTimeWorkSheetNo = overTimeWorkSheetNo;
		this.asTreatBindTime = asTreatBindTime;
		this.payOrder = payOrder;
		this.adjustTime = adjustTime;
	}
	
	/**
	 * 計算用残業枠時間帯から残業枠時間帯へ変換する
	 * @return　残業枠時間帯
	 */
	public OverTimeFrameTimeSheet changeNotWorkFrameTimeSheet() {
		return new OverTimeFrameTimeSheet(this.calcrange, this.frameTime.getOverWorkFrameNo());
	}

	/**
	 * 残業枠分ループし残業枠時間帯の作成
	 * @param overTimeHourSetList 固定勤務の時間帯設定クラス
	 * @param workingSystem 労働制クラス
	 * @param attendanceLeave 出退勤クラス
	 * @param secondStartTime 2回目の勤務の開始時間
	 * @param workNo 現在処理をしている勤務回数
	 * @param breakdownTimeDay
	 * @param dailyTime 法定労働時間
	 * @param autoCalculationSet
	 * @param statutorySet
	 * @param dayEndSet
	 * @param overDayEndSet
	 * @param overTimeWorkItem
	 * @param beforeDay
	 * @param toDay
	 * @param afterDay
	 * @param prioritySet
	 * @param createWithinWorkTimeSheet 
	 * @param workTimeDailyAtr 
	 * @param integrationOfDaily 
	 * @return
	 */
	public static List<OverTimeFrameTimeSheetForCalc> createOverWorkFrame(List<OverTimeOfTimeZoneSet> overTimeHourSetList,WorkingSystem workingSystem,
												TimeLeavingWork attendanceLeave,int workNo,
												BreakDownTimeDay breakdownTimeDay,DailyTime dailyTime,AutoCalOvertimeSetting autoCalculationSet,
												LegalOTSetting statutorySet,StatutoryPrioritySet prioritySet ,Optional<BonusPaySetting> bonuspaySetting,MidNightTimeSheet midNightTimeSheet,
												DailyCalculationPersonalInformation personalInfo,boolean isCalcWithinOverTime,DeductionTimeSheet deductionTimeSheet,DailyUnit dailyUnit,
												HolidayCalcMethodSet holidayCalcMethodSet, WithinWorkTimeSheet createWithinWorkTimeSheet,
                                        		VacationClass vacationClass, TimevacationUseTimeOfDaily timevacationUseTimeOfDaily, 
                                        		WorkType workType, PredetermineTimeSetForCalc predetermineTimeSet, Optional<WorkTimeCode> siftCode, 
                                        		boolean late, boolean leaveEarly,WorkDeformedLaborAdditionSet illegularAddSetting, WorkFlexAdditionSet flexAddSetting, 
                                        		WorkRegularAdditionSet regularAddSetting, HolidayAddtionSet holidayAddtionSet,Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
                                        		Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,Optional<CoreTimeSetting> coreTimeSetting,Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrSheets, WorkTimeDailyAtr workTimeDailyAtr) {
		List<OverTimeFrameTimeSheetForCalc> createTimeSheet = new ArrayList<>();
		
		for(OverTimeOfTimeZoneSet overTimeHourSet:overTimeHourSetList) {
			
			Optional<TimeSpanForCalc> calcrange = overTimeHourSet.getTimezone().getDuplicatedWith(attendanceLeave.getTimespan());
			if(calcrange.isPresent()) {
				createTimeSheet.add(OverTimeFrameTimeSheetForCalc.createOverWorkFramTimeSheet(overTimeHourSet,calcrange.get(),bonuspaySetting,midNightTimeSheet,deductionTimeSheet,commonSetting, specificDateAttrSheets));
			}
																					  
		}
//		/*変形残業　振替*/
		List<OverTimeFrameTimeSheetForCalc> afterVariableWork = new ArrayList<>();
		afterVariableWork = dicisionCalcVariableWork(createTimeSheet,breakdownTimeDay,autoCalculationSet,personalInfo,isCalcWithinOverTime,overTimeHourSetList,dailyUnit,holidayCalcMethodSet);
		afterVariableWork.stream().sorted((first,second) -> first.calcrange.getStart().compareTo(second.calcrange.getStart()));
//		/*法定内残業　振替*/
		List<OverTimeFrameTimeSheetForCalc> afterCalcStatutoryOverTimeWork = new ArrayList<>();
		afterCalcStatutoryOverTimeWork = diciaionCalcStatutory(statutorySet ,dailyTime ,afterVariableWork,autoCalculationSet,breakdownTimeDay,overTimeHourSetList,dailyUnit,holidayCalcMethodSet,createWithinWorkTimeSheet, 
															   vacationClass, timevacationUseTimeOfDaily, workType, predetermineTimeSet, siftCode, leaveEarly, leaveEarly, 
															   workingSystem, illegularAddSetting, flexAddSetting, regularAddSetting, holidayAddtionSet,commonSetting,conditionItem,predetermineTimeSetByPersonInfo,coreTimeSetting,
															   workTimeDailyAtr);
				
		
		/*return*/
		return afterCalcStatutoryOverTimeWork;
	}

	/**
	 * 残業枠時間帯の作成
	 * @param overTimeHourSet 残業時間の時間帯設定
	 * @param timeSpan 計算範囲
	 * @param integrationOfDaily 
	 * @return
	 */
	public static OverTimeFrameTimeSheetForCalc createOverWorkFramTimeSheet(OverTimeOfTimeZoneSet overTimeHourSet,TimeSpanForCalc timeSpan,
																			Optional<BonusPaySetting> bonuspaySetting,MidNightTimeSheet midNightTimeSheet,
																			DeductionTimeSheet deductionTimeSheet,Optional<WorkTimezoneCommonSet> commonSetting,
																			Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrSheets) {
		
		List<TimeSheetOfDeductionItem> dedTimeSheet = deductionTimeSheet.getDupliRangeTimeSheet(timeSpan, DeductionAtr.Deduction);
		dedTimeSheet.forEach(tc ->{
			tc.changeReverceRounding(tc.getTimeSheet().getRounding(), ActualWorkTimeSheetAtr.OverTimeWork, DeductionAtr.Deduction, commonSetting);
		});
		List<TimeSheetOfDeductionItem> recordTimeSheet = deductionTimeSheet.getDupliRangeTimeSheet(timeSpan, DeductionAtr.Appropriate);
		recordTimeSheet.forEach(tc ->{
			tc.changeReverceRounding(tc.getTimeSheet().getRounding(), ActualWorkTimeSheetAtr.OverTimeWork, DeductionAtr.Appropriate, commonSetting);
		});
		/*加給*/
		val duplibonusPayTimeSheet = getBonusPayTimeSheetIncludeDedTimeSheet(bonuspaySetting, timeSpan, recordTimeSheet, recordTimeSheet);
		/*特定日*/
		val duplispecifiedBonusPayTimeSheet = getSpecBonusPayTimeSheetIncludeDedTimeSheet(bonuspaySetting, timeSpan, recordTimeSheet, recordTimeSheet, specificDateAttrSheets);
		/*深夜*/
		val duplicatemidNightTimeSheet = getMidNightTimeSheetIncludeDedTimeSheet(midNightTimeSheet, timeSpan, recordTimeSheet, recordTimeSheet,commonSetting);
		
		
		OverTimeFrameTime overTimeFrameTime = new OverTimeFrameTime(new OverTimeFrameNo(overTimeHourSet.getOtFrameNo().v()),
				    												TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				    												TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				    												new AttendanceTime(0),
				    												new AttendanceTime(0));

		
		
		return new OverTimeFrameTimeSheetForCalc(new TimeZoneRounding(timeSpan.getStart(),timeSpan.getEnd(),overTimeHourSet.getTimezone().getRounding()),
											  	timeSpan,
											  	recordTimeSheet.stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
											  	dedTimeSheet.stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
											  	duplibonusPayTimeSheet,
											  	duplispecifiedBonusPayTimeSheet,
											  	duplicatemidNightTimeSheet,
											  	overTimeFrameTime,
											  	StatutoryAtr.Excess,
											  	overTimeHourSet.isEarlyOTUse(),
											  	overTimeHourSet.getWorkTimezoneNo(),
											  	false,
											  	Optional.of(overTimeHourSet.getSettlementOrder()),
											  	Optional.empty()
											  	);
	}

	
	
	/**
	 * 変形労働を計算するか判定
	 * @param isCalcWithinOverTime 法定内残業時間を計算するか(変形労働の法定内残業時間計算クラス実装後入替)
	 * @return
	 */
	private static List<OverTimeFrameTimeSheetForCalc> dicisionCalcVariableWork(
			List<OverTimeFrameTimeSheetForCalc> createTimeSheet, BreakDownTimeDay breakdownTimeDay, 
			AutoCalOvertimeSetting autoCalculationSet,DailyCalculationPersonalInformation personalInfo,
			boolean isCalcWithinOverTime,List<OverTimeOfTimeZoneSet> overTimeHourSetList,DailyUnit dailyUnit,HolidayCalcMethodSet holidayCalcMethodSet) {
		//変形労働か
		if(personalInfo.getWorkingSystem().isVariableWorkingTimeWork()) {
			//変形基準内残業計算するか
			if(isCalcWithinOverTime) {
				//変形できる時間計算
				AttendanceTime ableRangeTime = new AttendanceTime(dailyUnit.getDailyTime().valueAsMinutes() - breakdownTimeDay.getPredetermineWorkTime());
				if(ableRangeTime.greaterThan(0))
					return reclassified(ableRangeTime, createTimeSheet, autoCalculationSet,overTimeHourSetList,holidayCalcMethodSet);
			}
		}
		return createTimeSheet;
	}

    /**
     * 法定内残業時間の計算をするか判定
     * @param statutoryOverWorkSet 法定内残業設定クラス
     * @param statutorySet
     * @param dailyTime 法定労働時間
     * @param overTimeWorkFrameTimeSheetList
     * @param autoCalculationSet
     * @param createWithinWorkTimeSheet 
     * @param calcActualTime 
     * @param vacationClass 
     * @param timevacationUseTimeOfDaily 
     * @param statutoryDivision 
     * @param workType 
     * @param predetermineTimeSet 
     * @param siftCode 
     * @param personalCondition 
     * @param late 
     * @param leaveEarly 
     * @param workingSystem 
     * @param illegularAddSetting 
     * @param flexAddSetting 
     * @param regularAddSetting 
     * @param holidayAddtionSet 
     * @param workTimeDailyAtr 
     * @param prioritySet
     * @return
     */
    public static List<OverTimeFrameTimeSheetForCalc> diciaionCalcStatutory(LegalOTSetting statutorySet,DailyTime dailyTime,List<OverTimeFrameTimeSheetForCalc> overTimeWorkFrameTimeSheetList
                                                                    		,AutoCalOvertimeSetting autoCalculationSet,BreakDownTimeDay breakdownTimeDay,
                                                                    		List<OverTimeOfTimeZoneSet> overTimeHourSetList,DailyUnit dailyUnit,HolidayCalcMethodSet holidayCalcMethodSet, WithinWorkTimeSheet createWithinWorkTimeSheet, 
                                                                    		VacationClass vacationClass, TimevacationUseTimeOfDaily timevacationUseTimeOfDaily, 
                                                                    		WorkType workType, PredetermineTimeSetForCalc predetermineTimeSet, Optional<WorkTimeCode> siftCode,
                                                                    		boolean late, boolean leaveEarly, WorkingSystem workingSystem, WorkDeformedLaborAdditionSet illegularAddSetting, WorkFlexAdditionSet flexAddSetting, 
                                                                    		WorkRegularAdditionSet regularAddSetting, HolidayAddtionSet holidayAddtionSet, Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
                                                                    		Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,Optional<CoreTimeSetting> coreTimeSetting, WorkTimeDailyAtr workTimeDailyAtr) {
        if(statutorySet.isLegal()) {//statutorySet.isLegal()) {
            /*振替処理   法定内基準時間を計算する*/
        	AttendanceTime workTime = new AttendanceTime(0);
        	if(createWithinWorkTimeSheet != null)
        	{
        		boolean isOOtsukaMode = true;
        		//大塚納品ではこちらを通るようにする(実働と法定労働から振り替えられる時間を算出)
        		if(isOOtsukaMode) {
        			workTime = WithinStatutoryTimeOfDaily.calcActualWorkTime(createWithinWorkTimeSheet, 
																  vacationClass, 
																  workType, 
																  late, 
																  leaveEarly, 
																  workingSystem, 
																  illegularAddSetting, 
																  flexAddSetting, 
																  regularAddSetting, 
																  holidayAddtionSet, 
																  holidayCalcMethodSet, 
																  CalcMethodOfNoWorkingDay.isCalculateFlexTime, 
																  Optional.of(new SettingOfFlexWork(new FlexCalcMethodOfHalfWork(new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.OneDay, FlexCalcMethod.OneDay),
																			new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.OneDay, FlexCalcMethod.OneDay)))), 
																  Optional.of(workTimeDailyAtr), 
																  siftCode, 
																  new AttendanceTime(2880), //事前フレ
																  coreTimeSetting, 
																  predetermineTimeSet, 
																  Finally.of(timevacationUseTimeOfDaily), 
																  dailyUnit, 
																  commonSetting, 
																  conditionItem, 
																  predetermineTimeSetByPersonInfo, 
																  Optional.of(new DeductLeaveEarly(0, 1)));
					
		
        		}
        		//製品版では就業時間を求めて使うようにする
        		else {
            		Optional<WorkTimezoneCommonSet> leaveLatesetForWorkTime = commonSetting.isPresent() && commonSetting.get().getLateEarlySet().getCommonSet().isDelFromEmTime()
							?Optional.of(commonSetting.get().reverceTimeZoneLateEarlySet())
							:commonSetting;
            		workTime = createWithinWorkTimeSheet.calcWorkTime(PremiumAtr.RegularWork, 
							  regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation(), 
							  vacationClass, 
							  timevacationUseTimeOfDaily, 
							  StatutoryDivision.Nomal, 
							  workType, 
							  predetermineTimeSet, 
							  siftCode, 
							  late, 
							  leaveEarly, 
							  workingSystem, 
							  illegularAddSetting, 
							  flexAddSetting, 
							  regularAddSetting, 
							  holidayAddtionSet, 
							  holidayCalcMethodSet,
							  dailyUnit,
							  leaveLatesetForWorkTime,
							  conditionItem,
							  predetermineTimeSetByPersonInfo,coreTimeSetting
							  ,HolidayAdditionAtr.HolidayAddition.convertFromCalcByActualTimeToHolidayAdditionAtr(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME),
							  new DeductLeaveEarly(1, 0)
							).getWorkTime();
        		}
        	}
        	
        	AttendanceTime ableRangeTime = new AttendanceTime(dailyUnit.getDailyTime().valueAsMinutes() - workTime.valueAsMinutes());
        	
        	/*ログ差し込み*/
        	org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OverTimeFrameTimeSheetForCalc.class);
        	log.info("時間帯での法内への振替可能時間："+ ableRangeTime.valueAsMinutes()); 
        	/*ログ差し込み*/
        	
        	HolidayCalculation holidayCalculation = commonSetting.isPresent()?commonSetting.get().getHolidayCalculation():new HolidayCalculation(nts.uk.ctx.at.shared.dom.workdayoff.frame.NotUseAtr.USE);
        	//*ログ　計算区分*//
        	log.info("自動計算区分は："+ autoCalculationSet.getLegalOtTime().getCalAtr().toString() + ":です。"); 
        	if(ableRangeTime.greaterThan(0) && autoCalculationSet.getLegalOtTime().getCalAtr().isCalculateEmbossing())
        	{
        		//*ログ　今日の勤務種類  *//
        		log.info("今日の勤務種類 は特別休暇"+ (workType.getDailyWork().decisionMatchWorkType(WorkTypeClassification.SpecialHoliday).isFullTime()?"です。":"ではありません。"));
        		log.info("就業時間帯その他タブ　休暇の場合に就業時間を計算：" + (holidayCalculation.getIsCalculate().isNotUse() ? "しません。" : "します"));
        		
        		log.info("特休の時に組み込んだ条件が正しく動いているか出力します。出力結果　→："+(!workType.getDailyWork().decisionMatchWorkType(WorkTypeClassification.SpecialHoliday).isFullTime() || holidayCalculation.getIsCalculate().isNotUse()));
        		if(!workType.getDailyWork().decisionMatchWorkType(WorkTypeClassification.SpecialHoliday).isFullTime() || holidayCalculation.getIsCalculate().isNotUse()) {
        			List<OverTimeFrameTimeSheetForCalc> result = reclassified(ableRangeTime,overTimeWorkFrameTimeSheetList.stream()
							    .filter(tc -> tc.getPayOrder().isPresent())
							    .sorted((first,second) -> first.getPayOrder().get().compareTo(second.getPayOrder().isPresent()
							    																?second.getPayOrder().get()
							    																:new SettlementOrder(99)
							    																))
							    .collect(Collectors.toList()),
							    autoCalculationSet,
							    overTimeHourSetList,
							    holidayCalcMethodSet);    
            		
            		//returnの内容出力
                	log.info("法内残業振替処理後の出力を行います。");
                	for(OverTimeFrameTimeSheetForCalc ot : result) {
                		log.info("枠No＝" + ot.getOverTimeWorkSheetNo());
                		log.info("時間帯開始時刻："+ot.getTimeSheet().getStart());
                		log.info("時間帯終了時刻："+ot.getTimeSheet().getEnd());
                		
                		log.info("計上用控除時間帯は次の通りです。");
                		for(TimeSheetOfDeductionItem td : ot.getRecordedTimeSheet()) {
                    		log.info("計上用控除区分："+td.getDeductionAtr());
                    		log.info("計上用控除時間帯開始時刻："+td.getTimeSheet().getStart());
                    		log.info("計上用控除時間帯終了時刻："+td.getTimeSheet().getEnd());
                		}
                		log.info("計上用控除時間帯は以上です。");
                		
                		log.info("控除用控除時間帯は次の通りです。");
                		for(TimeSheetOfDeductionItem td : ot.getRecordedTimeSheet()) {
                    		log.info("控除用控除区分："+td.getDeductionAtr());
                    		log.info("控除用控除時間帯開始時刻："+td.getTimeSheet().getStart());
                    		log.info("控除用控除時間帯終了時刻："+td.getTimeSheet().getEnd());
                		}
                		log.info("控除用控除時間帯は以上です。");
                		
                		
                		if(ot.getPayOrder().isPresent()) {
                			log.info("清算順序："+ot.getPayOrder().get());
                		}
                		else {
                			log.info("この枠の清算順序は存在しません");
                		}
                		
                		log.info("法内区分："+ot.getWithinStatutryAtr());
                		
                	}
                	log.info("法内残業振替処理後の出力が完了しました。");
        			
            		return result;
        		}
        	}
        }
        return overTimeWorkFrameTimeSheetList;
    }


	/**
	 * 振替処理
	 * @param ableRangeTime 振替できる時間
	 * @param overTimeWorkFrameTimeSheetList　残業時間枠時間帯クラス
	 * @param autoCalculationSet　時間外の自動計算設定
	 */
	public static List<OverTimeFrameTimeSheetForCalc> reclassified(AttendanceTime ableRangeTime,List<OverTimeFrameTimeSheetForCalc> overTimeWorkFrameTimeSheetList,
			AutoCalOvertimeSetting autoCalculationSet,List<OverTimeOfTimeZoneSet> overTimeHourSetList,HolidayCalcMethodSet holidayCalcMethodSet) {
		boolean forceAtr = true;
		AttendanceTime overTime = new AttendanceTime(0);
		AttendanceTime transTime = new AttendanceTime(0);
		
    	org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OverTimeFrameTimeSheetForCalc.class);
    	
		
		//*ログ　残業時間帯.size*//
    	log.info("受け取った残業時間帯のサイズ"+ overTimeWorkFrameTimeSheetList.size()); 
		//*ログ　ループ開始を知らせるメッセージ*//
    	log.info("法内残業分割のためのループを始めます。");
		for(int number = 0; number < overTimeWorkFrameTimeSheetList.size(); number++) {
			//*ログ　ループ回数*//
			log.info(number+"回目のループです。");
			//*----------------ログ　現在処理中の残業時間帯の情報を全て出す--------------------------*//		
			log.info("****現在処理を行おうとしている残業時間帯確認用ログ出力開始****");
    		log.info("枠No＝" + overTimeWorkFrameTimeSheetList.get(number).getOverTimeWorkSheetNo());
    		log.info("時間帯開始時刻："+overTimeWorkFrameTimeSheetList.get(number).getTimeSheet().getStart());
    		log.info("時間帯終了時刻："+overTimeWorkFrameTimeSheetList.get(number).getTimeSheet().getEnd());
    		
    		log.info("計上用控除時間帯は次の通りです。");
    		for(TimeSheetOfDeductionItem td : overTimeWorkFrameTimeSheetList.get(number).getRecordedTimeSheet()) {
        		log.info("計上用控除区分："+td.getDeductionAtr());
        		log.info("計上用控除時間帯開始時刻："+td.getTimeSheet().getStart());
        		log.info("計上用控除時間帯終了時刻："+td.getTimeSheet().getEnd());
    		}
    		log.info("計上用控除時間帯は以上です。");
    		
    		log.info("控除用控除時間帯は次の通りです。");
    		for(TimeSheetOfDeductionItem td : overTimeWorkFrameTimeSheetList.get(number).getRecordedTimeSheet()) {
        		log.info("控除用控除区分："+td.getDeductionAtr());
        		log.info("控除用控除時間帯開始時刻："+td.getTimeSheet().getStart());
        		log.info("控除用控除時間帯終了時刻："+td.getTimeSheet().getEnd());
    		}
    		log.info("控除用控除時間帯は以上です。");
    		
    		
    		if(overTimeWorkFrameTimeSheetList.get(number).getPayOrder().isPresent()) {
    			log.info("清算順序："+overTimeWorkFrameTimeSheetList.get(number).getPayOrder().get());
    		}
    		else {
    			log.info("この枠の清算順序は存在しません");
    		}
    		log.info("法内区分："+overTimeWorkFrameTimeSheetList.get(number).getWithinStatutryAtr());
    		log.info("****現在処理を行おうとしている残業時間帯確認用ログ出力終了****");
    		//*----------------ログ　現在処理中の残業時間帯の情報を全て出す--------------------------*//
			
			
			overTime = overTimeWorkFrameTimeSheetList.get(number).correctCalculationTime(Optional.of(forceAtr),autoCalculationSet,DeductionAtr.Deduction);
			
			
			
			//振替できる時間計算
			log.info("振替できる時間:"+ableRangeTime);
			//*ログ　overTime*//
			log.info("現在処理中の時間帯の残業時間(仮):"+overTime);
			if(ableRangeTime.greaterThan(overTime)) {
				transTime = overTime;
			}
			else {
				transTime = ableRangeTime;
			}
			
			//*ログ　transTime*//
			log.info("振替できる時間は:"+transTime + ":に決定しました。");
			
			if(transTime.lessThanOrEqualTo(0))
				continue;
			
			//振替時間を調整時間に入れる
			if(overTimeWorkFrameTimeSheetList.get(number).adjustTime.isPresent()) {
				overTimeWorkFrameTimeSheetList.get(number).adjustTime = Optional.of(overTimeWorkFrameTimeSheetList.get(number).adjustTime.get().addMinutes(transTime.valueAsMinutes()));
			}
			else {
				overTimeWorkFrameTimeSheetList.get(number).adjustTime = Optional.of(transTime);
			}

			//終了時間の判断
			TimeWithDayAttr endTime = reCreateSiteiTimeFromStartTime(transTime,overTimeWorkFrameTimeSheetList.get(number));
			//*ログ　endTime*//
			log.info("終了判断時刻："+endTime);
			
			/*ここで分割*/
			overTimeWorkFrameTimeSheetList = correctTimeSpan(overTimeWorkFrameTimeSheetList.get(number).splitTimeSpan(endTime,overTimeHourSetList),overTimeWorkFrameTimeSheetList,number);
			//*ログ　分割後の残業時間帯(overTimeWorkFrameTimeSheetList)*//
			log.info("****分割処理後の残業時間帯確認用ログ出力開始****");
    		log.info("枠No＝" + overTimeWorkFrameTimeSheetList.get(number).getOverTimeWorkSheetNo());
    		log.info("時間帯開始時刻："+overTimeWorkFrameTimeSheetList.get(number).getTimeSheet().getStart());
    		log.info("時間帯終了時刻："+overTimeWorkFrameTimeSheetList.get(number).getTimeSheet().getEnd());
    		
    		log.info("計上用控除時間帯は次の通りです。");
    		for(TimeSheetOfDeductionItem td : overTimeWorkFrameTimeSheetList.get(number).getRecordedTimeSheet()) {
        		log.info("計上用控除区分："+td.getDeductionAtr());
        		log.info("計上用控除時間帯開始時刻："+td.getTimeSheet().getStart());
        		log.info("計上用控除時間帯終了時刻："+td.getTimeSheet().getEnd());
    		}
    		log.info("計上用控除時間帯は以上です。");
    		
    		log.info("控除用控除時間帯は次の通りです。");
    		for(TimeSheetOfDeductionItem td : overTimeWorkFrameTimeSheetList.get(number).getRecordedTimeSheet()) {
        		log.info("控除用控除区分："+td.getDeductionAtr());
        		log.info("控除用控除時間帯開始時刻："+td.getTimeSheet().getStart());
        		log.info("控除用控除時間帯終了時刻："+td.getTimeSheet().getEnd());
    		}
    		log.info("控除用控除時間帯は以上です。");
    		
    		
    		if(overTimeWorkFrameTimeSheetList.get(number).getPayOrder().isPresent()) {
    			log.info("清算順序："+overTimeWorkFrameTimeSheetList.get(number).getPayOrder().get());
    		}
    		else {
    			log.info("この枠の清算順序は存在しません");
    		}
    		log.info("法内区分："+overTimeWorkFrameTimeSheetList.get(number).getWithinStatutryAtr());
			log.info("****分割処理後の残業時間帯確認用ログ出力終了****");
			
			
			ableRangeTime = ableRangeTime.minusMinutes(transTime.valueAsMinutes()) ;
			//*ログ　計算後の振替可能時間*//
			log.info("分割処理後に再計算した振替可能時間："+ableRangeTime);
		}
		//*ログ　ループ終了を知らせるメッセージ*//
		log.info("法内残業分割のためのループを終了します。");
		return overTimeWorkFrameTimeSheetList;
	}
	
	/**
	 * 開始から指定時間経過後の終了時刻を取得
	 * @param transTime
	 * @return
	 */
	public static TimeWithDayAttr reCreateSiteiTimeFromStartTime(AttendanceTime transTime,OverTimeFrameTimeSheetForCalc overTimeWork) {
		return overTimeWork.reCreateTreatAsSiteiTimeEnd(transTime,overTimeWork).orElse(overTimeWork.timeSheet.getTimeSpan()).getEnd();
	}
	
	/**
	 * 分割後の残業時間枠時間帯を受け取り
	 * @param insertList　補正した時間帯
	 * @param originList　補正する前の時間帯
	 * @return　
	 */
	public static List<OverTimeFrameTimeSheetForCalc> correctTimeSpan(List<OverTimeFrameTimeSheetForCalc> insertList,List<OverTimeFrameTimeSheetForCalc> originList,int nowNumber){
		originList.remove(nowNumber);
		originList.addAll(insertList);
		List<OverTimeFrameTimeSheetForCalc> returnList = new ArrayList<>();
		returnList = originList;		
		
		boolean nextLoopFlag = true;
		while(nextLoopFlag) {
			for(int index = 0 ; index <= originList.size() ; index++) {
				if(index == originList.size() - 1)
				{
					nextLoopFlag = false;
					break;
				}
				//精算順序の比較
				if(returnList.get(index).getPayOrder().get().greaterThan(returnList.get(index + 1).getPayOrder().get())) {
					OverTimeFrameTimeSheetForCalc pary = returnList.get(index);
					returnList.set(index, returnList.get(index + 1));
					returnList.set(index + 1, pary);
					break;
				}
				//枠の比較(精算順序同じケースの整頓)
				else if(returnList.get(index).getPayOrder().get().equals(returnList.get(index + 1).getPayOrder().get())) {
					if(returnList.get(index).getCalcrange().getStart().greaterThan(returnList.get(index + 1).getCalcrange().getStart()) ) {
						OverTimeFrameTimeSheetForCalc pary = returnList.get(index);
						returnList.set(index, returnList.get(index + 1));
						returnList.set(index + 1, pary);
						break;
					}
				}

			}
		}
		
		return originList;
	}
	
	


    /**
     * 時間帯の分割
     * @param overTimeFrameTimeSheetForCalc 
     * @return
     */
    public List<OverTimeFrameTimeSheetForCalc> splitTimeSpan(TimeWithDayAttr baseTime,List<OverTimeOfTimeZoneSet> overTimeHourSetList){
        List<OverTimeFrameTimeSheetForCalc> returnList = new ArrayList<>();
    	val statutoryOverFrameNo = overTimeHourSetList.stream()
				  									  .filter(tc -> tc.getWorkTimezoneNo() == this.getOverTimeWorkSheetNo())
				  									  .findFirst();
        if(this.calcrange.getEnd().equals(baseTime)) {
            returnList.add(new OverTimeFrameTimeSheetForCalc(this.timeSheet
                    										,this.calcrange
                    										,this.recordedTimeSheet
                    										,this.deductionTimeSheet
                    										,this.bonusPayTimeSheet
                    										,this.specBonusPayTimesheet
                    										,this.midNightTimeSheet
                    										,this.getFrameTime().changeFrameNo(statutoryOverFrameNo.isPresent()?statutoryOverFrameNo.get().getLegalOTframeNo().v():this.getFrameTime().getOverWorkFrameNo().v())
                    										,StatutoryAtr.Statutory
                    										,this.goEarly
                    										,this.getOverTimeWorkSheetNo()
                    										,this.asTreatBindTime
                    										,this.payOrder
                    										,this.getAdjustTime()));
        }
        //分割
        else {
        	/*法内へ振り替える側*/
        	//計上の控除時間帯
        	val beforeRec = this.getRecordedTimeSheet().stream()
        										.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).isPresent())
        										.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).get())
        										.collect(Collectors.toList());
        	//控除の控除時間帯
        	val beforeDed = this.getDeductionTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).get())
												.collect(Collectors.toList());
        	val beforeBp = this.getBonusPayTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).get())
												.collect(Collectors.toList());
        	val beforeSpecBp = this.getSpecBonusPayTimesheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).get())
												.collect(Collectors.toList());
        	//深夜時間帯
        	Optional<MidNightTimeSheetForCalc> beforeMid = this.getMidNightTimeSheet().isPresent()
        													?this.getMidNightTimeSheet().get().getDuplicateRangeTimeSheet(new TimeSpanForCalc(this.calcrange.getStart(), baseTime))
        													:Optional.empty();
        	
            returnList.add(new OverTimeFrameTimeSheetForCalc(new TimeZoneRounding(this.calcrange.getStart(), baseTime, new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN))
                                                         ,new TimeSpanForCalc(this.calcrange.getStart(), baseTime)
                                                         ,beforeRec
                                                         ,beforeDed
                                                         ,beforeBp
                                                         ,beforeSpecBp
                                                         ,beforeMid
                                                         ,this.getFrameTime().changeFrameNo(statutoryOverFrameNo.isPresent()?statutoryOverFrameNo.get().getLegalOTframeNo().v():this.getFrameTime().getOverWorkFrameNo().v())
                                                         ,StatutoryAtr.Statutory
                                                         ,this.goEarly
                                                         ,this.getOverTimeWorkSheetNo()
                                                         ,this.asTreatBindTime
                                                         ,this.payOrder
                                                         ,Optional.of(new AttendanceTime(0))));
            
            /*法外側*/
        	//計上の控除時間帯
        	val afterRec = this.getRecordedTimeSheet().stream()
        										.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).isPresent())
        										.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).get())
        										.collect(Collectors.toList());
        	//控除の控除時間帯
        	val afterDed = this.getDeductionTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).get())
												.collect(Collectors.toList());
        	val afterBp = this.getBonusPayTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc( baseTime, this.calcrange.getEnd())).get())
												.collect(Collectors.toList());
        	val afterSpecBp = this.getSpecBonusPayTimesheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc( baseTime, this.calcrange.getEnd())).get())
												.collect(Collectors.toList());
        	//深夜時間帯
        	Optional<MidNightTimeSheetForCalc> afterMid = this.getMidNightTimeSheet().isPresent()
        													?this.getMidNightTimeSheet().get().getDuplicateRangeTimeSheet(new TimeSpanForCalc(baseTime, this.calcrange.getEnd()))
        													:Optional.empty();
            
            returnList.add(new OverTimeFrameTimeSheetForCalc(new TimeZoneRounding(baseTime, this.calcrange.getEnd(), this.timeSheet.getRounding())
                                                          ,new TimeSpanForCalc(baseTime, this.calcrange.getEnd())
                                                         ,afterRec
                                                         ,afterDed
                                                         ,afterBp
                                                         ,afterSpecBp
                                                         ,afterMid
                                                         ,this.getFrameTime().changeFrameNo(this.getFrameTime().getOverWorkFrameNo().v())
                                                         ,StatutoryAtr.Excess
                                                         ,this.goEarly
                                                         ,this.getOverTimeWorkSheetNo()
                                                         ,this.asTreatBindTime
                                                         ,this.payOrder
                                                         ,this.getAdjustTime()));
        }
        return returnList;
    }

	
	
	
	/**
	 * 計算処理
	 * 残業時間の計算
	 * @param forceCalcTime 強制時間区分
	 * @param autoCalcSet 
	 */
	public AttendanceTime correctCalculationTime(Optional<Boolean> forceCalcTime,AutoCalOvertimeSetting autoCalcSet
												,DeductionAtr dedAtr) {
		return overTimeCalculationByAdjustTime(dedAtr);
	}
	
	/**
	 * 調整時間を考慮した時間の計算
	 * @param autoCalcSet 時間外の自動計算区分
	 * @return 残業時間枠時間帯クラス
	 */
	public AttendanceTime overTimeCalculationByAdjustTime(DeductionAtr dedAtr) {
		//調整時間を加算
		this.calcrange = new TimeSpanForCalc(this.calcrange.getStart(), this.calcrange.getEnd().forwardByMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes()));
		this.timeSheet = new TimeZoneRounding(this.getTimeSheet().getStart(), this.getTimeSheet().getEnd().forwardByMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes()), this.getTimeSheet().getRounding());
		AttendanceTime time = this.calcTotalTime(dedAtr);
		//調整時間を減算(元に戻す)
		this.calcrange = new TimeSpanForCalc(this.calcrange.getStart(), this.calcrange.getEnd().backByMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes()));
		this.timeSheet = new TimeZoneRounding(this.getTimeSheet().getStart(), this.getTimeSheet().getEnd().backByMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes()), this.getTimeSheet().getRounding());
		time = time.minusMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes()) ;
		return time;
		
	}

}
