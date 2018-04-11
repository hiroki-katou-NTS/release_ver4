package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.eclipse.persistence.internal.oxm.schema.model.Content;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
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
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SalaryCalAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SpecificSalaryCalAttr;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.LogOnInfo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnNo;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeGoOutTimes;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.latetime.IntervalExemptionTime;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AbsenceOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AnnualOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.OverSalaryOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SpecialHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SubstituteHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.TimeDigestOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.YearlyReservedOfDaily;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck.CalculationErrorCheckService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ootsuka.OotsukaProcessService;
//import nts.uk.ctx.at.record.dom.dailyprocess.calc.ootsuka.OotsukaProcessService;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.workrule.specific.CalculateOfTotalConstraintTime;
import nts.uk.ctx.at.record.dom.workrule.specific.CalculationMethodOfConstraintTime;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkNo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.TimeOfDay;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.employment.EmploymentContractHistory;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalFlexOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.ctx.at.shared.dom.personallaborcondition.UseAtr;
import nts.uk.ctx.at.shared.dom.statutory.worktime.UsageUnitSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.ComRegularLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.ComTransLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employeeNew.ShainRegularWorkTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employeeNew.ShainTransLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpRegularWorkTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpTransWorkTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew.WkpRegularLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew.WkpTransLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfIrregularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfRegularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.CalculationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.IncludeHolidaysPremiumCalcDetailSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.IncludeHolidaysWorkCalcDetailSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.PremiumCalcMethodDetailOfHoliday;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.PremiumCalcMethodOfHoliday;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.WorkTimeCalcMethodDetailOfHoliday;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.WorkTimeCalcMethodOfHoliday;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.AddVacationSet;
import nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.VacationAddTimeSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndAggregateFrameSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfExcessHoliday;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfExcessSpecialHoliday;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfStatutoryHoliday;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfWeekDay;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.GetOfStatutoryWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodExceededPredAddVacation;
import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodNoBreak;
import nts.uk.ctx.at.shared.dom.worktime.common.CommonRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedRestCalculateMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.RestClockManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.RestTimeOfficeWorkCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.ExceededPredAddVacationCalc;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixOffdayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkCalcSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.OverTimeCalcNoBreak;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowFixedRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowFixedRestSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestSet;
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
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDivision;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
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
	private HolidayAddtionRepository holidayAddtionRepository;
	@Inject 
	private DivergenceTimeRepository divergenceTimeRepository;

	@Inject
	private OotsukaProcessService ootsukaProcessService;
	
	@Inject
	private ShainRegularWorkTimeRepository shainRegularWorkTimeRepository;
	
	@Inject
	private ShainTransLaborTimeRepository shainTransLaborTimeRepository;
	
	@Inject
	private WkpRegularLaborTimeRepository wkpRegularLaborTimeRepository;
	
	@Inject
	private WkpTransLaborTimeRepository wkpTransLaborTimeRepository;
	
	@Inject
	private EmpRegularWorkTimeRepository empRegularWorkTimeRepository;
	
	@Inject
	private EmpTransWorkTimeRepository empTransWorkTimeRepository;
	
	@Inject
	private ComRegularLaborTimeRepository comRegularLaborTimeRepository;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private ComTransLaborTimeRepository comTransLaborTimeRepository;
	
	@Inject
	private UsageUnitSettingRepository usageUnitSettingRepository;
	
	@Inject
	private CalculationErrorCheckService calculationErrorCheckService;
	
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
		//return calculationErrorCheckService.errorCheck(afterCalcResult);
		return afterCalcResult;
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
		//val workType = this.workTypeRepository.findByPK(companyId, "001").get();
		Optional<WorkType> workType = this.workTypeRepository.findByPK(companyId,workInfo.getRecordInfo().getWorkTypeCode().v()); // 要確認：勤務種類マスタが削除されている場合は考慮しない？
		if(!workType.isPresent()) return ManageReGetClass.cantCalc();
		
		
		/*就業時間帯勤務区分*/
		//Optional<WorkTimeSetting> workTime = workTimeSettingRepository.findByCode(companyId,//"901"); 
		if(workInfo == null || workInfo.getRecordInfo() == null || workInfo.getRecordInfo().getWorkTimeCode() == null)
			return ManageReGetClass.cantCalc();
		/* 労働制 */
		DailyCalculationPersonalInformation personalInfo = getPersonInfomation(integrationOfDaily);
		/**
		 * 勤務種類が休日系なら、所定時間の時間を変更する
		 */
		if (workType.get().getAttendanceHolidayAttr().equals(AttendanceHolidayAttr.HOLIDAY)) {
			oneRange.getPredetermineTimeSetForCalc().endTimeSetStartTime();
		}
		
