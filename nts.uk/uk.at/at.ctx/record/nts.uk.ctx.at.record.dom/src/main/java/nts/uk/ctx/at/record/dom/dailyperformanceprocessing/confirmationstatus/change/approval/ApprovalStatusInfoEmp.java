package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.approval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordImport;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootOfEmpMonthImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.application.realitystatus.RealityStatusService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.CommonProcess;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.ConfirmInfoResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.EmployeeDateErrorOuput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.InformationDay;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.InformationMonth;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 *
 */
@Stateless
public class ApprovalStatusInfoEmp {

	@Inject
	private IFindDataDCRecord iFindDataDCRecord;

	@Inject
	private IdentificationRepository identificationRepository;

	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	@Inject
	private ApplicationRecordAdapter applicationRecordAdapter;

	@Inject
	private ConfirmationMonthRepository confirmationMonthRepository;

	@Inject
	private RealityStatusService realityStatusService;
	
	@Inject
	private GetClosurePeriod getClosurePeriod;

	public List<ConfirmInfoResult> approvalStatusInfoOneEmp(String companyId, String empTarget, String employeeId,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt, boolean isCallBy587) {
		List<ClosurePeriod> lstClosure = new ArrayList<>();
		List<AppRootOfEmpMonthImport> lstAppRootOfEmpMonth = new ArrayList<>();
		
		if (periodOpt.isPresent()) {
			// 期間を指定して集計期間を求める
			lstClosure = getClosurePeriod.fromPeriod(employeeId, periodOpt.get().end(), periodOpt.get());
		} else {
			// 年月を指定して集計期間を求める
			GeneralDate dateRefer = GeneralDate.ymd(yearMonthOpt.get().year(), yearMonthOpt.get().month(), yearMonthOpt.get().lastDateInMonth());
			lstClosure = getClosurePeriod.fromYearMonth(employeeId, dateRefer, yearMonthOpt.get());
		}

		// Output「締め処理期間．集計期間．期間」のMAX期間を求める
		List<DatePeriod> periods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream()).map(x -> x.getPeriod())
				.collect(Collectors.toList());
		DatePeriod dateMax = CommonProcess.getMaxPeriod(periods);
		if (dateMax == null)
			return new ArrayList<>();

		List<StatusOfEmployeeExport> statusOfEmps = iFindDataDCRecord
				.getListAffComHistByListSidAndPeriod(Optional.empty(), Arrays.asList(employeeId), dateMax).stream()
				.filter(x -> x.getEmployeeId() != null).collect(Collectors.toList());
		if (statusOfEmps.isEmpty())
			return new ArrayList<>();
		// Output「社員の会社所属状況．所属状況．期間」のMAX期間を求める
		DatePeriod dateEmpExport = CommonProcess.getMaxPeriod(statusOfEmps.get(0).getListPeriod());
		// ドメインモデル「日の本人確認」を取得する
		List<Identification> indentities = identificationRepository.findByEmployeeID(employeeId, dateEmpExport.start(),
				dateEmpExport.end());

		// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
		List<ApproveRootStatusForEmpImport> lstApprovalDayStatus = approvalStatusAdapter
				.getApprovalByListEmplAndListApprovalRecordDateNew(dateEmpExport.datesBetween(),
						Arrays.asList(employeeId), 1);
		// lstApprovalDayStatuAll.addAll(lstApprovalDayStatus);
		// [No.595]対象者を指定して承認状況を取得する
		ApprovalRootOfEmployeeImport approvalRootDaily = approvalStatusAdapter.getDailyApprovalStatus(empTarget,
				Arrays.asList(employeeId), dateEmpExport);

		// [No.26]社員、期間に一致する申請を取得する
		List<ApplicationRecordImport> lstApplication = applicationRecordAdapter
				.getApplicationBySID(Arrays.asList(employeeId), dateEmpExport.start(), dateEmpExport.end());

