package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordImport;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.application.realitystatus.RealityStatusService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.CommonProcess;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.CalcPeriodForAggregate;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
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
public class ConfirmStatusInfoEmp {

	@Inject
	private CalcPeriodForAggregate calcPeriodForAggregate;

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

	/**
	 * 社員1件の確認状況情報を取得する
	 */
	public ConfirmInfoResult confirmStatusInfoOneEmp(String companyId, String employeeId, Optional<DatePeriod> periodOpt,
			Optional<YearMonth> yearMonthOpt) {
		List<ApproveRootStatusForEmpImport> lstApprovalMonthStatusAll = new ArrayList<>();
		List<ConfirmationMonth> lstConfirmMonthAll = new ArrayList<>();
		List<ClosurePeriod> lstClosure = new ArrayList<>();
		if (periodOpt.isPresent()) {
			// 期間を指定して集計期間を求める
			lstClosure = calcPeriodForAggregate.algorithm(companyId, employeeId, periodOpt.get().end());
		} else {
			// TODO: 年月を指定して集計期間を求める
		}

		// Output「締め処理期間．集計期間．期間」のMAX期間を求める
		List<DatePeriod> periods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream()).map(x -> x.getPeriod()).collect(Collectors.toList());
		DatePeriod dateMax = CommonProcess.getMaxPeriod(periods);
		if(dateMax == null) return null;
		List<StatusOfEmployeeExport> statusOfEmps = iFindDataDCRecord
				.getListAffComHistByListSidAndPeriod(Optional.empty(), Arrays.asList(employeeId), dateMax).stream()
				.filter(x -> x.getEmployeeId() != null).collect(Collectors.toList());
		
		if(statusOfEmps.isEmpty()) return null;
		//Output「社員の会社所属状況．所属状況．期間」のMAX期間を求める
		DatePeriod dateEmpExport = CommonProcess.getMaxPeriod(statusOfEmps.get(0).getListPeriod());
		// ドメインモデル「日の本人確認」を取得する
		List<Identification> indentities = identificationRepository.findByEmployeeID(employeeId,
				dateEmpExport.start(), dateEmpExport.end());
		
		// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
		List<ApproveRootStatusForEmpImport> lstApprovalDayStatus = approvalStatusAdapter
				.getApprovalByListEmplAndListApprovalRecordDateNew(dateEmpExport.datesBetween(),
						Arrays.asList(employeeId), 1);
		
