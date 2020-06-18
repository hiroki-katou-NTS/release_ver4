package nts.uk.ctx.at.record.dom.workrecord.approval.daily;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalProgress.Progress;
import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalSubjective;

/**
 * 承認者から見た日別実績の承認状況（実績コンテキストの状況を加味したもの）
 */
public class ApprovalSubjectiveDaily extends RecordApprovalSubjective<GeneralDate> {

	public ApprovalSubjectiveDaily(String targetEmployeeId, GeneralDate date, Progress progress,
			Subjective subjective) {
		super(targetEmployeeId, date, progress, subjective);
	}

}
