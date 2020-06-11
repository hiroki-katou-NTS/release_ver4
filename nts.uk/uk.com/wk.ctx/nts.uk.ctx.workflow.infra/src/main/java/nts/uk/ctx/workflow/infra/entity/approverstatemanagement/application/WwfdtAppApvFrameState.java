package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="WWFDT_APP_APV_FR_STATE")
@Builder
public class WwfdtAppApvFrameState extends UkJpaEntity {
	
	@EmbeddedId
	public WwfdpAppApvFrameStatePK wwfdpAppApvFrameStatePK;
	
	@Column(name="CID")
	public String companyID;
	
	@Column(name="EMPLOYEE_ID")
	public String employeeID;
	
	@Column(name="APP_DATE")
	public GeneralDate appDate;
	
	@Column(name="APP_FRAME_ATR")
	public Integer approvalAtr;
	
	@Column(name="CONFIRM_ATR")
	public Integer confirmAtr;
	
	@Column(name="APPROVER_ID")
	public String approverID;
	
	@Column(name="REPRESENTER_ID")
	public String representerID;
	
	@Column(name="APPROVAL_DATE")
	public GeneralDate approvalDate;
	
	@Column(name="APPROVAL_REASON")
	public String approvalReason;
	
	public List<WwfdtAppApvApproverState> listWwfdtAppApvApproverState;

	@Override
	protected Object getKey() {
		return wwfdpAppApvFrameStatePK;
	}
	
	public static WwfdtAppApvFrameState fromDomain(String companyID, String employeeID, GeneralDate appDate, ApprovalFrame approvalFrame){
		return WwfdtAppApvFrameState.builder()
				.wwfdpAppApvFrameStatePK(new WwfdpAppApvFrameStatePK(
						approvalFrame.getRootStateID(), 
						approvalFrame.getPhaseOrder(), 
						approvalFrame.getFrameOrder()))
				.companyID(companyID)
				.employeeID(employeeID)
				.appDate(appDate)
				
				.approvalAtr(approvalFrame.getApprovalAtr().value)
				.confirmAtr(approvalFrame.getConfirmAtr().value)
				.approverID(approvalFrame.getApproverID())
				.representerID(approvalFrame.getRepresenterID())
				.approvalDate(approvalFrame.getApprovalDate())
				.approvalReason(approvalFrame.getApprovalReason())
				.listWwfdtAppApvApproverState(approvalFrame.getListApproverState()
						.stream()
						.map(a -> WwfdtAppApvApproverState.fromDomain(companyID, employeeID, appDate, a))
								.collect(Collectors.toList()))
				.build();
	}

	
}
