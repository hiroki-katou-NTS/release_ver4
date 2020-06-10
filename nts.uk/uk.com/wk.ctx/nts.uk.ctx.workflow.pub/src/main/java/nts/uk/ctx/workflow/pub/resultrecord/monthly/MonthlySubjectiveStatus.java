package nts.uk.ctx.workflow.pub.resultrecord.monthly;

import nts.uk.ctx.workflow.pub.resultrecord.common.ApprovalProgress.Progress;
import nts.uk.ctx.workflow.pub.resultrecord.common.SubjectiveStatus;
import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * 承認者から見た月別実績の承認状況
 */
public class MonthlySubjectiveStatus extends SubjectiveStatus<ClosureMonth> {

	public MonthlySubjectiveStatus(String targetEmployeeId, ClosureMonth date, Progress progress,
			Subjective subjective) {
		super(targetEmployeeId, date, progress, subjective);
	}

}
