package nts.uk.ctx.workflow.dom.resultrecord.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nts.uk.ctx.workflow.dom.agent.output.AgentInfoOutput;
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
	 * 中間データには存在しないが、実績確認状態には存在するケース（２枠目承認後に、中間データが１枠のみになったとか）に対応
	 * @param confirm
	 * @return
	 */
	public static RouteConfirmStatusFrame confirmedButUndefined(AppFrameConfirm confirm) {

		boolean isRepresent = confirm.getRepresenterID().isPresent();
		String employeeId = isRepresent
				? confirm.getRepresenterID().get()
				: confirm.getApproverID().get();
				
		return new RouteConfirmStatusFrame(
				false,
				Collections.emptyList(),
				Optional.of(employeeId),
				isRepresent);
	}
	
	/**
	 * 指定した社員が枠内に存在するか
	 * @param approverId
	 * @return
	 */
	public boolean isApprover(String approverId, List<AgentInfoOutput> representRequesterIds) {
		boolean isAgentApprover = false;
		Optional<AgentInfoOutput> agentInfoOutput = representRequesterIds.stream().filter(x -> x.getApproverID().equals(approverId)).findAny();
		if(agentInfoOutput.isPresent()) {
			isAgentApprover = employeeIds.contains(agentInfoOutput.get().getAgentID());
		}
		return employeeIds.contains(approverId) || isAgentApprover;
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
	public boolean hasApproved() {
		return approvedEmployeeId.isPresent();
	}
	
	/**
	 * 代行者に承認された
	 * @return
	 */
	public boolean hasConfirmedByRepresenter(List<AgentInfoOutput> representRequesterIds) {
		if(!isRepresent) {
			return false;
		}
		List<String> representerIDLst = representRequesterIds.stream().filter(x -> employeeIds.contains(x.getAgentID()))
				.map(x -> x.getApproverID()).collect(Collectors.toList());
		
		return hasApproved() && representerIDLst.contains(approvedEmployeeId.get());
	}
	
	public boolean hasApprovedByOther(String approverId) {
		return hasApproved() && !employeeIds.contains(approverId);
	}
	
	/**
	 * この承認枠は指定した社員によって承認済みか
	 * @param approverId
	 * @return
	 */
	public boolean hasApprovedByApprover() {
		return hasApproved() && employeeIds.contains(approvedEmployeeId.get());
	}
}
