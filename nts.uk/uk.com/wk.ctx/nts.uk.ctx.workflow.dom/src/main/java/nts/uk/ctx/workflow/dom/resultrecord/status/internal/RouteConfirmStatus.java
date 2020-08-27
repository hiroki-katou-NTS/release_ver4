package nts.uk.ctx.workflow.dom.resultrecord.status.internal;

import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.resultrecord.status.RouteConfirmStatusForOneApprover;
import nts.uk.ctx.workflow.dom.resultrecord.status.RouteConfirmStatusPhases;
import nts.uk.ctx.workflow.dom.service.resultrecord.ApprovalActionByEmp;
import nts.uk.ctx.workflow.dom.service.resultrecord.ApproverEmpState;
import nts.uk.ctx.workflow.dom.service.resultrecord.ReleaseDivision;

@RequiredArgsConstructor
public abstract class RouteConfirmStatus<T> {

	/** 承認対象者 */
	@Getter
	private final String targetEmployeeId;
	
	/** 対象日 */
	@Getter
	private final T targetDate;
	
	@Getter
	private final RouteConfirmProgress progress;
	
	/** フェーズ */
	private final RouteConfirmStatusPhases phases;
	
	public abstract RecordRootType getRootType();
	
	/**
	 * 指定した承認者にとってのステータスを返す
	 * @param approverId 承認者
	 * @param representRequesterId 承認者への代行依頼者
	 * @return
	 */
	public RouteConfirmStatusForOneApprover getStatusFor(String approverId, List<String> representRequesterId) {
		
		return RouteConfirmStatusForOneApprover.create(
				stateFor(approverId, representRequesterId),
				releaseAtr(approverId, representRequesterId),
				actionFor(approverId, representRequesterId));
	}
	
	/**
	 * 指定した社員は承認解除できるか
	 * @param approverId
	 * @return
	 */
	private ReleaseDivision releaseAtr(String approverId, List<String> representRequesterIds) {
		return phases.canRelease(approverId, representRequesterIds)
				? ReleaseDivision.RELEASE
				: ReleaseDivision.NOT_RELEASE;
	}
	
	/**
	 * 指定した社員の承認アクション
	 * @param approverId
	 * @return
	 */
	private ApprovalActionByEmp actionFor(String approverId, List<String> representRequesterIds) {
		if (!phases.canApprove(approverId, representRequesterIds)) {
			return ApprovalActionByEmp.NOT_APPROVAL;
		}
		
		boolean approved = Stream.concat(Stream.of(approverId), representRequesterIds.stream())
				.anyMatch(id -> phases.hasApprovedBy(id));
		
		return approved
				? ApprovalActionByEmp.APPROVALED
				: ApprovalActionByEmp.APPROVAL_REQUIRE;
	}
	
	/**
	 * 指定した社員にとっての状況
	 * @param approverId
	 * @return
	 */
	private ApproverEmpState stateFor(String approverId, List<String> representRequesterIds) {
		// 最終フェーズが承認済み
		if (phases.finalPhase().hasApproved()) {
			return ApproverEmpState.COMPLETE;
		}
		
		// 承認中のフェーズ（複数ありえる）
		val progressingPhases = phases.progressingPhases();
		
		// 承認中のフェーズではなくその前後いずれか
		if (!progressingPhases.canApprove(approverId, representRequesterIds)) {
			return phases.isApproverInUnreachedPhase(approverId)
					? ApproverEmpState.PHASE_LESS
					: ApproverEmpState.PHASE_PASS;
		}
		
		// 他の確定者がいない
		if (!progressingPhases.existsOtherConcluder(approverId)) {
			return ApproverEmpState.PHASE_DURING;
		}
		
		// 他の確定者に確定されていれば通過済み
		return progressingPhases.hasConcludedByOtherConcluder(approverId)
				? ApproverEmpState.PHASE_PASS
				: ApproverEmpState.PHASE_DURING;
	}
}
