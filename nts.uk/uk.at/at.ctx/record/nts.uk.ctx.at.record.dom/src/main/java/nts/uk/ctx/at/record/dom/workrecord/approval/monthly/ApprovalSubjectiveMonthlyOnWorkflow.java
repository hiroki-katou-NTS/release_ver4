package nts.uk.ctx.at.record.dom.workrecord.approval.monthly;

import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalSubjective;
import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * ワークフロー上における月別実績の承認状況
 */
public class ApprovalSubjectiveMonthlyOnWorkflow extends RecordApprovalSubjective<ClosureMonth> {

	public ApprovalSubjectiveMonthlyOnWorkflow(
			String approverEmployeeId, String targetEmployeeId, ClosureMonth date, Status status) {
		super(approverEmployeeId, targetEmployeeId, date, status);
	}

}
