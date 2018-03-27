package nts.uk.ctx.at.record.infra.entity.divergence.time;

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
 * The persistent class for the KRCST_DVGC_TIME database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name="KRCST_DVGC_TIME")
public class KrcstDvgcTime extends UkJpaEntity implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@EmbeddedId
	private KrcstDvgcTimePK id;

	/** The dvgc reason inputed. */
	@Column(name="DVGC_REASON_INPUTED")
	private BigDecimal dvgcReasonInputed;

	/** The dvgc reason selected. */
	@Column(name="DVGC_REASON_SELECTED")
	private BigDecimal dvgcReasonSelected;

	/** The dvgc time name. */
	@Column(name="DVGC_TIME_NAME")
	private String dvgcTimeName;

	/** The dvgc time use set. */
	@Column(name="DVGC_TIME_USE_SET")
	private BigDecimal dvgcTimeUseSet;

	/** The dvgc type. */
	@Column(name="DVGC_TYPE")
	private BigDecimal dvgcType;

	/** The reason input canceled. */
	@Column(name="REASON_INPUT_CANCELED")
	private BigDecimal reasonInputCanceled;

	/** The reason select canceled. */
	@Column(name="REASON_SELECT_CANCELED")
	private BigDecimal reasonSelectCanceled;

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.id;
	}

}