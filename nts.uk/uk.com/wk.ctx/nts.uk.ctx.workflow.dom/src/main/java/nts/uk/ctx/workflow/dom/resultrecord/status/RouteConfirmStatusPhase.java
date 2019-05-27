package nts.uk.ctx.workflow.dom.resultrecord.status;

import java.util.List;

import lombok.RequiredArgsConstructor;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;

@RequiredArgsConstructor
public class RouteConfirmStatusPhase {
	
	/** 承認形態 */
	private final ApprovalForm approvalForm;
	
	/** 承認枠 */
	private final RouteConfirmStatusFrames frames;
	
	/** フェーズ状況 */
	private final State state;
	
	/**
	 * 未到達フェーズ
	 * @param approvalForm
	 * @param appFrameInstances
	 * @return
	 */
	public static RouteConfirmStatusPhase unreached(
			ApprovalForm approvalForm,
			List<AppFrameInstance> appFrameInstances) {
		
		return new RouteConfirmStatusPhase(
				approvalForm,
				RouteConfirmStatusFrames.unreached(appFrameInstances),
				State.UNREACHED);
	}
	
	public static RouteConfirmStatusPhase passed(
			ApprovalForm approvalForm,
			List<AppFrameInstance> appFrameInstances,
			List<AppFrameConfirm> appFrameConfirms) {
		
		return new RouteConfirmStatusPhase(
				approvalForm,
				RouteConfirmStatusFrames.reached(appFrameInstances, appFrameConfirms),
				State.PASSED);
	}
	
	public static RouteConfirmStatusPhase inProgress(
			ApprovalForm approvalForm,
			List<AppFrameInstance> appFrameInstances,
			List<AppFrameConfirm> appFrameConfirms) {
		
		return new RouteConfirmStatusPhase(
				approvalForm,
				RouteConfirmStatusFrames.reached(appFrameInstances, appFrameConfirms),
				State.IN_PROGRESS);
		
	}
	
	/**
	 * 指定した社員はこのフェーズの承認者か
	 * @param approverId
	 * @return
	 */
	public boolean isApprover(String approverId) {
		return frames.isApprover(approverId);
	}
	
	/**
	 * 指定した承認者は承認ができるか
	 * @param approverId
	 * @return
	 */
	public boolean canApprove(String approverId, List<String> representRequesterIds) {
		return state == State.IN_PROGRESS
				&& (frames.isApprover(approverId) || frames.isApprover(representRequesterIds))
				&& !frames.hasConcludedByOther(approverId);
	}
	
	/**
	 * 指定した承認者は承認解除できるか
	 * @param approverId
	 * @return
	 */
	public boolean canRelease(String approverId, List<String> representRequesterIds) {
		return canApprove(approverId, representRequesterIds)
				&& hasApprovedBy(approverId);
	}
	
	/**
	 * 指定した承認者は承認済みか
	 * @param approverId
	 * @return
	 */
	public boolean hasApprovedBy(String approverId) {
		return (hasApproved() && frames.isApprover(approverId))
				|| frames.hasApprovedByRepresenter(approverId);
	}
	
	/**
	 * フェーズが承認済みか
	 * @return
	 */
	public boolean hasApproved() {
		switch (approvalForm) {
		case EVERYONE_APPROVED: return frames.hasApprovedByAll();
		case SINGLE_APPROVED: return frames.hasApprovedByAnyone();
		default: throw new RuntimeException("unknown approvalForm: " + approvalForm);
		}
	}
	
	/**
	 * 指定した社員以外の確定者が存在するか
	 * @param approverId
	 * @return
	 */
	public boolean existsOtherConcluder(String approverId) {
		return frames.existsOtherConcluder(approverId);
	}
	
	/**
	 * 指定した社員以外の確定者が確定済みか
	 * @param approverId
	 * @return
	 */
	public boolean hasConcludedByOther(String approverId) {
		return frames.hasConcludedByOther(approverId);
	}
	
	/**
	 * このフェーズは承認処理中か
	 * @return
	 */
	public boolean isInProgress() {
		return state == State.IN_PROGRESS;
	}
	
	/**
	 * このフェーズは未到達か
	 * @return
	 */
	public boolean isUnreached() {
		return state == State.UNREACHED;
	}
	
	private enum State {
		/** 通過済み */
		PASSED,
		
		/** 最中 */
		IN_PROGRESS,
		
		/** 未到達 */
		UNREACHED,
	}
}
