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
 * The Class QwtmtWagetableRefPayPK.
 */
@Getter
@Setter
@Embeddable
public class QwtmtWagetableRefPayPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The ref pay no. */
	@Basic(optional = false)
	@Column(name = "REF_PAY_NO")
	private String refPayNo;

	/**
	 * Instantiates a new qwtmt wagetable ref pay PK.
	 */
	public QwtmtWagetableRefPayPK() {
	}

	/**
	 * Instantiates a new qwtmt wagetable ref pay PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param refPayNo
	 *            the ref pay no
	 */
	public QwtmtWagetableRefPayPK(String ccd, String refPayNo) {
		this.ccd = ccd;
		this.refPayNo = refPayNo;
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
		hash += (refPayNo != null ? refPayNo.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableRefPayPK)) {
			return false;
		}
		QwtmtWagetableRefPayPK other = (QwtmtWagetableRefPayPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.refPayNo == null && other.refPayNo != null)
				|| (this.refPayNo != null && !this.refPayNo.equals(other.refPayNo))) {
			return false;
		}
		return true;
	}

}
