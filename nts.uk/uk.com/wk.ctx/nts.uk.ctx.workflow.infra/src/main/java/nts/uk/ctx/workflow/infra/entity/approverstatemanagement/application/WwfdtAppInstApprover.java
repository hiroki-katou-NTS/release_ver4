package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverInfor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 承認枠_承認者情報
 * @author hoatt
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="WWFDT_APP_INST_APPROVER")
@Builder
public class WwfdtAppInstApprover extends ContractUkJpaEntity {
	/**主キー*/
	@EmbeddedId
	public WwfdpApproverStatePK wwfdpApprovrStatePK;
	@Version
	@Column(name="EXCLUS_VER")
	private int version;
    /**承認区分*/
	@Column(name="APPROVAL_ATR")
	public Integer approvalAtr;
	/**確定区分*/
	@Column(name="CONFIRM_ATR")
	public Integer confirmAtr;
	/**代行者*/
	@Column(name="AGENT_ID")
	public String agentID;
	/**承認日*/
	@Column(name="APPROVAL_DATE")
	public GeneralDateTime approvalDate;
	/**理由*/
	@Column(name="APPROVAL_REASON")
	public String approvalReason;
	/**対象日*/
	@Column(name="APP_DATE")
	public GeneralDate appDate;
	/**順序*/
	@Column(name="APPROVER_LIST_ORDER")
	public Integer approverInListOrder;
	
	@ManyToOne
	@PrimaryKeyJoinColumns({
		@PrimaryKeyJoinColumn(name="ROOT_STATE_ID",referencedColumnName="ROOT_STATE_ID"),
		@PrimaryKeyJoinColumn(name="PHASE_ORDER",referencedColumnName="PHASE_ORDER")
	})
	private WwfdtApprovalPhaseState wwfdtApprovalPhaseState;

	@Override
	protected Object getKey() {
		return wwfdpApprovrStatePK;
	}
	
	public static WwfdtAppInstApprover fromDomain(String rootStateID, int phaseOrder, ApprovalFrame frame, ApproverInfor approverInfo){
		return WwfdtAppInstApprover.builder()
				.wwfdpApprovrStatePK(
						new WwfdpApproverStatePK(
								rootStateID, 
								phaseOrder, 
								frame.getFrameOrder(),
								approverInfo.getApproverID()))
				.approvalAtr(approverInfo.getApprovalAtr().value)
				.confirmAtr(frame.getConfirmAtr().value)
				.agentID(approverInfo.getAgentID())
				.approvalDate(approverInfo.getApprovalDate())
				.approvalReason(approverInfo.getApprovalReason()==null?null:approverInfo.getApprovalReason().v())
				.appDate(frame.getAppDate())
				.approverInListOrder(approverInfo.getApproverInListOrder())
				.build();
	}
	
//	public ApprovalFrame toDomain(){
//		return ApprovalFrame.builder()
//				.rootStateID(this.wwfdpApprovrStatePK.rootStateID)
//				.phaseOrder(this.wwfdpApprovrStatePK.phaseOrder)
//				.frameOrder(this.wwfdpApprovrStatePK.approverOrder)
//				.approvalAtr(EnumAdaptor.valueOf(this.approvalAtr, ApprovalBehaviorAtr.class))
//				.confirmAtr(EnumAdaptor.valueOf(this.confirmAtr, ConfirmPerson.class))
//				.approverID(this.approverID)
//				.representerID(this.representerID)
//				.approvalDate(this.approvalDate)
//				.approvalReason(this.approvalReason)
//				.listApproverState(this.listWwfdtApproverState.stream()
//									.map(x -> x.toDomain()).collect(Collectors.toList()))
//				.build();
//	}
}
