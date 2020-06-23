package nts.uk.ctx.workflow.dom.resultrecord.status.daily;

import java.util.Optional;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.resultrecord.status.RouteConfirmStatusPhases;
import nts.uk.ctx.workflow.dom.resultrecord.status.internal.RouteConfirmProgress;
import nts.uk.ctx.workflow.dom.resultrecord.status.internal.RouteConfirmStatus;

/**
 * 日別実績の承認状況
 */
public class RouteConfirmStatusDaily extends RouteConfirmStatus<GeneralDate> {

	public RouteConfirmStatusDaily(
			String targetEmployeeId,
			GeneralDate targetDate,
			RouteConfirmProgress progress,
			RouteConfirmStatusPhases phases) {
		
		super(targetEmployeeId, targetDate, progress, phases);
	}

	public static Optional<RouteConfirmStatusDaily> create(Optional<AppRootConfirm> confirm, Optional<AppRootInstance> instance) {
		
		if (!confirm.isPresent() || !instance.isPresent()) {
			return Optional.empty();
		}
		String targetEmployeeId = instance.get().getEmployeeID();	
		val targetDate = confirm.get().getRecordDate();
		val phases = RouteConfirmStatusPhases.create(confirm.get().getListAppPhase(), instance.get().getListAppPhase());
		val progress = RouteConfirmProgress.of(phases.isUnapproved(), phases.isApproved());
		
		return Optional.of(new RouteConfirmStatusDaily(targetEmployeeId, targetDate, progress, phases));
	}

	@Override
	public RecordRootType getRootType() {
		return RecordRootType.CONFIRM_WORK_BY_DAY;
	}
	
}
