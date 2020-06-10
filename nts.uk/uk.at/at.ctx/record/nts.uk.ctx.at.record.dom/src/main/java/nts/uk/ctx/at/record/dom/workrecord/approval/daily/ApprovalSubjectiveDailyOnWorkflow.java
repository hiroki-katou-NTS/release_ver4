package nts.uk.ctx.at.record.dom.workrecord.approval.daily;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalSubjective;

/**
 * 承認者から見た日別実績の承認状況（ワークフローコンテキストでの状況のみ）
 */
public class ApprovalSubjectiveDailyOnWorkflow extends RecordApprovalSubjective<GeneralDate> {

	public ApprovalSubjectiveDailyOnWorkflow(
			String approverEmployeeId, String targetEmployeeId, GeneralDate date, Status status) {
		super(approverEmployeeId, targetEmployeeId, date, status);
	}

}
