package nts.uk.ctx.workflow.dom.resultrecord.status.monthly;

import lombok.val;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.resultrecord.status.RouteConfirmStatusPhases;
import nts.uk.ctx.workflow.dom.resultrecord.status.internal.RouteConfirmProgress;
import nts.uk.ctx.workflow.dom.resultrecord.status.internal.RouteConfirmStatus;
import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * 月別実績の承認状況
 */
public class RouteConfirmStatusMonthly extends RouteConfirmStatus<ClosureMonth> {

	public RouteConfirmStatusMonthly(
			String targetEmployeeId,
			ClosureMonth targetMonth,
			RouteConfirmProgress progress,
			RouteConfirmStatusPhases phases) {
		
		super(targetEmployeeId, targetMonth, progress, phases);
	}

	public static RouteConfirmStatusMonthly create(AppRootConfirm confirm, AppRootInstance instance) {
		
		String targetEmployeeId = confirm.getEmployeeID();
		val targetMonth = confirm.getClosureMonth();
		val phases = RouteConfirmStatusPhases.create(confirm.getListAppPhase(), instance.getListAppPhase());
		val progress = RouteConfirmProgress.of(phases.isApproved(), phases.isUnapproved());
		
		return new RouteConfirmStatusMonthly(targetEmployeeId, targetMonth, progress, phases);
	}

	@Override
	public RecordRootType getRootType() {
		return RecordRootType.CONFIRM_WORK_BY_MONTH;
	}
	
}
