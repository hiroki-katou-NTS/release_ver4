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
 * The Class QwtmtWagetableCertifyPK.
 */
@Getter
@Setter
@Embeddable
public class QwtmtWagetableCertifyPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The certify group cd. */
	@Basic(optional = false)
	@Column(name = "CERTIFY_GROUP_CD")
	private String certifyGroupCd;

	/** The certify cd. */
	@Basic(optional = false)
	@Column(name = "CERTIFY_CD")
	private String certifyCd;

	/**
	 * Instantiates a new qwtmt wagetable certify PK.
	 */
	public QwtmtWagetableCertifyPK() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable certify PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param certifyGroupCd
	 *            the certify group cd
	 * @param certifyCd
	 *            the certify cd
	 */
	public QwtmtWagetableCertifyPK(String ccd, String certifyGroupCd, String certifyCd) {
		this.ccd = ccd;
		this.certifyGroupCd = certifyGroupCd;
		this.certifyCd = certifyCd;
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
		hash += (certifyGroupCd != null ? certifyGroupCd.hashCode() : 0);
		hash += (certifyCd != null ? certifyCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableCertifyPK)) {
			return false;
		}
		QwtmtWagetableCertifyPK other = (QwtmtWagetableCertifyPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.certifyGroupCd == null && other.certifyGroupCd != null)
				|| (this.certifyGroupCd != null && !this.certifyGroupCd.equals(other.certifyGroupCd))) {
			return false;
		}
		if ((this.certifyCd == null && other.certifyCd != null)
				|| (this.certifyCd != null && !this.certifyCd.equals(other.certifyCd))) {
			return false;
		}
		return true;
	}

}
