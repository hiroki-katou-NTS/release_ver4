package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class WwfdpAppApvRootStatePK {
	
	@Column(name="APP_ID")
	public String appID;

}
