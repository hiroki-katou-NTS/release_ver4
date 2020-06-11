package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class WwfdpApprovalFramePK {
	
	@Column(name="ROOT_STATE_ID")
	public String rootStateID;
	
	@Column(name="PHASE_ORDER")
	public Integer phaseOrder;
	
	@Column(name="FRAME_ORDER")
	public Integer frameOrder;
	
}
