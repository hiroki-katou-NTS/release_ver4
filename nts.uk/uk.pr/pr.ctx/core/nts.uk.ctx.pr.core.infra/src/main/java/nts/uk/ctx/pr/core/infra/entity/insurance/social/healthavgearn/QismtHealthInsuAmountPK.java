/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QismtHealthInsuAmountPK.
 */
@Getter
@Setter
@Embeddable
public class QismtHealthInsuAmountPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The hist id. */
	@Column(name = "HIST_ID")
	private String histId;

	/** The health insu grade. */
	@Column(name = "HEALTH_INSU_GRADE")
	private BigDecimal healthInsuGrade;

	/**
	 * Instantiates a new qismt health insu amount PK.
	 */
	public QismtHealthInsuAmountPK() {
		super();
	}

	/**
	 * Instantiates a new qismt health insu amount PK.
	 *
	 * @param ccd the ccd
	 * @param siOfficeCd the si office cd
	 * @param histId the hist id
	 * @param healthInsuGrade the health insu grade
	 */
	public QismtHealthInsuAmountPK(String ccd, String siOfficeCd, String histId,
			BigDecimal healthInsuGrade) {
		super();
		this.histId = histId;
		this.healthInsuGrade = healthInsuGrade;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (histId != null ? histId.hashCode() : 0);
		hash +=  healthInsuGrade.intValue();
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtHealthInsuAmountPK)) {
			return false;
		}
		QismtHealthInsuAmountPK other = (QismtHealthInsuAmountPK) object;
		if ((this.histId == null && other.histId != null)
				|| (this.histId != null && !this.histId.equals(other.histId))) {
			return false;
		}
		if (this.healthInsuGrade != other.healthInsuGrade) {
			return false;
		}
		return true;
	}

}
