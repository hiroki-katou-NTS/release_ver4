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
 * The Class QwtmtWagetableMnyPK.
 */
@Getter
@Setter
@Embeddable
public class QwtmtWagetableMnyPK implements Serializable {

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

	/** The element 1 id. */
	@Column(name = "ELEMENT1_ID")
	private String element1Id;

	/** The element 2 id. */
	@Column(name = "ELEMENT2_ID")
	private String element2Id;

	/** The element 3 id. */
	@Column(name = "ELEMENT3_ID")
	private String element3Id;

	/**
	 * Instantiates a new qwtmt wagetable mny PK.
	 */
	public QwtmtWagetableMnyPK() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable mny PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param histId
	 *            the hist id
	 * @param element1Id
	 *            the element 1 id
	 * @param element2Id
	 *            the element 2 id
	 * @param element3Id
	 *            the element 3 id
	 */
	public QwtmtWagetableMnyPK(String ccd, String wageTableCd, String histId, String element1Id,
			String element2Id, String element3Id) {
		this.ccd = ccd;
		this.wageTableCd = wageTableCd;
		this.histId = histId;
		this.element1Id = element1Id;
		this.element2Id = element2Id;
		this.element3Id = element3Id;
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
		hash += (element1Id != null ? element1Id.hashCode() : 0);
		hash += (element2Id != null ? element2Id.hashCode() : 0);
		hash += (element3Id != null ? element3Id.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableMnyPK)) {
			return false;
		}
		QwtmtWagetableMnyPK other = (QwtmtWagetableMnyPK) object;
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
		if ((this.element1Id == null && other.element1Id != null)
				|| (this.element1Id != null && !this.element1Id.equals(other.element1Id))) {
			return false;
		}
		if ((this.element2Id == null && other.element2Id != null)
				|| (this.element2Id != null && !this.element2Id.equals(other.element2Id))) {
			return false;
		}
		if ((this.element3Id == null && other.element3Id != null)
				|| (this.element3Id != null && !this.element3Id.equals(other.element3Id))) {
			return false;
		}
		return true;
	}

}
