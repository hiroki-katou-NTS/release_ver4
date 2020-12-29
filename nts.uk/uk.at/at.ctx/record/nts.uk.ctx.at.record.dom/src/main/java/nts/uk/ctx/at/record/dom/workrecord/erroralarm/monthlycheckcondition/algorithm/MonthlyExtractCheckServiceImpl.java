package nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.algorithm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSttMonthEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.EmpPerformMonthParamImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMergeRepository;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.export.AbsenceleaveCurrentMonthOfEmployee;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.export.MonthlyAbsenceleaveRemainExport;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.export.DayoffCurrentMonthOfEmployee;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.export.MonthlyDayoffRemainExport;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.export.SpecialHolidayRemainDataOutput;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.export.SpecialHolidayRemainDataSevice;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.AttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.CompareOperatorText;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionAtr;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConvertCompareTypeToText;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ErrorAlarmConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.LogicalOperator;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.mastercheck.algorithm.WorkPlaceHistImportAl;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.ExtraResultMonthly;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.ExtraResultMonthlyRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.FixedExtraItemMon;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.FixedExtraItemMonRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.FixedExtraMon;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.FixedExtraMonRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.PerTimeMonActualResultService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.SysFixedMonPerEral;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.TypeCheckVacation;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.TypeMonCheckItem;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.checkremainnumber.CheckOperatorType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.checkremainnumber.CheckRemainNumberMon;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.checkremainnumber.CheckRemainNumberMonRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycondition.MonthlyCorrectConditionRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycondition.TimeItemCheckMonthly;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;
import nts.uk.ctx.at.record.dom.workrecord.export.WorkRecordExport;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.EmpAffInfoExport;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApprovalProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApprovalProcessRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcessRepository;
import nts.uk.ctx.at.shared.dom.adapter.attendanceitemname.AttendanceItemNameAdapter;
import nts.uk.ctx.at.shared.dom.adapter.attendanceitemname.MonthlyAttendanceItemNameDto;
import nts.uk.ctx.at.shared.dom.adapter.employment.AffPeriodEmpCodeImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.SharedSidPeriodDateEmploymentImport;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyAdapter;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckType;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ExtractionAlarmPeriodDate;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ExtractionResultDetail;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ResultOfEachCondition;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.OccurrenceDigClass;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveRemainingDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveUsedDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.RemainingMinutes;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.UsedMinutes;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.NumberRemainVacationLeaveRangeQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.NumberRemainVacationLeaveRangeProcess.RequireImpl;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.BreakDayOffRemainMngRefactParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.FixedManagementDataMonth;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.SubstituteHolidayAggrResult;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveRemainingDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveUsedDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.ComDayOffManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveComDayOffManaRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManaDataRepository;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.service.AttendanceItemConvertFactory;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.anyitem.AnyItemOfMonthlyRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.editstate.EditStateOfMonthlyPerRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.editstate.EditStateOfMonthlyPerformance;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnLeaRemNumEachMonthRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.RsvLeaRemNumEachMonthRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveEmSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveEmSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.i18n.TextResource;
@Stateless
public class MonthlyExtractCheckServiceImpl implements MonthlyExtractCheckService{
	@Inject
	private WorkRecordExport workRecordEx;
	@Inject
	private ExtraResultMonthlyRepository extCondMonRepo;
	@Inject
	private MonthlyCorrectConditionRepository correctCondRepo;
	@Inject
	private FixedExtraMonRepository fixCondRepo;
	@Inject
	private FixedExtraItemMonRepository fixItemCondRepo;
	@Inject
	private AttendanceItemNameAdapter attendanceAdapter;
	@Inject
	private AnnLeaRemNumEachMonthRepository annLeaRepos;
	@Inject
	private CheckRemainNumberMonRepository remainNumberRepos;

	@Inject
	private PerTimeMonActualResultService perTimeService;
	@Inject
	private ConvertCompareTypeToText convertComparaToText;
	@Inject
	private MonthlyDayoffRemainExport dayOffRemainExport;
	@Inject
	private MonthlyAbsenceleaveRemainExport pauseRemainExport;
	@Inject
	private RsvLeaRemNumEachMonthRepository leaRemainRepo;
	@Inject
	private SpecialHolidayRemainDataSevice speHolidaySevice;
	@Inject
	private IdentityProcessRepository indentityRepo;
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceRepo;
	@Inject
	private ConfirmationMonthRepository confirmRepo;
	@Inject
	private ApprovalProcessRepository approvalRepo;
	@Inject
	private CompensLeaveComSetRepository compenLeaveRepo;
	@Inject
	private CompensLeaveEmSetRepository compenEmLeaveRepo;
	@Inject
	private ShareEmploymentAdapter employmentAdapter;
	@Inject
	private EditStateOfMonthlyPerRepository editStateMonthRepo;
	@Inject
	private ApprovalStatusAdapter approvalAdapter;
	@Inject
	private ClosureEmploymentRepository cloEmpRepo;
	@Inject
	private ClosureRepository closureRepo;
	@Inject
	private ComDayOffManaDataRepository comDayOffManaDataRepository;

	@Inject
	private LeaveManaDataRepository leaveManaDataRepository;
	@Inject
	private LeaveComDayOffManaRepository leaveComDayOffManaRepository;
	@Inject
	private CompensLeaveEmSetRepository compensLeaveEmSetRepository;

	@Inject
	private CompensLeaveComSetRepository compensLeaveComSetRepository;

	@Inject
	private InterimRemainRepository interimRemainRepository;

