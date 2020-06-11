package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class WwfdpApvPhaseInstanceMonthlyPK {
	
	@Column(name="ROOT_ID")
	private String rootID;
	
	@Column(name="PHASE_ORDER")
	private Integer phaseOrder;
	
}