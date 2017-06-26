/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * The Class QismtEmpInsuRatePK.
 */
@Data
@Embeddable
public class QismtEmpInsuRatePK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The hist id. */
	@Basic(optional = false)
	@Column(name = "HIST_ID")
	private String histId;

	/**
	 * Instantiates a new qismt emp insu rate PK.
	 */
	public QismtEmpInsuRatePK() {
		super();
	}

	/**
	 * Instantiates a new qismt emp insu rate PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param histId
	 *            the hist id
	 */
	public QismtEmpInsuRatePK(String ccd, String histId) {
		this.ccd = ccd;
		this.histId = histId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ccd != null ? ccd.hashCode() : 0);
		hash += (histId != null ? histId.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtEmpInsuRatePK)) {
			return false;
		}
		QismtEmpInsuRatePK other = (QismtEmpInsuRatePK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.histId == null && other.histId != null)
				|| (this.histId != null && !this.histId.equals(other.histId))) {
			return false;
		}
		return true;
	}

}
