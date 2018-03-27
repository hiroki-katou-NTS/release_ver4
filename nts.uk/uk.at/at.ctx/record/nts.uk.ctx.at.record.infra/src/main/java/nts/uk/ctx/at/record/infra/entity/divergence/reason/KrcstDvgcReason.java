package nts.uk.ctx.at.record.infra.entity.divergence.reason;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;


/**
 * The persistent class for the KRCST_DVGC_REASON database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name="KRCST_DVGC_REASON")
public class KrcstDvgcReason extends UkJpaEntity implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@EmbeddedId
	private KrcstDvgcReasonPK id;

	/** The reason. */
	@Column(name="REASON")
	private String reason;

	/** The reason required. */
	@Column(name="REASON_REQUIRED")
	private BigDecimal reasonRequired;

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.id;
	}

}