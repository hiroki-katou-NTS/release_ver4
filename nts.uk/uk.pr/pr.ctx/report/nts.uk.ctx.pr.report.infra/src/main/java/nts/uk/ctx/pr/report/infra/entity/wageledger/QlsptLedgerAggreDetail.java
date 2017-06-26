/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.wageledger;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QlsptLedgerAggreDetail.
 */
@Setter
@Getter
@Entity
@Table(name = "QLSPT_LEDGER_AGGRE_DETAIL")
public class QlsptLedgerAggreDetail extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qlspt ledger aggre detail PK. */
	@EmbeddedId
	protected QlsptLedgerAggreDetailPK qlsptLedgerAggreDetailPK;

	/** The exclus ver. */
	@Basic(optional = false)
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The qlspt ledger aggre head. */
	@JoinColumns({ @JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "PAY_BONUS_ATR", referencedColumnName = "PAY_BONUS_ATR", insertable = false, updatable = false),
			@JoinColumn(name = "AGGREGATE_CD", referencedColumnName = "AGGREGATE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "CTG_ATR", referencedColumnName = "CTG_ATR", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QlsptLedgerAggreHead qlsptLedgerAggreHead;

	/**
	 * Instantiates a new qlspt ledger aggre detail.
	 */
	public QlsptLedgerAggreDetail() {
		super();
	}

	/**
	 * Instantiates a new qlspt ledger aggre detail.
	 *
	 * @param qlsptLedgerAggreDetailPK
	 *            the qlspt ledger aggre detail PK
	 */
	public QlsptLedgerAggreDetail(QlsptLedgerAggreDetailPK qlsptLedgerAggreDetailPK) {
		this.qlsptLedgerAggreDetailPK = qlsptLedgerAggreDetailPK;
	}

	/**
	 * Instantiates a new qlspt ledger aggre detail.
	 *
	 * @param qlsptLedgerAggreDetailPK
	 *            the qlspt ledger aggre detail PK
	 * @param exclusVer
	 *            the exclus ver
	 */
	public QlsptLedgerAggreDetail(QlsptLedgerAggreDetailPK qlsptLedgerAggreDetailPK, int exclusVer) {
		this.qlsptLedgerAggreDetailPK = qlsptLedgerAggreDetailPK;
		this.exclusVer = exclusVer;
	}

	/**
	 * Instantiates a new qlspt ledger aggre detail.
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
	public QlsptLedgerAggreDetail(String ccd, int payBonusAtr, String aggregateCd, int ctgAtr, String itemCd) {
		this.qlsptLedgerAggreDetailPK = new QlsptLedgerAggreDetailPK(ccd, payBonusAtr, aggregateCd, ctgAtr, itemCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qlsptLedgerAggreDetailPK != null ? qlsptLedgerAggreDetailPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptLedgerAggreDetail)) {
			return false;
		}
		QlsptLedgerAggreDetail other = (QlsptLedgerAggreDetail) object;
		if ((this.qlsptLedgerAggreDetailPK == null && other.qlsptLedgerAggreDetailPK != null)
				|| (this.qlsptLedgerAggreDetailPK != null
						&& !this.qlsptLedgerAggreDetailPK.equals(other.qlsptLedgerAggreDetailPK))) {
			return false;
		}
		return true;
	}

	@Override
	protected Object getKey() {
		return this.qlsptLedgerAggreDetailPK;
	}
}
