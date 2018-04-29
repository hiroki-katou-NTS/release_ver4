package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.ConstraintTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.OutingFrameNo;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfLeaveEarlySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalcSetOfDivergenceTime;
import nts.uk.ctx.at.record.dom.calculationattribute.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.HolidayTimesheetCalculationSetting;
import nts.uk.ctx.at.record.dom.calculationattribute.OvertimeTimesheetCalculationSetting;
import nts.uk.ctx.at.record.dom.calculationattribute.WorkingTimesheetCalculationSetting;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.AutoCalOverTimeAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.DivergenceTimeAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.LeaveAttr;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AbsenceOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AnnualOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.OverSalaryOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SpecialHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SubstituteHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.TimeDigestOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.YearlyReservedOfDaily;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectBreakTimeOfDailyDomainService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck.CalculationErrorCheckService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ootsuka.OotsukaProcessService;
//import nts.uk.ctx.at.record.dom.dailyprocess.calc.ootsuka.OotsukaProcessService;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTimeOfDaily;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethod;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfEachPremiumHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.DailyStatutoryWorkingHours;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.workrule.specific.CalculateOfTotalConstraintTime;
import nts.uk.ctx.at.record.dom.workrule.specific.SpecificWorkRuleRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HourlyPaymentAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.employment.EmploymentContractHistory;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalFlexOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.personallaborcondition.UseAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.OneDayTime;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndAggregateFrameSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfExcessHoliday;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfExcessSpecialHoliday;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfStatutoryHoliday;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfWeekDay;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.GetOfStatutoryWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodExceededPredAddVacation;
import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodNoBreak;
import nts.uk.ctx.at.shared.dom.worktime.common.CommonRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedRestCalculateMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OTFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.RestClockManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.RestTimeOfficeWorkCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.ExceededPredAddVacationCalc;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkCalcSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.OverTimeCalcNoBreak;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowFixedRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowFixedRestSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetermineTime;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class CalculateDailyRecordServiceImpl implements CalculateDailyRecordService{

	@Inject
	private WorkTypeRepository workTypeRepository;
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSetRepository;

	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;
	@Inject
	private GetOfStatutoryWorkTime getOfStatutoryWorkTime;
	@Inject
	private WorkInformationRepository workInformationRepository;
	@Inject
	private FixedWorkSettingRepository fixedWorkSettingRepository;
	@Inject
	private FlowWorkSettingRepository flowWorkSettingRepository;
	@Inject
	private DiffTimeWorkSettingRepository diffTimeWorkSettingRepository;
	@Inject
	private FlexWorkSettingRepository flexWorkSettingRepository;

	@Inject
	private DailyRecordToAttendanceItemConverter dailyRecordToAttendanceItemConverter;
	@Inject
	private CompensLeaveComSetRepository compensLeaveComSetRepository;
	@Inject 
	private DivergenceTimeRepository divergenceTimeRepository;

	@Inject
	private OotsukaProcessService ootsukaProcessService;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private CalculationErrorCheckService calculationErrorCheckService;
	
	@Inject
	private HolidayAddtionRepository holidayAddtionRepository;
	
	@Inject
	private DailyStatutoryWorkingHours dailyStatutoryWorkingHours;
	
	@Inject
	private ReflectBreakTimeOfDailyDomainService reflectBreakTimeOfDailyDomainService;
	
	@Inject
	private SpecificWorkRuleRepository specificWorkRuleRepository;
	
	
	/**
	 * 勤務情報を取得して計算
	 * @return 日別実績(Work)
	 */
	@Override
	public IntegrationOfDaily calculate(IntegrationOfDaily integrationOfDaily) {
		// /*日別実績(Work)の退避*/
		// val copyIntegrationOfDaily = integrationOfDaily;
		if (integrationOfDaily.getAffiliationInfor() == null)
			return integrationOfDaily;
		// 実績データの計算
		val afterCalcResult = this.calcDailyAttendancePerformance(integrationOfDaily);
		//エラーチェック
		return calculationErrorCheckService.errorCheck(afterCalcResult);
		//return afterCalcResult;
	}

	private IntegrationOfDaily calcDailyAttendancePerformance(IntegrationOfDaily integrationOfDaily) {
		val copyCalcAtr = integrationOfDaily.getCalAttr();
		val record = createRecord(integrationOfDaily);
		if (!record.calculatable) {
			integrationOfDaily.setCalAttr(copyCalcAtr);
			return integrationOfDaily;
		}
		val test = calcRecord(record);
		test.setCalAttr(copyCalcAtr);
		return test;
	}

	/**
	 * 実績データから時間帯の作成
	 * 
	 * @param companyId
	 *            会社ID
	 * @param employeeId
	 *            社員コード
	 * @param targetDate
	 *            対象日
	 * @param integrationOfDaily
	 */
	private ManageReGetClass createRecord(IntegrationOfDaily integrationOfDaily) {
		String companyId = AppContexts.user().companyId();
		String placeId = integrationOfDaily.getAffiliationInfor().getWplID();
		String employmentCd = integrationOfDaily.getAffiliationInfor().getEmploymentCode().toString();
		String employeeId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd(); 
		
		/*日別実績(Work)の退避*/
		val copyIntegrationOfDaily = integrationOfDaily;


		/* 1日の計算範囲クラスを作成 */
		val oneRange = createOneDayRange(integrationOfDaily);
		/* 勤務種類の取得 */
		val workInfo = integrationOfDaily.getWorkInformation();
		Optional<WorkType> workType = this.workTypeRepository.findByPK(companyId,workInfo.getRecordInfo().getWorkTypeCode().v()); // 要確認：勤務種類マスタが削除されている場合は考慮しない？
	
		
		if(!workType.isPresent()) return ManageReGetClass.cantCalc();
		val beforeWorkType = workType;
		
		/*就業時間帯勤務区分*/
		//Optional<WorkTimeSetting> workTime = workTimeSettingRepository.findByCode(companyId,//"901"); 
		if(workInfo == null || workInfo.getRecordInfo() == null || workInfo.getRecordInfo().getWorkTimeCode() == null)
			return ManageReGetClass.cantCalc();
		Optional<WorkTimeSetting> workTime = workTimeSettingRepository.findByCode(companyId,workInfo.getRecordInfo().getWorkTimeCode().toString());
		if(!workTime.isPresent()) return ManageReGetClass.cantCalc();
		/* 労働制 */
		DailyCalculationPersonalInformation personalInfo = getPersonInfomation(integrationOfDaily);
		/**
		 * 勤務種類が休日系なら、所定時間の時間を変更する
		 */
		if (workType.get().getAttendanceHolidayAttr().equals(AttendanceHolidayAttr.HOLIDAY)) {
			oneRange.getPredetermineTimeSetForCalc().endTimeSetStartTime();
			
		}
		
		/*法定労働時間(日単位)*/
		val dailyUnit = dailyStatutoryWorkingHours.getDailyUnit(companyId, employmentCd, employeeId, targetDate, personalInfo.getWorkingSystem());
		
		/*休憩時間帯（遅刻早退用）*/
		 List<TimeSheetOfDeductionItem> breakTimeList = new ArrayList<>();
		 Optional<BreakTimeOfDailyPerformance> test = reflectBreakTimeOfDailyDomainService.getBreakTime(companyId, employeeId, targetDate,integrationOfDaily.getWorkInformation());
		if(test != null) {
			if(test.isPresent()) {
				breakTimeList = test.get().changeAllTimeSheetToDeductionItem();
			}
		}
		else {
			test = Optional.empty();
		}
		

	
		/*各加算設定取得用*/
		Map<String, AggregateRoot> map = holidayAddtionRepository.findByCompanyId(companyId);
		
		//---------------------------------Repositoryが整理されるまでの一時的な作成-------------------------------------------
		//休憩時間帯(BreakManagement)
		List<BreakTimeSheet> breakTimeSheet = new ArrayList<>();
		if(!integrationOfDaily.getBreakTime().isEmpty()) {
			if(!integrationOfDaily.getBreakTime().get(0).getBreakTimeSheets().isEmpty()) {
				breakTimeSheet.addAll(integrationOfDaily.getBreakTime().get(0).getBreakTimeSheets());
			}
		}
		List<BreakTimeOfDailyPerformance> breakTimeOfDailyList = new ArrayList<>();
		breakTimeOfDailyList.add(new BreakTimeOfDailyPerformance(employeeId, 
																 BreakType.REFER_WORK_TIME, 
																 breakTimeSheet, 
																 targetDate));

		//外出時間帯
		WorkStamp goOut = new WorkStamp(new TimeWithDayAttr(780),new TimeWithDayAttr(780),new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET);
		WorkStamp back  = new WorkStamp(new TimeWithDayAttr(840),new TimeWithDayAttr(840),new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET);
		List<OutingTimeSheet> outingTimeSheets = new ArrayList<>();
		outingTimeSheets.add(new OutingTimeSheet(new OutingFrameNo(1),
												  Optional.of(new TimeActualStamp(goOut,goOut,1)),
												  new AttendanceTime(0),
												  new AttendanceTime(60),
												  GoingOutReason.PUBLIC,
												  Optional.of(new TimeActualStamp(back, back, 1))
												 ));
		OutingTimeOfDailyPerformance goOutTimeSheetList = new OutingTimeOfDailyPerformance(employeeId,targetDate,outingTimeSheets);
		
		MidNightTimeSheet midNightTimeSheet = new MidNightTimeSheet(companyId, 
																	new TimeWithDayAttr(1320),
																	new TimeWithDayAttr(1740));
		
		//流動勤務の休憩時間帯
		FlowWorkRestTimezone fluRestTime = new FlowWorkRestTimezone(
												  true,
												  new TimezoneOfFixedRestTimeSet(Collections.emptyList()),
												  new FlowRestTimezone(Collections.emptyList(),false,new FlowRestSetting(new AttendanceTime(0), new AttendanceTime(0)))
												  );
		
		//流動固定休憩設定
		FlowFixedRestSet fluidPrefixBreakTimeSet = new FlowFixedRestSet(false,false,false,FlowFixedRestCalcMethod.REFER_MASTER);
		
		//0時跨ぎ計算設定
		OverDayEndCalcSet overDayEndCalcSet = new OverDayEndCalcSet(companyId,
																	UseAtr.NOTUSE,
																	new OverDayEndAggregateFrameSet(Collections.emptyList(),Collections.emptyList(),Collections.emptyList()),
																	new OverDayEndCalcSetOfStatutoryHoliday(UseAtr.NOTUSE,UseAtr.NOTUSE,UseAtr.NOTUSE),
																	new OverDayEndCalcSetOfExcessHoliday(UseAtr.NOTUSE,UseAtr.NOTUSE,UseAtr.NOTUSE),
																	new OverDayEndCalcSetOfExcessSpecialHoliday(UseAtr.NOTUSE,UseAtr.NOTUSE,UseAtr.NOTUSE),
																	new OverDayEndCalcSetOfWeekDay(UseAtr.NOTUSE,UseAtr.NOTUSE,UseAtr.NOTUSE));
		
//		//日別実績の計算区分
//		if(integrationOfDaily.getCalAttr() != null) return ManageReGetClass.cantCalc();
//		//日別実績の計算区分の中身をチェック(マスタができれば消せる？）
//		if((integrationOfDaily.getCalAttr().getRasingSalarySetting() != null)
//			|| (integrationOfDaily.getCalAttr().getOvertimeSetting() != null)
//			|| (integrationOfDaily.getCalAttr().getLeaveEarlySetting() != null)
//			|| (integrationOfDaily.getCalAttr().getHolidayTimeSetting() != null)
//			|| (integrationOfDaily.getCalAttr().getFlexExcessTime() != null)
//			|| (integrationOfDaily.getCalAttr().getDivergenceTime() != null))
//			return ManageReGetClass.cantCalc();
		if(integrationOfDaily.getCalAttr() == null
				|| (integrationOfDaily.getCalAttr().getRasingSalarySetting() == null)
				|| (integrationOfDaily.getCalAttr().getOvertimeSetting() == null)
				|| (integrationOfDaily.getCalAttr().getLeaveEarlySetting() == null)
				|| (integrationOfDaily.getCalAttr().getHolidayTimeSetting() == null)
				|| (integrationOfDaily.getCalAttr().getFlexExcessTime() == null)
				|| (integrationOfDaily.getCalAttr().getDivergenceTime() == null))
		{
			val autoCalcSet = new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT,AutoCalAtrOvertime.CALCULATEMBOSS);
			val calAttr = new CalAttrOfDailyPerformance(employeeId, 
													targetDate,
													new AutoCalFlexOvertimeSetting(autoCalcSet),
													new AutoCalRaisingSalarySetting(true,true),
													new AutoCalRestTimeSetting(autoCalcSet,autoCalcSet),
													new AutoCalOvertimeSetting(autoCalcSet, 
																			   autoCalcSet, 
																			   autoCalcSet, 
																			   autoCalcSet, 
																			   autoCalcSet, 
																			   autoCalcSet),
													new AutoCalOfLeaveEarlySetting(LeaveAttr.USE, LeaveAttr.USE),
													new AutoCalcSetOfDivergenceTime(DivergenceTimeAttr.USE));
			integrationOfDaily.setCalAttr(calAttr);
		}	
		
		//自動計算設定
		CalAttrOfDailyPerformance calcSetinIntegre = integrationOfDaily.getCalAttr();
		
		AutoCalSetting sharedCalcSet = new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS);
		AutoCalOvertimeSetting sharedOtSet = new AutoCalOvertimeSetting(sharedCalcSet,sharedCalcSet, sharedCalcSet, sharedCalcSet, sharedCalcSet, sharedCalcSet);
		
		List<WorkTimezoneOtherSubHolTimeSet> subhol = new ArrayList<>();
		HolidayCalcMethodSet holidayCalcMethodSet = HolidayCalcMethodSet.emptyHolidayCalcMethodSet();
		Optional<FixRestTimezoneSet>fixRestTimeSet = Optional.empty();
		Optional<FixedWorkCalcSetting>ootsukaFixedWorkSet = Optional.empty();
		//---------------------------------Repositoryが整理されるまでの一時的な作成-------------------------------------------
		if (workTime.get().getWorkTimeDivision().getWorkTimeDailyAtr().isFlex()) {
			/* フレックス勤務 */
			val flexWorkSetOpt = flexWorkSettingRepository.find(companyId,workInfo.getRecordInfo().getWorkTimeCode().v());
			AggregateRoot aggregateRoot = map.get("flexWork");
			//フレックス勤務の加算設定
			WorkFlexAdditionSet WorkRegularAdditionSet = aggregateRoot!=null?(WorkFlexAdditionSet)aggregateRoot:null;
			//フレックス勤務の加算設定.休暇の計算方法の設定
			holidayCalcMethodSet = WorkRegularAdditionSet!=null?WorkRegularAdditionSet.getVacationCalcMethodSet():holidayCalcMethodSet;
			/*大塚モード*/
//			workType = Optional.of(ootsukaProcessService.getOotsukaWorkType(workType.get(), oneRange.getAttendanceLeavingWork()));
			workType = Optional.of(ootsukaProcessService.getOotsukaWorkType(workType.get(), ootsukaFixedWorkSet, oneRange.getAttendanceLeavingWork(),flexWorkSetOpt.get().getCommonSetting().getHolidayCalculation()));
			//出退勤削除
			if(!ootsukaProcessService.decisionOotsukaMode(workType.get(), ootsukaFixedWorkSet, oneRange.getAttendanceLeavingWork(),flexWorkSetOpt.get().getCommonSetting().getHolidayCalculation())
				&& workType.get().getDailyWork().isHolidayType()) {
				WorkStamp attendance = new WorkStamp(new TimeWithDayAttr(0),new TimeWithDayAttr(0), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
				WorkStamp leaving = new WorkStamp(new TimeWithDayAttr(0),new TimeWithDayAttr(0), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
				TimeActualStamp stamp = new TimeActualStamp(attendance,leaving,1);
				TimeLeavingWork timeLeavingWork = new TimeLeavingWork(new nts.uk.ctx.at.shared.dom.worktime.common.WorkNo(1),stamp,stamp);
				List<TimeLeavingWork> timeLeavingWorkList = new ArrayList<>();
				timeLeavingWorkList.add(timeLeavingWork);
				TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance = new TimeLeavingOfDailyPerformance(employeeId,new WorkTimes(1),timeLeavingWorkList,targetDate);
				oneRange.setAttendanceLeavingWork(timeLeavingOfDailyPerformance);
			}
				
				
			/*前日の勤務情報取得  */
			WorkInfoOfDailyPerformance yestarDayWorkInfo = workInformationRepository.find(employeeId, targetDate.addDays(-1)).orElse(workInfo);
			val yesterDay = this.workTypeRepository.findByPK(companyId, yestarDayWorkInfo.getRecordInfo().getWorkTypeCode().v()).orElse(workType.get());
			/*翌日の勤務情報取得 */
			WorkInfoOfDailyPerformance tomorrowDayWorkInfo = workInformationRepository.find(employeeId, targetDate.addDays(1)).orElse(workInfo);
			val tomorrow = this.workTypeRepository.findByPK(companyId, tomorrowDayWorkInfo.getRecordInfo().getWorkTypeCode().v()).orElse(workType.get());
			
			subhol = flexWorkSetOpt.get().getCommonSetting().getSubHolTimeSet();
				oneRange.createTimeSheetAsFlex(personalInfo.getWorkingSystem(),oneRange.getPredetermineTimeSetForCalc(),
												BonusPaySetting.createFromJavaType(companyId,
														"01"/*ここは聞く*/,
														"テスト加給設定"/*ここは聞く*/,
														Collections.emptyList(),
														Collections.emptyList()
														),
												flexWorkSetOpt.get().getOffdayWorkTime().getLstWorkTimezone(),
											   flexWorkSetOpt.get().getLstHalfDayWorkTimezone().get(0).getWorkTimezone().getLstOTTimezone(),
											   /*休出時間帯リスト*/Collections.emptyList(),overDayEndCalcSet, yesterDay, workType.get(),tomorrow,
											   new BreakDownTimeDay(new AttendanceTime(4),new AttendanceTime(4),new AttendanceTime(8)),
												personalInfo.getStatutoryWorkTime(),calcSetinIntegre.getOvertimeSetting(),LegalOTSetting.LEGAL_INTERNAL_TIME,StatutoryPrioritySet.priorityNormalOverTimeWork,
												workTime.get(),flexWorkSetOpt.get(),goOutTimeSheetList,oneRange.getOneDayOfRange(),oneRange.getAttendanceLeavingWork(),
												workTime.get().getWorkTimeDivision(),breakTimeOfDailyList,midNightTimeSheet,personalInfo,
												holidayCalcMethodSet,
												//new WorkTimeCalcMethodDetailOfHoliday(1,1),
												Optional.of(flexWorkSetOpt.get().getCoreTimeSetting()),
												dailyUnit,breakTimeList);
		} else {
			switch (workTime.get().getWorkTimeDivision().getWorkTimeMethodSet()) {
			case FIXED_WORK:
				/* 固定 */
				val fixedWorkSetting = fixedWorkSettingRepository.findByKey(companyId, workInfo.getRecordInfo().getWorkTimeCode().v());
				List<OverTimeOfTimeZoneSet> fixOtSetting = Collections.emptyList();
				if(workType.get().getAttendanceHolidayAttr().isFullTime()) {
					fixOtSetting = fixedWorkSetting.get().getLstHalfDayWorkTimezone().stream().filter(tc -> tc.getDayAtr().equals(AmPmAtr.ONE_DAY)).findFirst().get().getWorkTimezone().getLstOTTimezone();
				}
				else if(workType.get().getAttendanceHolidayAttr().isMorning()) {
					fixOtSetting = fixedWorkSetting.get().getLstHalfDayWorkTimezone().stream().filter(tc -> tc.getDayAtr().equals(AmPmAtr.AM)).findFirst().get().getWorkTimezone().getLstOTTimezone();
				}
				else if(workType.get().getAttendanceHolidayAttr().isAfternoon()) {
					fixOtSetting = fixedWorkSetting.get().getLstHalfDayWorkTimezone().stream().filter(tc -> tc.getDayAtr().equals(AmPmAtr.PM)).findFirst().get().getWorkTimezone().getLstOTTimezone();
				}
					
				
				AggregateRoot aggregateRoot = map.get("regularWork");
				//通常勤務の加算設定
				WorkRegularAdditionSet WorkRegularAdditionSet = aggregateRoot!=null?(WorkRegularAdditionSet)aggregateRoot:null;
				//通常勤務の加算設定.休暇の計算方法の設定
				holidayCalcMethodSet = WorkRegularAdditionSet!=null?WorkRegularAdditionSet.getVacationCalcMethodSet():holidayCalcMethodSet;
				//パターン１
//				ootsukaFixedWorkSet = Optional.of(new FixedWorkCalcSetting(new ExceededPredAddVacationCalc(CalcMethodExceededPredAddVacation.CALC_AS_WORKING,new OTFrameNo(1)),
//														   new OverTimeCalcNoBreak(CalcMethodNoBreak.CALC_AS_WORKING,new OTFrameNo(2),new OTFrameNo(3))));
				//パターン2
//				ootsukaFixedWorkSet = Optional.of(new FixedWorkCalcSetting(new ExceededPredAddVacationCalc(CalcMethodExceededPredAddVacation.CALC_AS_OVERTIME,new OverTimeFrameNo(1)),
//						   								   new OverTimeCalcNoBreak(CalcMethodNoBreak.CALC_AS_OVERTIME,new OverTimeFrameNo(2),new OverTimeFrameNo(3))));
				
				ootsukaFixedWorkSet = fixedWorkSetting.get().getCalculationSetting();
				
				List<nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime> a = new ArrayList<>();
				if(workType.get().getDailyWork().getAttendanceHolidayAttr().isHoliday()) {
					a = fixedWorkSetting.get().getOffdayWorkTimezone().getRestTimezone().getLstTimezone();
				}
				else {
					if(workType.get().getDailyWork().getWorkTypeUnit().isOneDay()) {
						a = fixedWorkSetting.get().getLstHalfDayWorkTimezone().stream()
								  .filter(tc -> tc.getDayAtr().equals(AmPmAtr.ONE_DAY))
								  .map(tc -> tc.getRestTimezone().getLstTimezone())
								  .flatMap(tc -> tc.stream())
								  .collect(Collectors.toList());
					}
					else {
						a = fixedWorkSetting.get().getLstHalfDayWorkTimezone().stream()
								  .filter(tc -> tc.getDayAtr().equals(AmPmAtr.AM)
										  		 || tc.getDayAtr().equals(AmPmAtr.PM))
								  .map(tc -> tc.getRestTimezone().getLstTimezone())
								  .flatMap(tc -> tc.stream())
								  .collect(Collectors.toList());
					}

				}
				fixRestTimeSet = Optional.of(new FixRestTimezoneSet(a));
						
				/*大塚モード*/
				workType = Optional.of(ootsukaProcessService.getOotsukaWorkType(workType.get(), ootsukaFixedWorkSet, oneRange.getAttendanceLeavingWork(),fixedWorkSetting.get().getCommonSetting().getHolidayCalculation()));
				//出退勤削除
				if(!ootsukaProcessService.decisionOotsukaMode(workType.get(), ootsukaFixedWorkSet, oneRange.getAttendanceLeavingWork(),fixedWorkSetting.get().getCommonSetting().getHolidayCalculation())
					&& workType.get().getDailyWork().isHolidayType()) {
					WorkStamp attendance = new WorkStamp(new TimeWithDayAttr(0),new TimeWithDayAttr(0), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
					WorkStamp leaving = new WorkStamp(new TimeWithDayAttr(0),new TimeWithDayAttr(0), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
					TimeActualStamp stamp = new TimeActualStamp(attendance,leaving,1);
					TimeLeavingWork timeLeavingWork = new TimeLeavingWork(new nts.uk.ctx.at.shared.dom.worktime.common.WorkNo(1),stamp,stamp);
					List<TimeLeavingWork> timeLeavingWorkList = new ArrayList<>();
					timeLeavingWorkList.add(timeLeavingWork);
					TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance = new TimeLeavingOfDailyPerformance(employeeId,new WorkTimes(1),timeLeavingWorkList,targetDate);
					oneRange.setAttendanceLeavingWork(timeLeavingOfDailyPerformance);
				}
				
				/*前日の勤務情報取得  */
				WorkInfoOfDailyPerformance yestarDayWorkInfo = workInformationRepository.find(employeeId, targetDate.addDays(-1)).orElse(workInfo);
				val yesterDay = this.workTypeRepository.findByPK(companyId, yestarDayWorkInfo.getRecordInfo().getWorkTypeCode().v()).orElse(workType.get());
				/*翌日の勤務情報取得 */
				WorkInfoOfDailyPerformance tomorrowDayWorkInfo = workInformationRepository.find(employeeId, targetDate.addDays(1)).orElse(workInfo);
				val tomorrow = this.workTypeRepository.findByPK(companyId, tomorrowDayWorkInfo.getRecordInfo().getWorkTypeCode().v()).orElse(workType.get());
				
				subhol = fixedWorkSetting.get().getCommonSetting().getSubHolTimeSet();
				oneRange.createWithinWorkTimeSheet(personalInfo.getWorkingSystem(),
						workTime.get().getWorkTimeDivision().getWorkTimeMethodSet(), RestClockManageAtr.IS_CLOCK_MANAGE,
						goOutTimeSheetList, new CommonRestSetting(RestTimeOfficeWorkCalcMethod.OFFICE_WORK_APPROP_ALL),
						Optional.of(FixedRestCalculateMethod.MASTER_REF), workTime.get().getWorkTimeDivision(),
						oneRange.getPredetermineTimeSetForCalc(), fixedWorkSetting.get(), 
						BonusPaySetting.createFromJavaType(companyId,
						"01"/*ここは聞く*/,
						"テスト加給設定"/*ここは聞く*/,
						Collections.emptyList(),
						Collections.emptyList()
						)
						,fixOtSetting ,
						fixedWorkSetting.get().getOffdayWorkTimezone().getLstWorkTimezone(), 
						overDayEndCalcSet, 
						Collections.emptyList(), 
						yesterDay, 
						workType.get(),
						tomorrow, 
						oneRange.getPredetermineTimeSetForCalc().getAdditionSet().getPredTime(),
						personalInfo.getStatutoryWorkTime(), 
						calcSetinIntegre.getOvertimeSetting(), 
						LegalOTSetting.LEGAL_INTERNAL_TIME,
						StatutoryPrioritySet.priorityNormalOverTimeWork, 
						workTime.get(),
						breakTimeOfDailyList,
						midNightTimeSheet,
						personalInfo,
						Optional.empty(),
						holidayCalcMethodSet,
						dailyUnit,
						breakTimeList
						);
				//大塚モードの判定(緊急対応)
				if(ootsukaProcessService.decisionOotsukaMode(workType.get(), ootsukaFixedWorkSet, oneRange.getAttendanceLeavingWork(),fixedWorkSetting.get().getCommonSetting().getHolidayCalculation()))
					oneRange.cleanLateLeaveEarlyTimeForOOtsuka();
				break;
			case FLOW_WORK:
				/* 流動勤務 */
				val flowWorkSetOpt = flowWorkSettingRepository.find(companyId,
						workInfo.getRecordInfo().getWorkTimeCode().v());
				break;
			case DIFFTIME_WORK:
				/* 時差勤務 */
				val diffWorkSetOpt = diffTimeWorkSettingRepository.find(companyId,
						workInfo.getRecordInfo().getWorkTimeCode().v());
				// case Enum_Overtime_Work:
				break;
			default:
				throw new RuntimeException(
						"unknown workTimeMethodSet" + workTime.get().getWorkTimeDivision().getWorkTimeMethodSet());
			}
		}
		//beforeWorkType,workType
		return ManageReGetClass.canCalc(oneRange, integrationOfDaily, workTime,beforeWorkType , subhol, personalInfo ,dailyUnit ,fixRestTimeSet,ootsukaFixedWorkSet,holidayCalcMethodSet);
	}

	/**
	 * 作成した時間帯から時間を計算する
	 * 
	 * @param integrationOfDaily
	 *            日別実績(WORK)
	 * @return 日別実績(WORK)
	 */
	private IntegrationOfDaily calcRecord(ManageReGetClass manageReGetClass) {
		String companyId = AppContexts.user().companyId();
		String placeId = manageReGetClass.getIntegrationOfDaily().getAffiliationInfor().getWplID();
		String employmentCd = manageReGetClass.getIntegrationOfDaily().getAffiliationInfor().getEmploymentCode()
				.toString();
		String employeeId = manageReGetClass.getIntegrationOfDaily().getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = manageReGetClass.getIntegrationOfDaily().getAffiliationInfor().getYmd();

		/* 日別実績(Work)の退避 */
		val copyIntegrationOfDaily = dailyRecordToAttendanceItemConverter
				.setData(manageReGetClass.getIntegrationOfDaily()).toDomain();

		// 日別実績の自動計算設定
		CalAttrOfDailyPerformance calcAtrOfDaily = manageReGetClass.getIntegrationOfDaily().getCalAttr();

		// 残業の自動計算設定
		AutoCalOvertimeSetting sharedOtSet = calcAtrOfDaily.getOvertimeSetting();

		// 休出の自動計算設定
		AutoCalRestTimeSetting holidayAutoCalcSetting = calcAtrOfDaily.getHolidayTimeSetting();
		// 遅刻早退の自動計算設定
		AutoCalOfLeaveEarlySetting lateLeave = new AutoCalOfLeaveEarlySetting(LeaveAttr.USE, LeaveAttr.USE);
		// 加給の自動計算設定
		AutoCalRaisingSalarySetting autoRaisingSet = calcAtrOfDaily.getRasingSalarySetting();
		//加給時間計算設定
		BonusPayAutoCalcSet bonusPayAutoCalcSet = new BonusPayAutoCalcSet(new CompanyId(companyId),
																		   1,
																		   WorkingTimesheetCalculationSetting.CalculateAutomatic,
																		   OvertimeTimesheetCalculationSetting.CalculateAutomatic,
																		   HolidayTimesheetCalculationSetting.CalculateAutomatical);
		
		/*各加算設定取得用*/
		Map<String, AggregateRoot> map = holidayAddtionRepository.findByCompanyId(companyId);
		AggregateRoot workRegularAdditionSet = map.get("regularWork");
		AggregateRoot workFlexAdditionSet = map.get("flexWork");
		AggregateRoot hourlyPaymentAdditionSet =  map.get("hourlyPaymentAdditionSet");
		AggregateRoot workDeformedLaborAdditionSet =  map.get("irregularWork");
		
		//通常勤務の加算設定
		WorkRegularAdditionSet regularAddSetting = workRegularAdditionSet!=null?(WorkRegularAdditionSet)workRegularAdditionSet:null;
		//フレックス勤務の加算設定
		WorkFlexAdditionSet flexAddSetting = workFlexAdditionSet!=null?(WorkFlexAdditionSet)workFlexAdditionSet:null;
		//変形労働勤務の加算設定
		WorkDeformedLaborAdditionSet illegularAddSetting = workDeformedLaborAdditionSet!=null?(WorkDeformedLaborAdditionSet)workDeformedLaborAdditionSet:null;
		//時給者の加算設定
		HourlyPaymentAdditionSet hourlyPaymentAddSetting = hourlyPaymentAdditionSet!=null?(HourlyPaymentAdditionSet)hourlyPaymentAdditionSet:null;

//		// 変形労働勤務の加算設定
//		WorkTimeCalcMethodDetailOfHoliday workDetailSet = new WorkTimeCalcMethodDetailOfHoliday(
//				nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To,
//				new IncludeHolidaysWorkCalcDetailSet(
//						nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To));
//		WorkTimeCalcMethodOfHoliday workTimeClacMethodOfHoliday = new WorkTimeCalcMethodOfHoliday(
//				CalculationByActualTimeAtr.CalculationByActualTime, workDetailSet);
//		PremiumCalcMethodDetailOfHoliday preDetailSet = new PremiumCalcMethodDetailOfHoliday(
//				new IncludeHolidaysPremiumCalcDetailSet(
//						nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To),
//				nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To);
//		PremiumCalcMethodOfHoliday premiumCalcMethodOfHoliday = new PremiumCalcMethodOfHoliday(preDetailSet,
//				CalculationByActualTimeAtr.CalculationByActualTime);
//		HolidayCalcMethodSet holidaycalcMethodSet = new HolidayCalcMethodSet(workTimeClacMethodOfHoliday,
//				premiumCalcMethodOfHoliday);
//		val illegularAddSetting = new AddSettingOfIrregularWork(new CompanyId(companyId), holidaycalcMethodSet);
//		// フレックス勤務の加算設定
//		AddSettingOfFlexWork flexAddSetting = new AddSettingOfFlexWork(new CompanyId(companyId), holidaycalcMethodSet);
//		// 固定勤務の加算設定
//		AddSettingOfRegularWork regularAddSetting = new AddSettingOfRegularWork(new CompanyId(companyId),
//				holidaycalcMethodSet);

		// 個人労働条件
		PersonalLaborCondition personalLabor = new PersonalLaborCondition(
				manageReGetClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc().getAdditionSet());
		// 休暇クラス
		VacationClass vacation = new VacationClass(new HolidayOfDaily(new AbsenceOfDaily(new AttendanceTime(0)),
				new TimeDigestOfDaily(new AttendanceTime(0), new AttendanceTime(0)),
				new YearlyReservedOfDaily(new AttendanceTime(0)),
				new SubstituteHolidayOfDaily(new AttendanceTime(0), new AttendanceTime(0)),
				new OverSalaryOfDaily(new AttendanceTime(0), new AttendanceTime(0)),
				new SpecialHolidayOfDaily(new AttendanceTime(0), new AttendanceTime(0)),
				new AnnualOfDaily(new AttendanceTime(0), new AttendanceTime(0))));
		
		Optional<SettingOfFlexWork> flexCalcMethod = Optional.of(new SettingOfFlexWork(new FlexCalcMethodOfHalfWork(new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.OneDay, FlexCalcMethod.OneDay),
																													new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.OneDay, FlexCalcMethod.OneDay))));
		
	    //日別実績の入退門　　　　
		Optional<AttendanceLeavingGateOfDaily> attendanceLeavingGateOfDaily = manageReGetClass.getIntegrationOfDaily().getAttendanceLeavingGate();
		//日別実績のPCログオン情報　　　
		Optional<PCLogOnInfoOfDaily> pCLogOnInfoOfDaily = manageReGetClass.getIntegrationOfDaily().getPcLogOnInfo();
		//日別実績の出退勤
		Optional<TimeLeavingOfDailyPerformance> attendanceLeave = manageReGetClass.getIntegrationOfDaily().getAttendanceLeave();
		//総拘束時間の計算
//		CalculateOfTotalConstraintTime calculateOfTotalConstraintTime = new CalculateOfTotalConstraintTime(new CompanyId(companyId),CalculationMethodOfConstraintTime.REQUEST_FROM_ENTRANCE_EXIT);
		Optional<CalculateOfTotalConstraintTime> optionalCalculateOfTotalConstraintTime = specificWorkRuleRepository.findCalcMethodByCid(companyId);
		if(!optionalCalculateOfTotalConstraintTime.isPresent()) {
			throw new BusinessException(new RawErrorMessage("総拘束時間の計算が存在しません"));
		}
		CalculateOfTotalConstraintTime calculateOfTotalConstraintTime = optionalCalculateOfTotalConstraintTime.get();
		
		//会社別代休設定取得

		val compensLeaveComSet = compensLeaveComSetRepository.find(companyId);
		List<CompensatoryOccurrenceSetting> eachCompanyTimeSet = new ArrayList<>();
		if(compensLeaveComSet != null)
			eachCompanyTimeSet = compensLeaveComSet.getCompensatoryOccurrenceSetting();
 
		//-------------------------計算用一時的クラス作成----------------------------
		
		/*計画所定算出のため、計画側の所定時間取得*/
		val scheWorkTime = manageReGetClass.getIntegrationOfDaily().getWorkInformation().getScheduleInfo().getWorkTimeCode();
				//workingConditionItemRepository.getBySidAndStandardDate(employeeId, targetDate);
		Optional<PredetermineTimeSetForCalc> schePreTimeSet = Optional.empty();
		if(scheWorkTime != null) {
			val schePreTime = predetemineTimeSetRepository.findByWorkTimeCode(companyId, scheWorkTime.toString());
			if(schePreTime.isPresent()) {
				schePreTimeSet = Optional.of(PredetermineTimeSetForCalc.convertMastarToCalc(schePreTime.get()));
			}
		}
		
		Optional<WorkTimeDailyAtr> workTimeDailyAtr = Optional.empty();
		if(manageReGetClass.getIntegrationOfDaily().getWorkInformation().getRecordInfo().getWorkTimeCode() != null) {
			val workTimeSetting = workTimeSettingRepository.findByCode(companyId,manageReGetClass.getIntegrationOfDaily().getWorkInformation().getRecordInfo().getWorkTimeCode().toString());
			workTimeDailyAtr = workTimeSetting.isPresent()?Optional.of(workTimeSetting.get().getWorkTimeDivision().getWorkTimeDailyAtr()):Optional.empty();
		}
		
		val workType = manageReGetClass.getWorkType();
		if(!workType.isPresent() || !workTimeDailyAtr.isPresent()) return manageReGetClass.getIntegrationOfDaily();
		//予定時間帯が作成されるまでの一時対応
		val scheWorkTypeCode = manageReGetClass.getCalculationRangeOfOneDay().getWorkInformationOfDaily().getScheduleInfo().getWorkTypeCode();
		Optional<WorkType> scheWorkType = Optional.empty();
		if(scheWorkTypeCode != null)
			 scheWorkType = workTypeRepository.findByPK(companyId, scheWorkTypeCode.toString()); 
		
		//休暇加算時間設定
		Optional<HolidayAddtionSet> holidayAddtionSetting = holidayAddtionRepository.findByCId(companyId);
		if(!holidayAddtionSetting.isPresent()) {
			throw new BusinessException(new RawErrorMessage("休暇加算時間設定が存在しません"));
		}
		HolidayAddtionSet holidayAddtionSet = holidayAddtionSetting.get();
		
//		VacationAddTimeSet vacationAddSetting = new VacationAddTimeSet(new BreakDownTimeDay(manageReGetClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc().getAdditionSet().getPredTime().getOneDay(), 
//																							manageReGetClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc().getAdditionSet().getPredTime().getMorning(),
//																							manageReGetClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc().getAdditionSet().getPredTime().getAfternoon()),
//																	   new AddVacationSet(nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To,
//																			   			  nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To,
//																			   			  nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To));
		//乖離時間(AggregateRoot)取得
		List<DivergenceTime> divergenceTimeList = divergenceTimeRepository.getAllDivTime(companyId);
		
		//乖離時間計算用　勤怠項目ID紐づけDto作成
		DailyRecordToAttendanceItemConverter forCalcDivergenceDto = this.dailyRecordToAttendanceItemConverter.setData(copyIntegrationOfDaily);
		
		//休憩回数
		int breakCount = 0;
		if(!manageReGetClass.getIntegrationOfDaily().getBreakTime().isEmpty())
			breakCount = manageReGetClass.getIntegrationOfDaily().getBreakTime().get(0).getBreakTimeSheets().size(); 
		/*時間の計算*/
		manageReGetClass.setIntegrationOfDaily(AttendanceTimeOfDailyPerformance.calcTimeResult(manageReGetClass.getCalculationRangeOfOneDay(),
																				 manageReGetClass.getIntegrationOfDaily(),
																				 sharedOtSet,
																				 holidayAutoCalcSetting.getRestTime(),
					Optional.empty(),//Optional.of(personalLabor),
				    vacation,
				    workType.get(),
				    lateLeave.getLeaveLate().isUse(),  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
				    lateLeave.getLeaveEarly().isUse(),  //日別実績の計算区分.遅刻早退の自動計算設定.早退
				    manageReGetClass.getPersonalInfo().getWorkingSystem(),
				    illegularAddSetting,
				    flexAddSetting,
				    regularAddSetting,
				    holidayAddtionSet,
				    AutoCalOverTimeAttr.CALCULATION_FROM_STAMP,
				    workTimeDailyAtr,
				    flexCalcMethod,
				    manageReGetClass.getHolidayCalcMethodSet(),
				    autoRaisingSet,
					bonusPayAutoCalcSet,
					calcAtrOfDaily,
					manageReGetClass.getSubHolTransferSetList(),
					eachCompanyTimeSet,
					attendanceLeavingGateOfDaily,
					pCLogOnInfoOfDaily,
					attendanceLeave,
					forCalcDivergenceDto,
					divergenceTimeList,
					calculateOfTotalConstraintTime, 
					schePreTimeSet,
					manageReGetClass.getOotsukaFixedWorkSet(),
					manageReGetClass.getFixRestTimeSetting(),
					scheWorkType,
					manageReGetClass.getIntegrationOfDaily().getCalAttr().getFlexExcessTime(),
					manageReGetClass.getDailyUnit(),
					breakCount
					));
//					schePreTimeSet));
	
	//  // 編集状態を取得（日別実績の編集状態が持つ勤怠項目IDのみのList作成）
		  List<Integer> attendanceItemIdList = manageReGetClass.getIntegrationOfDaily().getEditState().stream().filter(editState -> editState.getEmployeeId().equals(copyIntegrationOfDaily.getAffiliationInfor().getEmployeeId())
		       && editState.getYmd().equals(copyIntegrationOfDaily.getAffiliationInfor().getYmd()))
		        .map(editState -> editState.getAttendanceItemId())
		        .distinct()
		        .collect(Collectors.toList());

		  IntegrationOfDaily calcResultIntegrationOfDaily = manageReGetClass.getIntegrationOfDaily();  
		  if(!attendanceItemIdList.isEmpty()) {
		   DailyRecordToAttendanceItemConverter beforDailyRecordDto = this.dailyRecordToAttendanceItemConverter.setData(copyIntegrationOfDaily); 
		   List<ItemValue> itemValueList = beforDailyRecordDto.convert(attendanceItemIdList);  
		   DailyRecordToAttendanceItemConverter afterDailyRecordDto = this.dailyRecordToAttendanceItemConverter.setData(manageReGetClass.getIntegrationOfDaily()); 
		   afterDailyRecordDto.merge(itemValueList);
		   //手修正された項目の値を計算前に戻す   
		   calcResultIntegrationOfDaily = afterDailyRecordDto.toDomain();
		   
		   calcResultIntegrationOfDaily = reCalc(calcResultIntegrationOfDaily, companyId);
		   
		  }
		  
			
		/*日別実績への項目移送*/
		//return integrationOfDaily;
		return calcResultIntegrationOfDaily;
	}


	private IntegrationOfDaily reCalc(IntegrationOfDaily calcResultIntegrationOfDaily,String companyId) {
		//乖離時間(AggregateRoot)取得
		List<DivergenceTime> divergenceTimeList = divergenceTimeRepository.getAllDivTime(companyId);
		
		if(calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {
			
			AttendanceTimeOfDailyPerformance attendanceTimeOfDailyPerformance = calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get();
			
			ActualWorkingTimeOfDaily actualWorkingTimeOfDaily = attendanceTimeOfDailyPerformance.getActualWorkingTimeOfDaily();
			
			calcResultIntegrationOfDaily.setAttendanceTimeOfDailyPerformance(Optional.of(
						attendanceTimeOfDailyPerformance.inssertActualWorkingTimeOfDaily(
								actualWorkingTimeOfDaily.inssertTotalWorkingTime(
										actualWorkingTimeOfDaily.getTotalWorkingTime().calcDiverGenceTime()))));

		}
		
		
		//乖離時間計算用　勤怠項目ID紐づけDto作成
		DailyRecordToAttendanceItemConverter forCalcDivergenceDto = this.dailyRecordToAttendanceItemConverter.setData(calcResultIntegrationOfDaily);
		
		if(calcResultIntegrationOfDaily != null && calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {
			val reCalcDivergence = ActualWorkingTimeOfDaily.createDivergenceTimeOfDaily(calcResultIntegrationOfDaily.getAffiliationInfor().getEmployeeId(),
																												 calcResultIntegrationOfDaily.getAffiliationInfor().getYmd(),
																												 calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime(),
																												 calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getConstraintDifferenceTime(),
																												 calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getConstraintTime(),
																												 calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTimeDifferenceWorkingHours(),
																												 calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getPremiumTimeOfDailyPerformance(),
																											     forCalcDivergenceDto,
																											     divergenceTimeList,
																											     calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getWorkScheduleTimeOfDaily());
			val reCreateActual = ActualWorkingTimeOfDaily.of(calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getConstraintDifferenceTime(),
												 			 calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getConstraintTime(),
												 			 calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTimeDifferenceWorkingHours(),
												 			 calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime(),
												 			 reCalcDivergence,
												 			 calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getPremiumTimeOfDailyPerformance());
			val reCreateAttendanceTime = new AttendanceTimeOfDailyPerformance(calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getEmployeeId(),
																			  calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getYmd(),
																			  calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getWorkScheduleTimeOfDaily(),
																			  reCreateActual,
																			  calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getStayingTime(),
																			  calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getUnEmployedTime(),
																			  calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getBudgetTimeVariance(),
																			  calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getMedicalCareTime());
			calcResultIntegrationOfDaily.setAttendanceTimeOfDailyPerformance(Optional.of(reCreateAttendanceTime));
		}
		return calcResultIntegrationOfDaily;
	}

	/**
	 * １日の範囲クラス作成
	 * 
	 * @param companyId
	 *            会社コード
	 * @param employeeId
	 *            社員ID
	 * @param targetDate
	 *            対象日
	 * @param integrationOfDaily
	 *            日別実績(Work)
	 * @return 1日の計算範囲
	 */
	private CalculationRangeOfOneDay createOneDayRange(IntegrationOfDaily integrationOfDaily) {
		String companyId = AppContexts.user().companyId();
		String placeId = integrationOfDaily.getAffiliationInfor().getWplID();
		String employmentCd = integrationOfDaily.getAffiliationInfor().getEmploymentCode().toString();
		String employeeId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd(); 
		/*所定時間設定取得*/
		//val predetermineTimeSet = predetemineTimeSetRepository.findByWorkTimeCode(companyId,"901");// integrationOfDaily.getWorkInformation().getRecordWorkInformation().getWorkTimeCode().toString());
		Optional<PredetemineTimeSetting> predetermineTimeSet = Optional.empty();
		String workTimeCode = null;
		if(integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTimeCode() != null) {
			predetermineTimeSet = predetemineTimeSetRepository.findByWorkTimeCode(companyId,integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTimeCode().toString());
			workTimeCode = integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTimeCode().toString();
		}
		
		if(!predetermineTimeSet.isPresent()) {
			predetermineTimeSet = Optional.of(new PredetemineTimeSetting(companyId,
															new AttendanceTime(0),
															new WorkTimeCode(workTimeCode),
															new PredetermineTime(new BreakDownTimeDay(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)),
																	  			 new BreakDownTimeDay(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0))),
															false,
															new PrescribedTimezoneSetting(new TimeWithDayAttr(0),new TimeWithDayAttr(0),Collections.emptyList()),
															new TimeWithDayAttr(0),
															false));
					
		}
		/*1日の計算範囲取得*/
		val calcRangeOfOneDay = new TimeSpanForCalc(predetermineTimeSet.get().getStartDateClock()
												   ,predetermineTimeSet.get().getStartDateClock().forwardByMinutes(predetermineTimeSet.get().getRangeTimeDay().valueAsMinutes()));
		
		WorkInfoOfDailyPerformance toDayWorkInfo = integrationOfDaily.getWorkInformation();
		
		/*ジャストタイムの判断するための設定取得*/
//		boolean justLate        = /*就業時間帯から固定・流動・フレックスの設定を取得してくるロジック*/;
//		boolean justEarlyLeave  = /*就業時間帯から固定・流動・フレックスの設定を取得してくるロジック*/;
//		/*日別実績の出退勤時刻セット*/
		Optional<TimeLeavingOfDailyPerformance> timeLeavingOfDailyPerformance = integrationOfDaily.getAttendanceLeave();
		if(!timeLeavingOfDailyPerformance.isPresent()) {
			//TimeLeavingOfDailyPerformance attendanceLeavingOfDaily = timeLeavingOfDailyPerformanceRepository.
			WorkStamp attendance = new WorkStamp(new TimeWithDayAttr(0),new TimeWithDayAttr(0), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
			WorkStamp leaving = new WorkStamp(new TimeWithDayAttr(0),new TimeWithDayAttr(0), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
			TimeActualStamp stamp = new TimeActualStamp(attendance,leaving,1);
			TimeLeavingWork timeLeavingWork = new TimeLeavingWork(new nts.uk.ctx.at.shared.dom.worktime.common.WorkNo(1),stamp,stamp);
			List<TimeLeavingWork> timeLeavingWorkList = new ArrayList<>();
			timeLeavingWorkList.add(timeLeavingWork);
			timeLeavingOfDailyPerformance = Optional.of(new TimeLeavingOfDailyPerformance(employeeId,new WorkTimes(1),timeLeavingWorkList,targetDate)); 
		}
		return new CalculationRangeOfOneDay(Finally.empty(),  
											Finally.empty(),
											calcRangeOfOneDay,
											timeLeavingOfDailyPerformance.get(),/*出退勤*/
											PredetermineTimeSetForCalc.convertMastarToCalc(predetermineTimeSet.get())/*所定時間帯(計算用)*/,
											Finally.of(new TimevacationUseTimeOfDaily(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0))),
											toDayWorkInfo);
	}
	/**
	 * 労働制を取得する
	 * @return 日別計算用の個人情報
	 */
	private DailyCalculationPersonalInformation getPersonInfomation(IntegrationOfDaily integrationOfDaily) {
		String companyId = AppContexts.user().companyId();
		String placeId = integrationOfDaily.getAffiliationInfor().getWplID();
		String employmentCd = integrationOfDaily.getAffiliationInfor().getEmploymentCode().toString();
		String employeeId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd();
		
		// ドメインモデル「個人労働条件」を取得する
		Optional<WorkingConditionItem> personalLablorCodition = workingConditionItemRepository.getBySidAndStandardDate(employeeId,targetDate);
		
		if (!personalLablorCodition.isPresent()) {
			throw new RuntimeException("Can't get WorkingSystem");
		}
		// 労働制
		return getOfStatutoryWorkTime.getDailyTimeFromStaturoyWorkTime(
				personalLablorCodition.get().getLaborSystem(), companyId, placeId, employmentCd, employeeId,
				targetDate);
	}
}
