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
 * The Class QlsptPaylstAggreDetailPK.
 */
@Setter
@Getter
@Embeddable
public class QlsptPaylstAggreDetailPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The aggregate cd. */
	@Basic(optional = false)
	@Column(name = "AGGREGATE_CD")
	private String aggregateCd;

	/** The ctg atr. */
	@Basic(optional = false)
	@Column(name = "CTG_ATR")
	private int ctgAtr;

	/** The item cd. */
	@Basic(optional = false)
	@Column(name = "ITEM_CD")
	private String itemCd;

	/**
	 * Instantiates a new qlspt paylst aggre detail PK.
	 */
	public QlsptPaylstAggreDetailPK() {
		super();
	}

	/**
	 * Instantiates a new qlspt paylst aggre detail PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param aggregateCd
	 *            the aggregate cd
	 * @param ctgAtr
	 *            the ctg atr
	 * @param itemCd
	 *            the item cd
	 */
	public QlsptPaylstAggreDetailPK(String ccd, String aggregateCd, int ctgAtr, String itemCd) {
		this.ccd = ccd;
		this.aggregateCd = aggregateCd;
		this.ctgAtr = ctgAtr;
		this.itemCd = itemCd;
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
		hash += (aggregateCd != null ? aggregateCd.hashCode() : 0);
		hash += (int) ctgAtr;
		hash += (itemCd != null ? itemCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptPaylstAggreDetailPK)) {
			return false;
		}
		QlsptPaylstAggreDetailPK other = (QlsptPaylstAggreDetailPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.aggregateCd == null && other.aggregateCd != null)
				|| (this.aggregateCd != null && !this.aggregateCd.equals(other.aggregateCd))) {
			return false;
		}
		if (this.ctgAtr != other.ctgAtr) {
			return false;
		}
		if ((this.itemCd == null && other.itemCd != null)
				|| (this.itemCd != null && !this.itemCd.equals(other.itemCd))) {
			return false;
		}
		return true;
	}
}
