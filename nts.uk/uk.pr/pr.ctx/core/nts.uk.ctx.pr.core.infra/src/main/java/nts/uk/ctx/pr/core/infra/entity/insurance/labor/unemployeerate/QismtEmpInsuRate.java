/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QismtEmpInsuRate.
 */
@Setter
@Getter
@Entity
@Table(name = "QISMT_EMP_INSU_RATE")
public class QismtEmpInsuRate extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qismt emp insu rate PK. */
	@EmbeddedId
	protected QismtEmpInsuRatePK qismtEmpInsuRatePK;

	/** The str ym. */
	@Basic(optional = false)
	@Column(name = "STR_YM")
	private int strYm;

	/** The end ym. */
	@Basic(optional = false)
	@Column(name = "END_YM")
	private int endYm;

	/** The p emp rate general. */
	@Basic(optional = false)
	@Column(name = "P_EMP_RATE_GENERAL")
	private BigDecimal pEmpRateGeneral;

	/** The p emp round general. */
	@Basic(optional = false)
	@Column(name = "P_EMP_ROUND_GENERAL")
	private int pEmpRoundGeneral;

	/** The c emp rate general. */
	@Basic(optional = false)
	@Column(name = "C_EMP_RATE_GENERAL")
	private BigDecimal cEmpRateGeneral;

	/** The c emp round general. */
	@Basic(optional = false)
	@Column(name = "C_EMP_ROUND_GENERAL")
	private int cEmpRoundGeneral;

	/** The p emp rate other. */
	@Basic(optional = false)
	@Column(name = "P_EMP_RATE_OTHER")
	private BigDecimal pEmpRateOther;

	/** The p emp round other. */
	@Basic(optional = false)
	@Column(name = "P_EMP_ROUND_OTHER")
	private int pEmpRoundOther;

	/** The c emp rate other. */
	@Basic(optional = false)
	@Column(name = "C_EMP_RATE_OTHER")
	private BigDecimal cEmpRateOther;

	/** The c emp round other. */
	@Basic(optional = false)
	@Column(name = "C_EMP_ROUND_OTHER")
	private int cEmpRoundOther;

	/** The p emp rate const. */
	@Basic(optional = false)
	@Column(name = "P_EMP_RATE_CONST")
	private BigDecimal pEmpRateConst;

	/** The p emp round const. */
	@Basic(optional = false)
	@Column(name = "P_EMP_ROUND_CONST")
	private int pEmpRoundConst;

	/** The c emp rate const. */
	@Basic(optional = false)
	@Column(name = "C_EMP_RATE_CONST")
	private BigDecimal cEmpRateConst;

	/** The c emp round const. */
	@Basic(optional = false)
	@Column(name = "C_EMP_ROUND_CONST")
	private int cEmpRoundConst;

	/**
	 * Instantiates a new qismt emp insu rate.
	 */
	public QismtEmpInsuRate() {
		super();
	}

	/**
	 * Instantiates a new qismt emp insu rate.
	 *
	 * @param qismtEmpInsuRatePK
	 *            the qismt emp insu rate PK
	 */
	public QismtEmpInsuRate(QismtEmpInsuRatePK qismtEmpInsuRatePK) {
		this.qismtEmpInsuRatePK = qismtEmpInsuRatePK;
	}

	/**
	 * Instantiates a new qismt emp insu rate.
	 *
	 * @param ccd
	 *            the ccd
	 * @param histId
	 *            the hist id
	 */
	public QismtEmpInsuRate(String ccd, String histId) {
		this.qismtEmpInsuRatePK = new QismtEmpInsuRatePK(ccd, histId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qismtEmpInsuRatePK != null ? qismtEmpInsuRatePK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtEmpInsuRate)) {
			return false;
		}
		QismtEmpInsuRate other = (QismtEmpInsuRate) object;
		if ((this.qismtEmpInsuRatePK == null && other.qismtEmpInsuRatePK != null)
				|| (this.qismtEmpInsuRatePK != null
						&& !this.qismtEmpInsuRatePK.equals(other.qismtEmpInsuRatePK))) {
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
		return this.qismtEmpInsuRatePK;
	}

}
