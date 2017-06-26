/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QismtHealthInsuAmount.
 */
@Getter
@Setter
@Entity
@Table(name = "QISMT_HEALTH_INSU_AMOUNT")
public class QismtHealthInsuAmount extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Column(name = "CCD")
	private String ccd;

	/** The si office cd. */
	@Column(name = "SI_OFFICE_CD")
	private String siOfficeCd;

	/** The qismt health insu amount PK. */
	@EmbeddedId
	protected QismtHealthInsuAmountPK qismtHealthInsuAmountPK;

	/** The p health general mny. */
	@Column(name = "P_HEALTH_GENERAL_MNY")
	private BigDecimal pHealthGeneralMny;

	/** The p health nursing mny. */
	@Column(name = "P_HEALTH_NURSING_MNY")
	private BigDecimal pHealthNursingMny;

	/** The p health specific mny. */
	@Column(name = "P_HEALTH_SPECIFIC_MNY")
	private BigDecimal pHealthSpecificMny;

	/** The p health basic mny. */
	@Column(name = "P_HEALTH_BASIC_MNY")
	private BigDecimal pHealthBasicMny;

	/** The c health general mny. */
	@Column(name = "C_HEALTH_GENERAL_MNY")
	private BigDecimal cHealthGeneralMny;

	/** The c health nursing mny. */
	@Column(name = "C_HEALTH_NURSING_MNY")
	private BigDecimal cHealthNursingMny;

	/** The c health specific mny. */
	@Column(name = "C_HEALTH_SPECIFIC_MNY")
	private BigDecimal cHealthSpecificMny;

	/** The c health basic mny. */
	@Column(name = "C_HEALTH_BASIC_MNY")
	private BigDecimal cHealthBasicMny;

	/** The qismt heal insu avgearn D. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false),
			@JoinColumn(name = "HEALTH_INSU_GRADE", referencedColumnName = "HEALTH_INSU_GRADE", insertable = false, updatable = false) })
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	private QismtHealInsuAvgearnD qismtHealInsuAvgearnD;

	/**
	 * Instantiates a new qismt health insu amount.
	 */
	public QismtHealthInsuAmount() {
	}

	/**
	 * Instantiates a new qismt health insu amount.
	 *
	 * @param qismtHealthInsuAmountPK
	 *            the qismt health insu amount PK
	 */
	public QismtHealthInsuAmount(QismtHealthInsuAmountPK qismtHealthInsuAmountPK) {
		this.qismtHealthInsuAmountPK = qismtHealthInsuAmountPK;
	}

	/**
	 * Instantiates a new qismt health insu amount.
	 *
	 * @param ccd
	 *            the ccd
	 * @param siOfficeCd
	 *            the si office cd
	 * @param histId
	 *            the hist id
	 * @param healthInsuGrade
	 *            the health insu grade
	 */
	public QismtHealthInsuAmount(String ccd, String siOfficeCd, String histId,
			BigDecimal healthInsuGrade) {
		this.qismtHealthInsuAmountPK = new QismtHealthInsuAmountPK(ccd, siOfficeCd, histId,
				healthInsuGrade);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qismtHealthInsuAmountPK != null ? qismtHealthInsuAmountPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtHealthInsuAmount)) {
			return false;
		}
		QismtHealthInsuAmount other = (QismtHealthInsuAmount) object;
		if ((this.qismtHealthInsuAmountPK == null && other.qismtHealthInsuAmountPK != null)
				|| (this.qismtHealthInsuAmountPK != null
						&& !this.qismtHealthInsuAmountPK.equals(other.qismtHealthInsuAmountPK))) {
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
		return this.qismtHealthInsuAmountPK;
	}

}
