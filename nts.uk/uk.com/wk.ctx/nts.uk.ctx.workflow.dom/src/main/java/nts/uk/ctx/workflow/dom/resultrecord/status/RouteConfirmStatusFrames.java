package nts.uk.ctx.workflow.dom.resultrecord.status;

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
	 * 指定した代行依頼者が承認枠に存在するか
	 * @param representRequesterIds
	 * @return
	 */
	public boolean isRepresenter(List<String> representRequesterIds) {
		return frames.stream()
				.anyMatch(f -> f.isRepresenter(representRequesterIds));
	}
	
	/**
	 * 指定した社員以外の確定者によって確定済みか
	 */
	public boolean hasConcludedByOther(String approverId) {
		return frames.stream()
				.anyMatch(f -> f.isConclusive() && f.isApprover(approverId));
	}
	
	/**
	 * 指定した社員によって承認済みか
	 * @param approverId
	 * @return
	 */
	public boolean hasConfirmedBy(String approverId) {
		return frames.stream()
				.anyMatch(f -> f.hasConfirmedBy(approverId));
	}
	
	/**
	 * 少なくとも一人は承認済みか
	 * @return
	 */
	public boolean hasConfirmedByAnyone() {
		return frames.stream()
				.anyMatch(f -> f.hasConfirmed());
	}
	
	/**
	 * 全員が承認済みか
	 * @return
	 */
	public boolean hasConfirmedByAll() {
		return frames.stream()
				.allMatch(f -> f.hasConfirmed());
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
