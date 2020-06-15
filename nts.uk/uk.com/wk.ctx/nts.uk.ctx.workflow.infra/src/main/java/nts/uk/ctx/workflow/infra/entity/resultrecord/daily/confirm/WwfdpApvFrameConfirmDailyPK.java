package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
public class WwfdpApvFrameConfirmDailyPK {
	@Column(name="ROOT_ID")
	private String rootID;
	
	@Column(name="PHASE_ORDER")
	private Integer phaseOrder;
	
	@Column(name="FRAME_ORDER")
	private Integer frameOrder;
}