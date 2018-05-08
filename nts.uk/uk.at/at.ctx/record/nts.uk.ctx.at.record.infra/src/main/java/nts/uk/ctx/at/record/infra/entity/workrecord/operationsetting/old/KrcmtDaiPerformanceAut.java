package nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.old;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCMT_DAI_PERFORMANCE_AUT")
public class KrcmtDaiPerformanceAut extends UkJpaEntity{
	
	@EmbeddedId
	public KrcmtDaiPerformanceAutPk pk;
	
	@Column(name = "AVAILABILITY")
	public BigDecimal availability;

	@Override
	protected Object getKey() {
		return pk;
	}

}
