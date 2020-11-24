package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class WwfdpAppApvApproverStatePK {
	
	@Column(name="APP_ID")
	public String appID;
	
	@Column(name="PHASE_ORDER")
	public Integer phaseOrder;
	
	@Column(name="FRAME_ORDER")
	public Integer frameOrder;
	
	@Column(name="APPROVER_CHILD_ID")
	public String approverID;

}