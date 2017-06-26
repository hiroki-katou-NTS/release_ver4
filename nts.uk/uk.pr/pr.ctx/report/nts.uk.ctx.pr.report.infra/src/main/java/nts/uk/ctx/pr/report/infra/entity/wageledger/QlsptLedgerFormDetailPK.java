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
 * The Class QlsptLedgerFormDetailPK.
 */
@Getter
@Setter
@Embeddable
public class QlsptLedgerFormDetailPK implements Serializable {

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

	/** The pay bonus atr. */
	@Basic(optional = false)
	@Column(name = "PAY_BONUS_ATR")
	private int payBonusAtr;

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
	 * Instantiates a new qlspt ledger form detail PK.
	 */
	public QlsptLedgerFormDetailPK() {
		super();
	}

	/**
	 * Instantiates a new qlspt ledger form detail PK.
	 *
	 * @param ccd
	 *            the ccd
	 * @param formCd
	 *            the form cd
	 * @param payBonusAtr
	 *            the pay bonus atr
	 * @param ctgAtr
	 *            the ctg atr
	 * @param aggregateAtr
	 *            the aggregate atr
	 * @param itemAgreCd
	 *            the item agre cd
	 */
	public QlsptLedgerFormDetailPK(String ccd, String formCd, int payBonusAtr, int ctgAtr, int aggregateAtr,
			String itemAgreCd) {
		this.ccd = ccd;
		this.formCd = formCd;
		this.payBonusAtr = payBonusAtr;
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
		hash += (int) payBonusAtr;
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
		if (!(object instanceof QlsptLedgerFormDetailPK)) {
			return false;
		}
		QlsptLedgerFormDetailPK other = (QlsptLedgerFormDetailPK) object;
		if ((this.ccd == null && other.ccd != null) || (this.ccd != null && !this.ccd.equals(other.ccd))) {
			return false;
		}
		if ((this.formCd == null && other.formCd != null)
				|| (this.formCd != null && !this.formCd.equals(other.formCd))) {
			return false;
		}
		if (this.payBonusAtr != other.payBonusAtr) {
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
