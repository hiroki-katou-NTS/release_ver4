package nts.uk.ctx.workflow.dom.resultrecord.status;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.val;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;

/**
 * 承認枠コレクション
 */
@RequiredArgsConstructor
public class RouteConfirmStatusFrames {

	private final List<RouteConfirmStatusFrame> frames;
	
	/**
	 * 未到達フェーズの承認枠を作る
	 * @param appFrameInstances
	 * @return
	 */
	public static RouteConfirmStatusFrames unreached(
			List<AppFrameInstance> appFrameInstances) {
		
		val frames = appFrameInstances.stream()
				.map(i -> RouteConfirmStatusFrame.unConfirmed(i))
				.collect(Collectors.toList());
		
		return new RouteConfirmStatusFrames(frames);
	}
	
	/**
	 * 到達済み（最中or通過済み）フェーズの承認枠を作る
	 * @param appFrameInstances
	 * @return
	 */
	public static RouteConfirmStatusFrames reached(
			List<AppFrameInstance> appFrameInstances,
			List<AppFrameConfirm> appFrameConfirms) {
		
		val frames = appFrameInstances.stream()
				.map(instance -> {
					Optional<AppFrameConfirm> confirm = appFrameConfirms.stream()
							.filter(f -> f.getFrameOrder().equals(instance.getFrameOrder()))
							.findFirst();
					
					return confirm.map(c -> RouteConfirmStatusFrame.confirmed(instance, c))
							.orElseGet(() -> RouteConfirmStatusFrame.unConfirmed(instance));
				})
				.collect(Collectors.toList());

		
		// 中間データに定義されている最後の承認枠番号
		int definedLastFrameOrder = appFrameInstances.stream()
				.map(f -> f.getFrameOrder())
				.sorted(Comparator.reverseOrder())
				.findFirst().get();
		
		// 中間データに存在しないが、実績は承認済みのケース
		appFrameConfirms.stream()
				.filter(f -> f.getFrameOrder() > definedLastFrameOrder)
				.forEach(f -> {
					frames.add(RouteConfirmStatusFrame.confirmedButUndefined(f));
				});
		
		return new RouteConfirmStatusFrames(frames);
	}
	
	/**
	 * 指定した社員はこの承認枠の承認者か
	 * @param approverId
	 * @return
	 */
	public boolean isApprover(String approverId) {
		return frames.stream()
				.anyMatch(f -> f.isApprover(approverId));
	}
	
	/**
	 * 指定した社員が承認枠に存在するか
	 * @param approverIds
	 * @return
	 */
	public boolean isApprover(List<String> approverIds) {
		return frames.stream()
				.anyMatch(f -> f.isApprover(approverIds));
	}
	
	/**
	 * 指定した社員以外の確定者によって確定済みか
	 */
	public boolean hasConcludedByOther(String approverId) {
		return frames.stream()
				.filter(f -> f.isConclusive())
				.anyMatch(f -> f.hasApprovedByOther(approverId));
	}
	
	/**
	 * 少なくとも一人は承認済みか
	 * @return
	 */
	public boolean hasApprovedByAnyone() {
		return frames.stream()
				.anyMatch(f -> f.hasApproved());
	}
	
	/**
	 * 全員が承認済みか
	 * @return
	 */
	public boolean hasApprovedByAll() {
		return frames.stream()
				.allMatch(f -> f.hasApproved());
	}
	
	/**
	 * 指定した社員が代行者として承認したか
	 * @param representerId
	 * @return
	 */
	public boolean hasApprovedByRepresenter(String representerId) {
		return frames.stream()
				.anyMatch(f -> f.hasConfirmedByRepresenter(representerId));
	}
	
	/**
	 * 指定した社員以外の確定者が存在するか
	 * @param approverId
	 * @return
	 */
	public boolean existsOtherConcluder(String approverId) {
		return frames.stream()
				.anyMatch(f -> f.isConclusive() && !f.isApprover(approverId));
	}
}
