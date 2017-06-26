/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.report;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;

/**
 * The Class PismtPersonInsuCare.
 */
@Setter
@Getter
@Entity
@Table(name = "PISMT_PERSON_INSU_CARE")
public class PismtPersonInsuCare implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The pismt person insu care PK. */
	@EmbeddedId
	protected PismtPersonInsuCarePK pismtPersonInsuCarePK;
	
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
	
	/** The str D. */
	@Basic(optional = false)
	@Column(name = "STR_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate strD;
	
	/** The end D. */
	@Basic(optional = false)
	@Column(name = "END_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate endD;
	
	/** The care insu atr. */
	@Basic(optional = false)
	@Column(name = "CARE_INSU_ATR")
	private short careInsuAtr;
	
	/** The care insu no. */
	@Column(name = "CARE_INSU_NO")
	private String careInsuNo;
	
	/** The care insu insured no. */
	@Column(name = "CARE_INSU_INSURED_NO")
	private String careInsuInsuredNo;

	/**
	 * Instantiates a new pismt person insu care.
	 */
	public PismtPersonInsuCare() {
	}

	/**
	 * Instantiates a new pismt person insu care.
	 *
	 * @param pismtPersonInsuCarePK the pismt person insu care PK
	 */
	public PismtPersonInsuCare(PismtPersonInsuCarePK pismtPersonInsuCarePK) {
		this.pismtPersonInsuCarePK = pismtPersonInsuCarePK;
	}

	/**
	 * Instantiates a new pismt person insu care.
	 *
	 * @param pismtPersonInsuCarePK the pismt person insu care PK
	 * @param exclusVer the exclus ver
	 * @param strD the str D
	 * @param endD the end D
	 * @param careInsuAtr the care insu atr
	 */
	public PismtPersonInsuCare(PismtPersonInsuCarePK pismtPersonInsuCarePK, int exclusVer, GeneralDate strD, GeneralDate endD,
			short careInsuAtr) {
		this.pismtPersonInsuCarePK = pismtPersonInsuCarePK;
		this.exclusVer = exclusVer;
		this.strD = strD;
		this.endD = endD;
		this.careInsuAtr = careInsuAtr;
	}

	/**
	 * Instantiates a new pismt person insu care.
	 *
	 * @param ccd the ccd
	 * @param pid the pid
	 * @param histId the hist id
	 */
	public PismtPersonInsuCare(String ccd, String pid, String histId) {
		this.pismtPersonInsuCarePK = new PismtPersonInsuCarePK(ccd, pid, histId);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (pismtPersonInsuCarePK != null ? pismtPersonInsuCarePK.hashCode() : 0);
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof PismtPersonInsuCare)) {
			return false;
		}
		PismtPersonInsuCare other = (PismtPersonInsuCare) object;
		if ((this.pismtPersonInsuCarePK == null && other.pismtPersonInsuCarePK != null)
				|| (this.pismtPersonInsuCarePK != null
						&& !this.pismtPersonInsuCarePK.equals(other.pismtPersonInsuCarePK))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.PismtPersonInsuCare[ pismtPersonInsuCarePK=" + pismtPersonInsuCarePK + " ]";
	}

}
