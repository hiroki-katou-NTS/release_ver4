package nts.uk.ctx.workflow.pub.resultrecord.daily;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.pub.resultrecord.common.SubjectiveStatus;

public class DailySubjectiveStatus extends SubjectiveStatus<GeneralDate> {

	public DailySubjectiveStatus(String approverEmployeeId, String targetEmployeeId, GeneralDate date, Status status) {
		super(approverEmployeeId, targetEmployeeId, date, status);
	}

}
