package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_APV_MON_PH_INSTANCE")
public class WwfdtApvPhaseInstanceMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvPhaseInstanceMonthlyPK pk;
	
	@Column(name="APPROVAL_FORM")
	private Integer approvalForm;
	
	public List<WwfdtApvFrameInstanceMonthly> listWwfdtApvFrameInstanceMonthly;

	@Override
	protected Object getKey() {
		return pk;
	}
	
}
