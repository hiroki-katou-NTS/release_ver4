/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice;

import java.io.Serializable;
import java.math.BigDecimal;

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
 * The Class QupmtCUnitpriceDetail.
 */
@Getter
@Setter
@Entity
@Table(name = "QUPMT_C_UNITPRICE_DETAIL")
public class QupmtCUnitpriceDetail extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qupmt C unitprice detail PK. */
	@EmbeddedId
	protected QupmtCUnitpriceDetailPK qupmtCUnitpriceDetailPK;

	/** The str ym. */
	@Basic(optional = false)
	@Column(name = "STR_YM")
	private int strYm;

	/** The end ym. */
	@Basic(optional = false)
	@Column(name = "END_YM")
	private int endYm;

	/** The c unitprice. */
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@Column(name = "C_UNITPRICE")
	private BigDecimal cUnitprice;

	/** The fix pay set. */
	@Basic(optional = false)
	@Column(name = "FIX_PAY_SET")
	private int fixPaySet;

	/** The fix pay atr. */
	@Basic(optional = false)
	@Column(name = "FIX_PAY_ATR")
	private int fixPayAtr;

	/** The fix pay atr monthly. */
	@Basic(optional = false)
	@Column(name = "FIX_PAY_ATR_MONTHLY")
	private int fixPayAtrMonthly;

	/** The fix pay atr daymonth. */
	@Basic(optional = false)
	@Column(name = "FIX_PAY_ATR_DAYMONTH")
	private int fixPayAtrDaymonth;

	/** The fix pay atr daily. */
	@Basic(optional = false)
	@Column(name = "FIX_PAY_ATR_DAILY")
	private int fixPayAtrDaily;

	/** The fix pay atr hourly. */
	@Basic(optional = false)
	@Column(name = "FIX_PAY_ATR_HOURLY")
	private int fixPayAtrHourly;

	/** The memo. */
	@Column(name = "MEMO")
	private String memo;

	/** The qupmt C unitprice head. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "C_UNITPRICE_CD", referencedColumnName = "C_UNITPRICE_CD", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QupmtCUnitpriceHeader qupmtCUnitpriceHeader;

	/**
	 * Instantiates a new qupmt C unitprice detail.
	 */
	public QupmtCUnitpriceDetail() {
		super();
	}

	/**
	 * Instantiates a new qupmt C unitprice detail.
	 *
	 * @param qupmtCUnitpriceDetailPK
	 *            the qupmt C unitprice detail PK
	 */
	public QupmtCUnitpriceDetail(QupmtCUnitpriceDetailPK qupmtCUnitpriceDetailPK) {
		this.qupmtCUnitpriceDetailPK = qupmtCUnitpriceDetailPK;
	}

	/**
	 * Instantiates a new qupmt C unitprice detail.
	 *
	 * @param ccd
	 *            the ccd
	 * @param cUnitpriceCd
	 *            the c unitprice cd
	 * @param histId
	 *            the hist id
	 */
	public QupmtCUnitpriceDetail(String ccd, String cUnitpriceCd, String histId) {
		this.qupmtCUnitpriceDetailPK = new QupmtCUnitpriceDetailPK(ccd, cUnitpriceCd, histId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qupmtCUnitpriceDetailPK != null ? qupmtCUnitpriceDetailPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QupmtCUnitpriceDetail)) {
			return false;
		}
		QupmtCUnitpriceDetail other = (QupmtCUnitpriceDetail) object;
		if ((this.qupmtCUnitpriceDetailPK == null && other.qupmtCUnitpriceDetailPK != null)
				|| (this.qupmtCUnitpriceDetailPK != null
						&& !this.qupmtCUnitpriceDetailPK.equals(other.qupmtCUnitpriceDetailPK))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.qupmtCUnitpriceDetailPK;
	}
}
