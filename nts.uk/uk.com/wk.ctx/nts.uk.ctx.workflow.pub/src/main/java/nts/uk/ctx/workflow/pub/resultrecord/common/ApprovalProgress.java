package nts.uk.ctx.workflow.pub.resultrecord.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 実績承認の進捗状況
 * @param <T>
 */
@RequiredArgsConstructor
public abstract class ApprovalProgress<T> {

	/** 対象の社員ID */
	@Getter
	private final String employeeId;
	
	/** 年月日や年月など */
	@Getter
	private final T date;
	
	/** 承認の進捗状況 */
	@Getter
	private final Progress progress;
	
	public static enum Progress {
		/** まだ何も承認されていない */
		UNAPPROVED,
		
		/** 承認フェーズの一部が承認済み */
		APPROVING,
		
		/** 承認フェーズがすべて承認された */
		APPROVED,
	}

	public boolean isUnapproved() {
		return progress == Progress.UNAPPROVED;
	}
	
	public boolean isApproving() {
		return progress == Progress.APPROVING;
	}
	
	public boolean isApproved() {
		return progress == Progress.APPROVED;
	}
	
}