//		/*社員別通常勤務労働時間*/
//		val shainRegularLaborTime = shainRegularWorkTimeRepository.find(companyId, employeeId);
//		/*社員別変形労働労働時間*/
//		val shainTransLaborTime = shainTransLaborTimeRepository.find(companyId, employeeId);
//		/*職場別通常勤務労働時間*/
//		val wkpRegularLaborTime = wkpRegularLaborTimeRepository.find(companyId, employeeId);
//		/*職場別変形労働労働時間*/
//		val wkpTransLaborTime = wkpTransLaborTimeRepository.find(companyId, employeeId);
//		/*雇用別通常勤務労働時間*/
//		val empRegularLaborTime = empRegularWorkTimeRepository.findById(companyId, employeeId);
//		/*雇用別変形労働労働時間*/
//		val empTransLaborTime = empTransWorkTimeRepository.find(companyId, employeeId);
//		/*会社別通常勤務労働時間*/
//		val comRegularLaborTime = comRegularLaborTimeRepository.find(companyId);
//		/*会社別変形労働労働時間*/
//		val comTransLaborTime = comTransLaborTimeRepository.find(companyId);
//		/*労働時間と日数の設定の利用単位の設定*/
//		val usageUnitSetting = usageUnitSettingRepository.findByCompany(companyId);
//		/*法定労働時間(日単位)*/
//		val dailyUnit = usageUnitSetting.isPresent()?usageUnitSetting.get().getDailyUnit(personalInfo.getWorkingSystem(), 
//				 shainRegularLaborTime, 
//				 shainTransLaborTime, 
//				 wkpRegularLaborTime, 
//				 wkpTransLaborTime, 
//				 empRegularLaborTime, 
//				 empTransLaborTime, 
//				 comRegularLaborTime, 
//				 comTransLaborTime):new DailyUnit(new TimeOfDay(0));
		/*法定労働時間(日単位)_（仮）*/
		DailyUnit dailyUnit = new DailyUnit(new TimeOfDay(480));
		
		//---------------------------------Repositoryが整理されるまでの一時的な作成-------------------------------------------
		//休憩時間帯(BreakManagement)
		List<BreakTimeSheet> breakTimeSheet = new ArrayList<>();
				breakTimeSheet.addAll(integrationOfDaily.getBreakTime().get(0).getBreakTimeSheets());
			}
		}
		List<BreakTimeOfDailyPerformance> breakTimeOfDailyList = new ArrayList<>();
		breakTimeOfDailyList.add(
				new BreakTimeOfDailyPerformance(employeeId, BreakType.REFER_WORK_TIME, breakTimeSheet, targetDate));

		List<OutingTimeSheet> outingTimeSheets = new ArrayList<>();
		outingTimeSheets.add(new OutingTimeSheet(new OutingFrameNo(1),
				|| (integrationOfDaily.getCalAttr().getRasingSalarySetting() != null)
				|| (integrationOfDaily.getCalAttr().getOvertimeSetting() != null)
				|| (integrationOfDaily.getCalAttr().getLeaveEarlySetting() != null)
				|| (integrationOfDaily.getCalAttr().getHolidayTimeSetting() != null)
				|| (integrationOfDaily.getCalAttr().getFlexExcessTime() != null)
				|| (integrationOfDaily.getCalAttr().getDivergenceTime() != null)) {
			val autoCalcSet = new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT,
					AutoCalAtrOvertime.CALCULATEMBOSS);
			val calAttr = new CalAttrOfDailyPerformance(employeeId, targetDate,
					new AutoCalFlexOvertimeSetting(autoCalcSet), new AutoCalRaisingSalarySetting(true, true),
					new AutoCalRestTimeSetting(autoCalcSet, autoCalcSet),
					new AutoCalOvertimeSetting(autoCalcSet, autoCalcSet, autoCalcSet, autoCalcSet, autoCalcSet,
							autoCalcSet),
					new AutoCalOfLeaveEarlySetting(LeaveAttr.USE, LeaveAttr.USE),
					new AutoCalcSetOfDivergenceTime(DivergenceTimeAttr.USE));
			integrationOfDaily.setCalAttr(calAttr);
		CalAttrOfDailyPerformance calcSetinIntegre = integrationOfDaily.getCalAttr();
		
		
		List<WorkTimezoneOtherSubHolTimeSet> subhol = new ArrayList<>();
		Optional<FixedWorkCalcSetting> ootsukaFixedWorkSet = Optional.empty();
		Optional<TimezoneOfFixedRestTimeSet> fixRestTimeSet = Optional.empty(); 
		//---------------------------------Repositoryが整理されるまでの一時的な作成-------------------------------------------
		if (workTime.get().getWorkTimeDivision().getWorkTimeDailyAtr().isFlex()) {
			/* フレックス勤務 */
			val flexWorkSetOpt = flexWorkSettingRepository.find(companyId,workInfo.getRecordInfo().getWorkTimeCode().v());
			//val flexWork = holidayAddtionRepository.findByCId(employeeId);
			/*大塚モード*/
//			workType = Optional.of(ootsukaProcessService.getOotsukaWorkType(workType.get(), oneRange.getAttendanceLeavingWork()));
			
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
												new WorkTimeCalcMethodDetailOfHoliday(1,1),
//												new WorkTimeCalcMethodDetailOfHoliday(flexWork.isPresent()?flexWork.get().getFlexWork().getNotDeductLateleave2():1,
//																					  flexWork.isPresent()?flexWork.get().getFlexWork().getAdditionTime2():1),
												Optional.of(flexWorkSetOpt.get().getCoreTimeSetting()),
												dailyUnit);
		} else {
			switch (workTime.get().getWorkTimeDivision().getWorkTimeMethodSet()) {
			case FIXED_WORK:
				/* 固定 */
				val fixedWorkSetting = fixedWorkSettingRepository.findByKey(companyId, workInfo.getRecordInfo().getWorkTimeCode().v());
				//val regularWork = holidayAddtionRepository.findByCId(companyId);
				ootsukaFixedWorkSet = Optional.of(new FixedWorkCalcSetting(new ExceededPredAddVacationCalc(CalcMethodExceededPredAddVacation.CALC_AS_WORKING,new OverTimeFrameNo(1)),
														   new OverTimeCalcNoBreak(CalcMethodNoBreak.CALC_AS_WORKING,new OverTimeFrameNo(1),new OverTimeFrameNo(1))));
				/*大塚モード*/
				workType = Optional.of(ootsukaProcessService.getOotsukaWorkType(workType.get(), ootsukaFixedWorkSet, oneRange.getAttendanceLeavingWork()));
				
				
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
						, fixedWorkSetting.get().getLstHalfDayWorkTimezone().get(0).getWorkTimezone().getLstOTTimezone(),
						fixedWorkSetting.get().getOffdayWorkTimezone().getLstWorkTimezone(), 
						overDayEndCalcSet, 
						Collections.emptyList(), 
						yesterDay, 
						workType.get(),
						tomorrow, 
						new BreakDownTimeDay(new AttendanceTime(4),new AttendanceTime(4),new AttendanceTime(8)),
						personalInfo.getStatutoryWorkTime(), 
						calcSetinIntegre.getOvertimeSetting(), 
						fixedWorkSetting.get().getLegalOTSetting(), 
						StatutoryPrioritySet.priorityNormalOverTimeWork, 
						workTime.get(),
						breakTimeOfDailyList,
						midNightTimeSheet,
						personalInfo,
						Optional.empty(),
						new WorkTimeCalcMethodDetailOfHoliday(1,1),
//						new WorkTimeCalcMethodDetailOfHoliday(regularWork.isPresent()?regularWork.get().getRegularWork().getNotDeductLateleave2():1,
//															  regularWork.isPresent()?regularWork.get().getRegularWork().getAdditionTime2():1),
						dailyUnit);
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
		return ManageReGetClass.canCalc(oneRange, integrationOfDaily, workTime, workType, subhol, personalInfo ,dailyUnit ,fixRestTimeSet,ootsukaFixedWorkSet);
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
		// 加給時間計算設定
		BonusPayAutoCalcSet bonusPayAutoCalcSet = new BonusPayAutoCalcSet(new CompanyId(companyId), 1,
				WorkingTimesheetCalculationSetting.CalculateAutomatic,
				OvertimeTimesheetCalculationSetting.CalculateAutomatic,
				HolidayTimesheetCalculationSetting.CalculateAutomatical);

		// 変形労働勤務の加算設定
		WorkTimeCalcMethodDetailOfHoliday workDetailSet = new WorkTimeCalcMethodDetailOfHoliday(
				nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To,
				new IncludeHolidaysWorkCalcDetailSet(
						nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To));
		WorkTimeCalcMethodOfHoliday workTimeClacMethodOfHoliday = new WorkTimeCalcMethodOfHoliday(
				CalculationByActualTimeAtr.CalculationByActualTime, workDetailSet);
		PremiumCalcMethodDetailOfHoliday preDetailSet = new PremiumCalcMethodDetailOfHoliday(
				new IncludeHolidaysPremiumCalcDetailSet(
						nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To),
				nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To);
		PremiumCalcMethodOfHoliday premiumCalcMethodOfHoliday = new PremiumCalcMethodOfHoliday(preDetailSet,
				CalculationByActualTimeAtr.CalculationByActualTime);
		HolidayCalcMethodSet holidaycalcMethodSet = new HolidayCalcMethodSet(workTimeClacMethodOfHoliday,
				premiumCalcMethodOfHoliday);
		val illegularAddSetting = new AddSettingOfIrregularWork(new CompanyId(companyId), holidaycalcMethodSet);
		// フレックス勤務の加算設定
		AddSettingOfFlexWork flexAddSetting = new AddSettingOfFlexWork(new CompanyId(companyId), holidaycalcMethodSet);
		// 固定勤務の加算設定
		AddSettingOfRegularWork regularAddSetting = new AddSettingOfRegularWork(new CompanyId(companyId),
				holidaycalcMethodSet);

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

		//個人労働条件
		//PersonalLaborCondition personalLabor = new PersonalLaborCondition(manageReGetClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc().getAdditionSet());
		//休暇クラス
		VacationClass vacation = new VacationClass(	new HolidayOfDaily(new AbsenceOfDaily(new AttendanceTime(0)), 
				   													   new TimeDigestOfDaily(new AttendanceTime(0),new AttendanceTime(0)), 
				   													   new YearlyReservedOfDaily(new AttendanceTime(0)), 
				   													   new SubstituteHolidayOfDaily(new AttendanceTime(0), new AttendanceTime(0)), 
				   													   new OverSalaryOfDaily(new AttendanceTime(0), new AttendanceTime(0)), 
				   													   new SpecialHolidayOfDaily(new AttendanceTime(0), new AttendanceTime(0)), 
				   													   new AnnualOfDaily(new AttendanceTime(0), new AttendanceTime(0))));
		
		Optional<SettingOfFlexWork> flexCalcMethod = Optional.empty();
		
		
		
//		//高須テスト用--------------------------------------------------------------------------------
//		List<AttendanceLeavingGate> attendanceLeavingGates = new ArrayList<>();
//		attendanceLeavingGates.add(new AttendanceLeavingGate(new nts.uk.ctx.at.shared.dom.worktime.common.WorkNo(1),
//										   new WorkStamp(new TimeWithDayAttr(420),new TimeWithDayAttr(420),new WorkLocationCD(null),StampSourceInfo.TIME_RECORDER),
//										   new WorkStamp(new TimeWithDayAttr(1200),new TimeWithDayAttr(1200),new WorkLocationCD(null),StampSourceInfo.TIME_RECORDER)));
//		attendanceLeavingGates.add(new AttendanceLeavingGate(new nts.uk.ctx.at.shared.dom.worktime.common.WorkNo(2),
//				   new WorkStamp(new TimeWithDayAttr(420),new TimeWithDayAttr(420),new WorkLocationCD(null),StampSourceInfo.TIME_RECORDER),
//				   new WorkStamp(new TimeWithDayAttr(1200),new TimeWithDayAttr(1200),new WorkLocationCD(null),StampSourceInfo.TIME_RECORDER)));
//		//日別実績の入退門　　
//		Optional<AttendanceLeavingGateOfDaily> attendanceLeavingGateOfDaily = Optional.of(new AttendanceLeavingGateOfDaily(employeeId,targetDate,attendanceLeavingGates));
//		List<LogOnInfo> logOnInfo = new ArrayList<>();
//		logOnInfo.add(new LogOnInfo(new PCLogOnNo(1),new TimeWithDayAttr(420),new TimeWithDayAttr(1200)));
//		logOnInfo.add(new LogOnInfo(new PCLogOnNo(2),new TimeWithDayAttr(420),new TimeWithDayAttr(1200)));
//		//日別実績のPCログオン情報　　　
//		Optional<PCLogOnInfoOfDaily> pCLogOnInfoOfDaily = Optional.of(new PCLogOnInfoOfDaily(employeeId,targetDate,logOnInfo));
//		
//		//高須テスト用--------------------------------------------------------------------------------
		
	    //日別実績の入退門　　　　
		Optional<AttendanceLeavingGateOfDaily> attendanceLeavingGateOfDaily = manageReGetClass.getIntegrationOfDaily().getAttendanceLeavingGate();
		//日別実績のPCログオン情報　　　
		Optional<PCLogOnInfoOfDaily> pCLogOnInfoOfDaily = manageReGetClass.getIntegrationOfDaily().getPcLogOnInfo();
		//日別実績の出退勤
		Optional<TimeLeavingOfDailyPerformance> attendanceLeave = manageReGetClass.getIntegrationOfDaily().getAttendanceLeave();
		//総拘束時間の計算
		CalculateOfTotalConstraintTime calculateOfTotalConstraintTime = new CalculateOfTotalConstraintTime(new CompanyId(companyId),CalculationMethodOfConstraintTime.REQUEST_FROM_ENTRANCE_EXIT);
		
		//会社別代休設定取得

		val compensLeaveComSet = compensLeaveComSetRepository.find(companyId);
		List<CompensatoryOccurrenceSetting> eachCompanyTimeSet = new ArrayList<>();
		if(compensLeaveComSet != null)
			eachCompanyTimeSet = compensLeaveComSet.getCompensatoryOccurrenceSetting();
 
		//-------------------------計算用一時的クラス作成----------------------------
		
		Optional<WorkTimeDailyAtr> workTime = Optional.empty();
		if (manageReGetClass.getIntegrationOfDaily().getWorkInformation().getRecordInfo().getWorkTimeCode() != null) {
			val workTimeSetting = workTimeSettingRepository.findByCode(companyId, manageReGetClass
					.getIntegrationOfDaily().getWorkInformation().getRecordInfo().getWorkTimeCode().toString());
			workTime = workTimeSetting.isPresent()
					? Optional.of(workTimeSetting.get().getWorkTimeDivision().getWorkTimeDailyAtr()) : Optional.empty();
		}

		if (manageReGetClass.getIntegrationOfDaily().getWorkInformation() == null
				|| manageReGetClass.getIntegrationOfDaily().getWorkInformation().getRecordInfo() == null
				|| manageReGetClass.getIntegrationOfDaily().getWorkInformation().getRecordInfo()
						.getWorkTypeCode() == null)
			return manageReGetClass.getIntegrationOfDaily();

		val workType = this.workTypeRepository.findByPK(companyId,
				manageReGetClass.getIntegrationOfDaily().getWorkInformation().getRecordInfo().getWorkTypeCode().v()); // 要確認：勤務種類マスタが削除されている場合は考慮しない？
		if (!workType.isPresent() || !workTime.isPresent())
			return manageReGetClass.getIntegrationOfDaily();
		
		/*計画所定算出のため、計画側の所定時間取得*/
		val scheWorkTime = workingConditionItemRepository.getBySidAndStandardDate(employeeId, targetDate);
		Optional<PredetermineTimeSetForCalc> schePreTimeSet = Optional.empty();
		if(scheWorkTime != null && scheWorkTime.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().isPresent()) {
			val schePreTime = predetemineTimeSetRepository.findByWorkTimeCode(companyId, scheWorkTime.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().toString());
			if(schePreTime.isPresent()) {
				schePreTimeSet = Optional.of(PredetermineTimeSetForCalc.convertMastarToCalc(schePreTime.get()));
			}
		}
		
		
		val workType = manageReGetClass.getWorkType();
		//乖離時間(AggregateRoot)取得
		List<DivergenceTime> divergenceTimeList = divergenceTimeRepository.getAllDivTime(companyId);
		
		//乖離時間計算用　勤怠項目ID紐づけDto作成
		DailyRecordToAttendanceItemConverter forCalcDivergenceDto = this.dailyRecordToAttendanceItemConverter.setData(copyIntegrationOfDaily);
		
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
				    vacationAddSetting,
				    AutoCalOverTimeAttr.CALCULATION_FROM_STAMP,
				    workTime.get(),
				    flexCalcMethod,
				    holidaycalcMethodSet,
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
					manageReGetClass.getFixRestTimeSetting()
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
		  }
			
		/*日別実績への項目移送*/
		//return integrationOfDaily;
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
		Optional<PredetemineTimeSetting> predetermineTimeSet = Optional.empty();
		String workTimeCode = null;
			workTimeCode = integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTimeCode().toString();
		}
		}
		/* 1日の計算範囲取得 */
		val calcRangeOfOneDay = new TimeSpanForCalc(predetermineTimeSet.get().getStartDateClock(),
				predetermineTimeSet.get().getStartDateClock()
						.forwardByMinutes(predetermineTimeSet.get().getRangeTimeDay().valueAsMinutes()));

		WorkInfoOfDailyPerformance toDayWorkInfo = integrationOfDaily.getWorkInformation();
		Optional<TimeLeavingOfDailyPerformance> timeLeavingOfDailyPerformance = integrationOfDaily.getAttendanceLeave();
			List<TimeLeavingWork> timeLeavingWorkList = new ArrayList<>();
			timeLeavingWorkList.add(timeLeavingWork);
		}
		return new CalculationRangeOfOneDay(Finally.empty(), Finally.empty(), calcRangeOfOneDay,
				timeLeavingOfDailyPerformance.get(), /* 出退勤 */
				PredetermineTimeSetForCalc.convertMastarToCalc(
						predetermineTimeSet.get())/* 所定時間帯(計算用) */,
				Finally.of(new TimevacationUseTimeOfDaily(new AttendanceTime(0), new AttendanceTime(0),
						new AttendanceTime(0), new AttendanceTime(0))),
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
		// Optional<EmploymentContractHistory> employmentContractHistory =
		// this.employmentContractHistoryAdopter.findByEmployeeIdAndBaseDate(employeeId,
		// targetDate);
		Optional<EmploymentContractHistory> employmentContractHistory = Optional
				.of(new EmploymentContractHistory(employeeId, WorkingSystem.REGULAR_WORK));
		if (!employmentContractHistory.isPresent()) {
			throw new RuntimeException("Can't get WorkingSystem");
		}
		// 労働制
		return getOfStatutoryWorkTime.getDailyTimeFromStaturoyWorkTime(
				employmentContractHistory.get().getWorkingSystem(), companyId, placeId, employmentCd, employeeId,
				targetDate);
	}
}
