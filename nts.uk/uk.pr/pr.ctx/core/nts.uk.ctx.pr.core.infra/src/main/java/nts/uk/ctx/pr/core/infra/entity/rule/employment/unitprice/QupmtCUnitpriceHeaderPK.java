/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * The Class QupmtCUnitpriceHeadPK.
 */
@Data
@Embeddable
public class QupmtCUnitpriceHeaderPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The c unitprice cd. */
	@Basic(optional = false)
	@Column(name = "C_UNITPRICE_CD")
	private String cUnitpriceCd;

	/**
	 * Instantiates a new qupmt C unitprice head PK.
	 */
	public QupmtCUnitpriceHeaderPK() {
		super();
	}

	/**
	 * Instantiates a new qupmt C unitprice head PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param cUnitpriceCd
	 *            the c unitprice cd
	 */
	public QupmtCUnitpriceHeaderPK(String ccd, String cUnitpriceCd) {
		this.ccd = ccd;
		this.cUnitpriceCd = cUnitpriceCd;
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
		hash += (cUnitpriceCd != null ? cUnitpriceCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QupmtCUnitpriceHeaderPK)) {
			return false;
		}
		QupmtCUnitpriceHeaderPK other = (QupmtCUnitpriceHeaderPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.cUnitpriceCd == null && other.cUnitpriceCd != null)
				|| (this.cUnitpriceCd != null && !this.cUnitpriceCd.equals(other.cUnitpriceCd))) {
			return false;
		}
		return true;
	}

}
