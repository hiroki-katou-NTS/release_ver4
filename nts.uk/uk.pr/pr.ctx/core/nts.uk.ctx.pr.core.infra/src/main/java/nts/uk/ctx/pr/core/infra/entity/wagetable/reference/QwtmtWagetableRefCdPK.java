/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.reference;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QwtmtWagetableRefCdPK.
 */
@Getter
@Setter
@Embeddable
public class QwtmtWagetableRefCdPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The ref cd no. */
	@Basic(optional = false)
	@Column(name = "REF_CD_NO")
	private String refCdNo;

	/**
	 * Instantiates a new qwtmt wagetable ref cd PK.
	 */
	public QwtmtWagetableRefCdPK() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable ref cd PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param refCdNo
	 *            the ref cd no
	 */
	public QwtmtWagetableRefCdPK(String ccd, String refCdNo) {
		this.ccd = ccd;
		this.refCdNo = refCdNo;
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
		hash += (refCdNo != null ? refCdNo.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableRefCdPK)) {
			return false;
		}
		QwtmtWagetableRefCdPK other = (QwtmtWagetableRefCdPK) object;
		if ((this.ccd == null && other.ccd != null)
				|| (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.refCdNo == null && other.refCdNo != null)
				|| (this.refCdNo != null && !this.refCdNo.equals(other.refCdNo))) {
			return false;
		}
		return true;
	}
}
