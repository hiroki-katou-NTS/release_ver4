package nts.uk.ctx.at.record.infra.entity.dailyattendanceitem;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "KSHST_DAI_SER_TYPE_CTR")
public class KshstDailyServiceTypeControl extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KshstDailyServiceTypeControlPK kshstDailyServiceTypeControlPK;
	@Column(name = "CHANGED_BY_YOU")
	public BigDecimal youCanChangeIt;
	@Column(name = "CHANGED_BY_OTHERS")
	public BigDecimal canBeChangedByOthers;
	@Column(name = "USE_ATR")
	public BigDecimal use;
	
	@Override
	protected Object getKey() {
		return kshstDailyServiceTypeControlPK;
	}

}
