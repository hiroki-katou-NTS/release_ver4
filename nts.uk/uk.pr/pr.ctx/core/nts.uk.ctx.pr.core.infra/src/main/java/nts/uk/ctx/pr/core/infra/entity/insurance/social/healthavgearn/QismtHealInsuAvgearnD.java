/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn;

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
 * The Class QismtHealInsuAvgearnD.
 */
@Setter
@Getter
@Entity
@Table(name = "QISMT_HEAL_INSU_AVGEARN_D")
public class QismtHealInsuAvgearnD extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Column(name = "CCD")
	private String ccd;

	/** The qismt heal insu avgearn DPK. */
	@EmbeddedId
	protected QismtHealInsuAvgearnDPK qismtHealInsuAvgearnDPK;

	/** The health insu avg earn. */
	@Column(name = "HEALTH_INSU_AVG_EARN")
	private long healthInsuAvgEarn;

	/** The health insu upper limit. */
	@Column(name = "HEALTH_INSU_UPPER_LIMIT")
	private long healthInsuUpperLimit;

	/**
	 * Instantiates a new qismt heal insu avgearn D.
	 */
	public QismtHealInsuAvgearnD() {
		super();
	}

	/**
	 * Instantiates a new qismt heal insu avgearn D.
	 *
	 * @param qismtHealInsuAvgearnDPK
	 *            the qismt heal insu avgearn DPK
	 */
	public QismtHealInsuAvgearnD(QismtHealInsuAvgearnDPK qismtHealInsuAvgearnDPK) {
		super();
		this.qismtHealInsuAvgearnDPK = qismtHealInsuAvgearnDPK;
	}

	/**
	 * Instantiates a new qismt heal insu avgearn D.
	 *
	 * @param ccd
	 *            the ccd
	 * @param histId
	 *            the hist id
	 * @param healthInsuGrade
	 *            the health insu grade
	 */
	public QismtHealInsuAvgearnD(String ccd, String histId, BigDecimal healthInsuGrade) {
		super();
		this.qismtHealInsuAvgearnDPK = new QismtHealInsuAvgearnDPK(ccd, histId, healthInsuGrade);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qismtHealInsuAvgearnDPK != null ? qismtHealInsuAvgearnDPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtHealInsuAvgearnD)) {
			return false;
		}
		QismtHealInsuAvgearnD other = (QismtHealInsuAvgearnD) object;
		if ((this.qismtHealInsuAvgearnDPK == null && other.qismtHealInsuAvgearnDPK != null)
				|| (this.qismtHealInsuAvgearnDPK != null
						&& !this.qismtHealInsuAvgearnDPK.equals(other.qismtHealInsuAvgearnDPK))) {
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
		return this.qismtHealInsuAvgearnDPK;
	}

}
