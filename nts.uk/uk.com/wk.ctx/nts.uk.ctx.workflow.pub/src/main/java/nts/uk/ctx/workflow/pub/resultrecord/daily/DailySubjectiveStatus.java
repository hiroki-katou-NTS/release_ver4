package nts.uk.ctx.workflow.pub.resultrecord.daily;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.pub.resultrecord.common.ApprovalProgress.Progress;
import nts.uk.ctx.workflow.pub.resultrecord.common.SubjectiveStatus;

public class DailySubjectiveStatus extends SubjectiveStatus<GeneralDate> {

	public DailySubjectiveStatus(String targetEmployeeId, GeneralDate date, Progress progress, Subjective subjective) {
		super(targetEmployeeId, date, progress, subjective);
	}

}
