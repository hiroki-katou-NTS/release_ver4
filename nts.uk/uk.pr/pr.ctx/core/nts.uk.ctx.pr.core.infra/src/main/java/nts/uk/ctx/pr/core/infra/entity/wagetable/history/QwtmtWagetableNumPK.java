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
 * The Class QwtmtWagetableNumPK.
 */
@Getter
@Setter
@Embeddable
public class QwtmtWagetableNumPK implements Serializable {

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

	/** The element num no. */
	@Basic(optional = false)
	@Column(name = "ELEMENT_NUM_NO")
	private Integer elementNumNo;

	/**
	 * Instantiates a new qwtmt wagetable num PK.
	 */
	public QwtmtWagetableNumPK() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable num PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param histId
	 *            the hist id
	 * @param demensionNo
	 *            the demension no
	 * @param elementNumNo
	 *            the element num no
	 */
	public QwtmtWagetableNumPK(String ccd, String wageTableCd, String histId, Integer demensionNo,
			Integer elementNumNo) {
		this.ccd = ccd;
		this.wageTableCd = wageTableCd;
		this.histId = histId;
		this.demensionNo = demensionNo;
		this.elementNumNo = elementNumNo;
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
		hash += (int) elementNumNo;
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableNumPK)) {
			return false;
		}
		QwtmtWagetableNumPK other = (QwtmtWagetableNumPK) object;
		if ((this.ccd == null && other.ccd != null)
				|| (this.ccd != null && !this.ccd.equals(other.ccd))) {
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
		if (this.elementNumNo != other.elementNumNo) {
			return false;
		}
		return true;
	}
}
