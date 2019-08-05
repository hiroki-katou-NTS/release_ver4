package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootOfEmpMonthImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;

/**
 * @author thanhnx
 * 月の情報
 */
@AllArgsConstructor
@Data
public class InformationMonth {

	//締め期間
	private AggrPeriodEachActualClosure actualClosure;
	
	//月の本人確認
	private List<ConfirmationMonth> lstConfirmMonth;

	//承認対象者の承認状況
	private List<ApproveRootStatusForEmpImport> lstApprovalMonthStatus;
	
	//基準社員の承認対象者
	private List<AppRootOfEmpMonthImport> lstAppRootOfEmpMonth;

	public InformationMonth(AggrPeriodEachActualClosure actualClosure, List<ConfirmationMonth> lstConfirmMonth,
			List<ApproveRootStatusForEmpImport> lstApprovalMonthStatus) {
		super();
		this.actualClosure = actualClosure;
		this.lstConfirmMonth = lstConfirmMonth;
		this.lstApprovalMonthStatus = lstApprovalMonthStatus;
		this.lstAppRootOfEmpMonth = new ArrayList<>();
	}
	
	
}
