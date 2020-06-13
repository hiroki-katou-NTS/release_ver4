package nts.uk.ctx.at.record.dom.workrecord.approval.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;

/**
 * 実績承認の進捗状況
 */
@RequiredArgsConstructor
public abstract class RecordApprovalProgress<T> {

	/** 対象の社員ID */
	@Getter
	private final String employeeId;

	/** 年月日や年月など */
	@Getter
	private final T date;

	@Getter
	private final Progress progress;

	public boolean isUnapproved() {
		return progress == Progress.UNAPPROVED;
	}

	public boolean isApproving() {
		return progress == Progress.APPROVING;
	}

	public boolean isApproved() {
		return progress == Progress.APPROVED;
	}

	public static enum Progress {

		/** まだ何も承認されていない */
		UNAPPROVED,

		/** 承認フェーズの一部が承認済み */
		APPROVING,

		/** 承認フェーズがすべて承認された */
		APPROVED,
	}


	public ApprovalStatusForEmployee approvalStatusForEmployee() {
		switch (progress) {
			case UNAPPROVED:
				return ApprovalStatusForEmployee.UNAPPROVED;
			case APPROVED:
				return ApprovalStatusForEmployee.APPROVED;
			case APPROVING:
				return ApprovalStatusForEmployee.DURING_APPROVAL;
			default:
				break;
		}
		return null;
	}
}
