package nts.uk.ctx.workflow.dom.resultrecord.status.monthly;

import java.util.Optional;

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

	public static Optional<RouteConfirmStatusMonthly> create(Optional<AppRootConfirm> confirm, Optional<AppRootInstance> instance) {
		
		if(!confirm.isPresent() || !instance.isPresent()) {
			return Optional.empty();
		}
		
		String targetEmployeeId = confirm.get().getEmployeeID();
		val targetMonth = confirm.get().getClosureMonth();
		val phases = RouteConfirmStatusPhases.create(confirm.get().getListAppPhase(), instance.get().getListAppPhase());
		val progress = RouteConfirmProgress.of(phases.isUnapproved(), phases.isApproved());
		
		return Optional.of(new RouteConfirmStatusMonthly(targetEmployeeId, targetMonth, progress, phases));
	}

	@Override
	public RecordRootType getRootType() {
		return RecordRootType.CONFIRM_WORK_BY_MONTH;
	}
	
}