	@Inject
	private InterimBreakDayOffMngRepository interimBreakDayOffMngRepository;
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;
	@Inject
	private CompanyAdapter companyAdapter;
	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;
	@Override
	public void extractMonthlyAlarm(String cid, List<String> lstSid, YearMonthPeriod mPeriod, String fixConId,
			List<String> lstAnyConID, List<WorkPlaceHistImportAl> getWplByListSidAndPeriod,
			List<ResultOfEachCondition> lstResultCondition, List<AlarmListCheckInfor> lstCheckType) {
		DataCheck data = new DataCheck(cid, lstSid, mPeriod, fixConId, lstAnyConID);
		// 任意抽出条件をチェック
		data.lstAnyCondMon.stream().forEach(anyCond -> {
			lstCheckType.add(new AlarmListCheckInfor(String.valueOf(anyCond.getSortBy()), AlarmListCheckType.FreeCheck));
			this.extractAnyCondAlarm(lstSid, mPeriod, getWplByListSidAndPeriod, anyCond, lstResultCondition, data);
		});
		
		//固定チェック
		data.lstFixCond.stream().forEach(fixCond -> {
			lstCheckType.add(new AlarmListCheckInfor(String.valueOf(fixCond.getFixedExtraItemMonNo().value), AlarmListCheckType.FixCheck));
			this.extractFixCondAlarm(cid, lstSid, mPeriod, getWplByListSidAndPeriod, fixCond, lstResultCondition, data);
			
		});
	}
	/**
	 * 固定チェック
	 * @param cid
	 * @param lstSid
	 * @param mPeriod
	 * @param getWplByListSidAndPeriod
	 * @param fixCond
	 * @param lstResultCondition
	 * @param data
	 */
	private void extractFixCondAlarm(String cid, List<String> lstSid, YearMonthPeriod mPeriod, List<WorkPlaceHistImportAl> getWplByListSidAndPeriod,
			FixedExtraMon fixCond,
			List<ResultOfEachCondition> lstResultCondition, DataCheck data) {
		for(String sid : lstSid) {
			for (YearMonth ym : mPeriod.yearMonthsBetween()) {
				YearMonth endMonthTemp = mPeriod.end().addMonths(1);
				GeneralDate endDateTemp = GeneralDate.ymd(endMonthTemp.year(), endMonthTemp.month(), 1);
				GeneralDate enDate = endDateTemp.addDays(-1);
				GeneralDate startDate = GeneralDate.ymd(mPeriod.start().year(), mPeriod.end().month(), 1);
				String checkValue = "";
				String alarmContent = "";
				switch (fixCond.getFixedExtraItemMonNo()) {
				case MYSELF_UNCONFIRMED:
					if(data.optIndentiry.isPresent()) {
						IdentityProcess indentiry = data.optIndentiry.get();
						if(indentiry.getUseMonthSelfCK() == 1) {
							List<ConfirmationMonth> lstConfirM = data.lstMyselConfirm.stream()
									.filter(x -> x.getEmployeeId().equals(sid) && x.getProcessYM().equals(ym))
									.collect(Collectors.toList());
							if(lstConfirM.isEmpty()) {
								checkValue = TextResource.localize("KAL010_130");
								alarmContent = TextResource.localize("KAL010_108");
							}
						}
					}
					break;
				case ADMIN_UNCONFIRMED:
					if(data.optApprovalSetting.isPresent()) {
						ApprovalProcess approPro = data.optApprovalSetting.get();
						if(approPro.getUseMonthBossChk() == 1) {
							List<AttendanceTimeOfMonthly> lstAttendanceTime = data.lstAttendanceTime.stream()
									.filter(x -> x.getEmployeeId().equals(sid) && x.getYearMonth().equals(ym))
									.collect(Collectors.toList());
							if(lstAttendanceTime.isEmpty()) {
								continue;
							}
							
							List<EmpPerformMonthParamImport> lstImportParam = lstAttendanceTime.stream()
									.map(s -> new EmpPerformMonthParamImport(s.getYearMonth(),
											s.getClosureId().value,
											s.getClosureDate(),
											s.getDatePeriod().end(),
											s.getEmployeeId()))
									.collect(Collectors.toList());
							
							List<AppRootSttMonthEmpImport> lstAppRoot = approvalAdapter.getAppRootStatusByEmpsMonth(lstImportParam);
							if(lstAppRoot.isEmpty()) {
								continue;
							}
							for(AppRootSttMonthEmpImport a : lstAppRoot) {
								if(a.getApprovalStatus() != ApprovalStatusForEmployee.APPROVED) {
									checkValue = TextResource.localize("KAL010_131");
									alarmContent = TextResource.localize("KAL010_129");
								}
							}
						}
					}
					break;
				case CHECK_DEADLINE_HOLIDAY:
					List<SharedSidPeriodDateEmploymentImport> lstEmploymentHis = data.lstEmploymentHis.stream()
						.filter(x -> x.getEmployeeId().equals(sid)).collect(Collectors.toList());
					if(lstEmploymentHis.isEmpty()) continue;
					
					//所属期間と雇用コードを探す
					List<AffPeriodEmpCodeImport> affPeriodEmpCodeExports = lstEmploymentHis.get(0)
							.getAffPeriodEmpCodeExports().stream().filter(x -> x.getPeriod().start().beforeOrEquals(enDate) 
									&& x.getPeriod().end().afterOrEquals(startDate)).collect(Collectors.toList());
					if(affPeriodEmpCodeExports.isEmpty()) continue;
					for(AffPeriodEmpCodeImport affEmp : affPeriodEmpCodeExports) {
						//雇用に紐づく締めを取得する
						List<ClosureEmployment> optCloEmp = data.lstCloEmp.stream()
								.filter(x -> x.getEmploymentCD().equals(affEmp.getEmploymentCode()))
								.collect(Collectors.toList());
						if(optCloEmp.isEmpty()) continue;
						
						ClosureEmployment cloEmp = optCloEmp.get(0);
						//代休管理設定を探す
						List<CompensatoryLeaveEmSetting> lstCompenEmpSetting = data.lstCompenEmpSetting.stream()
								.filter(x -> x.getEmploymentCode().equals(affEmp.getEmploymentCode())).collect(Collectors.toList());
						if(!lstCompenEmpSetting.isEmpty() || lstCompenEmpSetting.get(0).getIsManaged() == ManageDistinct.NO) {
							continue;
						}
						if(lstCompenEmpSetting.isEmpty() && !data.comLeaveSetting.isManaged()) continue;
						int deadlCheckMonth;
						if(!lstCompenEmpSetting.isEmpty() ) {
							deadlCheckMonth = lstCompenEmpSetting.get(0).getCompensatoryAcquisitionUse().getDeadlCheckMonth().value + 1;
						} else {
							deadlCheckMonth = data.comLeaveSetting.getCompensatoryAcquisitionUse().getDeadlCheckMonth().value + 1;
						}
						List<Closure> closure = data.lstClosure.stream().filter(x -> x.getClosureId().value == cloEmp.getClosureId()
								&& x.getClosureMonth().getProcessingYm().equals(ym)).collect(Collectors.toList());
						if(closure.isEmpty()) continue;
						Closure emplOfClosure = closure.get(0);
						//締めのアルゴリズム「当月の期間を算出する」を実行する
						val closureOpt = Optional.ofNullable(emplOfClosure);
						DatePeriod periodCurrentMonth = ClosureService.getClosurePeriod(emplOfClosure.getClosureId().value,
								emplOfClosure.getClosureMonth().getProcessingYm(), closureOpt);
						//代休期限アラーム基準日を決定する
						DatePeriod periodCheckDealMonth = ClosureService.getClosurePeriod(emplOfClosure.getClosureId().value,
								getDeadlCheckMonth(periodCurrentMonth, deadlCheckMonth), closureOpt);
					
						//RequestList No.203 期間内の休出代休残数を取得する
						//集計開始日
						//集計終了日
						DatePeriod newPeriod = new DatePeriod(periodCurrentMonth.start(), periodCurrentMonth.end().addYears(1));
						RequireImpl requireImpl = new RequireImpl.RequireImplBuilder(comDayOffManaDataRepository,
								leaveManaDataRepository, shareEmploymentAdapter, compensLeaveEmSetRepository,
								compensLeaveComSetRepository).interimRemainRepo(interimRemainRepository)
										.interimBreakDayOffMngRepo(interimBreakDayOffMngRepository).companyAdapter(companyAdapter)
										.closureEmploymentRepo(closureEmploymentRepo).closureRepo(closureRepo)
										.leaveComDayOffManaRepository(leaveComDayOffManaRepository).build();
						BreakDayOffRemainMngRefactParam param = new BreakDayOffRemainMngRefactParam(cid, sid, newPeriod,
								true,
								startDate,
								false,
								Collections.emptyList(),
								Optional.empty(), 
								Optional.empty(), 
								Collections.emptyList(), 
								Collections.emptyList(), 
								Optional.empty(),
								new FixedManagementDataMonth(Collections.emptyList(), Collections.emptyList()));
						SubstituteHolidayAggrResult subsResult = NumberRemainVacationLeaveRangeQuery.getBreakDayOffMngInPeriod(requireImpl, param);
						List<AccumulationAbsenceDetail> lstAcctAbsenDetail = subsResult.getVacationDetails().getLstAcctAbsenDetail();
						if(lstAcctAbsenDetail.isEmpty()) continue;
						
						List<AccumulationAbsenceDetail> lstAcctAbsen = lstAcctAbsenDetail.stream()
								.filter(x -> x.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE
										&& !x.getUnbalanceNumber().allFieldZero()
										&& x.getDateOccur().getDayoffDate().isPresent() 
										&& x.getDateOccur().getDayoffDate().get().beforeOrEquals(periodCheckDealMonth.end()))
								.collect(Collectors.toList());
						if(lstAcctAbsen.isEmpty()) continue;
						
						for(AccumulationAbsenceDetail detail : lstAcctAbsen) {
							alarmContent = "\n" + TextResource.localize("KAL010_279",
									String.valueOf(deadlCheckMonth), detail.getDateOccur().getDayoffDate().get().toString(),
									String.valueOf(detail.getUnbalanceNumber().getDay().v()));
							checkValue = "\n" + TextResource.localize("KAL010_305",
									detail.getDateOccur().getDayoffDate().get().toString(),
									String.valueOf(detail.getUnbalanceNumber().getDay().v()));
							
						}
						if(!alarmContent.isEmpty()) {
							alarmContent = alarmContent.substring(2);
							checkValue = checkValue.substring(2);
						}
					}
					break;
				case MANUAL_INPUT:
					List<EditStateOfMonthlyPerformance> lstEditState = data.lstEditState.stream()
						.filter(x -> x.getEmployeeId().equals(sid) && x.getYm().equals(ym))
						.collect(Collectors.toList());
					if(lstEditState.isEmpty()) continue;
					String itemName = "";
					for(EditStateOfMonthlyPerformance editStare: lstEditState) {
						List<String> lstItemName = data.lstItemMond.stream().filter(x -> x.getAttendanceItemId() == editStare.getAttendanceItemId())
								.collect(Collectors.toList())
								.stream().map(x -> x.getAttendanceItemName()).collect(Collectors.toList());
						if(!lstItemName.isEmpty()) {
							itemName = "、" + lstItemName.get(0);
						}
					}
					if(!itemName.isEmpty()) {
						itemName = itemName.substring(1);
						alarmContent =  TextResource.localize("KAL010_606");
						checkValue = itemName;
					}
					break;
					default:
						break;
				}
				if(!alarmContent.isEmpty()) {
					List<FixedExtraItemMon> lstFixItemCond = data.lstFixItemCond.stream()
							.filter(x -> x.getFixedExtraItemMonNo() == fixCond.getFixedExtraItemMonNo())
							.collect(Collectors.toList());
					ExtractionAlarmPeriodDate date = new ExtractionAlarmPeriodDate(Optional.ofNullable(GeneralDate.ymd(ym.year(), ym.month(), 1)), Optional.empty());
					String workplaceId = getWplByListSidAndPeriod.stream().filter(x -> x.getEmployeeId().equals(sid))
							.collect(Collectors.toList())
							.get(0).getLstWkpIdAndPeriod().stream().filter(y -> y.getDatePeriod().start().beforeOrEquals(enDate) 
									&& y.getDatePeriod().end().afterOrEquals(startDate))
							.collect(Collectors.toList()).get(0).getWorkplaceId();
					ExtractionResultDetail exDetail = new ExtractionResultDetail(sid,
							date,
							lstFixItemCond.get(0).getFixedExtraItemMonName().v(),
							alarmContent,
							GeneralDateTime.now(),
							Optional.ofNullable(workplaceId),
							Optional.ofNullable(fixCond.getMessage().isPresent() ? fixCond.getMessage().get().v() : null),
							Optional.ofNullable(checkValue));
					List<ResultOfEachCondition> result = lstResultCondition.stream()
							.filter(x -> x.getCheckType() == AlarmListCheckType.FixCheck && x.getNo().equals(String.valueOf(fixCond.getFixedExtraItemMonNo().value)))
							.collect(Collectors.toList());
					if(result.isEmpty()) {
						ResultOfEachCondition resultCon = new ResultOfEachCondition(AlarmListCheckType.FixCheck,
								String.valueOf(fixCond.getFixedExtraItemMonNo().value),
								new ArrayList<>());
						resultCon.getLstResultDetail().add(exDetail);
						lstResultCondition.add(resultCon);
					} else {
						ResultOfEachCondition ex = result.get(0);
						lstResultCondition.remove(ex);
						ex.getLstResultDetail().add(exDetail);
						lstResultCondition.add(ex);
					}
				}
			}
		}
			
	}
	/**
	 * get period of check unused holiday
	 * @param currentPeriod
	 * @param deadlCheckMonth
	 * @return
	 */
	private YearMonth getDeadlCheckMonth(DatePeriod currentPeriod, int deadlCheckMonth) {
		GeneralDate endCurrentDate = currentPeriod.end();
		int currentMonth = endCurrentDate.month();
		int currentYear = endCurrentDate.year();
		
		int monthCheck = currentMonth - deadlCheckMonth;
		
		if (monthCheck <= 0) {
			monthCheck = monthCheck + 12; 
			currentYear = currentYear - 1;
		}
		
		return YearMonth.of(currentYear, monthCheck);
	}
	/**
	 * 任意抽出条件をチェック
	 * @param lstSid
	 * @param mPeriod
	 * @param getWplByListSidAndPeriod
	 * @param anyCond
	 * @param lstResultCondition
	 * @param data
	 */
	private void extractAnyCondAlarm(List<String> lstSid, YearMonthPeriod mPeriod, List<WorkPlaceHistImportAl> getWplByListSidAndPeriod,
			ExtraResultMonthly anyCond,
			List<ResultOfEachCondition> lstResultCondition, DataCheck data) {
		//残数チェック
		Optional<CheckRemainNumberMon> optRemainCond = remainNumberRepos.getByEralCheckID(anyCond.getErrorAlarmCheckID());
		Optional<AttendanceItemCondition> optCheckConMonthly = anyCond.getCheckConMonthly();
		if(!optCheckConMonthly.isPresent()) {
			return;
		}
		AttendanceItemCondition checkConMonthly = optCheckConMonthly.get();
		CheckRemainNumberMon remainCond = null;
		TypeCheckVacation checkVacation = TypeCheckVacation.ANNUAL_PAID_LEAVE;
		if(optRemainCond.isPresent()) {
			remainCond = optRemainCond.get();
			checkVacation = remainCond.getCheckVacation(); //チェックする休暇
			if(checkVacation == TypeCheckVacation.ANNUAL_PAID_LEAVE) {
				//社員の月毎の確定済み年休を取得する
				data.mapAnnLeaveData = getAnnLeaRemainData(lstSid, mPeriod);
			}
			if(checkVacation == TypeCheckVacation.YEARLY_RESERVED) {
				data.mapReserveData = getReserveLeaRemain(lstSid, mPeriod);
			}
		}
		YearMonth endMonthTemp = mPeriod.end().addMonths(1);
		GeneralDate endDateTemp = GeneralDate.ymd(endMonthTemp.year(), endMonthTemp.month(), 1);
		GeneralDate enDate = endDateTemp.addDays(-1);
		GeneralDate startDate = GeneralDate.ymd(mPeriod.start().year(), mPeriod.end().month(), 1);
		Map<String, AttendanceItemCondition> condition = new HashMap<>();
		condition.put(anyCond.getErrorAlarmCheckID(), anyCond.getCheckConMonthly().get());
		Map<String, Map<YearMonth, Map<String,String>>> resultsData = new HashMap<>();
		Map<String, Map<YearMonth, Map<String, Integer>>> checkPerTimeMonActualResult = new HashMap<>();
		if(anyCond.getTypeCheckItem().value > 3) {
			checkPerTimeMonActualResult = perTimeService.checkPerTimeMonActualResult(mPeriod, 
					lstSid,
					condition, 
					resultsData);
		}

		for(String sid : lstSid) {
			String workplaceId = getWplByListSidAndPeriod.stream().filter(x -> x.getEmployeeId().equals(sid))
					.collect(Collectors.toList())
					.get(0).getLstWkpIdAndPeriod().stream().filter(y -> y.getDatePeriod().start().beforeOrEquals(enDate) 
							&& y.getDatePeriod().end().afterOrEquals(startDate))
					.collect(Collectors.toList()).get(0).getWorkplaceId();
			
			if(remainCond != null) {//残数チェック
				CompareOperatorText compareOperatorText = convertComparaToText.convertCompareType(
						remainCond.getCheckOperatorType() == CheckOperatorType.SINGLE_VALUE 
								? checkConMonthly.getGroup1().getLstErAlAtdItemCon().get(0).getCompareSingleValue().getConditionType().value
								:checkConMonthly.getGroup1().getLstErAlAtdItemCon().get(0).getCompareRange().getCompareOperator().value);
				switch (checkVacation) {
				case ANNUAL_PAID_LEAVE:
					List<AnnualLeaveUsageDto> lstAnn = data.mapAnnLeaveData.get(sid);
					if(lstAnn.isEmpty()) {
						continue;
					}
					for(AnnualLeaveUsageDto annaData : lstAnn) {
						double remainingDays = annaData.getRemainingDays().v();
						chkCheckValue(anyCond, lstResultCondition, checkConMonthly, remainCond, sid, workplaceId,
								compareOperatorText, remainingDays, annaData.getYearMonth());							
					}
					break;
				case SUB_HOLIDAY:
					List<DayoffCurrentMonthOfEmployee> lstDayOffRemain = dayOffRemainExport.lstDayoffCurrentMonthOfEmployee(sid, mPeriod.start(), mPeriod.end());
					if(lstDayOffRemain.isEmpty()) {
						continue;
					}
					for(DayoffCurrentMonthOfEmployee dayOffRemain : lstDayOffRemain) {
						double remainingDays = dayOffRemain.getRemainingDays();
						chkCheckValue(anyCond, lstResultCondition, checkConMonthly, remainCond, sid, workplaceId,
								compareOperatorText, remainingDays, dayOffRemain.getYm());
					}
					break;
				case PAUSE:
					List<AbsenceleaveCurrentMonthOfEmployee> lstPauseRemain = pauseRemainExport.getDataCurrentMonthOfEmployee(sid, mPeriod.start(), mPeriod.end());
					if(lstPauseRemain.isEmpty()) {
						continue;
					}
					
					for(AbsenceleaveCurrentMonthOfEmployee pauseRemain: lstPauseRemain) {
						double remainDays = pauseRemain.getRemainingDays();
						chkCheckValue(anyCond, lstResultCondition, checkConMonthly, remainCond, sid, workplaceId,
								compareOperatorText, remainDays, pauseRemain.getYm());
					}
					
					break;
				case YEARLY_RESERVED:
					List<ReserveLeaveUsageDto> lstReserve = data.mapReserveData.get(sid);
					if(lstReserve.isEmpty()) {
						continue;
					}
					for(ReserveLeaveUsageDto reserve : lstReserve) {
						double remaiDays = reserve.getRemainingDays().v();
						chkCheckValue(anyCond, lstResultCondition, checkConMonthly, remainCond, sid, workplaceId,
								compareOperatorText, remaiDays, reserve.getYearMonth());
					}
					break;
				case SPECIAL_HOLIDAY:
					List<Integer> listSpeCode = remainCond.getListAttdID().get();
					List<SpecialHolidayRemainDataOutput> lstSpeHoliday = speHolidaySevice.getSpeHoliOfPeriodAndCodes(sid, mPeriod.start(), mPeriod.end(), listSpeCode);
					if(lstSpeHoliday.isEmpty()) {
						continue;
					}
					for(SpecialHolidayRemainDataOutput speHoliday : lstSpeHoliday) {
						double remainDay = speHoliday.getRemainDays();
						chkCheckValue(anyCond, lstResultCondition, checkConMonthly, remainCond, sid, workplaceId,
								compareOperatorText, remainDay, speHoliday.getYm());
					}
					
					default:
						break;
				}
			}

			if(anyCond.getTypeCheckItem().value > 3) { //チェック種類：時間、日数、回数、金額、複合条件

				for (YearMonth yearMonth : mPeriod.yearMonthsBetween()) {
					if(isError(checkPerTimeMonActualResult, anyCond.getErrorAlarmCheckID(), sid, yearMonth)) {
						if(anyCond.getTypeCheckItem() == TypeMonCheckItem.COMPOUND_CON) {
							extractCompoun(lstResultCondition, anyCond, sid, yearMonth, resultsData, data, workplaceId);
						} else {
							extractTimeDayTimesMoney(lstResultCondition, anyCond, sid, yearMonth, resultsData, data, workplaceId);
						}
					}
				}
				
			}
		}		
		
	}
	/**
	 * 積立年休
	 * @return
	 */
	private Map<String, List<ReserveLeaveUsageDto>> getReserveLeaRemain(List<String> lstSid, YearMonthPeriod mPeriod){
		Map<String, List<ReserveLeaveUsageDto>> mapResult = new HashMap<>();
		
		List<RsvLeaRemNumEachMonth> lstLeaRemains = leaRemainRepo.findBySidsAndYearMonths(lstSid, mPeriod.yearMonthsBetween());
		if(lstLeaRemains.isEmpty()) {
			return mapResult;
		}
		for(String sid : lstSid) {
			Map<YearMonth, ReserveLeaveUsageDto>  results = new HashMap<>();
			Map<YearMonth, GeneralDate> saveDates = new HashMap<>();
			List<RsvLeaRemNumEachMonth> lstLeaRemain = lstLeaRemains.stream().filter(x -> x.getEmployeeId().equals(sid))
					.collect(Collectors.toList());
			if(lstLeaRemain.isEmpty()) {
				continue;
			}
			for(RsvLeaRemNumEachMonth data : lstLeaRemain) {
				val yearMonth = data.getYearMonth();
				val reserveLeave = data.getReserveLeave();
				val usedNumber = reserveLeave.getUsedNumber();
				val remNumber = reserveLeave.getRemainingNumber();
				ReserveLeaveUsedDayNumber usedDays =
						new ReserveLeaveUsedDayNumber(usedNumber.getUsedDays().v());
				ReserveLeaveRemainingDayNumber remainingDays =
						new ReserveLeaveRemainingDayNumber(remNumber.getTotalRemainingDays().v());
				// 同じ年月が複数ある時、合算する
				if (results.containsKey(yearMonth)){
					val oldResult = results.get(yearMonth);
					val oldDate = saveDates.get(yearMonth);
					
					usedDays = new ReserveLeaveUsedDayNumber(usedDays.v() + oldResult.getUsedDays().v());
					
					// 積立年休残数に限り、締め期間．終了日の遅い方を保持する
					if (data.getClosurePeriod().end().before(oldDate)){
						remainingDays = new ReserveLeaveRemainingDayNumber(oldResult.getRemainingDays().v());
					}
					else {
						saveDates.put(yearMonth, data.getClosurePeriod().end());
					}
				}
				results.put(yearMonth, new ReserveLeaveUsageDto(
						yearMonth,
						usedDays,
						remainingDays));
				
				saveDates.putIfAbsent(yearMonth, data.getClosurePeriod().end());
			}
			// 積立年休月別残数データリストを返す
			val resultList = results.values().stream().collect(Collectors.toList());
			resultList.sort((a, b) -> a.getYearMonth().compareTo(b.getYearMonth()));
			mapResult.put(sid, resultList);
		}
		return mapResult;
	}
	
