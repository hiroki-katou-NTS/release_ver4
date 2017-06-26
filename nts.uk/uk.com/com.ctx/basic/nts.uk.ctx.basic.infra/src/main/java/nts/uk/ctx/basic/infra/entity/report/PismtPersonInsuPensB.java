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
 * The Class PismtPersonInsuPensB.
 */
@Entity
@Setter
@Getter
@Table(name = "PISMT_PERSON_INSU_PENS_B")
public class PismtPersonInsuPensB implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The pismt person insu pens BPK. */
	@EmbeddedId
	protected PismtPersonInsuPensBPK pismtPersonInsuPensBPK;
	
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
	
	/** The pension insu grade. */
	@Basic(optional = false)
	@Column(name = "PENSION_INSU_GRADE")
	private BigDecimal pensionInsuGrade;
	
	/** The pension avg earn. */
	@Basic(optional = false)
	@Column(name = "PENSION_AVG_EARN")
	private long pensionAvgEarn;
	
	/** The pension pra earn. */
	@Basic(optional = false)
	@Column(name = "PENSION_PRA_EARN")
	private long pensionPraEarn;

	/**
	 * Instantiates a new pismt person insu pens B.
	 */
	public PismtPersonInsuPensB() {
	}

	/**
	 * Instantiates a new pismt person insu pens B.
	 *
	 * @param pismtPersonInsuPensBPK the pismt person insu pens BPK
	 */
	public PismtPersonInsuPensB(PismtPersonInsuPensBPK pismtPersonInsuPensBPK) {
		this.pismtPersonInsuPensBPK = pismtPersonInsuPensBPK;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (pismtPersonInsuPensBPK != null ? pismtPersonInsuPensBPK.hashCode() : 0);
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof PismtPersonInsuPensB)) {
			return false;
		}
		PismtPersonInsuPensB other = (PismtPersonInsuPensB) object;
		if ((this.pismtPersonInsuPensBPK == null && other.pismtPersonInsuPensBPK != null)
				|| (this.pismtPersonInsuPensBPK != null
						&& !this.pismtPersonInsuPensBPK.equals(other.pismtPersonInsuPensBPK))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.PismtPersonInsuPensB[ pismtPersonInsuPensBPK=" + pismtPersonInsuPensBPK + " ]";
	}

}
