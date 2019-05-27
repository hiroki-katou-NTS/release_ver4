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
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.arc.enums.EnumAdaptor;
/**
 * 承認フェーズインスタンス
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ApprovalPhaseState extends DomainObject {
	
	private String rootStateID;
	
	private Integer phaseOrder;
	
	@Setter
	private ApprovalBehaviorAtr approvalAtr;
	
	private ApprovalForm approvalForm;
	@Setter
	private List<ApprovalFrame> listApprovalFrame;
	
	public static ApprovalPhaseState createFromFirst(String companyID, GeneralDate date, 
			String rootStateID, ApprovalPhaseState approvalPhaseState){
		if(Strings.isBlank(approvalPhaseState.getRootStateID())){
			return ApprovalPhaseState.builder()
					.rootStateID(rootStateID)
					.phaseOrder(approvalPhaseState.getPhaseOrder())
					.approvalAtr(approvalPhaseState.getApprovalAtr())
					.approvalForm(approvalPhaseState.getApprovalForm())
					.listApprovalFrame(approvalPhaseState.getListApprovalFrame().stream()
							.map(x -> ApprovalFrame.createFromFirst(companyID, date, rootStateID, x)).collect(Collectors.toList()))
					.build();
		}
		return approvalPhaseState;
	}
	public static ApprovalPhaseState createFormTypeJava(String rootStateID, Integer phaseOrder,
			int approvalAtr,int approvalForm, List<ApprovalFrame> listApprovalFrame){
		return new ApprovalPhaseState(rootStateID,
				phaseOrder,
				EnumAdaptor.valueOf(approvalAtr, ApprovalBehaviorAtr.class),
				EnumAdaptor.valueOf(approvalForm, ApprovalForm.class),
				listApprovalFrame);
	}
	
	public boolean canRelease(String approverId) {
		return hasApprovedBy(approverId) || isRepresenter(approverId);
	}
	
	public boolean hasApprovedBy(String approverId) {
		return hasApproved() && isApprover(approverId);
	}
	
	private boolean hasApproved() {
		return approvalAtr == ApprovalBehaviorAtr.APPROVED;
	}
	
	private boolean isApprover(String employeeId) {
		return listApprovalFrame.stream()
				.anyMatch(f -> f.isApprover(employeeId));
	}
	
	private boolean isRepresenter(String employeeId) {
		return listApprovalFrame.stream()
				.anyMatch(f -> f.isRepresenter(employeeId));
	}
}
