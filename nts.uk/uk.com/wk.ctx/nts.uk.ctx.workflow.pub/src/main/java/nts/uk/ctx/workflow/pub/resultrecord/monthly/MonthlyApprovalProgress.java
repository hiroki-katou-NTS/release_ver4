package nts.uk.ctx.workflow.pub.resultrecord.monthly;

import nts.uk.ctx.workflow.pub.resultrecord.common.ApprovalProgress;
import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * 月別実績の承認の進捗状況
 */
public class MonthlyApprovalProgress extends ApprovalProgress<ClosureMonth> {

	public MonthlyApprovalProgress(String employeeId, ClosureMonth date, Progress progress) {
		super(employeeId, date, progress);
	}

}
