/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * The Class QismtLaborInsuOfficePK.
 */
@Data
@Embeddable
public class QismtLaborInsuOfficePK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The li office cd. */
	@Basic(optional = false)
	@Column(name = "LI_OFFICE_CD")
	private String liOfficeCd;

	/**
	 * Instantiates a new qismt labor insu office PK.
	 */
	public QismtLaborInsuOfficePK() {
		super();
	}

	/**
	 * Instantiates a new qismt labor insu office PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param liOfficeCd
	 *            the li office cd
	 */
	public QismtLaborInsuOfficePK(String ccd, String liOfficeCd) {
		this.ccd = ccd;
		this.liOfficeCd = liOfficeCd;
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
		hash += (liOfficeCd != null ? liOfficeCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtLaborInsuOfficePK)) {
			return false;
		}
		QismtLaborInsuOfficePK other = (QismtLaborInsuOfficePK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.liOfficeCd == null && other.liOfficeCd != null)
				|| (this.liOfficeCd != null && !this.liOfficeCd.equals(other.liOfficeCd))) {
			return false;
		}
		return true;
	}
}
