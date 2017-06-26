/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QismtPensionAvgearnDPK.
 */
@Getter
@Setter
@Embeddable
public class QismtPensionAvgearnDPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The hist id. */
	@Column(name = "HIST_ID")
	private String histId;

	/** The pension grade. */
	@Column(name = "PENSION_GRADE")
	private BigDecimal pensionGrade;

	/**
	 * Instantiates a new qismt pension avgearn DPK.
	 */
	public QismtPensionAvgearnDPK() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (histId != null ? histId.hashCode() : 0);
		hash += pensionGrade.intValue();
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtPensionAvgearnDPK)) {
			return false;
		}
		QismtPensionAvgearnDPK other = (QismtPensionAvgearnDPK) object;
		if ((this.histId == null && other.histId != null)
				|| (this.histId != null && !this.histId.equals(other.histId))) {
			return false;
		}
		if (this.pensionGrade != other.pensionGrade) {
			return false;
		}
		return true;
	}
}
