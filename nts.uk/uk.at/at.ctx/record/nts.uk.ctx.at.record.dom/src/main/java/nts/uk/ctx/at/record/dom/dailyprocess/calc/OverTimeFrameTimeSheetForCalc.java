package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfOverTime;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.SettlementOrder;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
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
	 * @return
	 */
	public static List<OverTimeFrameTimeSheetForCalc> createOverWorkFrame(List<OverTimeOfTimeZoneSet> overTimeHourSetList,WorkingSystem workingSystem,
												TimeLeavingWork attendanceLeave,int workNo,
												BreakDownTimeDay breakdownTimeDay,DailyTime dailyTime,AutoCalOvertimeSetting autoCalculationSet,
												LegalOTSetting statutorySet,StatutoryPrioritySet prioritySet ,BonusPaySetting bonusPaySetting,MidNightTimeSheet midNightTimeSheet,
												DailyCalculationPersonalInformation personalInfo,boolean isCalcWithinOverTime,DeductionTimeSheet deductionTimeSheet) {
		List<OverTimeFrameTimeSheetForCalc> createTimeSheet = new ArrayList<>();
		
		for(OverTimeOfTimeZoneSet overTimeHourSet:overTimeHourSetList) {
			
			Optional<TimeSpanForCalc> calcrange = overTimeHourSet.getTimezone().getDuplicatedWith(attendanceLeave.getTimespan());
			if(calcrange.isPresent()) {
				createTimeSheet.add(OverTimeFrameTimeSheetForCalc.createOverWorkFramTimeSheet(overTimeHourSet,calcrange.get(),bonusPaySetting,midNightTimeSheet,deductionTimeSheet));
			}
																					  
		}
//		/*変形残業　振替*/
		List<OverTimeFrameTimeSheetForCalc> afterVariableWork = new ArrayList<>();
		afterVariableWork = dicisionCalcVariableWork(createTimeSheet,breakdownTimeDay,autoCalculationSet,personalInfo,isCalcWithinOverTime,overTimeHourSetList);
		afterVariableWork.stream().sorted((first,second) -> first.calcrange.getStart().compareTo(second.calcrange.getStart()));
//		/*法定内残業　振替*/
		List<OverTimeFrameTimeSheetForCalc> afterCalcStatutoryOverTimeWork = new ArrayList<>();
		afterCalcStatutoryOverTimeWork = diciaionCalcStatutory(statutorySet ,dailyTime ,afterVariableWork,autoCalculationSet,breakdownTimeDay,overTimeHourSetList);
		
		/*return*/
		//return afterCalcStatutoryOverTimeWork;
		return afterCalcStatutoryOverTimeWork;
	}

	/**
	 * 残業枠時間帯の作成
	 * @param overTimeHourSet 残業時間の時間帯設定
	 * @param timeSpan 計算範囲
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
		val duplibonusPayTimeSheet = getDuplicatedBonusPay(bonusPaySetting.getLstBonusPayTimesheet().stream().map(tc ->BonusPayTimeSheetForCalc.convertForCalc(tc)).collect(Collectors.toList()),
													  	   timeSpan);
											 
		/*特定日*/
		val duplispecifiedBonusPayTimeSheet = getDuplicatedSpecBonusPay(bonusPaySetting.getLstSpecBonusPayTimesheet().stream().map(tc -> SpecBonusPayTimeSheetForCalc.convertForCalc(tc)).collect(Collectors.toList()),
																   		timeSpan);
		/*深夜*/
		val duplicatemidNightTimeSheet = getDuplicateMidNight(midNightTimeSheet,
														 	  timeSpan);
		
		return new OverTimeFrameTimeSheetForCalc(new TimeZoneRounding(timeSpan.getStart(),timeSpan.getEnd(),overTimeHourSet.getTimezone().getRounding()),
											  	timeSpan,
											  	dedTimeSheet.stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
											  	recordTimeSheet.stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
											  	duplibonusPayTimeSheet,
											  	duplispecifiedBonusPayTimeSheet,
											  	duplicatemidNightTimeSheet,
											  	new OverTimeFrameTime(new OverTimeFrameNo(overTimeHourSet.getOtFrameNo().v()),
													  					TimeWithCalculation.sameTime(new AttendanceTime(0)),
													  					TimeWithCalculation.sameTime(new AttendanceTime(0)),
													  					new AttendanceTime(0),
													  					new AttendanceTime(0)),
											  	StatutoryAtr.Statutory,
											  	false,
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
			boolean isCalcWithinOverTime,List<OverTimeOfTimeZoneSet> overTimeHourSetList) {
		List<OverTimeFrameTimeSheetForCalc> returnList = createTimeSheet;
		//変形労働か
		if(personalInfo.getWorkingSystem().isVariableWorkingTimeWork()) {
			//変形基準内残業計算するか
			if(isCalcWithinOverTime) {
				//変形できる時間計算
				//480　＝　法定労働
				//breakdownTimeDay = 所定内時間
				AttendanceTime ableRangeTime = new AttendanceTime(480 - breakdownTimeDay.getPredetermineWorkTime());
				if(ableRangeTime.greaterThan(0))
					returnList.addAll(reclassified(ableRangeTime, createTimeSheet, autoCalculationSet,Optional.of(true),overTimeHourSetList));
			}
		}
		return returnList;
	}

    /**
     * 法定内残業時間の計算をするか判定
     * @param statutoryOverWorkSet 法定内残業設定クラス
     * @param statutorySet
     * @param dailyTime 法定労働時間
     * @param overTimeWorkFrameTimeSheetList
     * @param autoCalculationSet
     * @param prioritySet
     * @return
     */
    public static List<OverTimeFrameTimeSheetForCalc> diciaionCalcStatutory(LegalOTSetting statutorySet,DailyTime dailyTime,List<OverTimeFrameTimeSheetForCalc> overTimeWorkFrameTimeSheetList
                                                                    		,AutoCalOvertimeSetting autoCalculationSet,BreakDownTimeDay breakdownTimeDay,List<OverTimeOfTimeZoneSet> overTimeHourSetList) {
    	List<OverTimeFrameTimeSheetForCalc> returnList = new ArrayList<>();
        if(statutorySet.isLegal()) {
            /*振替処理   法定内基準時間を計算する*/
        	AttendanceTime ableRangeTime = new AttendanceTime(480 - breakdownTimeDay.getPredetermineWorkTime());
        	if(ableRangeTime.greaterThan(0))
        		returnList.addAll(reclassified(ableRangeTime,
        									   overTimeWorkFrameTimeSheetList.stream()
        									   								 .filter(tc -> tc.getPayOrder().isPresent())
        									   								 .sorted((first,second) -> first.getPayOrder().get().compareTo(second.getPayOrder().get()))
        									   								 .collect(Collectors.toList()),
        									   autoCalculationSet,
        									   Optional.of(true),
        									   overTimeHourSetList));
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
			AutoCalOvertimeSetting autoCalculationSet,Optional<Boolean> forceCalcTime,
																   List<OverTimeOfTimeZoneSet> overTimeHourSetList) {
		AttendanceTime overTime = new AttendanceTime(0);
		AttendanceTime transTime = new AttendanceTime(0);
		for(int number = 0; number < overTimeWorkFrameTimeSheetList.size(); number++) {
			overTime = overTimeWorkFrameTimeSheetList.get(number).correctCalculationTime(Optional.of(true),autoCalculationSet,DeductionAtr.Deduction);
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
			ableRangeTime.minusMinutes(transTime.valueAsMinutes()) ; 
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
		return originList;
	}
	
	
	/**
	 * 計算区分の判定処理
	 * @return 打刻から計算する
	 */
	public static boolean decisionCalcAtr(OverTimeFrameTimeSheetForCalc overTimeWorkFrameTimeSheet,AutoCalOvertimeSetting autoCalculationSet) {
		if(overTimeWorkFrameTimeSheet.getWithinStatutryAtr().isStatutory()) {
			if(overTimeWorkFrameTimeSheet.isGoEarly()) {
				/*早出残業区分を参照*/
				return autoCalculationSet.getEarlyOtTime().getCalAtr().isCalculateEmbossing();
			}
			else {
				/*普通残業計算区分を参照*/
				return autoCalculationSet.getNormalOtTime().getCalAtr().isCalculateEmbossing();
			}
		}
		else {
			/*法定内の場合*/
			return autoCalculationSet.getLegalOtTime().getCalAtr().isCalculateEmbossing();
		}
	}

    /**
     * 時間帯の分割
     * @return
     */
    public List<OverTimeFrameTimeSheetForCalc> splitTimeSpan(TimeWithDayAttr baseTime,List<OverTimeOfTimeZoneSet> overTimeHourSetList){
        List<OverTimeFrameTimeSheetForCalc> returnList = new ArrayList<>();
        if(this.calcrange.getEnd().equals(baseTime)) {
            returnList.add(this);
        }
        else {
        	val statutoryOverFrameNo = overTimeHourSetList.stream()
        												  .filter(tc -> tc.getLegalOTframeNo().v().intValue() == this.getFrameTime().getOverWorkFrameNo().v())
        												  .findFirst();
            returnList.add(new OverTimeFrameTimeSheetForCalc(this.timeSheet
                                                         ,new TimeSpanForCalc(this.calcrange.getStart(), baseTime)
                                                         ,this.recreateDeductionItemBeforeBase(baseTime, true,DeductionAtr.Appropriate)
                                                         ,this.recreateDeductionItemBeforeBase(baseTime, true,DeductionAtr.Deduction )
                                                         ,this.recreateBonusPayListBeforeBase(baseTime, true)
                                                         ,this.recreateSpecifiedBonusPayListBeforeBase(baseTime, true)
                                                         ,this.recreateMidNightTimeSheetBeforeBase(baseTime, true)
                                                         ,this.getFrameTime().changeFrameNo(statutoryOverFrameNo.isPresent()?statutoryOverFrameNo.get().getLegalOTframeNo().v():this.getFrameTime().getOverWorkFrameNo().v())
                                                         ,StatutoryAtr.DeformationCriterion
                                                         ,this.goEarly
                                                         ,this.getOverTimeWorkSheetNo()
                                                         ,this.asTreatBindTime
                                                         ,this.payOrder
                                                         ,this.getAdjustTime()));
            
            returnList.add(new OverTimeFrameTimeSheetForCalc(this.timeSheet
                                                          ,new TimeSpanForCalc(baseTime, this.calcrange.getEnd())
                                                         ,this.recreateDeductionItemBeforeBase(baseTime, false, DeductionAtr.Appropriate)
                                                         ,this.recreateDeductionItemBeforeBase(baseTime, false, DeductionAtr.Deduction)
                                                         ,this.recreateBonusPayListBeforeBase(baseTime, false)
                                                         ,this.recreateSpecifiedBonusPayListBeforeBase(baseTime, false)
                                                         ,this.recreateMidNightTimeSheetBeforeBase(baseTime, false)
                                                         ,this.getFrameTime()
                                                         ,StatutoryAtr.DeformationCriterion
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
		
		//計算処理
		AttendanceTime time = new AttendanceTime(this.calcrange.lengthAsMinutes()
								 -calcBreakTime.valueAsMinutes()
								 -calcUnionGoOutTime.valueAsMinutes()
								 -calcPrivateGoOutTime.valueAsMinutes()
								 -calcCareTime.valueAsMinutes()
								 -calcChildTime.valueAsMinutes());
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
