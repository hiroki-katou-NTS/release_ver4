package nts.uk.ctx.workflow.dom.resultrecord.status;

import lombok.Value;
import lombok.val;
import nts.uk.ctx.workflow.dom.service.resultrecord.ApprovalActionByEmp;
import nts.uk.ctx.workflow.dom.service.resultrecord.ApprovalStatus;
import nts.uk.ctx.workflow.dom.service.resultrecord.ApproverEmpState;
import nts.uk.ctx.workflow.dom.service.resultrecord.ReleaseDivision;

@Value
public class RouteConfirmStatusForOneApprover {
	
	/** ルート状況 */
	private final ApproverEmpState state;
	
	/** 承認状況 */
	private final ApprovalStatus approvalStatus;

	/**
	 * ファクトリ
	 * @param releaseAtr
	 * @param action
	 * @return
	 */
	public static RouteConfirmStatusForOneApprover create(
			ApproverEmpState state,
			ReleaseDivision releaseAtr,
			ApprovalActionByEmp action) {
		
		val status = new ApprovalStatus();
		status.setReleaseAtr(releaseAtr);
		status.setApprovalAction(action);
		
		return new RouteConfirmStatusForOneApprover(state, status);
	}
}
