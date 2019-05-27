package nts.uk.ctx.workflow.dom.resultrecord.status;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;

/**
 * 承認フェーズのコレクション
 */
@RequiredArgsConstructor
public class RouteConfirmStatusPhases {

	private final List<RouteConfirmStatusPhase> phases;
	
	public static RouteConfirmStatusPhases create(List<AppPhaseConfirm> confirmPhases, List<AppPhaseInstance> instancePhases) {
		
		// 前のフェーズから処理
		List<RouteConfirmStatusPhase> phases = instancePhases.stream()
				.sorted((a, b) -> a.getPhaseOrder().compareTo(b.getPhaseOrder()))
				.map(instance -> {
					
					int phaseOrder = instance.getPhaseOrder();
					
					// 実績確認
					List<AppFrameConfirm> confirmedFrames = confirmPhases.stream()
							.filter(p -> p.getPhaseOrder().equals(instance.getPhaseOrder()))
							.map(c -> c.getListAppFrame())
							.findFirst()
							.orElseGet(() -> Collections.emptyList());
					
					// 後ろのフェーズが1つでも承認されているなら、通過済み
					if (hasAnyFrameConfirmedAfterPhase(phaseOrder, confirmPhases)) {
						return RouteConfirmStatusPhase.passed(
								instance.getApprovalForm(), instance.getListAppFrame(), confirmedFrames);
					}
					
					// 第1フェーズで後ろが全て未承認、または前フェーズが全て承認済みなら、最中
					if (phaseOrder == 1
							|| hasAllPhaseConfirmedBefore(phaseOrder, confirmPhases)) {
						return RouteConfirmStatusPhase.inProgress(
								instance.getApprovalForm(), instance.getListAppFrame(), confirmedFrames);
					}
					
					// 未到達
					return RouteConfirmStatusPhase.unreached(
							instance.getApprovalForm(), instance.getListAppFrame());
				})
				.collect(Collectors.toList());
		
		
		return new RouteConfirmStatusPhases(phases);
	}
	
	/**
	 * 後ろのフェーズに1つでも承認済みの承認枠があるか
	 * @param basePhaseOrder
	 * @param confirmPhases
	 * @param instancePhases
	 * @return
	 */
	private static boolean hasAnyFrameConfirmedAfterPhase(
			int basePhaseOrder,
			List<AppPhaseConfirm> confirmPhases) {
		
		return confirmPhases.stream()
				.filter(p -> p.getPhaseOrder() > basePhaseOrder)
				.anyMatch(p -> !p.getListAppFrame().isEmpty());
	}
	
	/**
	 * 前のフェーズが全て承認済みか
	 * @param basePhaseOrder
	 * @param confirmPhases
	 * @param instancePhases
	 * @return
	 */
	private static boolean hasAllPhaseConfirmedBefore(
			int basePhaseOrder,
			List<AppPhaseConfirm> confirmPhases) {
		
		return confirmPhases.stream()
				.filter(p -> p.getPhaseOrder() < basePhaseOrder)
				.allMatch(p -> p.getAppPhaseAtr() == ApprovalBehaviorAtr.APPROVED);
	}
	
	/**
	 * 承認できるか
	 * @param approverId
	 * @return
	 */
	public boolean canApprove(String approverId, List<String> representRequesterIds) {
		return phases.stream()
				.anyMatch(p -> p.canApprove(approverId, representRequesterIds));
	}
	
	/**
	 * 指定した承認者は承認解除できるか
	 * @param approverId
	 * @return
	 */
	public boolean canRelease(String approverId, List<String> representRequesterIds) {
		return phases.stream()
				.anyMatch(p -> p.canRelease(approverId, representRequesterIds));
	}
	
	/**
	 * 指定した承認者は承認済みか
	 * @param approverId
	 * @return
	 */
	public boolean hasApprovedBy(String approverId) {
		return phases.stream()
				.anyMatch(p -> p.hasApprovedBy(approverId));
	}
	
	/**
	 * 承認中フェーズを返す
	 * @return
	 */
	public Progressings progressingPhases() {
		return new Progressings(
				phases.stream().filter(p -> p.isInProgress()).collect(Collectors.toList()));
	}
	
	/**
	 * 指定した社員は未到達フェーズの承認者か
	 * @param approverId
	 * @return
	 */
	public boolean isApproverInUnreachedPhase(String approverId) {
		return phases.stream()
				.anyMatch(p -> p.isUnreached() && p.isApprover(approverId));
	}
	
	/**
	 * 最終フェーズ
	 * @return
	 */
	public RouteConfirmStatusPhase finalPhase() {
		return phases.get(phases.size() - 1);
	}
	
	/**
	 * 承認中フェーズのコレクション
	 */
	@RequiredArgsConstructor
	public static class Progressings {
		
		private final List<RouteConfirmStatusPhase> phases;
		
		/**
		 * 指定した社員はいずれかのフェーズを承認できるか
		 * @param approverId
		 * @return
		 */
		public boolean canApprove(String approverId, List<String> representRequesterIds) {
			return phases.stream()
					.anyMatch(p -> p.canApprove(approverId, representRequesterIds));
		}
		
		/**
		 * 指定した社員以外の確定者が存在するか
		 * @param approverId
		 * @return
		 */
		public boolean existsOtherConcluder(String approverId) {
			return phases.stream()
					.anyMatch(p -> p.existsOtherConcluder(approverId));
		}
		
		/**
		 * 指定した社員以外の確定者が確定済みか
		 * @param approverId
		 * @return
		 */
		public boolean hasConcludedByOtherConcluder(String approverId) {
			return phases.stream()
					.allMatch(p -> p.hasConcludedByOther(approverId));
		}
	}
}
