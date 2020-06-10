package nts.uk.ctx.workflow.pub.resultrecord.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * 承認者から見た実績の承認状況
 */
@RequiredArgsConstructor
public abstract class SubjectiveStatus<T> {
	
	/** 対象の社員ID */
	@Getter
	private final String targetEmployeeId;
	
	/** 年月日や年月など */
	@Getter
	private final T date;
	
	/** 承認ルートの進捗状況 */
	@Getter
	private final ApprovalProgress.Progress progress;
	
	/** 承認者から見た承認状況 */
	@Getter
	private final Subjective subjective;
	
	@Value
	public static class Subjective {

		/** 主体(承認者)の社員ID */
		private final String approverEmployeeId;
		
		/** 主体が承認済み */
		private final boolean isApproved;

		/** 主体が実施できる */
		private final boolean canExecute;

		/** 主体が解除できる */
		private final boolean canRelease;
	}
}