	private void chkCheckValue(ExtraResultMonthly anyCond, List<ResultOfEachCondition> lstResultCondition,
			AttendanceItemCondition checkConMonthly, CheckRemainNumberMon remainCond, String sid, String workplaceId,
			CompareOperatorText compareOperatorText, double remainingDays, YearMonth ym) {
		GeneralDate date = GeneralDate.ymd(ym.year(), ym.month(), 1);
		ErAlAttendanceItemCondition<?> erAlAtdItemCon = checkConMonthly.getGroup1()
				.getLstErAlAtdItemCon().get(0);
		String checkerValue;
		String alarmMessage;
		boolean checkTarget;
		checkTarget = erAlAtdItemCon.checkTarget(item -> {
			return Arrays.asList(remainingDays);
		});
		if(checkTarget) {
			String str = "";
			checkerValue = TextResource.localize("KAL010_306",TextResource.localize("KAL010_123"),
					String.valueOf(remainingDays));
			if(remainCond.getCheckOperatorType() == CheckOperatorType.SINGLE_VALUE){
				str = TextResource.localize("KAL010_984",
						remainCond.getCheckVacation().nameId,
						compareOperatorText.getCompareLeft(),
						checkConMonthly.getGroup1().getLstErAlAtdItemCon().get(0).getCompareSingleValue().getValue().toString());
			}else {
				if(checkConMonthly.getGroup1().getLstErAlAtdItemCon().get(0).getCompareRange().getCompareOperator().value <=7 ){
					str = TextResource.localize("KAL010_985",
							remainCond.getCheckVacation().nameId,
							compareOperatorText.getCompareLeft(),
							checkConMonthly.getGroup1().getLstErAlAtdItemCon().get(0).getCompareRange().getStartValue().toString(),
							checkConMonthly.getGroup1().getLstErAlAtdItemCon().get(0).getCompareRange().getEndValue().toString());
				}else {
					str = TextResource.localize("KAL010_986",
							remainCond.getCheckVacation().nameId,
							compareOperatorText.getCompareLeft(),
							checkConMonthly.getGroup1().getLstErAlAtdItemCon().get(0).getCompareRange().getStartValue().toString(),
							checkConMonthly.getGroup1().getLstErAlAtdItemCon().get(0).getCompareRange().getEndValue().toString());
				}
			}
			alarmMessage = TextResource.localize("KAL010_110",str,checkerValue);
			ExtractionAlarmPeriodDate pDate = new ExtractionAlarmPeriodDate(Optional.ofNullable(date),
					Optional.empty());
			setExtractAlarm(anyCond, lstResultCondition, sid, workplaceId, checkerValue, alarmMessage,
					pDate);
		}
	}
	private void setExtractAlarm(ExtraResultMonthly anyCond, List<ResultOfEachCondition> lstResultCondition, String sid,
			String workplaceId, String checkerValue, String alarmMessage, ExtractionAlarmPeriodDate pDate) {
		ExtractionResultDetail detail = new ExtractionResultDetail(sid, 
				pDate,
				anyCond.getNameAlarmExtraCon().v(),
				alarmMessage, 
				GeneralDateTime.now(),
				Optional.ofNullable(workplaceId),
				Optional.ofNullable(anyCond.getDisplayMessage().isPresent() ? anyCond.getDisplayMessage().get().v() : null),
				Optional.ofNullable(checkerValue));
		List<ResultOfEachCondition> result = lstResultCondition.stream()
				.filter(x -> x.getCheckType() == AlarmListCheckType.FreeCheck && x.getNo().equals(String.valueOf(anyCond.getSortBy())))
				.collect(Collectors.toList());
		if(result.isEmpty()) {
			ResultOfEachCondition resultCon = new ResultOfEachCondition(AlarmListCheckType.FreeCheck,
					String.valueOf(anyCond.getSortBy()),
					new ArrayList<>());
			resultCon.getLstResultDetail().add(detail);
		} else {
			ResultOfEachCondition ex = result.get(0);
			lstResultCondition.remove(ex);
			ex.getLstResultDetail().add(detail);
			lstResultCondition.add(ex);
		}
	}
	private void extractCompoun(List<ResultOfEachCondition> lstResultCondition, ExtraResultMonthly anyCond,
			String sid, YearMonth yearMonth, Map<String, Map<YearMonth, Map<String,String>>> resultsData, DataCheck data
			, String workplaceId) {
		String checkedValue = resultsData.get(sid).get(yearMonth).get(anyCond.getErrorAlarmCheckID());
		String alarmDescription2 = "";
		List<ErAlAttendanceItemCondition<?> > listErAlAtdItemCon = anyCond.getCheckConMonthly().get().getGroup1().getLstErAlAtdItemCon();
		
		//group 1 
		String alarmDescription1 = getDesGroup(anyCond, listErAlAtdItemCon, data);
		if(anyCond.getCheckConMonthly().get().isUseGroup2()) {
			List<ErAlAttendanceItemCondition<?> > listErAlAtdItemCon2 = anyCond.getCheckConMonthly().get().getGroup2().getLstErAlAtdItemCon();
			//group 2 
			alarmDescription2 = getDesGroup(anyCond, listErAlAtdItemCon2, data);
		}
		String alarmDescriptionValue= "";
		if(anyCond.getCheckConMonthly().get().getOperatorBetweenGroups() == LogicalOperator.AND) {//AND
			if(!alarmDescription2.equals("")) {
				alarmDescriptionValue = "("+alarmDescription1+") AND ("+alarmDescription2+")";
			}else {
				alarmDescriptionValue = alarmDescription1;
			}
		}else{
			if(!alarmDescription2.equals("")) {
				alarmDescriptionValue = "("+alarmDescription1+") OR ("+alarmDescription2+")";
			}else {
				alarmDescriptionValue = alarmDescription1;
			}
		}
		ExtractionAlarmPeriodDate periodDate = new ExtractionAlarmPeriodDate();
		periodDate.setStartDate(Optional.ofNullable(GeneralDate.ymd(yearMonth.year(), yearMonth.month(), 1)));
		periodDate.setEndDate(Optional.empty());
		setExtractAlarm(anyCond, lstResultCondition, sid, workplaceId, checkedValue, alarmDescriptionValue, periodDate);		
	}
	private String getDesGroup(ExtraResultMonthly anyCond, List<ErAlAttendanceItemCondition<?>> listErAlAtdItemConG1, DataCheck data) {
		String alarmDescription = "";
		for(ErAlAttendanceItemCondition<?> erAlAtdItemCon : listErAlAtdItemConG1 ) {
			int compare =  erAlAtdItemCon.getCompareSingleValue() != null ? erAlAtdItemCon.getCompareSingleValue().getCompareOpertor().value
					: erAlAtdItemCon.getCompareRange().getCompareOperator().value;
		    String startValue ="";
			String endValue= "";
			String nameErrorAlarm = "";	
			List<MonthlyAttendanceItemNameDto> listAttdNameAddCompare = new ArrayList<>();
			//get name attdanceName 										
			if(erAlAtdItemCon.getType() == ErrorAlarmConditionType.FIXED_VALUE){
				 startValue = String.valueOf(erAlAtdItemCon.getCompareRange() != null ?
							new BigDecimal(((CheckedTimeDuration) erAlAtdItemCon.getCompareRange().getStartValue()).v())
							: erAlAtdItemCon.getCompareSingleValue().getValue());											
				//if type = time
				if(erAlAtdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION) {
					startValue =this.timeToString(erAlAtdItemCon.getCompareRange() != null ?
							Integer.valueOf(erAlAtdItemCon.getCompareRange().getStartValue().toString())
							: Integer.valueOf(erAlAtdItemCon.getCompareSingleValue().getValue().toString()));
				}	
			}else{
				//Integer singleAtdItem[] = {Integer.valueOf(erAlAtdItemCon.getCountableTarget().getAddSubAttendanceItems()) };												
				List<Integer> singleAtdItemList= erAlAtdItemCon.getCountableTarget().getAddSubAttendanceItems().getAdditionAttendanceItems();
				listAttdNameAddCompare = data.lstItemMond.stream().filter(ati -> singleAtdItemList
						.contains(ati.getAttendanceItemId())).collect(Collectors.toList());
				startValue = getNameErrorAlarm(listAttdNameAddCompare,0,nameErrorAlarm);										 
			}
		
			List<MonthlyAttendanceItemNameDto> listAttdNameAdd =  data.lstItemMond.stream()
					.filter(ati -> erAlAtdItemCon.getCountableTarget().getAddSubAttendanceItems().getAdditionAttendanceItems()
							.contains(ati.getAttendanceItemId())).collect(Collectors.toList());
			nameErrorAlarm = getNameErrorAlarm(listAttdNameAdd,0,nameErrorAlarm);//0 add atd item
			List<MonthlyAttendanceItemNameDto> listAttdNameSub =  data.lstItemMond.stream()
					.filter(ati -> erAlAtdItemCon.getCountableTarget().getAddSubAttendanceItems().getSubstractionAttendanceItems()
							.contains(ati.getAttendanceItemId())).collect(Collectors.toList());
			nameErrorAlarm = getNameErrorAlarm(listAttdNameSub,1,nameErrorAlarm);//1 sub atd item

			CompareOperatorText compareOperatorText = convertComparaToText.convertCompareType(compare);
			//0 : AND, 1 : OR
			String compareAndOr = "";
			if(anyCond.getCheckConMonthly().get().getGroup1().getConditionOperator() == LogicalOperator.AND) {
				compareAndOr = "AND";
			}else {
				compareAndOr = "OR";
			}
			if(!alarmDescription.equals("")) {
				alarmDescription += compareAndOr +" ";
			}
			if(compare<=5) {
					alarmDescription +=  nameErrorAlarm + " " + compareOperatorText.getCompareLeft()+" "+ startValue+" ";											
																										
			}else {
				endValue = String.valueOf(erAlAtdItemCon.getCompareRange().getEndValue());
				if(erAlAtdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION) {
					endValue =  this.timeToString(Integer.valueOf(erAlAtdItemCon.getCompareRange().getStartValue().toString())); 
				}
				if(compare>5 && compare<=7) {
					alarmDescription += startValue +" "+
							compareOperatorText.getCompareLeft()+ " "+
							nameErrorAlarm+ " "+
							compareOperatorText.getCompareright()+ " "+
							endValue+ " ";	
				}else {
					alarmDescription += nameErrorAlarm + " "+
							compareOperatorText.getCompareLeft()+ " "+
							startValue + ","+endValue+ " "+
							compareOperatorText.getCompareright()+ " "+
							nameErrorAlarm+ " " ;
				}
			}

					
		}
		return alarmDescription;
	}
	/**
	 * 時間、日数、回数、金額
	 * @param lstResultCondition
	 * @param anyCond
	 * @param sid
	 * @param yearMonth
	 * @param resultsData
	 * @param data
	 * @param workplaceId
	 */
	private void extractTimeDayTimesMoney(List<ResultOfEachCondition> lstResultCondition, ExtraResultMonthly anyCond,
			String sid, YearMonth yearMonth, Map<String, Map<YearMonth, Map<String,String>>> resultsData, DataCheck data
			, String workplaceId) {
		String checkedValue = resultsData.get(sid).get(yearMonth).get(anyCond.getErrorAlarmCheckID());
		ErAlAttendanceItemCondition<?> erAlAtdItemConAdapterDto = anyCond.getCheckConMonthly().get().getGroup1().getLstErAlAtdItemCon().get(0);
		int compare = erAlAtdItemConAdapterDto.getCompareSingleValue() != null ? erAlAtdItemConAdapterDto.getCompareSingleValue().getCompareOpertor().value
				: erAlAtdItemConAdapterDto.getCompareRange().getCompareOperator().value;
		
		BigDecimal startValue = erAlAtdItemConAdapterDto.getCompareRange() != null ?
				new BigDecimal(erAlAtdItemConAdapterDto.getCompareRange().getStartValue().toString())
				: new BigDecimal(erAlAtdItemConAdapterDto.getCompareSingleValue().getValue().toString());
		BigDecimal endValue = erAlAtdItemConAdapterDto.getCompareRange() != null ? 
				new BigDecimal(erAlAtdItemConAdapterDto.getCompareRange().getEndValue().toString())
				: null;
		CompareOperatorText compareOperatorText = convertComparaToText.convertCompareType(compare);
		String nameErrorAlarm = "";
		//0 is monthly,1 is dayly
		List<MonthlyAttendanceItemNameDto> listAttdNameAdd =  data.lstItemMond.stream()
				.filter(c -> erAlAtdItemConAdapterDto.getCountableTarget().getAddSubAttendanceItems().getAdditionAttendanceItems()
						.contains(c.getAttendanceItemId())).collect(Collectors.toList());
		nameErrorAlarm = getNameErrorAlarm(listAttdNameAdd,0,nameErrorAlarm);//0 add atd item
		List<MonthlyAttendanceItemNameDto> listAttdNameSub =  data.lstItemMond.stream()
				.filter(c -> erAlAtdItemConAdapterDto.getCountableTarget().getAddSubAttendanceItems().getSubstractionAttendanceItems()
						.contains(c.getAttendanceItemId())).collect(Collectors.toList());
		nameErrorAlarm = getNameErrorAlarm(listAttdNameSub,1,nameErrorAlarm);//1 sub atd item
		String alarmDescription = "";
		switch (anyCond.getTypeCheckItem()) {
			case TIME:
				String startValueTime = this.timeToString(startValue.intValue());
				String endValueTime = "";
				if(compare<=5) {
					alarmDescription = TextResource.localize("KAL010_276",
							nameErrorAlarm,
							compareOperatorText.getCompareLeft(),
							startValueTime);
				}else {
					endValueTime = this.timeToString(endValue.intValue());
					if(compare>5 && compare<=7) {
						alarmDescription = TextResource.localize("KAL010_277",startValueTime,
								compareOperatorText.getCompareLeft(),
								nameErrorAlarm,
								compareOperatorText.getCompareright()+
								endValueTime
								);	
					}else {
						alarmDescription = TextResource.localize("KAL010_277",
								nameErrorAlarm,
								compareOperatorText.getCompareLeft(),
								startValueTime + ","+endValueTime,
								compareOperatorText.getCompareright()+
								nameErrorAlarm
								);
					}
				}
				checkedValue = this.timeToString(Double.valueOf(checkedValue).intValue());
				break;
			case DAYS:
				String startValueDays = String.valueOf(startValue.intValue());
				String endValueDays = "";
				if(compare<=5) {
					alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueDays);
				}else {
					endValueDays = String.valueOf(endValue.intValue());
					if(compare>5 && compare<=7) {
						alarmDescription = TextResource.localize("KAL010_277",startValueDays,
								compareOperatorText.getCompareLeft(),
								nameErrorAlarm,
								compareOperatorText.getCompareright(),
								endValueDays
								);	
					}else {
						alarmDescription = TextResource.localize("KAL010_277",
								nameErrorAlarm,
								compareOperatorText.getCompareLeft(),
								startValueDays + "," + endValueDays,
								compareOperatorText.getCompareright(),
								nameErrorAlarm
								);
					}
				}
				break;
			case TIMES:
				String startValueTimes = String.valueOf(startValue.intValue());
				String endValueTimes = "";
				if(compare<=5) {
					alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueTimes);
				}else {
					endValueTimes = String.valueOf(endValue.intValue());
					if(compare>5 && compare<=7) {
						alarmDescription = TextResource.localize("KAL010_277",startValueTimes,
								compareOperatorText.getCompareLeft(),
								nameErrorAlarm,
								compareOperatorText.getCompareright()+
								endValueTimes
								);	
					}else {
						alarmDescription = TextResource.localize("KAL010_277",
								nameErrorAlarm,
								compareOperatorText.getCompareLeft(),
								startValueTimes + "," + endValueTimes,
								compareOperatorText.getCompareright()+
								nameErrorAlarm
								);
					}
				}
				break;
			case AMOUNT_OF_MONEY:
				String startValueMoney = String.valueOf(startValue.intValue());
				String endValueMoney = "";
				if(compare<=5) {
					alarmDescription = TextResource.localize("KAL010_276",nameErrorAlarm,compareOperatorText.getCompareLeft(),startValueMoney);
				}else {
					endValueMoney = String.valueOf(endValue.intValue());
					if(compare>5 && compare<=7) {
						alarmDescription = TextResource.localize("KAL010_277",startValueMoney,
								compareOperatorText.getCompareLeft(),
								nameErrorAlarm,
								compareOperatorText.getCompareright()+
								endValueMoney
								);	
					}else {
						alarmDescription = TextResource.localize("KAL010_277",
								nameErrorAlarm,
								compareOperatorText.getCompareLeft(),
								startValueMoney + "," + endValueMoney,
								compareOperatorText.getCompareright()+
								nameErrorAlarm
								);
					}
				}
				break;
			default:
				break;
		}
		ExtractionAlarmPeriodDate periodDate = new ExtractionAlarmPeriodDate();
		periodDate.setStartDate(Optional.ofNullable(GeneralDate.ymd(yearMonth.year(), yearMonth.month(), 1)));
		periodDate.setEndDate(Optional.empty());
		ExtractionResultDetail detail = new ExtractionResultDetail(sid,
				periodDate,
				anyCond.getNameAlarmExtraCon().v(),
				alarmDescription,
				GeneralDateTime.now(),
				Optional.ofNullable(workplaceId),
				anyCond.getDisplayMessage().isPresent() ? Optional.ofNullable(anyCond.getDisplayMessage().get().v()) : Optional.empty(),
				Optional.ofNullable(checkedValue));
		List<ResultOfEachCondition> result = lstResultCondition.stream()
				.filter(x -> x.getCheckType() == AlarmListCheckType.FreeCheck && x.getNo().equals(String.valueOf(anyCond.getSortBy())))
				.collect(Collectors.toList());
		if(result.isEmpty()) {
			ResultOfEachCondition resultCon = new ResultOfEachCondition(AlarmListCheckType.FreeCheck,
					String.valueOf(anyCond.getSortBy()),
					new ArrayList<>());
			resultCon.getLstResultDetail().add(detail);
			lstResultCondition.add(resultCon);
		} else {
			ResultOfEachCondition ex = result.get(0);
			lstResultCondition.remove(ex);
			ex.getLstResultDetail().add(detail);
			lstResultCondition.add(ex);
		}
	}
	private String getNameErrorAlarm(List<MonthlyAttendanceItemNameDto> attendanceItemNames ,int type,String nameErrorAlarm){
		if(!CollectionUtil.isEmpty(attendanceItemNames)) {
			for(int i=0; i< attendanceItemNames.size(); i++) {
				String beforeOperator = "";
				String operator = (i == (attendanceItemNames.size() - 1)) ? "" : type == 1 ? " - " : " + ";
				
				if (!"".equals(nameErrorAlarm) || type == 1) {
					beforeOperator = (i == 0) ? type == 1 ? " - " : " + " : "";
				}
                nameErrorAlarm += beforeOperator + attendanceItemNames.get(i).getAttendanceItemName() + operator;
			}
		}		
		return nameErrorAlarm;
	}
	private String timeToString(int value ){
		if(value%60<10){
			return  String.valueOf(value/60)+":0"+  String.valueOf(value%60);
		}
		return String.valueOf(value/60)+":"+  String.valueOf(value%60);
	}

	private boolean isError(Map<String, Map<YearMonth, Map<String, Integer>>> checkPerTimeMonActualResults,
			String eralId, String empId, YearMonth yearMonth) {
		if(checkPerTimeMonActualResults.containsKey(empId)){
			if(checkPerTimeMonActualResults.get(empId).containsKey(yearMonth)) {
				if(checkPerTimeMonActualResults.get(empId).get(yearMonth).containsKey(eralId)) {
					return checkPerTimeMonActualResults.get(empId).get(yearMonth).get(eralId) == 1;
				}
			}
		}
		return false;
	}
	/**
	 * 年休月別残数データ
	 * @param lstSid
	 * @param mPeriod
	 * @return
	 */
	private Map<String,List<AnnualLeaveUsageDto>> getAnnLeaRemainData(List<String> lstSid, YearMonthPeriod mPeriod) {
		List<AnnLeaRemNumEachMonth> findBySidsAndYearMonthss = annLeaRepos.findBySidsAndYearMonths(lstSid, mPeriod.yearMonthsBetween());
		Map<String,List<AnnualLeaveUsageDto>> mapResult = new HashMap<>();
		for(String sid : lstSid) {
			List<AnnLeaRemNumEachMonth> findBySidsAndYearMonths = findBySidsAndYearMonthss.stream()
					.filter(x -> x.getEmployeeId().equals(sid)).collect(Collectors.toList());
			if(findBySidsAndYearMonths.isEmpty()) {
				continue;
			}
			Map<YearMonth, AnnualLeaveUsageDto> results = new HashMap<>();
			Map<YearMonth, GeneralDate> saveDates = new HashMap<>();
			for (val data : findBySidsAndYearMonths){
				val yearMonth = data.getYearMonth();
				val annualLeave = data.getAnnualLeave();
				val usedNumber = annualLeave.getUsedNumber();
				val remNumber = annualLeave.getRemainingNumber();
				AnnualLeaveUsedDayNumber usedDays =
						new AnnualLeaveUsedDayNumber(usedNumber.getUsedDays().getUsedDays().v());
				UsedMinutes usedTime = null;
				if (usedNumber.getUsedTime().isPresent()){
					usedTime = new UsedMinutes(usedNumber.getUsedTime().get().getUsedTime().v());
				}
				AnnualLeaveRemainingDayNumber remainingDays =
						new AnnualLeaveRemainingDayNumber(remNumber.getTotalRemainingDays().v());
				RemainingMinutes remainingTime = null;
				if (remNumber.getTotalRemainingTime().isPresent()){
					remainingTime = new RemainingMinutes(remNumber.getTotalRemainingTime().get().v());
				}
				// 同じ年月が複数ある時、合算する
				if (results.containsKey(yearMonth)){
					val oldResult = results.get(yearMonth);
					val oldDate = saveDates.get(yearMonth);
					
					usedDays = new AnnualLeaveUsedDayNumber(usedDays.v() + oldResult.getUsedDays().v());
					if (oldResult.getUsedTime().isPresent()){
						if (usedTime == null){
							usedTime = new UsedMinutes(oldResult.getUsedTime().get().v());
						}
						else {
							usedTime = new UsedMinutes(usedTime.v() + oldResult.getUsedTime().get().v());
						}
					}
					
					// 年休残数に限り、締め期間．終了日の遅い方を保持する
					if (data.getClosurePeriod().end().before(oldDate)){
						remainingDays = new AnnualLeaveRemainingDayNumber(oldResult.getRemainingDays().v());
						remainingTime = null;
						if (oldResult.getRemainingTime().isPresent()){
							remainingTime = new RemainingMinutes(oldResult.getRemainingTime().get().v());
						}
					}
					else {
						saveDates.put(yearMonth, data.getClosurePeriod().end());
					}
				}
				results.put(yearMonth, new AnnualLeaveUsageDto(
						yearMonth,
						usedDays,
						Optional.ofNullable(usedTime),
						remainingDays,
						Optional.ofNullable(remainingTime)));
				saveDates.putIfAbsent(yearMonth, data.getClosurePeriod().end());
				
			}
			// 年休月別残数データリストを返す
			val resultList = results.values().stream().collect(Collectors.toList());
			resultList.sort((a, b) -> a.getYearMonth().compareTo(b.getYearMonth()));
			mapResult.put(sid, resultList);
		}
		
		return mapResult;
	}
	
	public class DataCheck {
		/**所属状況(年月)(AffiliationStatus)*/
		private EmpAffInfoExport empAffInfo;
		/**	月別実績の抽出条件	 */
		private List<ExtraResultMonthly> lstAnyCondMon;
		/**月別実績の勤怠項目チェック */
		private List<TimeItemCheckMonthly> lstTimeItem;
		/**	月別実績の固定抽出条件	 */
		private List<FixedExtraMon> lstFixCond; 
		/**月別実績の固定抽出項目 */
		private List<FixedExtraItemMon> lstFixItemCond;
		/**	月次の勤怠項目 */
		private List<MonthlyAttendanceItemNameDto> lstItemMond;
		/**年休月別残数データ		 */
		private Map<String, List<AnnualLeaveUsageDto>> mapAnnLeaveData;
		/**積立年休残数データ	 */
		private Map<String, List<ReserveLeaveUsageDto>> mapReserveData;
		/**本人確認処理の利用設定	 */
		private Optional<IdentityProcess> optIndentiry;
		/**月別実績の勤怠時間 */
		private List<AttendanceTimeOfMonthly> lstAttendanceTime;
		/**月の本人確認*/
		private List<ConfirmationMonth> lstMyselConfirm;
		/**承認処理の利用設定 */
		private Optional<ApprovalProcess> optApprovalSetting;
		/**代休管理設定 */
		private CompensatoryLeaveComSetting comLeaveSetting;
		/**雇用の代休管理設定	 */
		private List<CompensatoryLeaveEmSetting> lstCompenEmpSetting;
		/**社員ID（List）と指定期間から社員の雇用履歴		 */
		private List<SharedSidPeriodDateEmploymentImport> lstEmploymentHis;
		/**月別実績の編集状態 */
		private List<EditStateOfMonthlyPerformance> lstEditState; 
		/**雇用に紐づく締め	 */
		private List<ClosureEmployment> lstCloEmp;
		/**締め		 */
		private List<Closure> lstClosure;
		public DataCheck(String cid, List<String> lstSid, YearMonthPeriod mPeriod, String fixConId,
			List<String> lstAnyConID) {
			//社員の指定期間中の所属期間を取得する（年月）
			this.empAffInfo = workRecordEx.getAffiliationPeriod(lstSid, mPeriod, GeneralDate.today());
			//ドメインモデル「月別実績の抽出条件」を取得
			this.lstAnyCondMon = extCondMonRepo.getAnyItemBySidAndUseAtr(lstAnyConID, true);
			//TODO ドメインモデル「月別実績の勤怠項目チェック」を取得
			
			//ドメインモデル「月別実績の固定抽出条件」を取得する
			this.lstFixCond = fixCondRepo.getFixedItem(fixConId, true);
			if(!this.lstFixCond.isEmpty()) {
				this.lstFixItemCond = fixItemCondRepo.getAllFixedExtraItemMon();

				YearMonth endMonthTemp = mPeriod.end().addMonths(1);
				GeneralDate endDateTemp = GeneralDate.ymd(endMonthTemp.year(), endMonthTemp.month(), 1);
				GeneralDate enDate = endDateTemp.addDays(-1);
				GeneralDate startDate = GeneralDate.ymd(mPeriod.start().year(), mPeriod.end().month(), 1);
				DatePeriod dPeriod =  new DatePeriod(startDate, enDate);
				if(!this.lstFixCond.stream().filter(x -> x.getFixedExtraItemMonNo() == SysFixedMonPerEral.MYSELF_UNCONFIRMED)
						.collect(Collectors.toList()).isEmpty()) {
					//ドメインモデル「本人確認処理の利用設定」を取得
					this.optIndentiry = indentityRepo.getIdentityProcessById(cid);
					this.lstMyselConfirm = confirmRepo.findBySomeProperty(lstSid, mPeriod.yearMonthsBetween());
				}
				
				if(!this.lstFixCond.stream().filter(x -> x.getFixedExtraItemMonNo() == SysFixedMonPerEral.ADMIN_UNCONFIRMED)
						.collect(Collectors.toList()).isEmpty()) {
					this.optApprovalSetting = approvalRepo.getApprovalProcessById(cid);
					this.lstAttendanceTime = attendanceRepo.findBySidsAndYearMonths(lstSid, mPeriod.yearMonthsBetween());
				}
				if(!this.lstFixCond.stream().filter(x -> x.getFixedExtraItemMonNo() == SysFixedMonPerEral.CHECK_DEADLINE_HOLIDAY)
						.collect(Collectors.toList()).isEmpty()) {
					this.comLeaveSetting = compenLeaveRepo.find(cid);
					this.lstCompenEmpSetting = compenEmLeaveRepo.findAll(cid);
					this.lstCloEmp = cloEmpRepo.findAllByCid(cid);
					//社員ID（List）と指定期間から社員の雇用履歴を取得
					this.lstEmploymentHis = employmentAdapter.getEmpHistBySidAndPeriod(lstSid,dPeriod);
					this.lstClosure = closureRepo.findAll(cid);
				}
				if(!this.lstFixCond.stream().filter(x -> x.getFixedExtraItemMonNo() == SysFixedMonPerEral.MANUAL_INPUT)
						.collect(Collectors.toList()).isEmpty()) {
					this.lstEditState = editStateMonthRepo.findBySidsAndYM(lstSid, dPeriod);
				}
			}
			
			//社員ID（List）、期間を設定して月別実績を取得する
			
			//月次の勤怠項目を取得する
			this.lstItemMond = attendanceAdapter.getMonthlyAttendanceItem(2);
			
			
		}		
		
	}

}
