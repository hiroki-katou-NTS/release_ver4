/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.wageledger;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QlsptLedgerAggreHead.
 */
@Getter
@Setter
@Entity
@Table(name = "QLSPT_LEDGER_AGGRE_HEAD")
public class QlsptLedgerAggreHead extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qlspt ledger aggre head PK. */
	@EmbeddedId
	protected QlsptLedgerAggreHeadPK qlsptLedgerAggreHeadPK;

	/** The exclus ver. */
	@Basic(optional = false)
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The aggregate name. */
	@Column(name = "AGGREGATE_NAME")
	private String aggregateName;

	/** The disp name zero atr. */
	@Basic(optional = false)
	@Column(name = "DISP_NAME_ZERO_ATR")
	private int dispNameZeroAtr;

	/** The disp zero atr. */
	@Basic(optional = false)
	@Column(name = "DISP_ZERO_SET")
	private int dispZeroAtr;

	/** The qlspt ledger aggre detail list. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qlsptLedgerAggreHead", orphanRemoval = true)
	private List<QlsptLedgerAggreDetail> qlsptLedgerAggreDetailList;

	/**
	 * Instantiates a new qlspt ledger aggre head.
	 */
	public QlsptLedgerAggreHead() {
		super();
	}

	/**
	 * Instantiates a new qlspt ledger aggre head.
	 *
	 * @param qlsptLedgerAggreHeadPK
	 *            the qlspt ledger aggre head PK
	 */
	public QlsptLedgerAggreHead(QlsptLedgerAggreHeadPK qlsptLedgerAggreHeadPK) {
		this.qlsptLedgerAggreHeadPK = qlsptLedgerAggreHeadPK;
	}

	/**
	 * Instantiates a new qlspt ledger aggre head.
	 *
	 * @param qlsptLedgerAggreHeadPK
	 *            the qlspt ledger aggre head PK
	 * @param exclusVer
	 *            the exclus ver
	 * @param dispNameZeroAtr
	 *            the disp name zero atr
	 * @param dispZeroAtr
	 *            the disp zero atr
	 */
	public QlsptLedgerAggreHead(QlsptLedgerAggreHeadPK qlsptLedgerAggreHeadPK, int exclusVer, int dispNameZeroAtr,
			int dispZeroAtr) {
		this.qlsptLedgerAggreHeadPK = qlsptLedgerAggreHeadPK;
		this.exclusVer = exclusVer;
		this.dispNameZeroAtr = dispNameZeroAtr;
		this.dispZeroAtr = dispZeroAtr;
	}

	/**
	 * Instantiates a new qlspt ledger aggre head.
	 *
	 * @param ccd
	 *            the ccd
	 * @param payBonusAtr
	 *            the pay bonus atr
	 * @param aggregateCd
	 *            the aggregate cd
	 * @param ctgAtr
	 *            the ctg atr
	 */
	public QlsptLedgerAggreHead(String ccd, int payBonusAtr, String aggregateCd, int ctgAtr) {
		this.qlsptLedgerAggreHeadPK = new QlsptLedgerAggreHeadPK(ccd, payBonusAtr, aggregateCd, ctgAtr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qlsptLedgerAggreHeadPK != null ? qlsptLedgerAggreHeadPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptLedgerAggreHead)) {
			return false;
		}
		QlsptLedgerAggreHead other = (QlsptLedgerAggreHead) object;
		if ((this.qlsptLedgerAggreHeadPK == null && other.qlsptLedgerAggreHeadPK != null)
				|| (this.qlsptLedgerAggreHeadPK != null
						&& !this.qlsptLedgerAggreHeadPK.equals(other.qlsptLedgerAggreHeadPK))) {
			return false;
		}
		return true;
	}

	@Override
	protected Object getKey() {
		return this.qlsptLedgerAggreHeadPK;
	}
}
