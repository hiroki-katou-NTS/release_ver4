/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QwtmtWagetableHeadPK.
 */
@Getter
@Setter
@Embeddable
public class QwtmtWagetableHeadPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The wage table cd. */
	@Basic(optional = false)
	@Column(name = "WAGE_TABLE_CD")
	private String wageTableCd;

	/**
	 * Instantiates a new qwtmt wagetable head PK.
	 */
	public QwtmtWagetableHeadPK() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable head PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 */
	public QwtmtWagetableHeadPK(String ccd, String wageTableCd) {
		this.ccd = ccd;
		this.wageTableCd = wageTableCd;
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
		hash += (wageTableCd != null ? wageTableCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableHeadPK)) {
			return false;
		}
		QwtmtWagetableHeadPK other = (QwtmtWagetableHeadPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.wageTableCd == null && other.wageTableCd != null)
				|| (this.wageTableCd != null && !this.wageTableCd.equals(other.wageTableCd))) {
			return false;
		}
		return true;
	}

}
