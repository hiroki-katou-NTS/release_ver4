package nts.uk.ctx.workflow.dom.resultrecord.status;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;

/**
 * 承認枠
 */
@RequiredArgsConstructor
public class RouteConfirmStatusFrame {

	/** 確定枠か */
	@Getter
	private final boolean isConclusive;
	
	/** 枠内の社員 */
	private final List<String> employeeIds;
	
	/** 承認済みの社員 */
	private final Optional<String> approvedEmployeeId;
	
	/** 代行者に承認された */
	private final boolean isRepresent;
	
	public static RouteConfirmStatusFrame unConfirmed(AppFrameInstance instance) {
		
		return new RouteConfirmStatusFrame(
				instance.isConfirmAtr(),
				instance.getListApprover(),
				Optional.empty(),
				false);
	}
	
	public static RouteConfirmStatusFrame confirmed(AppFrameInstance instance, AppFrameConfirm confirm) {
		
		boolean isRepresent = confirm.getRepresenterID().isPresent();
		String employeeId = isRepresent
				? confirm.getRepresenterID().get()
				: confirm.getApproverID().get();
		
		return new RouteConfirmStatusFrame(
				instance.isConfirmAtr(),
				instance.getListApprover(),
				Optional.of(employeeId),
				isRepresent);
	}
	
	/**
	 * 指定した社員が枠内に存在するか
	 * @param approverId
	 * @return
	 */
	public boolean isApprover(String approverId) {
		return employeeIds.contains(approverId);
	}
	
	/**
	 * 代行依頼している社員が枠内に存在するか
	 * @param approverIds
	 * @return
	 */
	public boolean isApprover(List<String> approverIds) {
		return employeeIds.stream()
				.anyMatch(e -> approverIds.contains(e));
	}
	
	/**
	 * この承認枠は承認済みか
	 * @return
	 */
	public boolean hasConfirmed() {
		return approvedEmployeeId.isPresent();
	}
	
	/**
	 * 代行者に承認された
	 * @return
	 */
	public boolean hasConfirmedByRepresenter() {
		return hasConfirmed() && isRepresent;
	}
	
	/**
	 * この承認枠は指定した社員によって承認済みか
	 * @param approverId
	 * @return
	 */
	public boolean hasConfirmedBy(String approverId) {
		return approvedEmployeeId
				.filter(e -> e.equals(approverId))
				.isPresent();
	}
	
	public boolean hasApprovedByOther(String approverId) {
		return approvedEmployeeId
				.filter(e -> !e.equals(approverId))
				.isPresent();
	}
}
