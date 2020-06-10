package nts.uk.ctx.at.record.dom.workrecord.approval.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RecordApprovalSubjective<T> {
	
	/** 対象の社員ID */
	@Getter
	private final String targetEmployeeId;
	
	/** 年月日や年月など */
	@Getter
	private final T date;
	
	/** 承認ルートの進捗状況 */
	@Getter
	private final RecordApprovalProgress.Progress progress;
	
	/** 承認者から見た状況 */
	@Getter
	private final Subjective subjective;
	
	@RequiredArgsConstructor
	public static class Subjective {

		/** 主体(承認者)の社員ID */
		@Getter
		private final String approverEmployeeId;
		/** 主体が承認済み */
		@Getter
		private final boolean isApproved;

		/** 主体が実施できる */
		private final boolean canExecute;

		/** 主体が解除できる */
		private final boolean canRelease;
		
		public boolean canExecute() {
			return canExecute;
		}
		
		public boolean canRelease() {
			return canRelease;
		}
	}
}
