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
@Table(name="WWFDT_MON_APV_FR_INSTANCE")
public class WwfdtApvFrameInstanceMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvFrameInstanceMonthlyPK pk;
	
	@Column(name="CONFIRM_ATR")
	private Integer confirmAtr;
	
	public List<WwfdtApvApproveInstanceMonthly> listWwfdtApvApproveInstanceMonthly;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
}
