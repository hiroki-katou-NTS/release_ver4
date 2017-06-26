/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.certification;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QcemtCertificationPK.
 */
@Getter
@Setter
@Embeddable
public class QcemtCertificationPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The cert cd. */
	@Basic(optional = false)
	@Column(name = "CERT_CD")
	private String certCd;

	/**
	 * Instantiates a new qcemt certification PK.
	 */
	public QcemtCertificationPK() {
		super();
	}

	/**
	 * Instantiates a new qcemt certification PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param certCd
	 *            the cert cd
	 */
	public QcemtCertificationPK(String ccd, String certCd) {
		this.ccd = ccd;
		this.certCd = certCd;
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
		hash += (certCd != null ? certCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QcemtCertificationPK)) {
			return false;
		}
		QcemtCertificationPK other = (QcemtCertificationPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.certCd == null && other.certCd != null)
				|| (this.certCd != null && !this.certCd.equals(other.certCd))) {
			return false;
		}
		return true;
	}

}
