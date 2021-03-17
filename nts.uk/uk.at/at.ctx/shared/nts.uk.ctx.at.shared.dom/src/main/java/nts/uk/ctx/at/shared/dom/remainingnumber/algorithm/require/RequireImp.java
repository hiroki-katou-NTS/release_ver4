package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.require;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.adapter.employee.AffCompanyHistSharedImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.SClsHistImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.SharedSidPeriodDateEmploymentImport;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyAdapter;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyDto;
import nts.uk.ctx.at.shared.dom.adapter.workplace.SharedAffWorkPlaceHisAdapter;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbasMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.DigestionAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.DataManagementAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutManagementData;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutManagementDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManagementData;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.ComDayOffManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.CompensatoryDayOffManaData;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManagementData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByApplicationData;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.bonuspay.enums.UseAtr;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.outsideot.OutsideOTSetting;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.outsideot.OutsideOTSettingRepository;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.UsageUnitSetting;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.UsageUnitSettingRepository;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.defor.DeforLaborTimeCom;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.defor.DeforLaborTimeComRepo;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.defor.DeforLaborTimeEmp;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.defor.DeforLaborTimeEmpRepo;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.defor.DeforLaborTimeSha;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.defor.DeforLaborTimeShaRepo;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.defor.DeforLaborTimeWkp;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.defor.DeforLaborTimeWkpRepo;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.regular.RegularLaborTimeCom;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.regular.RegularLaborTimeComRepo;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.regular.RegularLaborTimeEmp;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.regular.RegularLaborTimeEmpRepo;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.regular.RegularLaborTimeSha;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.regular.RegularLaborTimeShaRepo;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.regular.RegularLaborTimeWkp;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.regular.RegularLaborTimeWkpRepo;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.ElapseYear;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantDateTbl;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantDateTblRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveEmSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveEmSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.EmploymentSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.EmptYearlyRetentionSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.RetentionYearlySetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.RetentionYearlySettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrameRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayRepository;

@AllArgsConstructor
public class RequireImp implements RemainNumberTempRequireService.Require {
	
	protected ComSubstVacationRepository comSubstVacationRepo;
	
	protected CompensLeaveComSetRepository compensLeaveComSetRepo;
	
	protected SpecialLeaveGrantRepository specialLeaveGrantRepo;
	
	protected EmpEmployeeAdapter empEmployeeAdapter;
	
	protected GrantDateTblRepository grantDateTblRepo;
	
	protected AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepo;
	
	protected SpecialHolidayRepository specialHolidayRepo;
	
	protected InterimSpecialHolidayMngRepository interimSpecialHolidayMngRepo;
	
	protected SpecialLeaveBasicInfoRepository specialLeaveBasicInfoRepo;
	
	protected InterimRecAbasMngRepository interimRecAbasMngRepo;
	
	protected EmpSubstVacationRepository empSubstVacationRepo;
	 
	protected InterimRemainRepository interimRemainRepo;
	
	protected SubstitutionOfHDManaDataRepository substitutionOfHDManaDataRepo;
	
	protected PayoutManagementDataRepository payoutManagementDataRepo;
	
	protected InterimBreakDayOffMngRepository interimBreakDayOffMngRepo;
	
	protected ComDayOffManaDataRepository comDayOffManaDataRepo;
	
	protected CompanyAdapter companyAdapter;
	
	protected ShareEmploymentAdapter shareEmploymentAdapter;
	
	protected LeaveManaDataRepository leaveManaDataRepo;
	
	protected WorkingConditionItemRepository workingConditionItemRepo;
	
	protected WorkingConditionRepository workingConditionRepo;
	
	protected WorkTimeSettingRepository workTimeSettingRepo;
	
	protected FixedWorkSettingRepository fixedWorkSettingRepo;
	
	protected FlowWorkSettingRepository flowWorkSettingRepo;
	
	protected DiffTimeWorkSettingRepository diffTimeWorkSettingRepo;
	
	protected FlexWorkSettingRepository flexWorkSettingRepo;
	
	protected PredetemineTimeSettingRepository predetemineTimeSettingRepo;
	
	protected ClosureRepository closureRepo;
	
