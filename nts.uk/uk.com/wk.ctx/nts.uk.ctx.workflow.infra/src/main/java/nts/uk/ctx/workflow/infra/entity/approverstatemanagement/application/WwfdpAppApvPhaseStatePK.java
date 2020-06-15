package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Getter
public class WwfdpAppApvPhaseStatePK {
	
	@Column(name="APP_ID")
	public String appID;
	
	@Column(name="PHASE_ORDER")
	public Integer phaseOrder;

}
