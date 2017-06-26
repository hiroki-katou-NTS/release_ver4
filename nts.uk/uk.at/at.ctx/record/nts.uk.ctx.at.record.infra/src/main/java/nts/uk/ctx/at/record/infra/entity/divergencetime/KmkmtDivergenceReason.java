package nts.uk.ctx.at.record.infra.entity.divergencetime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@Entity
@Table(name = "KMKMT_DIVERGENCE_REASON")
@AllArgsConstructor
@NoArgsConstructor
public class KmkmtDivergenceReason extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/*主キー*/
	@EmbeddedId
	public KmkmtDivergenceReasonPK kmkmtDivergenceReasonPK;
	/*乖離理由*/
	@Column(name = "DIVERGENCE_REASON")
	public String divReason;
	/*必須区分*/
	@Column(name = "REQUIRED_ATR")
	public int requiredAtr;
	
	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
