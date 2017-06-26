package nts.uk.ctx.at.record.infra.entity.divergencetime;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name="KMKMT_DIVERGENCE_ITEM_SET")
@AllArgsConstructor
@NoArgsConstructor
public class KmkmtDivergenceItemSet extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	/*主キー*/
	@EmbeddedId
    public KmkmtDivergenceItemSetPK kmkmtDivergenceItemSetPK;
	
	@Override
	protected Object getKey() {
		return kmkmtDivergenceItemSetPK;
	}

}
