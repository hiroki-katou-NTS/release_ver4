/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class PismtPersonInsuHealB.
 */
@Entity
@Setter
@Getter
@Table(name = "PISMT_PERSON_INSU_HEAL_B")
public class PismtPersonInsuHealB implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The pismt person insu heal BPK. */
	@EmbeddedId
	protected PismtPersonInsuHealBPK pismtPersonInsuHealBPK;

	/** The ins date. */
	@Column(name = "INS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insDate;

	/** The ins ccd. */
	@Column(name = "INS_CCD")
	private String insCcd;

	/** The ins scd. */
	@Column(name = "INS_SCD")
	private String insScd;

	/** The ins pg. */
	@Column(name = "INS_PG")
	private String insPg;

	/** The upd date. */
	@Column(name = "UPD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updDate;

	/** The upd ccd. */
	@Column(name = "UPD_CCD")
	private String updCcd;

	/** The upd scd. */
	@Column(name = "UPD_SCD")
	private String updScd;

	/** The upd pg. */
	@Column(name = "UPD_PG")
	private String updPg;

	/** The exclus ver. */
	@Basic(optional = false)
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The inv scd. */
	@Column(name = "INV_SCD")
	private String invScd;

	/** The str ym. */
	@Basic(optional = false)
	@Column(name = "STR_YM")
	private int strYm;

	/** The end ym. */
	@Basic(optional = false)
	@Column(name = "END_YM")
	private int endYm;

	/** The fix occasion atr. */
	@Basic(optional = false)
	@Column(name = "FIX_OCCASION_ATR")
	private short fixOccasionAtr;

	/** The health insu grade. */
	@Basic(optional = false)
	@Column(name = "HEALTH_INSU_GRADE")
	private BigDecimal healthInsuGrade;

	/** The health insu avg earn. */
	@Basic(optional = false)
	@Column(name = "HEALTH_INSU_AVG_EARN")
	private long healthInsuAvgEarn;

	/** The health insu pra earn. */
	@Basic(optional = false)
	@Column(name = "HEALTH_INSU_PRA_EARN")
	private long healthInsuPraEarn;

	/**
	 * Instantiates a new pismt person insu heal B.
	 */
	public PismtPersonInsuHealB() {
	}

	/**
	 * Instantiates a new pismt person insu heal B.
	 *
	 * @param pismtPersonInsuHealBPK the pismt person insu heal BPK
	 */
	public PismtPersonInsuHealB(PismtPersonInsuHealBPK pismtPersonInsuHealBPK) {
		this.pismtPersonInsuHealBPK = pismtPersonInsuHealBPK;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (pismtPersonInsuHealBPK != null ? pismtPersonInsuHealBPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof PismtPersonInsuHealB)) {
			return false;
		}
		PismtPersonInsuHealB other = (PismtPersonInsuHealB) object;
		if ((this.pismtPersonInsuHealBPK == null && other.pismtPersonInsuHealBPK != null)
				|| (this.pismtPersonInsuHealBPK != null
						&& !this.pismtPersonInsuHealBPK.equals(other.pismtPersonInsuHealBPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.PismtPersonInsuHealB[ pismtPersonInsuHealBPK=" + pismtPersonInsuHealBPK + " ]";
	}

}
