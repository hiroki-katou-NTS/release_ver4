package nts.uk.ctx.at.record.infra.entity.dailyperformanceformat;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="KDWST_WORK_TYPE_SORTED")
public class KdwstWorkTypeSorted extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    public KdwstWorkTypeSortedPK kdwstWorkTypeSortedPK;
	
	@Column(name ="ORDER")
	public BigDecimal order;
	
	@Override
	protected Object getKey() {
		return this.kdwstWorkTypeSortedPK;
	}

}
