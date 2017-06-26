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
 * The Class QupmtCUnitpriceHistPK.
 */
@Data
@Embeddable
public class QupmtCUnitpriceDetailPK implements Serializable {

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

	/** The hist id. */
	@Basic(optional = false)
	@Column(name = "HIST_ID")
	private String histId;

	/**
	 * Instantiates a new qupmt C unitprice hist PK.
	 */
	public QupmtCUnitpriceDetailPK() {
		super();
	}

	/**
	 * Instantiates a new qupmt C unitprice hist PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param cUnitpriceCd
	 *            the c unitprice cd
	 * @param histId
	 *            the hist id
	 */
	public QupmtCUnitpriceDetailPK(String ccd, String cUnitpriceCd, String histId) {
		this.ccd = ccd;
		this.cUnitpriceCd = cUnitpriceCd;
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
		hash += (cUnitpriceCd != null ? cUnitpriceCd.hashCode() : 0);
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
		if (!(object instanceof QupmtCUnitpriceDetailPK)) {
			return false;
		}
		QupmtCUnitpriceDetailPK other = (QupmtCUnitpriceDetailPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.cUnitpriceCd == null && other.cUnitpriceCd != null)
				|| (this.cUnitpriceCd != null && !this.cUnitpriceCd.equals(other.cUnitpriceCd))) {
			return false;
		}
		if ((this.histId == null && other.histId != null)
				|| (this.histId != null && !this.histId.equals(other.histId))) {
			return false;
		}
		return true;
	}

}
