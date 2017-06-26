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
 * The Class QwtmtWagetableRefTablePK.
 */
@Getter
@Setter
@Embeddable
public class QwtmtWagetableRefTablePK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The ref table no. */
	@Basic(optional = false)
	@Column(name = "REF_TABLE_NO")
	private String refTableNo;

	/**
	 * Instantiates a new qwtmt wagetable ref table PK.
	 */
	public QwtmtWagetableRefTablePK() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable ref table PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param refTableNo
	 *            the ref table no
	 */
	public QwtmtWagetableRefTablePK(String ccd, String refTableNo) {
		this.ccd = ccd;
		this.refTableNo = refTableNo;
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
		hash += (refTableNo != null ? refTableNo.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableRefTablePK)) {
			return false;
		}
		QwtmtWagetableRefTablePK other = (QwtmtWagetableRefTablePK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.refTableNo == null && other.refTableNo != null)
				|| (this.refTableNo != null && !this.refTableNo.equals(other.refTableNo))) {
			return false;
		}
		return true;
	}

}
