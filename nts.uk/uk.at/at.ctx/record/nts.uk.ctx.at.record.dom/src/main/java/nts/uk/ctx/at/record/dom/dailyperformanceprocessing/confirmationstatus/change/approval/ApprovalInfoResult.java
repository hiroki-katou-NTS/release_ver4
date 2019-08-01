package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.approval;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootOfEmpMonthImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.ConfirmInfoResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.EmployeeDateErrorOuput;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;

@Getter
public class ApprovalInfoResult extends ConfirmInfoResult {

	List<ApprovalRootOfEmployeeImport> lstApprovalRootDaily;

	List<AppRootOfEmpMonthImport> lstAppRootOfEmpMonth;

	public ApprovalInfoResult(ConfirmInfoResult confirmResult, List<ApprovalRootOfEmployeeImport> lstApprovalRootDaily,
			List<AppRootOfEmpMonthImport> lstAppRootOfEmpMonth) {
		super(confirmResult.getIndentities(), confirmResult.getLstApprovalDayStatus(), confirmResult.getAggrPeriods(),
				confirmResult.getLstConfirmMonth(), confirmResult.getLstApprovalMonthStatus(),
				confirmResult.getLstOut(), confirmResult.getLstApplication(), confirmResult.getStatusOfEmps());
		this.lstApprovalRootDaily = lstApprovalRootDaily;
		this.lstAppRootOfEmpMonth = lstAppRootOfEmpMonth;
	}

	public ApprovalInfoResult getDataWithEmp(String empTarget) {
		List<Identification> indentities = this.getIndentities().stream()
				.filter(x -> x.getEmployeeId().equals(empTarget)).collect(Collectors.toList());
		List<ApproveRootStatusForEmpImport> lstApprovalDayStatus = this.getLstApprovalDayStatus().stream()
				.filter(x -> x.getEmployeeID().equals(empTarget)).collect(Collectors.toList());
		List<ConfirmationMonth> lstConfirmMonth = this.getLstConfirmMonth().stream()
				.filter(x -> x.getEmployeeId().equals(empTarget)).collect(Collectors.toList());
		List<ApproveRootStatusForEmpImport> lstApprovalMonthStatus = this.getLstApprovalMonthStatus().stream()
				.filter(x -> x.getEmployeeID().equals(empTarget)).collect(Collectors.toList());
		List<EmployeeDateErrorOuput> lstOut = this.getLstOut().stream().filter(x -> x.getEmployeeId().equals(empTarget))
				.collect(Collectors.toList());
		List<ApplicationRecordImport> lstApplication = this.getLstApplication().stream()
				.filter(x -> x.getEmployeeID().equals(empTarget)).collect(Collectors.toList());
		return new ApprovalInfoResult(
				new ConfirmInfoResult(indentities, lstApprovalDayStatus, this.getAggrPeriods(), lstConfirmMonth,
						lstApprovalMonthStatus, lstOut, lstApplication, this.getStatusOfEmps()),
				lstApprovalRootDaily, lstAppRootOfEmpMonth);
	}
}
