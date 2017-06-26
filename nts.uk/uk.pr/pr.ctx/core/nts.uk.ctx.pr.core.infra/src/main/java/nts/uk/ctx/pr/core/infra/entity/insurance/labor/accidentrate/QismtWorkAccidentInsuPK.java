/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * The Class QismtWorkAccidentInsuPK.
 */
@Data
@Embeddable
public class QismtWorkAccidentInsuPK implements Serializable {

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

	/** The wa insu cd. */
	@Basic(optional = false)
	@Column(name = "WA_INSU_CD")
	private int waInsuCd;

	/**
	 * Instantiates a new qismt work accident insu PK.
	 */
	public QismtWorkAccidentInsuPK() {
		super();
	}

	/**
	 * Instantiates a new qismt work accident insu PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param histId
	 *            the hist id
	 * @param waInsuCd
	 *            the wa insu cd
	 */
	public QismtWorkAccidentInsuPK(String ccd, String histId, int waInsuCd) {
		this.ccd = ccd;
		this.histId = histId;
		this.waInsuCd = waInsuCd;
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
		hash += (int) waInsuCd;
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtWorkAccidentInsuPK)) {
			return false;
		}
		QismtWorkAccidentInsuPK other = (QismtWorkAccidentInsuPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.histId == null && other.histId != null)
				|| (this.histId != null && !this.histId.equals(other.histId))) {
			return false;
		}
		if (this.waInsuCd != other.waInsuCd) {
			return false;
		}
		return true;
	}

}
