/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.wageledger;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QlsptLedgerAggreDetailPK.
 */
@Getter
@Setter
@Embeddable
public class QlsptLedgerAggreDetailPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The pay bonus atr. */
	@Basic(optional = false)
	@Column(name = "PAY_BONUS_ATR")
	private int payBonusAtr;

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
	 * Instantiates a new qlspt ledger aggre detail PK.
	 */
	public QlsptLedgerAggreDetailPK() {
		super();
	}

	/**
	 * Instantiates a new qlspt ledger aggre detail PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param payBonusAtr
	 *            the pay bonus atr
	 * @param aggregateCd
	 *            the aggregate cd
	 * @param ctgAtr
	 *            the ctg atr
	 * @param itemCd
	 *            the item cd
	 */
	public QlsptLedgerAggreDetailPK(String ccd, int payBonusAtr, String aggregateCd, int ctgAtr, String itemCd) {
		this.ccd = ccd;
		this.payBonusAtr = payBonusAtr;
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
		hash += (int) payBonusAtr;
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
		if (!(object instanceof QlsptLedgerAggreDetailPK)) {
			return false;
		}
		QlsptLedgerAggreDetailPK other = (QlsptLedgerAggreDetailPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if (this.payBonusAtr != other.payBonusAtr) {
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
