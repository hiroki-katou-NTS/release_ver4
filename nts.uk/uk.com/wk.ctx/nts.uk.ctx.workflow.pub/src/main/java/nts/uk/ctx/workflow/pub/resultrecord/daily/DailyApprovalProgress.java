package nts.uk.ctx.workflow.pub.resultrecord.daily;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.pub.resultrecord.common.ApprovalProgress;

public class DailyApprovalProgress extends ApprovalProgress<GeneralDate> {

	public DailyApprovalProgress(String employeeId, GeneralDate date, Progress progress) {
		super(employeeId, date, progress);
	}

}
