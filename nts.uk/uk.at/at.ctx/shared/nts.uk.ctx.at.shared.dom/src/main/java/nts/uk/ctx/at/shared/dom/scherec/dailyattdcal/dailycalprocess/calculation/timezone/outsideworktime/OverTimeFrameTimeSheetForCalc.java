package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.outsideworktime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrame;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.AddSettingOfWorkingTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.ActualWorkTimeSheetAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.paytime.SpecificDateAttrOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workingstyle.flex.SettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.ActualWorkingTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.DeductionTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.ManagePerCompanySet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.ManagePerPersonDailySet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.SpecBonusPayTimeSheetForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.TimeSpanForDailyCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.MidNightTimeSheetForCalcList;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.deductiontime.StaggerDiductionTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.deductiontime.TimeSheetOfDeductionItem;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.someitems.BonusPayTimeSheetForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.withinworkinghours.WithinWorkTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.midnighttimezone.MidNightTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.roleofovertimework.roleofovertimework.RoleOvertimeWorkEnum;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.IntegrationOfWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.HolidayCalculation;
import nts.uk.ctx.at.shared.dom.worktime.common.OTFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.SettlementOrder;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneGoOutSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowOTTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * ??????????????????(WORK)
 * @author keisuke_hoshina
 */
@Getter
public class OverTimeFrameTimeSheetForCalc extends ActualWorkingTimeSheet {
	
	//?????????
	private OverTimeFrameTime frameTime;
	
	//???????????????
	private  StatutoryAtr withinStatutryAtr;
	
	//????????????
	private boolean goEarly;
	
	//???????????????No
	private EmTimezoneNo overTimeWorkSheetNo;
	
	//???????????????????????????
	private boolean asTreatBindTime;
	
	//????????????
	private Optional<SettlementOrder> payOrder;

	//????????????
	private Optional<AttendanceTime> adjustTime;

