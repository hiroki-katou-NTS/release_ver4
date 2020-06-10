package nts.uk.ctx.at.record.dom.workrecord.approval.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RecordApprovalSubjective<T> {

	/** 主体(承認者)の社員ID */
	@Getter
	private final String approverEmployeeId;
	
	/** 対象の社員ID */
	@Getter
	private final String targetEmployeeId;
	
	/** 年月日や年月など */
	@Getter
	private final T date;
	
	@Getter
	private final Status status;
	
	@RequiredArgsConstructor
	public static class Status {
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
