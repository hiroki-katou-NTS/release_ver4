package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordImport;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConfirmInfoResult {

	private List<Identification> indentities;
	
	private List<ApproveRootStatusForEmpImport> lstApprovalDayStatus;

	private List<AggrPeriodEachActualClosure> aggrPeriods;

	private List<ConfirmationMonth> lstConfirmMonth;

	private List<ApproveRootStatusForEmpImport> lstApprovalMonthStatus;

	private List<EmployeeDateErrorOuput> lstOut;

	private List<ApplicationRecordImport> lstApplication;
	
	private List<StatusOfEmployeeExport> statusOfEmps;
	
	public ConfirmInfoResult getDataWithEmp(String empTarget) {
		List<Identification> indentities = this.indentities.stream().filter(x -> x.getEmployeeId().equals(empTarget)).collect(Collectors.toList());
		List<ApproveRootStatusForEmpImport> lstApprovalDayStatus = this.lstApprovalDayStatus.stream().filter(x -> x.getEmployeeID().equals(empTarget)).collect(Collectors.toList());
		List<ConfirmationMonth> lstConfirmMonth = this.lstConfirmMonth.stream().filter(x -> x.getEmployeeId().equals(empTarget)).collect(Collectors.toList());
		List<ApproveRootStatusForEmpImport> lstApprovalMonthStatus = this.lstApprovalMonthStatus.stream().filter(x -> x.getEmployeeID().equals(empTarget)).collect(Collectors.toList());
		List<EmployeeDateErrorOuput> lstOut = this.lstOut.stream().filter(x -> x.getEmployeeId().equals(empTarget)).collect(Collectors.toList());
		List<ApplicationRecordImport> lstApplication = this.lstApplication.stream().filter(x -> x.getEmployeeID().equals(empTarget)).collect(Collectors.toList());
		return new ConfirmInfoResult(indentities, lstApprovalDayStatus, aggrPeriods, lstConfirmMonth, lstApprovalMonthStatus, lstOut, lstApplication, statusOfEmps);
	}

}
