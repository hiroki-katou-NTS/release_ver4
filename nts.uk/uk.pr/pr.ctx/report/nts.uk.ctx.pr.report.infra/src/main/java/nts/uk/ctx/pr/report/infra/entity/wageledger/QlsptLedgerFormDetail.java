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
 * The Class QlsptLedgerFormDetail.
 */
@Getter
@Setter
@Entity
@Table(name = "QLSPT_LEDGER_FORM_DETAIL")
public class QlsptLedgerFormDetail extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qlspt ledger form detail PK. */
	@EmbeddedId
	protected QlsptLedgerFormDetailPK qlsptLedgerFormDetailPK;
	
	/** The exclus ver. */
	@Basic(optional = false)
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The disp order. */
	@Basic(optional = false)
	@Column(name = "DISP_ORDER")
	private int dispOrder;

	/** The qlspt ledger form head. */
	@JoinColumns({ @JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "FORM_CD", referencedColumnName = "FORM_CD", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QlsptLedgerFormHead qlsptLedgerFormHead;

	/**
	 * Instantiates a new qlspt ledger form detail.
	 */
	public QlsptLedgerFormDetail() {
		super();
	}

	/**
	 * Instantiates a new qlspt ledger form detail.
	 *
	 * @param qlsptLedgerFormDetailPK
	 *            the qlspt ledger form detail PK
	 */
	public QlsptLedgerFormDetail(QlsptLedgerFormDetailPK qlsptLedgerFormDetailPK) {
		this.qlsptLedgerFormDetailPK = qlsptLedgerFormDetailPK;
	}

	/**
	 * Instantiates a new qlspt ledger form detail.
	 *
	 * @param qlsptLedgerFormDetailPK
	 *            the qlspt ledger form detail PK
	 * @param exclusVer
	 *            the exclus ver
	 * @param dispOrder
	 *            the disp order
	 */
	public QlsptLedgerFormDetail(QlsptLedgerFormDetailPK qlsptLedgerFormDetailPK, int exclusVer, int dispOrder) {
		this.qlsptLedgerFormDetailPK = qlsptLedgerFormDetailPK;
		this.exclusVer = exclusVer;
		this.dispOrder = dispOrder;
	}

	/**
	 * Instantiates a new qlspt ledger form detail.
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
	public QlsptLedgerFormDetail(String ccd, String formCd, int payBonusAtr, int ctgAtr, int aggregateAtr,
			String itemAgreCd) {
		this.qlsptLedgerFormDetailPK = new QlsptLedgerFormDetailPK(ccd, formCd, payBonusAtr, ctgAtr, aggregateAtr,
				itemAgreCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qlsptLedgerFormDetailPK != null ? qlsptLedgerFormDetailPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptLedgerFormDetail)) {
			return false;
		}
		QlsptLedgerFormDetail other = (QlsptLedgerFormDetail) object;
		if ((this.qlsptLedgerFormDetailPK == null && other.qlsptLedgerFormDetailPK != null)
				|| (this.qlsptLedgerFormDetailPK != null
						&& !this.qlsptLedgerFormDetailPK.equals(other.qlsptLedgerFormDetailPK))) {
			return false;
		}
		return true;
	}

	@Override
	protected Object getKey() {
		return this.qlsptLedgerFormDetailPK;
	}
}