	protected ClosureEmploymentRepository closureEmploymentRepo;
	
	protected WorkTypeRepository workTypeRepo;
	
	protected RemainCreateInforByApplicationData remainCreateInforByApplicationData;
	
	protected CompensLeaveEmSetRepository compensLeaveEmSetRepo;
	
	protected EmploymentSettingRepository employmentSettingRepo;
	
	protected RetentionYearlySettingRepository retentionYearlySettingRepo;
	
	protected AnnualPaidLeaveSettingRepository annualPaidLeaveSettingRepo;
	
	protected OutsideOTSettingRepository outsideOTSettingRepo;
	
	protected WorkdayoffFrameRepository workdayoffFrameRepo;
	
	protected YearHolidayRepository yearHolidayRepo;
	
	protected UsageUnitSettingRepository usageUnitSettingRepo;
	 
	protected RegularLaborTimeComRepo regularLaborTimeComRepo;
	
	protected DeforLaborTimeComRepo deforLaborTimeComRepo;
	 
	protected RegularLaborTimeWkpRepo regularLaborTimeWkpRepo;
	
	protected DeforLaborTimeWkpRepo deforLaborTimeWkpRepo;
	 
	protected RegularLaborTimeEmpRepo regularLaborTimeEmpRepo;
	
	protected DeforLaborTimeEmpRepo deforLaborTimeEmpRepo;
	 
	protected RegularLaborTimeShaRepo regularLaborTimeShaRepo;
	
	protected DeforLaborTimeShaRepo deforLaborTimeShaRepo;
	
	protected SharedAffWorkPlaceHisAdapter sharedAffWorkPlaceHisAdapter;
	
	private Optional<OutsideOTSetting> outsideOTSettingCache = Optional.empty();
	
	private HashMap<String, Optional<FlowWorkSetting>>  flowWorkSetMap = new HashMap<String, Optional<FlowWorkSetting>>();
	
	private HashMap<String, Optional<FlexWorkSetting>>  flexWorkSetMap = new HashMap<String, Optional<FlexWorkSetting>>();
	
	private HashMap<String, Optional<FixedWorkSetting>>  fixedWorkSetMap = new HashMap<String, Optional<FixedWorkSetting>>();
	
	private HashMap<String, Optional<WorkTimeSetting>>  workTimeSetMap = new HashMap<String, Optional<WorkTimeSetting>>();
	
	private HashMap<String, Optional<WorkType>>  workTypeMap = new HashMap<String, Optional<WorkType>>();
	
