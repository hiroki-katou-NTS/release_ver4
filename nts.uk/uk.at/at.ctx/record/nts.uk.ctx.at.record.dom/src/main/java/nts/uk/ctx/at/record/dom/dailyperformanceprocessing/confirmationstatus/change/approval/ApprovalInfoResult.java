package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.approval;

import java.util.List;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootOfEmpMonthImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.ConfirmInfoResult;

@Getter
public class ApprovalInfoResult extends ConfirmInfoResult {

	List<ApprovalRootOfEmployeeImport> lstApprovalRootDaily;

	List<AppRootOfEmpMonthImport> lstAppRootOfEmpMonth;
}