		// TODO: Output「締め処理期間．実締め毎集計期間」の件数ループする
		List<AggrPeriodEachActualClosure> aggrPeriods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream())
				.collect(Collectors.toList());
		List<InformationMonth> inforMonths = new ArrayList<>();
		for (AggrPeriodEachActualClosure mergePeriodClr : aggrPeriods) {

			List<ConfirmationMonth> lstConfirmMonth = confirmationMonthRepository.findBySomeProperty(
					Arrays.asList(employeeId), mergePeriodClr.getYearMonth().v(),
					mergePeriodClr.getClosureDate().getClosureDay().v(),
					mergePeriodClr.getClosureDate().getLastDayOfMonth(), mergePeriodClr.getClosureId().value);

			// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
			List<ApproveRootStatusForEmpImport> lstApprovalMonthStatus = approvalStatusAdapter
					.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(mergePeriodClr.getOriginalClosurePeriod().end()),
							Arrays.asList(employeeId), 2);

			// [No.534](中間データ版)承認状況を取得する （月別）
			if (isCallBy587) {
				AppRootOfEmpMonthImport appRootOfEmpMonth = approvalStatusAdapter.getApprovalEmpStatusMonth(empTarget,
						mergePeriodClr.getYearMonth(), mergePeriodClr.getClosureId().value,
						mergePeriodClr.getClosureDate(), mergePeriodClr.getPeriod().end(), true,
						mergePeriodClr.getPeriod());
				lstAppRootOfEmpMonth.add(appRootOfEmpMonth);

			}
			inforMonths.add(new InformationMonth(mergePeriodClr, lstConfirmMonth, lstApprovalMonthStatus,
					lstAppRootOfEmpMonth));
		}

		// [No.303]対象期間に日別実績のエラーが発生している年月日を取得する
		List<EmployeeDateErrorOuput> lstOut = realityStatusService
				.checkEmployeeErrorOnProcessingDate(employeeId, dateEmpExport.start(), dateEmpExport.end()).stream()
				.map(x -> {
					return new EmployeeDateErrorOuput(employeeId, x.getDate(), x.getHasError());
				}).collect(Collectors.toList());

		return Arrays.asList(ConfirmInfoResult.builder().employeeId(employeeId).period(dateMax)
				.informationDay(new InformationDay(indentities, lstApprovalDayStatus, Arrays.asList(approvalRootDaily)))
				.lstApplication(lstApplication).lstOut(lstOut).statusOfEmp(statusOfEmps.get(0))
				.informationMonths(inforMonths).build());

	}

	public List<ConfirmInfoResult> approvalStatusInfoMulEmp(String companyId, String empTarget,
			List<String> employeeIds, Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt,
			boolean isCallBy587) {
		List<ConfirmInfoResult> results = new ArrayList<>();
		List<ApprovalRootOfEmployeeImport> lstApprovalRootDaily = new ArrayList<>();
		List<ApproveRootStatusForEmpImport> lstApprovalMonthStatusAll = new ArrayList<>();
		List<ConfirmationMonth> lstConfirmMonthAll = new ArrayList<>();
		List<Identification> indentityAll = new ArrayList<>();
		List<ApproveRootStatusForEmpImport> lstApprovalDayStatuAll = new ArrayList<>();
		List<ApplicationRecordImport> lstApplicationAll = new ArrayList<>();
		List<EmployeeDateErrorOuput> lstOutAll = new ArrayList<>();
		List<AggrPeriodEachActualClosure> aggrPeriodAll = new ArrayList<>();
		List<AppRootOfEmpMonthImport> lstAppRootOfEmpMonth = new ArrayList<>();
		List<StatusOfEmployeeExport> lstStatusOfEmpAll = new ArrayList<>();
		for (String employeeId : employeeIds) {
			List<ClosurePeriod> lstClosure = new ArrayList<>();
			if (periodOpt.isPresent()) {
				// 期間を指定して集計期間を求める
				lstClosure = getClosurePeriod.fromPeriod(employeeId, periodOpt.get().end(), periodOpt.get());
			} else {
				// TODO: 年月を指定して集計期間を求める
				GeneralDate dateRefer = GeneralDate.ymd(yearMonthOpt.get().year(), yearMonthOpt.get().month(), yearMonthOpt.get().lastDateInMonth());
				lstClosure = getClosurePeriod.fromYearMonth(employeeId, dateRefer, yearMonthOpt.get());
			}

			// Output「締め処理期間．集計期間．期間」のMAX期間を求める
			List<DatePeriod> periods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream())
					.map(x -> x.getPeriod()).collect(Collectors.toList());
			DatePeriod dateMax = CommonProcess.getMaxPeriod(periods);
			if (dateMax == null)
				continue;

			List<StatusOfEmployeeExport> statusOfEmps = iFindDataDCRecord
					.getListAffComHistByListSidAndPeriod(Optional.empty(), Arrays.asList(employeeId), dateMax).stream()
					.filter(x -> x.getEmployeeId() != null).collect(Collectors.toList());
			lstStatusOfEmpAll.addAll(statusOfEmps);
			if (statusOfEmps.isEmpty())
				continue;
			// Output「社員の会社所属状況．所属状況．期間」のMAX期間を求める
			DatePeriod dateEmpExport = CommonProcess.getMaxPeriod(statusOfEmps.get(0).getListPeriod());
			// ドメインモデル「日の本人確認」を取得する
			List<Identification> indentities = identificationRepository.findByEmployeeID(employeeId,
					dateEmpExport.start(), dateEmpExport.end());
			indentityAll.addAll(indentities);
			// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
			List<ApproveRootStatusForEmpImport> lstApprovalDayStatus = approvalStatusAdapter
					.getApprovalByListEmplAndListApprovalRecordDateNew(dateEmpExport.datesBetween(),
							Arrays.asList(employeeId), 1);
			lstApprovalDayStatuAll.addAll(lstApprovalDayStatus);
			// [No.595]対象者を指定して承認状況を取得する
			ApprovalRootOfEmployeeImport approvalRootDaily = approvalStatusAdapter.getDailyApprovalStatus(empTarget,
					Arrays.asList(employeeId), dateEmpExport);
			lstApprovalRootDaily.add(approvalRootDaily);
			// [No.26]社員、期間に一致する申請を取得する
			List<ApplicationRecordImport> lstApplication = applicationRecordAdapter
					.getApplicationBySID(Arrays.asList(employeeId), dateEmpExport.start(), dateEmpExport.end());
			lstApplicationAll.addAll(lstApplication);
			// TODO: Output「締め処理期間．実締め毎集計期間」の件数ループする
			List<AggrPeriodEachActualClosure> aggrPeriods = lstClosure.stream()
					.flatMap(x -> x.getAggrPeriods().stream()).collect(Collectors.toList());
			aggrPeriodAll.addAll(aggrPeriods);
			List<InformationMonth> inforMonths = new ArrayList<>();
			for (AggrPeriodEachActualClosure mergePeriodClr : aggrPeriods) {

				List<ConfirmationMonth> lstConfirmMonth = confirmationMonthRepository.findBySomeProperty(
						Arrays.asList(employeeId), mergePeriodClr.getYearMonth().v(),
						mergePeriodClr.getClosureDate().getClosureDay().v(),
						mergePeriodClr.getClosureDate().getLastDayOfMonth(), mergePeriodClr.getClosureId().value);

				// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
				List<ApproveRootStatusForEmpImport> lstApprovalMonthStatus = approvalStatusAdapter
						.getApprovalByListEmplAndListApprovalRecordDateNew(
								Arrays.asList(mergePeriodClr.getOriginalClosurePeriod().end()), Arrays.asList(employeeId), 2);

				// [No.534](中間データ版)承認状況を取得する （月別）
				if (isCallBy587) {
					AppRootOfEmpMonthImport appRootOfEmpMonth = approvalStatusAdapter.getApprovalEmpStatusMonth(
							empTarget, mergePeriodClr.getYearMonth(), mergePeriodClr.getClosureId().value,
							mergePeriodClr.getClosureDate(), mergePeriodClr.getPeriod().end(), true,
							mergePeriodClr.getPeriod());
					lstAppRootOfEmpMonth.add(appRootOfEmpMonth);
				}

				lstApprovalMonthStatusAll.addAll(lstApprovalMonthStatus);
				lstConfirmMonthAll.addAll(lstConfirmMonth);
				inforMonths.add(new InformationMonth(mergePeriodClr, lstConfirmMonth, lstApprovalMonthStatus,
						lstAppRootOfEmpMonth));
			}
			// [No.303]対象期間に日別実績のエラーが発生している年月日を取得する
			List<EmployeeDateErrorOuput> lstOut = realityStatusService
					.checkEmployeeErrorOnProcessingDate(employeeId, dateEmpExport.start(), dateEmpExport.end()).stream()
					.map(x -> {
						return new EmployeeDateErrorOuput(employeeId, x.getDate(), x.getHasError());
					}).collect(Collectors.toList());
			lstOutAll.addAll(lstOut);
			ConfirmInfoResult result = ConfirmInfoResult.builder().employeeId(employeeId).period(dateMax)
					.informationDay(new InformationDay(indentities, lstApprovalDayStatus, lstApprovalRootDaily))
					.lstApplication(lstApplication).lstOut(lstOut).statusOfEmp(statusOfEmps.get(0))
					.informationMonths(inforMonths).build();
			results.add(result);
		};
		return results;
	}
}
