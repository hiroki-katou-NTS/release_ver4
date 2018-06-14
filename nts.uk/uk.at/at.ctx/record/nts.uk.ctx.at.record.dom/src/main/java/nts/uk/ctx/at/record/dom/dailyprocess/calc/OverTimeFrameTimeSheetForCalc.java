package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.PremiumAtr;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.ENUM.CalcurationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.SettlementOrder;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
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
		return new OverTimeFrameTimeSheet(this.calcrange, new OverTimeFrameNo(this.overTimeWorkSheetNo.v()));
	}
	
//	public OverTimeFrameTimeSheetForCalc changeFromPremiumTimeSheet(TimeSpanForCalc timeSpan) {
//		List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
//		List<TimeSheetOfDeductionItem> deductionTimeSheets, List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,
//		List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet,
//		Optional<MidNightTimeSheetForCalc> midNighttimeSheet, OverTimeFrameTime frameTime,
//		StatutoryAtr withinStatutryAtr, boolean goEarly, EmTimezoneNo overTimeWorkSheetNo, boolean asTreatBindTime,
//		Optional<SettlementOrder> payOrder, Optional<AttendanceTime> adjustTime) {
//		return new OverTimeFrameTimeSheet(new TimeZoneRounding(timeSpan.getStart(), timeSpan.getEnd(), null)),
//										  timeSpan,
//										  ,
//										  ,
//										  ,
//										  ,
//										  ,
//										  new OverTimeFrameTime(overWorkFrameNo, 
//												  				overTimeWork, 
//												  				transferTime, 
//												  				beforeApplicationTime, 
//												  				orderTime),
//										  StatutoryAtr.Excess,
//										  false,
//										  new EmTimezoneNo(0),
//										  false,
//										  Optional.empty(),
//										  new AttendanceTime(0));
//	}
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
	 * @param integrationOfDaily 
	 * @return
	 */
	public static List<OverTimeFrameTimeSheetForCalc> createOverWorkFrame(List<OverTimeOfTimeZoneSet> overTimeHourSetList,WorkingSystem workingSystem,
												TimeLeavingWork attendanceLeave,int workNo,
												BreakDownTimeDay breakdownTimeDay,DailyTime dailyTime,AutoCalOvertimeSetting autoCalculationSet,
												LegalOTSetting statutorySet,StatutoryPrioritySet prioritySet ,BonusPaySetting bonusPaySetting,MidNightTimeSheet midNightTimeSheet,
												DailyCalculationPersonalInformation personalInfo,boolean isCalcWithinOverTime,DeductionTimeSheet deductionTimeSheet,DailyUnit dailyUnit,
												HolidayCalcMethodSet holidayCalcMethodSet, WithinWorkTimeSheet createWithinWorkTimeSheet,
                                        		VacationClass vacationClass, TimevacationUseTimeOfDaily timevacationUseTimeOfDaily, 
                                        		WorkType workType, PredetermineTimeSetForCalc predetermineTimeSet, Optional<WorkTimeCode> siftCode, Optional<PersonalLaborCondition> personalCondition, 
                                        		boolean late, boolean leaveEarly,WorkDeformedLaborAdditionSet illegularAddSetting, WorkFlexAdditionSet flexAddSetting, 
                                        		WorkRegularAdditionSet regularAddSetting, HolidayAddtionSet holidayAddtionSet) {
		List<OverTimeFrameTimeSheetForCalc> createTimeSheet = new ArrayList<>();
		
		for(OverTimeOfTimeZoneSet overTimeHourSet:overTimeHourSetList) {
			
			Optional<TimeSpanForCalc> calcrange = overTimeHourSet.getTimezone().getDuplicatedWith(attendanceLeave.getTimespan());
			if(calcrange.isPresent()) {
				createTimeSheet.add(OverTimeFrameTimeSheetForCalc.createOverWorkFramTimeSheet(overTimeHourSet,calcrange.get(),bonusPaySetting,midNightTimeSheet,deductionTimeSheet));
			}
																					  
		}
//		/*変形残業　振替*/
		List<OverTimeFrameTimeSheetForCalc> afterVariableWork = new ArrayList<>();
		afterVariableWork = dicisionCalcVariableWork(createTimeSheet,breakdownTimeDay,autoCalculationSet,personalInfo,isCalcWithinOverTime,overTimeHourSetList,dailyUnit,holidayCalcMethodSet);
		afterVariableWork.stream().sorted((first,second) -> first.calcrange.getStart().compareTo(second.calcrange.getStart()));
//		/*法定内残業　振替*/
		List<OverTimeFrameTimeSheetForCalc> afterCalcStatutoryOverTimeWork = new ArrayList<>();
		afterCalcStatutoryOverTimeWork = diciaionCalcStatutory(statutorySet ,dailyTime ,afterVariableWork,autoCalculationSet,breakdownTimeDay,overTimeHourSetList,dailyUnit,holidayCalcMethodSet,createWithinWorkTimeSheet, 
															   vacationClass, timevacationUseTimeOfDaily, workType, predetermineTimeSet, siftCode, personalCondition, leaveEarly, leaveEarly, 
															   workingSystem, illegularAddSetting, flexAddSetting, regularAddSetting, holidayAddtionSet);
				
		
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
																			BonusPaySetting bonusPaySetting,MidNightTimeSheet midNightTimeSheet,
																			DeductionTimeSheet deductionTimeSheet) {
		
		//TODO: get DeductionTimeSheet
		List<TimeSheetOfDeductionItem> dedTimeSheet = deductionTimeSheet.getDupliRangeTimeSheet(timeSpan, DeductionAtr.Deduction);
		List<TimeSheetOfDeductionItem> recordTimeSheet = deductionTimeSheet.getDupliRangeTimeSheet(timeSpan, DeductionAtr.Appropriate);
//		DeductionTimeSheet deductionTimeSheet = new DeductionTimeSheet(Collections.emptyList(), Collections.emptyList());/*実働時間の時間帯を跨いだ控除時間帯を分割する*/
//		deductionTimeSheet.getForRecordTimeZoneList();/*法定内区分の置き換え*/
//		deductionTimeSheet.getForDeductionTimeZoneList();/*法定内区分の置き換え*/
		/*加給*/
		val duplibonusPayTimeSheet = getBonusPayTimeSheetIncludeDedTimeSheet(bonusPaySetting, timeSpan, recordTimeSheet, recordTimeSheet);
											 
		/*特定日*/
		val duplispecifiedBonusPayTimeSheet = getSpecBonusPayTimeSheetIncludeDedTimeSheet(bonusPaySetting, timeSpan, recordTimeSheet, recordTimeSheet);
		/*深夜*/
		val duplicatemidNightTimeSheet = getMidNightTimeSheetIncludeDedTimeSheet(midNightTimeSheet, timeSpan, recordTimeSheet, recordTimeSheet);
		
		
		OverTimeFrameTime overTimeFrameTime = new OverTimeFrameTime(new OverTimeFrameNo(overTimeHourSet.getOtFrameNo().v()),
				    												TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				    												TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				    												new AttendanceTime(0),
				    												new AttendanceTime(0));

		
		
		return new OverTimeFrameTimeSheetForCalc(new TimeZoneRounding(timeSpan.getStart(),timeSpan.getEnd(),overTimeHourSet.getTimezone().getRounding()),
											  	timeSpan,
											  	dedTimeSheet.stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
											  	recordTimeSheet.stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
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
     * @param prioritySet
     * @return
     */
    public static List<OverTimeFrameTimeSheetForCalc> diciaionCalcStatutory(LegalOTSetting statutorySet,DailyTime dailyTime,List<OverTimeFrameTimeSheetForCalc> overTimeWorkFrameTimeSheetList
                                                                    		,AutoCalOvertimeSetting autoCalculationSet,BreakDownTimeDay breakdownTimeDay,
                                                                    		List<OverTimeOfTimeZoneSet> overTimeHourSetList,DailyUnit dailyUnit,HolidayCalcMethodSet holidayCalcMethodSet, WithinWorkTimeSheet createWithinWorkTimeSheet, 
                                                                    		VacationClass vacationClass, TimevacationUseTimeOfDaily timevacationUseTimeOfDaily, 
                                                                    		WorkType workType, PredetermineTimeSetForCalc predetermineTimeSet, Optional<WorkTimeCode> siftCode, Optional<PersonalLaborCondition> personalCondition, 
                                                                    		boolean late, boolean leaveEarly, WorkingSystem workingSystem, WorkDeformedLaborAdditionSet illegularAddSetting, WorkFlexAdditionSet flexAddSetting, 
                                                                    		WorkRegularAdditionSet regularAddSetting, HolidayAddtionSet holidayAddtionSet) {
        if(statutorySet.isLegal()) {//statutorySet.isLegal()) {
            /*振替処理   法定内基準時間を計算する*/
        	AttendanceTime workTime = new AttendanceTime(0);
        	if(createWithinWorkTimeSheet != null)
        	{
        		workTime = createWithinWorkTimeSheet.calcWorkTime(PremiumAtr.RegularWork, 
        														  CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME, 
        														  vacationClass, 
        														  timevacationUseTimeOfDaily, 
        														  StatutoryDivision.Nomal, 
        														  workType, 
        														  predetermineTimeSet, 
        														  siftCode, 
        														  personalCondition, 
        														  true, 
        														  true, 
        														  workingSystem, 
        														  illegularAddSetting, 
        														  flexAddSetting, 
        														  regularAddSetting, 
        														  holidayAddtionSet, 
        														  holidayCalcMethodSet);
        				
        				
        	}
        	AttendanceTime ableRangeTime = new AttendanceTime(dailyUnit.getDailyTime().valueAsMinutes() - workTime.valueAsMinutes());
        	if(ableRangeTime.greaterThan(0))
        		return reclassified(ableRangeTime,overTimeWorkFrameTimeSheetList.stream()
        									   								    .filter(tc -> tc.getPayOrder().isPresent())
        									   								    .sorted((first,second) -> first.getPayOrder().get().compareTo(second.getPayOrder().isPresent()
        									   								    																?second.getPayOrder().get()
        									   								    																:new SettlementOrder(99)
        									   								    																))
        									   								    .collect(Collectors.toList()),
        									   								    autoCalculationSet,
        									   								    overTimeHourSetList,
        									   								    holidayCalcMethodSet);
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
		for(int number = 0; number < overTimeWorkFrameTimeSheetList.size(); number++) {
			overTime = overTimeWorkFrameTimeSheetList.get(number).correctCalculationTime(Optional.of(forceAtr),autoCalculationSet,DeductionAtr.Deduction);
//			if(forceCalcTime.get()) {
//				if(!decisionCalcAtr(overTimeWorkFrameTimeSheetList.get(number),autoCalculationSet)) {
//					overTime = new AttendanceTime(0);
//				}
//			}
//			else {
//				overTime = new AttendanceTime(0);
//			}
			//振替できる時間計算
			if(ableRangeTime.greaterThan(overTime)) {
				transTime = overTime;
			}
			else {
				transTime = ableRangeTime;
			}
			
			if(transTime.lessThanOrEqualTo(0))
				break;
			
			//振替時間を調整時間に入れる
			if(overTimeWorkFrameTimeSheetList.get(number).adjustTime.isPresent()) {
				overTimeWorkFrameTimeSheetList.get(number).adjustTime = Optional.of(overTimeWorkFrameTimeSheetList.get(number).adjustTime.get().addMinutes(transTime.valueAsMinutes()));
			}
			else {
				overTimeWorkFrameTimeSheetList.get(number).adjustTime = Optional.of(transTime);
			}

			//終了時間の判断
			TimeWithDayAttr endTime = reCreateSiteiTimeFromStartTime(transTime,overTimeWorkFrameTimeSheetList.get(number));
			
			/*ここで分割*/
			overTimeWorkFrameTimeSheetList = correctTimeSpan(overTimeWorkFrameTimeSheetList.get(number).splitTimeSpan(endTime,overTimeHourSetList),overTimeWorkFrameTimeSheetList,number);
			ableRangeTime = ableRangeTime.minusMinutes(transTime.valueAsMinutes()) ; 
		}
		return overTimeWorkFrameTimeSheetList;
	}
	
	/**
	 * 開始から指定時間経過後の終了時刻を取得
	 * @param transTime
	 * @return
	 */
	public static TimeWithDayAttr reCreateSiteiTimeFromStartTime(AttendanceTime transTime,OverTimeFrameTimeSheetForCalc overTimeWork) {
		return overTimeWork.reCreateTreatAsSiteiTimeEnd(transTime,overTimeWork).getEnd();
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
		originList = originList.stream()
							   .filter(tc -> tc.getPayOrder().isPresent())
							   .sorted((first,second) -> first.getPayOrder().get().compareTo(second.getPayOrder().isPresent()
		    																				 ?second.getPayOrder().get()
		    																				 :new SettlementOrder(99)))
							   .collect(Collectors.toList());
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
        else {
        	//控除の控除時間帯
        	val beforeRec = this.getRecordedTimeSheet().stream()
        										.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).isPresent())
        										.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).get())
        										.collect(Collectors.toList());
        	//計上の控除時間帯
        	val beforeDed = this.getDeductionTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(this.calcrange.getStart(), baseTime)).get())
												.collect(Collectors.toList());
        	//深夜時間帯
        	Optional<MidNightTimeSheetForCalc> beforeMid = this.getMidNightTimeSheet().isPresent()
        													?this.getMidNightTimeSheet().get().getDuplicateRangeTimeSheet(new TimeSpanForCalc(this.calcrange.getStart(), baseTime))
        													:Optional.empty();
        	
            returnList.add(new OverTimeFrameTimeSheetForCalc(new TimeZoneRounding(this.calcrange.getStart(), baseTime, this.timeSheet.getRounding())
                                                         ,new TimeSpanForCalc(this.calcrange.getStart(), baseTime)
                                                         ,beforeRec
                                                         ,beforeDed
                                                         ,Collections.emptyList()
                                                         ,Collections.emptyList()
                                                         ,beforeMid
                                                         ,this.getFrameTime().changeFrameNo(statutoryOverFrameNo.isPresent()?statutoryOverFrameNo.get().getLegalOTframeNo().v():this.getFrameTime().getOverWorkFrameNo().v())
                                                         ,StatutoryAtr.Statutory
                                                         ,this.goEarly
                                                         ,this.getOverTimeWorkSheetNo()
                                                         ,this.asTreatBindTime
                                                         ,this.payOrder
                                                         ,this.getAdjustTime()));
            
        	//控除の控除時間帯
        	val afterRec = this.getRecordedTimeSheet().stream()
        										.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).isPresent())
        										.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).get())
        										.collect(Collectors.toList());
        	//計上の控除時間帯
        	val afterDed = this.getDeductionTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForCalc(baseTime, this.calcrange.getEnd())).get())
												.collect(Collectors.toList());
        	//深夜時間帯
        	Optional<MidNightTimeSheetForCalc> afterMid = this.getMidNightTimeSheet().isPresent()
        													?this.getMidNightTimeSheet().get().getDuplicateRangeTimeSheet(new TimeSpanForCalc(baseTime, this.calcrange.getEnd()))
        													:Optional.empty();
            
            returnList.add(new OverTimeFrameTimeSheetForCalc(new TimeZoneRounding(baseTime, this.calcrange.getEnd(), this.timeSheet.getRounding())
                                                          ,new TimeSpanForCalc(baseTime, this.calcrange.getEnd())
                                                         ,afterRec
                                                         ,afterDed
                                                         ,Collections.emptyList()
                                                         ,Collections.emptyList()
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
		//区分をみて、計算設定を設定
		//一旦、打刻から計算する場合　を入れとく
		//val forceAtr = AutoCalAtrOvertime.CALCULATEMBOSS;
		AttendanceTime calcTime = overTimeCalculationByAdjustTime(dedAtr);
		//if(!forceAtr.isCalculateEmbossing()) {
		//	calcTime = new AttendanceTime(0);
		//}
		return calcTime;
	}
	
	/**
	 * 調整時間を考慮した時間の計算
	 * @param autoCalcSet 時間外の自動計算区分
	 * @return 残業時間枠時間帯クラス
	 */
	public AttendanceTime overTimeCalculationByAdjustTime(DeductionAtr dedAtr) {
		//調整時間を加算
		this.calcrange.getEnd().forwardByMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes());
		AttendanceTime time = this.afterMinusDeductionTime(dedAtr);
		//調整時間を減算(元に戻す)
		this.calcrange.getEnd().backByMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes());
		
		return time;
		
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

}
