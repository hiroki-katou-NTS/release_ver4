/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionrate.QismtPensionRate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QismtPensionAvgearnD.
 */
@Getter
@Setter
@Entity
@Table(name = "QISMT_PENSION_AVGEARN_D")
public class QismtPensionAvgearnD extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Column(name = "CCD")
	private String ccd;

	/** The qismt pension avgearn DPK. */
	@EmbeddedId
	protected QismtPensionAvgearnDPK qismtPensionAvgearnDPK;

	/** The pension avg earn. */
	@Column(name = "PENSION_AVG_EARN")
	private long pensionAvgEarn;

	/** The pension upper limit. */
	@Column(name = "PENSION_UPPER_LIMIT")
	private long pensionUpperLimit;

	/** The qismt pension rate. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QismtPensionRate qismtPensionRate;

	/**
	 * Instantiates a new qismt pension avgearn D.
	 */
	public QismtPensionAvgearnD() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qismtPensionAvgearnDPK != null ? qismtPensionAvgearnDPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtPensionAvgearnD)) {
			return false;
		}
		QismtPensionAvgearnD other = (QismtPensionAvgearnD) object;
		if ((this.qismtPensionAvgearnDPK == null && other.qismtPensionAvgearnDPK != null)
				|| (this.qismtPensionAvgearnDPK != null
						&& !this.qismtPensionAvgearnDPK.equals(other.qismtPensionAvgearnDPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.qismtPensionAvgearnDPK;
	}

}
