package nts.uk.ctx.at.record.dom.workrecord.approval.monthly;

import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalProgress;
import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * 月別実績の承認の進捗状況
 */
public class ApprovalProgressMonthly extends RecordApprovalProgress<ClosureMonth> {

	public ApprovalProgressMonthly(String employeeId, ClosureMonth date, Progress progress) {
		super(employeeId, date, progress);
	}

}
