/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.history;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QwtmtWagetableEleHistPK.
 */
@Getter
@Setter
@Embeddable
public class QwtmtWagetableEleHistPK implements Serializable {

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

	/** The hist id. */
	@Basic(optional = false)
	@Column(name = "HIST_ID")
	private String histId;

	/** The demension no. */
	@Basic(optional = false)
	@Column(name = "DEMENSION_NO")
	private Integer demensionNo;

	/**
	 * Instantiates a new qwtmt wagetable ele hist PK.
	 */
	public QwtmtWagetableEleHistPK() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable ele hist PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param histId
	 *            the hist id
	 * @param demensionNo
	 *            the demension no
	 */
	public QwtmtWagetableEleHistPK(String ccd, String wageTableCd, String histId, Integer demensionNo) {
		this.ccd = ccd;
		this.wageTableCd = wageTableCd;
		this.histId = histId;
		this.demensionNo = demensionNo;
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
		hash += (histId != null ? histId.hashCode() : 0);
		hash += (int) demensionNo;
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableEleHistPK)) {
			return false;
		}
		QwtmtWagetableEleHistPK other = (QwtmtWagetableEleHistPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.wageTableCd == null && other.wageTableCd != null)
				|| (this.wageTableCd != null && !this.wageTableCd.equals(other.wageTableCd))) {
			return false;
		}
		if ((this.histId == null && other.histId != null)
				|| (this.histId != null && !this.histId.equals(other.histId))) {
			return false;
		}
		if (this.demensionNo != other.demensionNo) {
			return false;
		}
		return true;
	}
}
