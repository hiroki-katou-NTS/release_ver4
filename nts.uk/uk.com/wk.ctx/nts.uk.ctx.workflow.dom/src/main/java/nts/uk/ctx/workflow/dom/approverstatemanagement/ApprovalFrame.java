package nts.uk.ctx.workflow.dom.approverstatemanagement;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
/**
 * 承認枠
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ApprovalFrame extends DomainObject {
	
	private String rootStateID;
	
	private Integer phaseOrder;
	
	private Integer frameOrder;
	
	@Setter
	private ApprovalBehaviorAtr approvalAtr;
	
	private ConfirmPerson confirmAtr;
	@Setter
	private List<ApproverState> listApproverState;
	
	@Setter
	private String approverID;
	
	@Setter
	private String representerID;
	
	@Setter
	private GeneralDate approvalDate;
	
	@Setter
	private String approvalReason;
	
	public static ApprovalFrame firstCreate(String rootStateID, Integer phaseOrder, Integer frameOrder, ConfirmPerson confirmPerson, List<ApproverState> listApproverState){
		return ApprovalFrame.builder()
				.rootStateID(rootStateID)
				.phaseOrder(phaseOrder)
				.frameOrder(frameOrder)
				.approvalAtr(ApprovalBehaviorAtr.UNAPPROVED)
				.confirmAtr(confirmPerson)
				.listApproverState(listApproverState)
				.build();
	}
	
	public static ApprovalFrame createFromFirst(String companyID, GeneralDate date, String rootStateID, ApprovalFrame approvalFrame){
		if(Strings.isBlank(approvalFrame.getRootStateID())){
			return ApprovalFrame.builder()
					.rootStateID(rootStateID)
					.phaseOrder(approvalFrame.getPhaseOrder())
					.frameOrder(approvalFrame.getFrameOrder())
					.approvalAtr(approvalFrame.getApprovalAtr())
					.confirmAtr(approvalFrame.getConfirmAtr())
					.listApproverState(approvalFrame.getListApproverState().stream()
							.map(x -> ApproverState.createFromFirst(companyID, date, rootStateID, x)).collect(Collectors.toList()))
					.approverID(approvalFrame.getApproverID())
					.representerID(approvalFrame.getRepresenterID())
					.approvalDate(approvalFrame.getApprovalDate())
					.approvalReason(approvalFrame.getApprovalReason())
					.build();
		}
		return approvalFrame;
	}
	
	public boolean isApprover(String employeeId) {
		return listApproverState.stream()
				.anyMatch(a -> a.getApproverID().equals(employeeId));
	}
	
	public boolean isRepresenter(String employeeId) {
		return representerID != null && representerID.equals(employeeId);
	}
}