	private HashMap<Integer, Optional<Closure>> closureMap = new HashMap<Integer, Optional<Closure>>(); 
	
	
	public RequireImp(ComSubstVacationRepository comSubstVacationRepo,
			CompensLeaveComSetRepository compensLeaveComSetRepo, SpecialLeaveGrantRepository specialLeaveGrantRepo,
			EmpEmployeeAdapter empEmployeeAdapter, GrantDateTblRepository grantDateTblRepo,
			AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepo, SpecialHolidayRepository specialHolidayRepo,
			InterimSpecialHolidayMngRepository interimSpecialHolidayMngRepo,
			SpecialLeaveBasicInfoRepository specialLeaveBasicInfoRepo,
			InterimRecAbasMngRepository interimRecAbasMngRepo, EmpSubstVacationRepository empSubstVacationRepo,
			InterimRemainRepository interimRemainRepo,
			SubstitutionOfHDManaDataRepository substitutionOfHDManaDataRepo,
			PayoutManagementDataRepository payoutManagementDataRepo,
			InterimBreakDayOffMngRepository interimBreakDayOffMngRepo,
			ComDayOffManaDataRepository comDayOffManaDataRepo, CompanyAdapter companyAdapter,
			ShareEmploymentAdapter shareEmploymentAdapter, LeaveManaDataRepository leaveManaDataRepo,
			WorkingConditionItemRepository workingConditionItemRepo,
			WorkingConditionRepository workingConditionRepo, WorkTimeSettingRepository workTimeSettingRepo,
			FixedWorkSettingRepository fixedWorkSettingRepo, FlowWorkSettingRepository flowWorkSettingRepo,
			DiffTimeWorkSettingRepository diffTimeWorkSettingRepo, FlexWorkSettingRepository flexWorkSettingRepo,
			PredetemineTimeSettingRepository predetemineTimeSettingRepo, ClosureRepository closureRepo,
			ClosureEmploymentRepository closureEmploymentRepo, WorkTypeRepository workTypeRepo,
			RemainCreateInforByApplicationData remainCreateInforByApplicationData,
			CompensLeaveEmSetRepository compensLeaveEmSetRepo, EmploymentSettingRepository employmentSettingRepo,
			RetentionYearlySettingRepository retentionYearlySettingRepo,
			AnnualPaidLeaveSettingRepository annualPaidLeaveSettingRepo,
			OutsideOTSettingRepository outsideOTSettingRepo, WorkdayoffFrameRepository workdayoffFrameRepo,
			YearHolidayRepository yearHolidayRepo, UsageUnitSettingRepository usageUnitSettingRepo,
			RegularLaborTimeComRepo regularLaborTimeComRepo, DeforLaborTimeComRepo deforLaborTimeComRepo,
			RegularLaborTimeWkpRepo regularLaborTimeWkpRepo, DeforLaborTimeWkpRepo deforLaborTimeWkpRepo,
			RegularLaborTimeEmpRepo regularLaborTimeEmpRepo, DeforLaborTimeEmpRepo deforLaborTimeEmpRepo,
			RegularLaborTimeShaRepo regularLaborTimeShaRepo, DeforLaborTimeShaRepo deforLaborTimeShaRepo,
			SharedAffWorkPlaceHisAdapter sharedAffWorkPlaceHisAdapter) {
		this.comSubstVacationRepo = comSubstVacationRepo;
		this.compensLeaveComSetRepo = compensLeaveComSetRepo;
		this.specialLeaveGrantRepo = specialLeaveGrantRepo;
		this.empEmployeeAdapter = empEmployeeAdapter;
		this.grantDateTblRepo = grantDateTblRepo;
		this.annLeaEmpBasicInfoRepo = annLeaEmpBasicInfoRepo;
		this.specialHolidayRepo = specialHolidayRepo;
		this.interimSpecialHolidayMngRepo = interimSpecialHolidayMngRepo;
		this.specialLeaveBasicInfoRepo = specialLeaveBasicInfoRepo;
		this.interimRecAbasMngRepo = interimRecAbasMngRepo;
		this.empSubstVacationRepo = empSubstVacationRepo;
		this.interimRemainRepo = interimRemainRepo;
		this.substitutionOfHDManaDataRepo = substitutionOfHDManaDataRepo;
		this.payoutManagementDataRepo = payoutManagementDataRepo;
		this.interimBreakDayOffMngRepo = interimBreakDayOffMngRepo;
		this.comDayOffManaDataRepo = comDayOffManaDataRepo;
		this.companyAdapter = companyAdapter;
		this.shareEmploymentAdapter = shareEmploymentAdapter;
		this.leaveManaDataRepo = leaveManaDataRepo;
		this.workingConditionItemRepo = workingConditionItemRepo;
		this.workingConditionRepo = workingConditionRepo;
		this.workTimeSettingRepo = workTimeSettingRepo;
		this.fixedWorkSettingRepo = fixedWorkSettingRepo;
		this.flowWorkSettingRepo = flowWorkSettingRepo;
		this.diffTimeWorkSettingRepo = diffTimeWorkSettingRepo;
		this.flexWorkSettingRepo = flexWorkSettingRepo;
		this.predetemineTimeSettingRepo = predetemineTimeSettingRepo;
		this.closureRepo = closureRepo;
		this.closureEmploymentRepo = closureEmploymentRepo;
		this.workTypeRepo = workTypeRepo;
		this.remainCreateInforByApplicationData = remainCreateInforByApplicationData;
		this.compensLeaveEmSetRepo = compensLeaveEmSetRepo;
		this.employmentSettingRepo = employmentSettingRepo;
		this.retentionYearlySettingRepo = retentionYearlySettingRepo;
		this.annualPaidLeaveSettingRepo = annualPaidLeaveSettingRepo;
		this.outsideOTSettingRepo = outsideOTSettingRepo;
		this.workdayoffFrameRepo = workdayoffFrameRepo;
		this.yearHolidayRepo = yearHolidayRepo;
		this.usageUnitSettingRepo = usageUnitSettingRepo;
		this.regularLaborTimeComRepo = regularLaborTimeComRepo;
		this.deforLaborTimeComRepo = deforLaborTimeComRepo;
		this.regularLaborTimeWkpRepo = regularLaborTimeWkpRepo;
		this.deforLaborTimeWkpRepo = deforLaborTimeWkpRepo;
		this.regularLaborTimeEmpRepo = regularLaborTimeEmpRepo;
		this.deforLaborTimeEmpRepo = deforLaborTimeEmpRepo;
		this.regularLaborTimeShaRepo = regularLaborTimeShaRepo;
		this.deforLaborTimeShaRepo = deforLaborTimeShaRepo;
		this.sharedAffWorkPlaceHisAdapter = sharedAffWorkPlaceHisAdapter;
	}

