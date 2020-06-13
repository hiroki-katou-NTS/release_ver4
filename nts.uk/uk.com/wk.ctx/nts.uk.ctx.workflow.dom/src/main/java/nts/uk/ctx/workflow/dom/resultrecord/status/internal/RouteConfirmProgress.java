package nts.uk.ctx.workflow.dom.resultrecord.status.internal;

public enum RouteConfirmProgress {
	
	UNAPPROVED,
	APPROVING,
	APPROVED
	;
	
	public static RouteConfirmProgress of(boolean isUnapproved, boolean isApproved) {
		if (isApproved) {
			return APPROVED;
		} else if (isUnapproved) {
			return UNAPPROVED;
		} else {
			return APPROVING;
		}
	}
	
	public boolean isUnapproved() {
		return this == UNAPPROVED; 
	}
	
	public boolean isApproving() {
		return this == APPROVING;
	}
	
	public boolean isApproved() {
		return this == APPROVED;
	}
}
