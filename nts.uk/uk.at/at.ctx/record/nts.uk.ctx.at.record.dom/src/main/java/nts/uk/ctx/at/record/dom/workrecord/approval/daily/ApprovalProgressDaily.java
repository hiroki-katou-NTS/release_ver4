package nts.uk.ctx.at.record.dom.workrecord.approval.daily;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalProgress;

/**
 * 日別実績の承認の進捗状況
 */
public class ApprovalProgressDaily extends RecordApprovalProgress<GeneralDate> {

	public ApprovalProgressDaily(String employeeId, GeneralDate date, Progress progress) {
		super(employeeId, date, progress);
	}

}