	@Override
	public Optional<ComSubstVacation> comSubstVacation(String companyId) {
		return comSubstVacationRepo.findById(companyId);
	}

	@Override
	public Optional<InterimAbsMng> interimAbsMng(String absId) {
		return interimRecAbasMngRepo.getAbsById(absId);
	}

	@Override
	public List<InterimRecAbsMng> interimRecAbsMng(String interimId, boolean isRec, DataManagementAtr mngAtr) {
		return interimRecAbasMngRepo.getRecOrAbsMng(interimId, isRec, mngAtr);
	}

	@Override
	public Optional<InterimRecMng> interimRecMng(String recId) {
		return interimRecAbasMngRepo.getReruitmentById(recId);
	}

	@Override
	public List<SubstitutionOfHDManagementData> substitutionOfHDManagementData(String cid, String sid,
			GeneralDate ymd, double unOffseDays) {
		return substitutionOfHDManaDataRepo.getByYmdUnOffset(cid, sid, ymd, unOffseDays);
	}

	@Override
	public List<PayoutManagementData> payoutManagementData(String cid, String sid, GeneralDate ymd,
			double unUse, DigestionAtr state) {
		return payoutManagementDataRepo.getByUnUseState(cid, sid, ymd, unUse, state);
	}

	@Override
	public List<InterimRemain> interimRemains(String employeeId, DatePeriod dateData, RemainType remainType) {
		return interimRemainRepo.getRemainBySidPriod(employeeId, dateData, remainType);
	}

	@Override
	public Optional<InterimDayOffMng> interimDayOffMng(String dayOffManaId) {
		return interimBreakDayOffMngRepo.getDayoffById(dayOffManaId);
	}

	@Override
	public Optional<InterimBreakMng> interimBreakMng(String breakManaId) {
		return interimBreakDayOffMngRepo.getBreakManaBybreakMngId(breakManaId);
	}

	@Override
	public List<CompensatoryDayOffManaData> compensatoryDayOffManaData(String cid, String sid,
			GeneralDate ymd) {
		return comDayOffManaDataRepo.getBySidDate(cid, sid, ymd);
	}

	@Override
	public List<InterimBreakDayOffMng> interimBreakDayOffMng(String mngId, boolean breakDay,
			DataManagementAtr mngAtr) {
		return interimBreakDayOffMngRepo.getBreakDayOffMng(mngId, breakDay, mngAtr);
	}

	@Override
	public CompanyDto firstMonth(CacheCarrier cacheCarrier, String companyId) {
		return companyAdapter.getFirstMonthRequire(cacheCarrier, companyId);
	}

	@Override
	public Optional<BsEmploymentHistoryImport> employmentHistory(CacheCarrier cacheCarrier, String companyId,
			String employeeId, GeneralDate baseDate) {
		return shareEmploymentAdapter.findEmploymentHistoryRequire(cacheCarrier, companyId, employeeId, baseDate);
	}

	@Override
	public List<SharedSidPeriodDateEmploymentImport> employmentHistory(CacheCarrier cacheCarrier,
			List<String> sids, DatePeriod datePeriod) {
		return shareEmploymentAdapter.getEmpHistBySidAndPeriodRequire(cacheCarrier, sids, datePeriod);
	}

