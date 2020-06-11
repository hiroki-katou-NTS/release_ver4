package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_APV_MON_AP_INSTANCE")
public class WwfdtApvApproveInstanceMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvApproveInstanceMonthlyPK pk;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
}
