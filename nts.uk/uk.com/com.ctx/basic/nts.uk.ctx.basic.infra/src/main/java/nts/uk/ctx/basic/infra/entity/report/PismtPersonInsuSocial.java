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
 * The Class PismtPersonInsuSocial.
 */
@Entity
@Setter
@Getter
@Table(name = "PISMT_PERSON_INSU_SOCIAL")
public class PismtPersonInsuSocial implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The pismt person insu social PK. */
	@EmbeddedId
	protected PismtPersonInsuSocialPK pismtPersonInsuSocialPK;
	
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
	
	/** The aged atr. */
	@Basic(optional = false)
	@Column(name = "AGED_ATR")
	private short agedAtr;
	
	/** The monthly earn by money. */
	@Basic(optional = false)
	@Column(name = "MONTHLY_EARN_BY_MONEY")
	private long monthlyEarnByMoney;
	
	/** The monthly earn by kind. */
	@Basic(optional = false)
	@Column(name = "MONTHLY_EARN_BY_KIND")
	private long monthlyEarnByKind;
	
	/** The dependant apply atr. */
	@Basic(optional = false)
	@Column(name = "DEPENDANT_APPLY_ATR")
	private short dependantApplyAtr;
	
	/** The insured atr. */
	@Basic(optional = false)
	@Column(name = "INSURED_ATR")
	private short insuredAtr;
	
	/** The join quit remark. */
	@Column(name = "JOIN_QUIT_REMARK")
	private String joinQuitRemark;
	
	/** The quit reason. */
	@Basic(optional = false)
	@Column(name = "QUIT_REASON")
	private short quitReason;
	
	/** The j 401 k atr. */
	@Basic(optional = false)
	@Column(name = "J401K_ATR")
	private short j401kAtr;
	
	/** The j 401 k bank account. */
	@Column(name = "J401K_BANK_ACCOUNT")
	private String j401kBankAccount;
	
	/** The health insu no. */
	@Basic(optional = false)
	@Column(name = "HEALTH_INSU_NO")
	private String healthInsuNo;
	
	/** The health insu quarify atr. */
	@Basic(optional = false)
	@Column(name = "HEALTH_INSU_QUARIFY_ATR")
	private String healthInsuQuarifyAtr;
	
	/** The basic pension no. */
	@Basic(optional = false)
	@Column(name = "BASIC_PENSION_NO")
	private String basicPensionNo;
	
	/** The first join date. */
	@Column(name = "FIRST_JOIN_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date firstJoinDate;
	
	/** The si office cd. */
	@Basic(optional = false)
	@Column(name = "SI_OFFICE_CD")
	private String siOfficeCd;

	/**
	 * Instantiates a new pismt person insu social.
	 */
	public PismtPersonInsuSocial() {
	}

	/**
	 * Instantiates a new pismt person insu social.
	 *
	 * @param pismtPersonInsuSocialPK the pismt person insu social PK
	 */
	public PismtPersonInsuSocial(PismtPersonInsuSocialPK pismtPersonInsuSocialPK) {
		this.pismtPersonInsuSocialPK = pismtPersonInsuSocialPK;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (pismtPersonInsuSocialPK != null ? pismtPersonInsuSocialPK.hashCode() : 0);
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof PismtPersonInsuSocial)) {
			return false;
		}
		PismtPersonInsuSocial other = (PismtPersonInsuSocial) object;
		if ((this.pismtPersonInsuSocialPK == null && other.pismtPersonInsuSocialPK != null)
				|| (this.pismtPersonInsuSocialPK != null
						&& !this.pismtPersonInsuSocialPK.equals(other.pismtPersonInsuSocialPK))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.PismtPersonInsuSocial[ pismtPersonInsuSocialPK=" + pismtPersonInsuSocialPK + " ]";
	}

}