	@Override
	public List<LeaveManagementData> leaveManagementData(String cid, String sid, GeneralDate ymd,
			DigestionAtr state) {
		return leaveManaDataRepo.getBySidYmd(cid, sid, ymd, state);
	}

	@Override
	public Optional<WorkingConditionItem> workingConditionItem(String historyId) {
		return workingConditionItemRepo.getByHistoryId(historyId);
	}

	@Override
	public Optional<WorkingCondition> workingCondition(String companyId, String employeeId,
			GeneralDate baseDate) {
		return workingConditionRepo.getBySidAndStandardDate(companyId, employeeId, baseDate);
	}


	@Override
	public Optional<FlowWorkSetting> flowWorkSetting(String companyId, String workTimeCode) {
		if(flowWorkSetMap.containsKey(workTimeCode)) {
			return flowWorkSetMap.get(workTimeCode);
		}
		Optional<FlowWorkSetting> item = flowWorkSettingRepo.find(companyId, workTimeCode);
		flowWorkSetMap.put(workTimeCode, item);
		return item;
	}

	@Override
	public Optional<FlexWorkSetting> flexWorkSetting(String companyId, String workTimeCode) {
		if(flexWorkSetMap.containsKey(workTimeCode)) {
			return flexWorkSetMap.get(workTimeCode);
		}
		Optional<FlexWorkSetting> item = flexWorkSettingRepo.find(companyId, workTimeCode);
		flexWorkSetMap.put(workTimeCode, item);
		return item;
	}

	@Override
	public Optional<FixedWorkSetting> fixedWorkSetting(String companyId, String workTimeCode) {
		if(fixedWorkSetMap.containsKey(workTimeCode)) {
			return fixedWorkSetMap.get(workTimeCode);
		}
		Optional<FixedWorkSetting> item = fixedWorkSettingRepo.findByKey(companyId, workTimeCode);
		fixedWorkSetMap.put(workTimeCode, item);
		return item;
	}

	@Override
	public Optional<DiffTimeWorkSetting> diffTimeWorkSetting(String companyId, String workTimeCode) {
		return diffTimeWorkSettingRepo.find(companyId, workTimeCode);
	}

	@Override
	public Optional<WorkTimeSetting> workTimeSetting(String companyId, String workTimeCode) {
		if(workTimeSetMap.containsKey(workTimeCode)) {
			return workTimeSetMap.get(workTimeCode);
		}
		Optional<WorkTimeSetting> item = workTimeSettingRepo.findByCode(companyId, workTimeCode);
		workTimeSetMap.put(workTimeCode, item);
		return item;
	}

	@Override
	public CompensatoryLeaveComSetting compensatoryLeaveComSetting(String companyId) {
		return compensLeaveComSetRepo.find(companyId);
	}

	@Override
	public Optional<PredetemineTimeSetting> predetemineTimeSetting(String companyId, String workTimeCode) {
		return predetemineTimeSettingRepo.findByWorkTimeCode(companyId, workTimeCode);
	}

	@Override
	public List<Integer> getSpecialHolidayNumber(String cid, int sphdSpecLeaveNo) {
		return specialHolidayRepo.findBySphdSpecLeave(cid, sphdSpecLeaveNo);
	}

	@Override
	public Optional<Closure> closure(String companyId, int closureId) {
		if(closureMap.containsKey(closureId)) {
			return closureMap.get(closureId);
		}
		Optional<Closure> item = closureRepo.findById(companyId, closureId);
		closureMap.put(closureId, item);
		return item;
	}

	@Override
	public Optional<ClosureEmployment> employmentClosure(String companyID, String employmentCD) {
		return closureEmploymentRepo.findByEmploymentCD(companyID, employmentCD);
	}

	@Override
	public Optional<WorkType> workType(String companyId, String workTypeCd) {
		if(workTypeMap.containsKey(workTypeCd)) {
			return workTypeMap.get(workTypeCd);
		}
		Optional<WorkType> item = workTypeRepo.findByPK(companyId, workTypeCd);
		workTypeMap.put(workTypeCd, item);
		return item;
	}

