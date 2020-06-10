package nts.uk.ctx.at.record.dom.workrecord.approval.monthly;

import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalProgress.Progress;
import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalSubjective;
import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * ワークフロー上における月別実績の承認状況
 */
public class ApprovalSubjectiveMonthlyOnWorkflow extends RecordApprovalSubjective<ClosureMonth> {

	public ApprovalSubjectiveMonthlyOnWorkflow(String targetEmployeeId, ClosureMonth date, Progress progress,
			Subjective subjective) {
		super(targetEmployeeId, date, progress, subjective);
	}

}