	//?????????????????????NO
	private Optional<OverTimeFrameNo> tempLegalFrameNo = Optional.empty();
	
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
	public OverTimeFrameTimeSheetForCalc(TimeSpanForDailyCalc timeSheet, TimeRoundingSetting rounding,
			List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
			List<TimeSheetOfDeductionItem> deductionTimeSheets, List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,
			List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet,
			MidNightTimeSheetForCalcList midNighttimeSheet, OverTimeFrameTime frameTime,
			StatutoryAtr withinStatutryAtr, boolean goEarly, EmTimezoneNo overTimeWorkSheetNo, boolean asTreatBindTime,
			Optional<SettlementOrder> payOrder, Optional<AttendanceTime> adjustTime) {
		super(timeSheet, rounding, recorddeductionTimeSheets, deductionTimeSheets, bonusPayTimeSheet,
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
	 * ????????????????????????????????????
	 * @param tempLegalFrameNo ?????????????????????No
	 */
	public OverTimeFrameTimeSheetForCalc(
			TimeSpanForDailyCalc timeSheet,
			TimeRoundingSetting rounding,
			List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
			List<TimeSheetOfDeductionItem> deductionTimeSheets,
			List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,
			List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet,
			MidNightTimeSheetForCalcList midNighttimeSheet,
			OverTimeFrameTime frameTime,
			StatutoryAtr withinStatutryAtr,
			boolean goEarly,
			EmTimezoneNo overTimeWorkSheetNo,
			boolean asTreatBindTime,
			Optional<SettlementOrder> payOrder,
			Optional<AttendanceTime> adjustTime,
			Optional<OverTimeFrameNo> tempLegalFrameNo) {
		this(timeSheet, rounding, recorddeductionTimeSheets, deductionTimeSheets, bonusPayTimeSheet,
				specifiedBonusPayTimeSheet, midNighttimeSheet, frameTime, withinStatutryAtr, goEarly,
				overTimeWorkSheetNo, asTreatBindTime, payOrder, adjustTime);
		this.tempLegalFrameNo = tempLegalFrameNo;
	}
	
	/**
	 * ??????????????????????????????????????????????????????????????????
	 * @return?????????????????????
	 */
	public OverTimeFrameTimeSheet changeNotWorkFrameTimeSheet() {
		return new OverTimeFrameTimeSheet(this.timeSheet, this.frameTime.getOverWorkFrameNo());
	}


	/**
	 * ????????????(??????????????????
	 * @param companyCommonSetting ?????????????????????
	 * @param personDailySetting ??????????????????
	 * @param todayWorkType ????????????
	 * @param integrationOfWorkTime ?????????????????????
	 * @param integrationOfDaily ????????????(Work)
	 * @param predetermineTimeSetForCalc ????????????????????????
	 * @param deductionTimeSheet ???????????????
	 * @param timeLeavingWork ?????????
	 * @param createdWithinWorkTimeSheet ????????????????????????
	 * @param oneDayOfRange 1????????????
	 * @return ??????????????????(WORK)(List)
	 */
	public static List<OverTimeFrameTimeSheetForCalc> createOverWorkFrame(
			ManagePerCompanySet companyCommonSetting,
			ManagePerPersonDailySet personDailySetting,
			WorkType todayWorkType,
			IntegrationOfWorkTime integrationOfWorkTime,
			IntegrationOfDaily integrationOfDaily,
			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			DeductionTimeSheet deductionTimeSheet,
			TimeLeavingWork timeLeavingWork,
			WithinWorkTimeSheet createdWithinWorkTimeSheet,
			TimeSpanForDailyCalc oneDayOfRange) {
		List<OverTimeFrameTimeSheetForCalc> createTimeSheet = new ArrayList<>();
		
		for(OverTimeOfTimeZoneSet overTimeHourSet:integrationOfWorkTime.getOverTimeOfTimeZoneSetList(todayWorkType)) {
			
			Optional<OverTimeFrameTimeSheetForCalc> overTimeFrameTimeSheet =
					OverTimeFrameTimeSheetForCalc.createOverWorkFramTimeSheet(
							overTimeHourSet,
							timeLeavingWork,
							personDailySetting.getBonusPaySetting(),
							companyCommonSetting.getMidNightTimeSheet(),
							deductionTimeSheet,
							Optional.of(integrationOfWorkTime.getCommonSetting()),
							integrationOfDaily.getSpecDateAttr(), companyCommonSetting);
			if (overTimeFrameTimeSheet.isPresent()) {
				createTimeSheet.add(overTimeFrameTimeSheet.get());
			}
		}
		// ???????????????????????????????????????
		createTimeSheet.addAll(OverTimeSheet.getOverTimeSheetFromTemporary(
				companyCommonSetting,
				personDailySetting,
				integrationOfWorkTime,
				integrationOfDaily,
				todayWorkType,
				oneDayOfRange));
		///*?????????????????????*/
		List<OverTimeFrameTimeSheetForCalc> afterVariableWork = new ArrayList<>();
		afterVariableWork = dicisionCalcVariableWork(
				companyCommonSetting,
				personDailySetting,
				todayWorkType,
				integrationOfWorkTime,
				integrationOfDaily,
				predetermineTimeSetForCalc,
				createTimeSheet);
		afterVariableWork = afterVariableWork.stream()
				.sorted((first,second) -> first.getTimeSheet().getStart().compareTo(second.getTimeSheet().getStart()))
				.collect(Collectors.toList());
		///*????????????????????????*/
		List<OverTimeFrameTimeSheetForCalc> afterCalcStatutoryOverTimeWork = new ArrayList<>();
		afterCalcStatutoryOverTimeWork = diciaionCalcStatutory(
				companyCommonSetting,
				personDailySetting,
				todayWorkType,
				integrationOfWorkTime,
				integrationOfDaily,
				predetermineTimeSetForCalc,
				afterVariableWork,
				createdWithinWorkTimeSheet);
		
		/*return*/
		return afterCalcStatutoryOverTimeWork;
	}

	/**
	 * ???????????????????????????
	 * @param overTimeHourSet ??????????????????????????????
	 * @param timeLeavingWork ?????????
	 * @param bonuspaySetting ????????????
	 * @param midNightTimeSheet ???????????????
	 * @param deductTimeSheet ???????????????
	 * @param commonSetting ??????????????????????????????
	 * @param specificDateAttrSheets ???????????????
	 * @return ??????????????????(WORK)
	 */
	public static Optional<OverTimeFrameTimeSheetForCalc> createOverWorkFramTimeSheet(
			OverTimeOfTimeZoneSet overTimeHourSet,
			TimeLeavingWork timeLeavingWork,
			Optional<BonusPaySetting> bonuspaySetting,
			MidNightTimeSheet midNightTimeSheet,
			DeductionTimeSheet deductTimeSheet,
			Optional<WorkTimezoneCommonSet> commonSetting,
			Optional<SpecificDateAttrOfDailyAttd> specificDateAttrSheets, 
			ManagePerCompanySet companyCommonSetting) {
		
		// ?????????????????????
		Optional<TimeSpanForCalc> calcrange = overTimeHourSet.getTimezone().getDuplicatedWith(timeLeavingWork.getTimespan());
		if (!calcrange.isPresent()) return Optional.empty();
		// ????????????????????????
		OverTimeFrameTime overTimeFrameTime = new OverTimeFrameTime(
				new OverTimeFrameNo(overTimeHourSet.getOtFrameNo().v()),
				TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				new AttendanceTime(0),
				new AttendanceTime(0));
		// ???????????????????????????
		OverTimeFrameTimeSheetForCalc overTimeFrameTimeSheet = new OverTimeFrameTimeSheetForCalc(
				new TimeSpanForDailyCalc(calcrange.get()),
				overTimeHourSet.getTimezone().getRounding(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				MidNightTimeSheetForCalcList.createEmpty(),
				overTimeFrameTime,
				getStatutoryAtr(companyCommonSetting.getOvertimeFrameList(), overTimeHourSet.getOtFrameNo()),
				overTimeHourSet.isEarlyOTUse(),
				overTimeHourSet.getWorkTimezoneNo(),
				false,
				Optional.of(overTimeHourSet.getSettlementOrder()),
				Optional.empty());
		// ????????????????????????
		overTimeFrameTimeSheet.registDeductionList(ActualWorkTimeSheetAtr.OverTimeWork, deductTimeSheet, commonSetting);
		// ????????????????????????
		overTimeFrameTimeSheet.createBonusPayTimeSheet(bonuspaySetting, specificDateAttrSheets, deductTimeSheet);;
		// ????????????????????????
		overTimeFrameTimeSheet.createMidNightTimeSheet(midNightTimeSheet, commonSetting, deductTimeSheet);
		
		return Optional.of(overTimeFrameTimeSheet);
	}
	
	//??????????????????????????????
	private static StatutoryAtr getStatutoryAtr(List<OvertimeWorkFrame> overtimeFrameLis, OTFrameNo overtimeNo) {
		return overtimeFrameLis.stream()
				.filter(x -> x.getOvertimeWorkFrNo().v().intValue() == overtimeNo.v().intValue() && x.isUse())
				.findFirst().map(x -> {
					if (x.getRole() == RoleOvertimeWorkEnum.OT_STATUTORY_WORK) {
						return StatutoryAtr.Statutory;
					} else {
						return StatutoryAtr.Excess;
					}
				}).orElse(StatutoryAtr.Excess);
	}
	
	/**
	 * ????????????????????????????????????
	 * @param companyCommonSetting ?????????????????????
	 * @param personDailySetting ??????????????????
	 * @param todayWorkType ????????????
	 * @param integrationOfWorkTime ?????????????????????
	 * @param integrationOfDaily ????????????(Work)
	 * @param predetermineTimeSetForCalc ???????????????????????????
	 * @param createTimeSheet ??????????????????(WORK)(List)
	 * @return ??????????????????(WORK)(List)
	 */
	public static List<OverTimeFrameTimeSheetForCalc> dicisionCalcVariableWork(
			ManagePerCompanySet companyCommonSetting,
			ManagePerPersonDailySet personDailySetting,
			WorkType todayWorkType,
			IntegrationOfWorkTime integrationOfWorkTime,
			IntegrationOfDaily integrationOfDaily,
			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			List<OverTimeFrameTimeSheetForCalc> createTimeSheet) {
		//???????????????
		if(personDailySetting.getPersonInfo().getLaborSystem().isVariableWorkingTimeWork()) {
			//????????????????????????????????????
			if(companyCommonSetting.getDeformLaborOT().isLegalOtCalc()) {
				//???????????????????????????
				AttendanceTime ableRangeTime =
						new AttendanceTime(personDailySetting.getDailyUnit().getDailyTime().valueAsMinutes()
								- predetermineTimeSetForCalc.getAdditionSet().getPredTime().getPredetermineWorkTime());
				if(ableRangeTime.greaterThan(0))
					return reclassified(
							ableRangeTime,
							createTimeSheet,
							integrationOfDaily.getCalAttr().getOvertimeSetting(),
							new HashMap<>(),
							personDailySetting.getAddSetting().getAddSetOfWorkingTime(),
							integrationOfWorkTime.getCommonSetting().getGoOutSet(),
							StatutoryAtr.DeformationCriterion);
			}
		}
		return createTimeSheet;
	}
	
	/**
	 * ????????????????????????????????????????????????
	 * @param companyCommonSetting ?????????????????????
	 * @param personDailySetting ??????????????????
	 * @param todayWorkType ????????????
	 * @param integrationOfWorkTime ?????????????????????
	 * @param integrationOfDaily ????????????(Work)
	 * @param predetermineTimeSetForCalc ???????????????????????????
	 * @param overTimeWorkFrameTimeSheetList ????????????????????????????????????????????????
	 * @param createdWithinWorkTimeSheet ????????????????????????
	 * @param deductTimeSheet ???????????????
	 * @return ??????????????????(WORK)(List)
	 */
	public static List<OverTimeFrameTimeSheetForCalc> diciaionCalcStatutory(
			ManagePerCompanySet companyCommonSetting,
			ManagePerPersonDailySet personDailySetting,
			WorkType todayWorkType,
			IntegrationOfWorkTime integrationOfWorkTime,
			IntegrationOfDaily integrationOfDaily,
			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			List<OverTimeFrameTimeSheetForCalc> overTimeWorkFrameTimeSheetList,
			WithinWorkTimeSheet createdWithinWorkTimeSheet) {
		
		// ??????????????????????????????????????????????????????????????????????????????????????????
		boolean isExistAttendanceLeave = false;
		if (integrationOfDaily.getAttendanceLeave().isPresent()) {
			if (integrationOfDaily.getAttendanceLeave().get().getTimeLeavingWorks().size() > 0)
				isExistAttendanceLeave = true;		// ????????????????????????
		}
		if (!isExistAttendanceLeave) return overTimeWorkFrameTimeSheetList;
		
		if(integrationOfWorkTime.isLegalInternalTime()) {
			/*????????????   ????????????????????????????????????*/
			AttendanceTime workTime = new AttendanceTime(0);
			if(createdWithinWorkTimeSheet != null){
				workTime = WithinStatutoryTimeOfDaily.calcActualWorkTime(
						createdWithinWorkTimeSheet,
						personDailySetting,
						integrationOfDaily,
						Optional.of(integrationOfWorkTime),
						todayWorkType,
						integrationOfDaily.getCalAttr().getLeaveEarlySetting(),
						personDailySetting.getAddSetting(),
						companyCommonSetting.getHolidayAdditionPerCompany().get(),
						Optional.of(SettingOfFlexWork.defaultValue()),
						new AttendanceTime(2880),	//????????????
						predetermineTimeSetForCalc,
						personDailySetting.getDailyUnit(),
						Optional.of(integrationOfWorkTime.getCommonSetting()),
						NotUseAtr.NOT_USE);
			}
			// ????????????????????????????????????????????????????????????????????????????????????????????????
			AttendanceTime ableRangeTime = new AttendanceTime(
					personDailySetting.getDailyUnit().getDailyTime().valueAsMinutes() - workTime.valueAsMinutes());
			
			HolidayCalculation holidayCalculation = integrationOfWorkTime.getCommonSetting().getHolidayCalculation();
			if(ableRangeTime.greaterThan(0))
			{
				boolean isReclass = false;		//?????????????????????????????????
				//?????????????????????????????????
				if (holidayCalculation.getIsCalculate().isNotUse()){
					isReclass = true;
				}
				else{
					//????????????????????????
					boolean isOOtsukaMode = AppContexts.optionLicense().customize().ootsuka();
					//???????????????1????????????????????????????????????
					if (isOOtsukaMode && todayWorkType.getDailyWork().decisionMatchWorkType(WorkTypeClassification.SpecialHoliday).isFullTime()){
						isReclass = false;		//???????????????????????????1???????????????????????????????????????
					}
					else{
						isReclass = true;
					}
				}
				if (isReclass){
					//??????????????????????????????????????????????????????
					List<OverTimeFrameTimeSheetForCalc> result = reclassified(ableRangeTime,overTimeWorkFrameTimeSheetList.stream()
							.filter(tc -> tc.getPayOrder().isPresent())
							.sorted((first,second) -> first.getPayOrder().get().compareTo(second.getPayOrder().isPresent()
									?second.getPayOrder().get()
									:new SettlementOrder(99)))
							.collect(Collectors.toList()),
					integrationOfDaily.getCalAttr().getOvertimeSetting(),
					integrationOfWorkTime.getLegalOverTimeFrameNoMap(todayWorkType),
					personDailySetting.getAddSetting().getAddSetOfWorkingTime(),
					integrationOfWorkTime.getCommonSetting().getGoOutSet(),
					StatutoryAtr.Statutory);
					
					return result;
				}
			}
		}
		return overTimeWorkFrameTimeSheetList;
	}

	/**
	 * ????????????
	 * @param ableRangeTime ?????????????????????
	 * @param overTimeWorkFrameTimeSheetList ?????????????????????????????????
	 * @param autoCalculationSet ??????????????????????????????
	 * @param statutoryOverFrames ???????????????No
	 * @param addSetOfWorkTime ???????????????????????????
	 * @param goOutSet ??????????????????????????????
	 */
	public static List<OverTimeFrameTimeSheetForCalc> reclassified(
			AttendanceTime ableRangeTime,
			List<OverTimeFrameTimeSheetForCalc> overTimeWorkFrameTimeSheetList,
			AutoCalOvertimeSetting autoCalculationSet,
			Map<EmTimezoneNo, OverTimeFrameNo> statutoryOverFrames,
			AddSettingOfWorkingTime addSetOfWorkTime,
			WorkTimezoneGoOutSet goOutSet,
			StatutoryAtr statutoryAtr) {
		boolean forceAtr = true;
		AttendanceTime overTime = new AttendanceTime(0);
		AttendanceTime transTime = new AttendanceTime(0);
		
		for(int number = 0; number < overTimeWorkFrameTimeSheetList.size(); number++) {
			overTime = overTimeWorkFrameTimeSheetList.get(number).correctCalculationTime(Optional.of(forceAtr), autoCalculationSet, Optional.of(goOutSet)).getTime();
			
			if(ableRangeTime.greaterThan(overTime)) {
				transTime = overTime;
			}
			else {
				transTime = ableRangeTime;
			}
			
			if(transTime.lessThanOrEqualTo(0))
				continue;
			
			//???????????????????????????????????????
			if(overTimeWorkFrameTimeSheetList.get(number).adjustTime.isPresent()) {
				overTimeWorkFrameTimeSheetList.get(number).adjustTime = Optional.of(overTimeWorkFrameTimeSheetList.get(number).adjustTime.get().addMinutes(transTime.valueAsMinutes()));
			}
			else {
				overTimeWorkFrameTimeSheetList.get(number).adjustTime = Optional.of(transTime);
			}

			//?????????????????????
			TimeWithDayAttr endTime = overTimeWorkFrameTimeSheetList.get(number).reCreateSiteiTimeFromStartTime(transTime, goOutSet);
			
			/*???????????????*/
			overTimeWorkFrameTimeSheetList = correctTimeSpan(overTimeWorkFrameTimeSheetList.get(number).splitTimeSpan(endTime,statutoryOverFrames, statutoryAtr),overTimeWorkFrameTimeSheetList,number);
			
			ableRangeTime = ableRangeTime.minusMinutes(transTime.valueAsMinutes()) ;
		}
		return overTimeWorkFrameTimeSheetList;
	}
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @param transTime
	 * @param goOutSet
	 * @return
	 */
	public TimeWithDayAttr reCreateSiteiTimeFromStartTime(AttendanceTime transTime, WorkTimezoneGoOutSet goOutSet) {
		TimeWithDayAttr minusTime = new TimeWithDayAttr(this.calcTime(ActualWorkTimeSheetAtr.OverTimeWork, Optional.of(goOutSet)).valueAsMinutes() - transTime.valueAsMinutes());
		return this.contractTimeSheet(minusTime).orElse(this.timeSheet).getEnd();
	}
	
	/**
	 * ???????????????????????????????????????????????????
	 * @param insertList????????????????????????
	 * @param originList??????????????????????????????
	 * @return???
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
				//?????????????????????
				if(returnList.get(index).getPayOrder().get().greaterThan(returnList.get(index + 1).getPayOrder().get())) {
					OverTimeFrameTimeSheetForCalc pary = returnList.get(index);
					returnList.set(index, returnList.get(index + 1));
					returnList.set(index + 1, pary);
					break;
				}
				//????????????(????????????????????????????????????)
				else if(returnList.get(index).getPayOrder().get().equals(returnList.get(index + 1).getPayOrder().get())) {
					if(returnList.get(index).getTimeSheet().getStart().greaterThan(returnList.get(index + 1).getTimeSheet().getStart()) ) {
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
     * ??????????????????
     * @param baseTime 
     * @param statutoryOverFrames Map<???????????????No, ??????????????????No>
     * @return
     */
    public List<OverTimeFrameTimeSheetForCalc> splitTimeSpan(TimeWithDayAttr baseTime, Map<EmTimezoneNo, OverTimeFrameNo> statutoryOverFrames, StatutoryAtr statutoryAtr){
        List<OverTimeFrameTimeSheetForCalc> returnList = new ArrayList<>();

        Optional<OverTimeFrameNo> statutoryOverFrameNo = Optional.ofNullable(statutoryOverFrames.get(this.getOverTimeWorkSheetNo()));
        if (this.tempLegalFrameNo.isPresent()) statutoryOverFrameNo = this.tempLegalFrameNo;
		
        if(this.timeSheet.getEnd().lessThanOrEqualTo(baseTime)) {
            returnList.add(new OverTimeFrameTimeSheetForCalc(this.timeSheet
                    										,new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN) 
                    										,this.recordedTimeSheet
                    										,this.deductionTimeSheet
                    										,this.bonusPayTimeSheet
                    										,this.specBonusPayTimesheet
                    										,this.midNightTimeSheet
                    										,this.getFrameTime().changeFrameNo(statutoryOverFrameNo.isPresent()?statutoryOverFrameNo.get().v():this.getFrameTime().getOverWorkFrameNo().v())
                    										,statutoryAtr
                    										,this.goEarly
                    										,this.getOverTimeWorkSheetNo()
                    										,this.asTreatBindTime
                    										,this.payOrder
                    										,this.getAdjustTime()));
        }
        //??????
        else {
        	/*???????????????????????????*/
        	//????????????????????????
        	val beforeRec = this.getRecordedTimeSheet().stream()
        										.filter(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime)).isPresent())
        										.map(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime)).get())
        										.map(tc -> tc.reCreateOwn(baseTime, true))
        										.collect(Collectors.toList());
        	//????????????????????????
        	val beforeDed = this.getDeductionTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime)).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime)).get())
												.map(tc -> tc.reCreateOwn(baseTime, true))
												.collect(Collectors.toList());
        	val beforeBp = this.getBonusPayTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime)).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime)).get())
												.collect(Collectors.toList());
        	val beforeSpecBp = this.getSpecBonusPayTimesheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime)).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime)).get())
												.collect(Collectors.toList());
			//???????????????
			MidNightTimeSheetForCalcList beforDuplicate = this.getMidNightTimeSheet().getDuplicateRangeTimeSheet(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime));
			MidNightTimeSheetForCalcList beforeMid = beforDuplicate.recreateMidNightTimeSheetBeforeBase(baseTime, true);
        	
            returnList.add(new OverTimeFrameTimeSheetForCalc(new TimeSpanForDailyCalc(this.timeSheet.getStart(), baseTime)
                                                         ,new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN)
                                                         ,beforeRec
                                                         ,beforeDed
                                                         ,beforeBp
                                                         ,beforeSpecBp
                                                         ,beforeMid
                                                         ,this.getFrameTime().changeFrameNo(statutoryOverFrameNo.isPresent()?statutoryOverFrameNo.get().v():this.getFrameTime().getOverWorkFrameNo().v())
                                                         ,statutoryAtr
                                                         ,this.goEarly
                                                         ,this.getOverTimeWorkSheetNo()
                                                         ,this.asTreatBindTime
                                                         ,this.payOrder
                                                         ,Optional.of(new AttendanceTime(0))));
            
            /*?????????*/
        	//????????????????????????
        	val afterRec = this.getRecordedTimeSheet().stream()
        										.filter(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(baseTime, this.timeSheet.getEnd())).isPresent())
        										.map(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(baseTime, this.timeSheet.getEnd())).get())
        										.map(tc -> tc.reCreateOwn(baseTime, false))
        										.collect(Collectors.toList());
        	//????????????????????????
        	val afterDed = this.getDeductionTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(baseTime, this.timeSheet.getEnd())).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(baseTime, this.timeSheet.getEnd())).get())
												.map(tc -> tc.reCreateOwn(baseTime, false))
												.collect(Collectors.toList());
        	val afterBp = this.getBonusPayTimeSheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(baseTime, this.timeSheet.getEnd())).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc( baseTime, this.timeSheet.getEnd())).get())
												.collect(Collectors.toList());
        	val afterSpecBp = this.getSpecBonusPayTimesheet().stream()
												.filter(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc(baseTime, this.timeSheet.getEnd())).isPresent())
												.map(tc -> tc.createDuplicateRange(new TimeSpanForDailyCalc( baseTime, this.timeSheet.getEnd())).get())
												.collect(Collectors.toList());
			//???????????????
			MidNightTimeSheetForCalcList afterDuplicate = this.getMidNightTimeSheet().getDuplicateRangeTimeSheet(new TimeSpanForDailyCalc(baseTime, this.timeSheet.getEnd()));
			MidNightTimeSheetForCalcList afterMid = afterDuplicate.recreateMidNightTimeSheetBeforeBase(baseTime, false);
            
            returnList.add(new OverTimeFrameTimeSheetForCalc(new TimeSpanForDailyCalc(baseTime, this.timeSheet.getEnd())
                                                         ,this.rounding
                                                         ,afterRec
                                                         ,afterDed
                                                         ,afterBp
                                                         ,afterSpecBp
                                                         ,afterMid
                                                         ,this.getFrameTime().changeFrameNo(this.getFrameTime().getOverWorkFrameNo().v())
                                                         ,this.getWithinStatutryAtr()
                                                         ,this.goEarly
                                                         ,this.getOverTimeWorkSheetNo()
                                                         ,this.asTreatBindTime
                                                         ,this.payOrder
                                                         ,this.getAdjustTime()));
        }
        return returnList;
    }

	/**
	 * ????????????
	 * ?????????????????????
	 * @param forceCalcTime ??????????????????
	 * @param autoCalcSet ?????????????????????????????????
	 * @param goOutSet ??????????????????????????????
	 */
	public TimeDivergenceWithCalculation correctCalculationTime(
			Optional<Boolean> forceCalcTime,
			AutoCalOvertimeSetting autoCalcSet,
			Optional<WorkTimezoneGoOutSet> goOutSet) {
		
		//??????????????????????????????
		boolean isCalculateEmboss = false;
		
		if(forceCalcTime.orElse(false)) {
			//??????????????????????????????????????????????????????
			isCalculateEmboss = true;
		}
		else {
			//?????????????????????
			isCalculateEmboss = this.isCalculateEmboss(autoCalcSet);
		}
		//?????????????????????
		AttendanceTime overTime = overTimeCalculationByAdjustTime(goOutSet);
		
		if(isCalculateEmboss) return TimeDivergenceWithCalculation.sameTime(overTime);
		
		return TimeDivergenceWithCalculation.createTimeWithCalculation(AttendanceTime.ZERO, overTime);
	}
	
	/**
	 * ???????????????????????????
	 * @param autoCalcSet ?????????????????????????????????
	 * @return ????????????????????????  true:???????????????????????? false:????????????
	 */
	public boolean isCalculateEmboss(AutoCalOvertimeSetting autoCalcSet) {
		return autoCalcSet.decisionCalcAtr(this.withinStatutryAtr, this.goEarly);
	}
	
	/**
	 * ??????????????????????????????????????????
	 * @param autoCalcSet ??????????????????????????????
	 * @param goOutSet ??????????????????????????????
	 * @return ?????????????????????????????????
	 */
	public AttendanceTime overTimeCalculationByAdjustTime(Optional<WorkTimezoneGoOutSet> goOutSet) {
		//?????????????????????
		this.timeSheet = new TimeSpanForDailyCalc(this.timeSheet.getStart(), this.timeSheet.getEnd().forwardByMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes()));
		AttendanceTime time = this.calcTime(ActualWorkTimeSheetAtr.OverTimeWork, goOutSet);
		//?????????????????????(????????????)
		this.timeSheet = new TimeSpanForDailyCalc(this.timeSheet.getStart(), this.timeSheet.getEnd().backByMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes()));
		time = time.minusMinutes(this.adjustTime.orElse(new AttendanceTime(0)).valueAsMinutes()) ;
		return time;
		
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param flowWorkSetting ??????????????????
	 * @param processingFlowOTTimezone ?????????????????????????????????
	 * @param deductTimeSheet ???????????????
	 * @param itemsWithinCalc ?????????????????????
	 * @param overTimeStartTime ??????????????????
	 * @param leaveStampTime ????????????
	 * @param bonusPaySetting ????????????
	 * @param specDateAttr ??????????????????????????????
	 * @param midNightTimeSheet ???????????????
	 * @return ??????????????????(WORK)
	 */
	public static OverTimeFrameTimeSheetForCalc createAsFlow(
			ManagePerPersonDailySet personDailySetting,
			FlowWorkSetting flowWorkSetting,
			FlowOTTimezone processingFlowOTTimezone,
			DeductionTimeSheet deductTimeSheet,
			List<TimeSheetOfDeductionItem> itemsWithinCalc,
			TimeWithDayAttr overTimeStartTime,
			TimeWithDayAttr leaveStampTime,
			Optional<SpecificDateAttrOfDailyAttd> specDateAttr,
			MidNightTimeSheet midNightTimeSheet) {
		
		// ?????????????????????
		TimeWithDayAttr endTime = OverTimeFrameTimeSheetForCalc.calcEndTimeForFlow(
				processingFlowOTTimezone,
				flowWorkSetting,
				itemsWithinCalc,
				overTimeStartTime,
				leaveStampTime,
				personDailySetting.getAddSetting().getAddSetOfWorkingTime());
		// ????????????????????????
		OverTimeFrameTime frameTime = new OverTimeFrameTime(
				new OverTimeFrameNo(processingFlowOTTimezone.getOTFrameNo().v().intValue()),
				TimeDivergenceWithCalculation.defaultValue(),
				TimeDivergenceWithCalculation.defaultValue(),
				AttendanceTime.ZERO,
				AttendanceTime.ZERO);
		// ??????????????????
		OverTimeFrameTimeSheetForCalc overTimeFrame = new OverTimeFrameTimeSheetForCalc(
				new TimeSpanForDailyCalc(overTimeStartTime, endTime),
				processingFlowOTTimezone.getFlowTimeSetting().getRounding(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				MidNightTimeSheetForCalcList.createEmpty(),
				frameTime,
				StatutoryAtr.Excess,
				false,
				new EmTimezoneNo(processingFlowOTTimezone.getWorktimeNo()),
				false,
				Optional.of(processingFlowOTTimezone.getSettlementOrder()),
				Optional.empty());
		// ????????????????????????
		overTimeFrame.registDeductionList(ActualWorkTimeSheetAtr.OverTimeWork, deductTimeSheet,
				Optional.of(flowWorkSetting.getCommonSetting()));
		// ????????????????????????
		overTimeFrame.createBonusPayTimeSheet(personDailySetting.getBonusPaySetting(), specDateAttr, deductTimeSheet);
		// ????????????????????????
		overTimeFrame.createMidNightTimeSheet(
				midNightTimeSheet, Optional.of(flowWorkSetting.getCommonSetting()), deductTimeSheet);
		
		return overTimeFrame;
	}
	
	/**
	 * ?????????????????????????????????
	 * @param processingFlowOTTimezone ?????????????????????????????????
	 * @param lstOTTimezone ?????????????????????(List)
	 * @param timeSheetOfDeductionItems ????????????????????????(List)
	 * @param overTimeStartTime ??????????????????
	 * @param leaveStampTime ????????????
	 * @return ????????????
	 */
	private static TimeWithDayAttr calcEndTimeForFlow(
			FlowOTTimezone processingFlowOTTimezone,
			FlowWorkSetting flowWorkSetting,
			List<TimeSheetOfDeductionItem> timeSheetOfDeductionItems,
			TimeWithDayAttr overTimeStartTime,
			TimeWithDayAttr leaveStampTime,
			AddSettingOfWorkingTime holidaySet) {
		
		Optional<FlowOTTimezone> plusOneFlowOTTimezone = flowWorkSetting.getHalfDayWorkTimezone().getWorkTimeZone().getLstOTTimezone().stream()
				.filter(timezone -> timezone.getWorktimeNo().equals(processingFlowOTTimezone.getWorktimeNo()+1))
				.findFirst();
		
		TimeWithDayAttr endTime = TimeWithDayAttr.THE_PRESENT_DAY_0000;
		
		if(plusOneFlowOTTimezone.isPresent()) {
			//?????????????????????????????????
			AttendanceTime overTimeFrameTime = plusOneFlowOTTimezone.get().getFlowTimeSetting().getElapsedTime().minusMinutes(
					processingFlowOTTimezone.getFlowTimeSetting().getElapsedTime().valueAsMinutes());
			
			//????????????????????????????????????????????????
			endTime = overTimeStartTime.forwardByMinutes(overTimeFrameTime.valueAsMinutes());
			
			//?????????????????????????????????????????????
			TimeSpanForDailyCalc timeSpan = new TimeSpanForDailyCalc(overTimeStartTime, endTime);
			StaggerDiductionTimeSheet forward = new StaggerDiductionTimeSheet(timeSpan, processingFlowOTTimezone.getFlowTimeSetting().getRounding(), timeSheetOfDeductionItems);
			endTime = forward.getForwardEnd(ActualWorkTimeSheetAtr.OverTimeWork, flowWorkSetting.getCommonSetting(), holidaySet);
			
			//??????input.?????????????????????????????????input.???????????????????????????
			if(timeSpan.shiftOnlyEnd(endTime).contains(leaveStampTime)) endTime = leaveStampTime;
		}
		else {
			endTime = leaveStampTime;
		}
		return endTime;
	}
	
	/**
	 * ????????????????????????????????????????????????
	 * @param calcrange ????????????
	 * @param flowOTTimezone ?????????????????????
	 * @return ??????????????????(WORK)
	 */
	public static OverTimeFrameTimeSheetForCalc createEmpty(TimeSpanForDailyCalc calcrange, FlowOTTimezone flowOTTimezone) {
		return new OverTimeFrameTimeSheetForCalc(
				calcrange,
				new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN),
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList(),
				MidNightTimeSheetForCalcList.createEmpty(),
				new OverTimeFrameTime(
						new OverTimeFrameNo(flowOTTimezone.getOTFrameNo().v().intValue()),
						TimeDivergenceWithCalculation.defaultValue(),
						TimeDivergenceWithCalculation.defaultValue(),
						AttendanceTime.ZERO,
						AttendanceTime.ZERO),
				StatutoryAtr.Excess,
				false,
				new EmTimezoneNo(flowOTTimezone.getWorktimeNo()),
				false,
				Optional.empty(),
				Optional.empty());
	}

	/**
	 * ????????????????????????????????????
	 * @param timeSpan ?????????
	 * @param commonSet ??????????????????????????????
	 * @return ??????????????????
	 */
	public Optional<OverTimeFrameTimeSheetForCalc> recreateWithDuplicate(TimeSpanForDailyCalc timeSpan, Optional<WorkTimezoneCommonSet> commonSet) {
		Optional<TimeSpanForDailyCalc> duplicate = this.timeSheet.getDuplicatedWith(timeSpan);
		if(!duplicate.isPresent()) {
			return Optional.empty();
		}
		OverTimeFrameTimeSheetForCalc recreated = new OverTimeFrameTimeSheetForCalc(
				duplicate.get(),
				this.rounding.clone(),
				this.recordedTimeSheet.stream().map(r -> r.getAfterDeleteOffsetTime()).collect(Collectors.toList()),
				this.deductionTimeSheet.stream().map(d -> d.getAfterDeleteOffsetTime()).collect(Collectors.toList()),
				this.getDuplicatedBonusPayNotStatic(this.bonusPayTimeSheet, duplicate.get()),
				this.getDuplicatedSpecBonusPayzNotStatic(this.specBonusPayTimesheet, duplicate.get()),
				this.midNightTimeSheet.getDuplicateRangeTimeSheet(duplicate.get()),
				this.frameTime.clone(),
				StatutoryAtr.valueOf(this.withinStatutryAtr.toString()),
				this.goEarly,
				new EmTimezoneNo(this.overTimeWorkSheetNo.v().intValue()),
				this.asTreatBindTime,
				this.payOrder.map(p -> new SettlementOrder(p.v())),
				this.adjustTime.map(a -> new AttendanceTime(a.valueAsMinutes())));
		
		//????????????????????????
		recreated.registDeductionList(ActualWorkTimeSheetAtr.OverTimeWork, this.getCloneDeductionTimeSheet(), commonSet);
		
		return Optional.of(recreated);
	}
	
	/**
	 * ??????????????????????????????
	 * @return ??????????????????
	 */
	public OverTimeFrameTimeSheetForCalc getReverseRounding() {
		return new OverTimeFrameTimeSheetForCalc(
				this.timeSheet.clone(),
				this.rounding.getReverseRounding(),
				this.recordedTimeSheet.stream().map(r -> r.clone()).collect(Collectors.toList()),
				this.deductionTimeSheet.stream().map(d -> d.clone()).collect(Collectors.toList()),
				this.bonusPayTimeSheet.stream().map(b -> b.clone()).collect(Collectors.toList()),
				this.specBonusPayTimesheet.stream().map(s -> s.clone()).collect(Collectors.toList()),
				this.midNightTimeSheet.clone(),
				this.frameTime.clone(),
				StatutoryAtr.valueOf(this.withinStatutryAtr.toString()),
				this.goEarly,
				new EmTimezoneNo(this.overTimeWorkSheetNo.v().intValue()),
				this.asTreatBindTime,
				this.payOrder.map(p -> new SettlementOrder(p.v())),
				this.adjustTime.map(a -> new AttendanceTime(a.valueAsMinutes())));
	}
}
