/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.salarydetail;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QlsptPaylstFormDetailPK.
 */
@Getter
@Setter
@Embeddable
public class QlsptPaylstFormDetailPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The form cd. */
	@Basic(optional = false)
	@Column(name = "FORM_CD")
	private String formCd;

	/** The ctg atr. */
	@Basic(optional = false)
	@Column(name = "CTG_ATR")
	private int ctgAtr;

	/** The aggregate atr. */
	@Basic(optional = false)
	@Column(name = "AGGREGATE_ATR")
	private int aggregateAtr;

	/** The item agre cd. */
	@Basic(optional = false)
	@Column(name = "ITEM_AGRE_CD")
	private String itemAgreCd;

	/**
	 * Instantiates a new qlspt paylst form detail PK.
	 */
	public QlsptPaylstFormDetailPK() {
		super();
	}

	/**
	 * Instantiates a new qlspt paylst form detail PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param formCd
	 *            the form cd
	 * @param ctgAtr
	 *            the ctg atr
	 * @param aggregateAtr
	 *            the aggregate atr
	 * @param itemAgreCd
	 *            the item agre cd
	 */
	public QlsptPaylstFormDetailPK(String ccd, String formCd, int ctgAtr, int aggregateAtr, String itemAgreCd) {
		this.ccd = ccd;
		this.formCd = formCd;
		this.ctgAtr = ctgAtr;
		this.aggregateAtr = aggregateAtr;
		this.itemAgreCd = itemAgreCd;
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
		hash += (formCd != null ? formCd.hashCode() : 0);
		hash += (int) ctgAtr;
		hash += (int) aggregateAtr;
		hash += (itemAgreCd != null ? itemAgreCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptPaylstFormDetailPK)) {
			return false;
		}
		QlsptPaylstFormDetailPK other = (QlsptPaylstFormDetailPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.formCd == null && other.formCd != null)
				|| (this.formCd != null && !this.formCd.equals(other.formCd))) {
			return false;
		}
		if (this.ctgAtr != other.ctgAtr) {
			return false;
		}
		if (this.aggregateAtr != other.aggregateAtr) {
			return false;
		}
		if ((this.itemAgreCd == null && other.itemAgreCd != null)
				|| (this.itemAgreCd != null && !this.itemAgreCd.equals(other.itemAgreCd))) {
			return false;
		}
		return true;
	}

}
