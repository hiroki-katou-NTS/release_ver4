package nts.uk.ctx.at.record.infra.entity.dailyperformanceformat;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="KFNMT_DAY_FORM_BUS_SORT")
public class KrcstBusinessTypeSorted extends ContractUkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    public KrcstBusinessTypeSortedPK krcstBusinessTypeSortedPK;
	
	@Column(name ="ORDER_SORTED")
	public BigDecimal order;
	
	@Override
	protected Object getKey() {
		return this.krcstBusinessTypeSortedPK;
	}

}