		List<ApplicationRecordImport> lstApplication = applicationRecordAdapter.getApplicationBySID(Arrays.asList(employeeId), dateEmpExport.start(), dateEmpExport.end());
		
		
		List<AggrPeriodEachActualClosure> aggrPeriods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream()).collect(Collectors.toList());
        //TODO: Output「締め処理期間．実締め毎集計期間」の件数ループする
		for (AggrPeriodEachActualClosure mergePeriodClr : aggrPeriods) {
			
			List<ConfirmationMonth> lstConfirmMonth = confirmationMonthRepository.findBySomeProperty(Arrays.asList(employeeId),
					mergePeriodClr.getYearMonth().v(), mergePeriodClr.getClosureDate().getClosureDay().v(),
					mergePeriodClr.getClosureDate().getLastDayOfMonth(), mergePeriodClr.getClosureId().value);
			
			// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
			List<ApproveRootStatusForEmpImport> lstApprovalMonthStatus = approvalStatusAdapter
					.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(mergePeriodClr.getPeriod().end()),
							Arrays.asList(employeeId), 2);
			lstApprovalMonthStatusAll.addAll(lstApprovalMonthStatus);
			lstConfirmMonthAll.addAll(lstConfirmMonth);
		}
		
		//[No.303]対象期間に日別実績のエラーが発生している年月日を取得する
		List<EmployeeDateErrorOuput> lstOut = realityStatusService.checkEmployeeErrorOnProcessingDate(employeeId,
				dateEmpExport.start(), dateEmpExport.end()).stream().map(x -> {
					return new EmployeeDateErrorOuput(employeeId, x.getDate(), x.getHasError());
				}).collect(Collectors.toList());
		return ConfirmInfoResult.builder()
								.indentities(indentities)
								.lstApprovalDayStatus(lstApprovalDayStatus)
								.aggrPeriods(aggrPeriods)
								.lstApprovalMonthStatus(lstApprovalMonthStatusAll)
								.lstConfirmMonth(lstConfirmMonthAll)
								.lstApplication(lstApplication)
								.lstOut(lstOut)
								.statusOfEmps(statusOfEmps).build();
		
	}

	//複数社員の承認状況情報を取得する
	public ConfirmInfoResult confirmStatusInfoMulEmp(String companyId, List<String> employeeIds, Optional<DatePeriod> periodOpt,
			Optional<YearMonth> yearMonthOpt) {
		List<ApproveRootStatusForEmpImport> lstApprovalMonthStatusAll = new ArrayList<>();
		List<ConfirmationMonth> lstConfirmMonthAll = new ArrayList<>();
		List<Identification> indentityAll = new ArrayList<>();
		List<ApproveRootStatusForEmpImport> lstApprovalDayStatuAll = new ArrayList<>();
		List<ApplicationRecordImport> lstApplicationAll = new ArrayList<>();
		List<EmployeeDateErrorOuput> lstOutAll = new ArrayList<>();
		List<AggrPeriodEachActualClosure> aggrPeriodAll = new ArrayList<>();
		List<StatusOfEmployeeExport> latStatusOfEmpAll = new ArrayList<>();
		for (String employeeId : employeeIds) {
			List<ClosurePeriod> lstClosure = new ArrayList<>();
			if (periodOpt.isPresent()) {
				// 期間を指定して集計期間を求める
				lstClosure = calcPeriodForAggregate.algorithm(companyId, employeeId, periodOpt.get().end());
			} else {
				// TODO: 年月を指定して集計期間を求める
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

			latStatusOfEmpAll.addAll(statusOfEmps);
			if (statusOfEmps.isEmpty())
				return null;
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
			
			//[No.26]社員、期間に一致する申請を取得する
			List<ApplicationRecordImport> lstApplication = applicationRecordAdapter.getApplicationBySID(Arrays.asList(employeeId), dateEmpExport.start(), dateEmpExport.end());
			lstApplicationAll.addAll(lstApplication);
			//[No.303]対象期間に日別実績のエラーが発生している年月日を取得する
			//[No.303]対象期間に日別実績のエラーが発生している年月日を取得する
			List<EmployeeDateErrorOuput> lstOut = realityStatusService.checkEmployeeErrorOnProcessingDate(employeeId,
					dateEmpExport.start(), dateEmpExport.end()).stream().map(x -> {
						return new EmployeeDateErrorOuput(employeeId, x.getDate(), x.getHasError());
					}).collect(Collectors.toList());
			lstOutAll.addAll(lstOut);
			
			//TODO: Output「締め処理期間．実締め毎集計期間」の件数ループする
			List<AggrPeriodEachActualClosure> aggrPeriods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream()).collect(Collectors.toList());
			aggrPeriodAll.addAll(aggrPeriods);
			for (AggrPeriodEachActualClosure mergePeriodClr : aggrPeriods) {
				
				List<ConfirmationMonth> lstConfirmMonth = confirmationMonthRepository.findBySomeProperty(Arrays.asList(employeeId),
						mergePeriodClr.getYearMonth().v(), mergePeriodClr.getClosureDate().getClosureDay().v(),
						mergePeriodClr.getClosureDate().getLastDayOfMonth(), mergePeriodClr.getClosureId().value);
				
				// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
				List<ApproveRootStatusForEmpImport> lstApprovalMonthStatus = approvalStatusAdapter
						.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(mergePeriodClr.getPeriod().end()),
								Arrays.asList(employeeId), 2);
				lstApprovalMonthStatusAll.addAll(lstApprovalMonthStatus);
				lstConfirmMonthAll.addAll(lstConfirmMonth);
			}
			
		}
		
		 return ConfirmInfoResult.builder()
				.indentities(indentityAll)
				.lstApprovalDayStatus(lstApprovalDayStatuAll)
				.aggrPeriods(aggrPeriodAll)
				.lstApprovalMonthStatus(lstApprovalMonthStatusAll)
				.lstConfirmMonth(lstConfirmMonthAll)
				.lstApplication(lstApplicationAll)
				.lstOut(lstOutAll)
				.statusOfEmps(latStatusOfEmpAll).build();
	}
}