	@Override
	public Integer excludeHolidayAtr(CacheCarrier cacheCarrier, String cid, String appID) {
		return remainCreateInforByApplicationData.excludeHolidayAtr(cacheCarrier, cid, appID);
	}
	@Override
	public Optional<EmpSubstVacation> empSubstVacation(String companyId, String contractTypeCode) {
		return empSubstVacationRepo.findById(companyId, contractTypeCode);
	}

	@Override
	public CompensatoryLeaveEmSetting compensatoryLeaveEmSetting(String companyId,
			String employmentCode) {
		return compensLeaveEmSetRepo.find(companyId, employmentCode);
	}

	@Override
	public List<Closure> closure(String companyId) {
		return closureRepo.findAll(companyId);
	}

	@Override
	public EmployeeImport employee(CacheCarrier cacheCarrier, String empId) {
		return empEmployeeAdapter.findByEmpIdRequire(cacheCarrier, empId);
	}

	@Override
	public Optional<OutsideOTSetting> outsideOTSetting(String companyId) {
		if(outsideOTSettingCache.isPresent()) {
			return outsideOTSettingCache;
		}
		outsideOTSettingCache = outsideOTSettingRepo.findById(companyId);
		return outsideOTSettingCache;
	}

	@Override
	public List<WorkdayoffFrame> workdayoffFrames(String companyId) {
		return workdayoffFrameRepo.getAllWorkdayoffFrame(companyId);
	}

	@Override
	public AnnualPaidLeaveSetting annualPaidLeaveSetting(String companyId) {
		return annualPaidLeaveSettingRepo.findByCompanyId(companyId);
	}

	@Override
	public Optional<EmptYearlyRetentionSetting> employmentYearlyRetentionSetting(String companyId,
			String employmentCode) {
		return employmentSettingRepo.find(companyId, employmentCode);
	}

	@Override
	public Optional<RetentionYearlySetting> retentionYearlySetting(String companyId) {
		return retentionYearlySettingRepo.findByCompanyId(companyId);
	}

	@Override
	public Optional<UsageUnitSetting> usageUnitSetting(String companyId) {
		return usageUnitSettingRepo.findByCompany(companyId);
	}

	@Override
	public Optional<RegularLaborTimeCom> regularLaborTimeByCompany(String companyId) {
		return regularLaborTimeComRepo.find(companyId);
	}

	@Override
	public Optional<DeforLaborTimeCom> deforLaborTimeByCompany(String companyId) {
		return deforLaborTimeComRepo.find(companyId);
	}

	@Override
	public Optional<RegularLaborTimeWkp> regularLaborTimeByWorkplace(String cid, String wkpId) {
		return regularLaborTimeWkpRepo.find(cid, wkpId);
	}

	@Override
	public Optional<DeforLaborTimeWkp> deforLaborTimeByWorkplace(String cid, String wkpId) {
		return deforLaborTimeWkpRepo.find(cid, wkpId);
	}

	@Override
	public Optional<RegularLaborTimeEmp> regularLaborTimeByEmployment(String cid, String employmentCode) {
		return regularLaborTimeEmpRepo.findById(cid, employmentCode);
	}

	@Override
	public Optional<DeforLaborTimeEmp> deforLaborTimeByEmployment(String cid, String employmentCode) {
		return deforLaborTimeEmpRepo.find(cid, employmentCode);
	}

	@Override
	public Optional<RegularLaborTimeSha> regularLaborTimeByEmployee(String Cid, String EmpId) {
		return regularLaborTimeShaRepo.find(Cid, EmpId);
	}

	@Override
	public Optional<DeforLaborTimeSha> deforLaborTimeByEmployee(String cid, String empId) {
		return deforLaborTimeShaRepo.find(cid, empId);
	}

	@Override
	public List<String> getCanUseWorkplaceForEmp(CacheCarrier cacheCarrier, String companyId, String employeeId,
			GeneralDate baseDate) {
		return sharedAffWorkPlaceHisAdapter.findAffiliatedWorkPlaceIdsToRootRequire(cacheCarrier, companyId, employeeId, baseDate);
	}
}
