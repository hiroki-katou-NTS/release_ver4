/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.report;

import java.io.Serializable;

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
 * The Class PcpmtPersonCom.
 */
@Entity
@Table(name = "PCPMT_PERSON_COM")
@Setter
@Getter
public class PcpmtPersonCom implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The pcpmt person com PK. */
	@EmbeddedId
	protected PcpmtPersonComPK pcpmtPersonComPK;

	/** The ins date. */
	@Column(name = "INS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate insDate;

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
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate updDate;

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

	/** The scd. */
	@Basic(optional = false)
	@Column(name = "SCD")
	private String scd;

	/** The str D. */
	@Basic(optional = false)
	@Column(name = "STR_D")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate strD;

	/** The end D. */
	@Column(name = "END_D")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate endD;

	/** The exp D. */
	@Basic(optional = false)
	@Column(name = "EXP_D")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate expD;

	/** The adopt date. */
	@Column(name = "ADOPT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate adoptDate;

	/** The com cellphone. */
	@Column(name = "COM_CELLPHONE")
	private String comCellphone;

	/** The dialin phone. */
	@Column(name = "DIALIN_PHONE")
	private String dialinPhone;

	/** The extention phone. */
	@Column(name = "EXTENTION_PHONE")
	private String extentionPhone;

	/** The com mail. */
	@Column(name = "COM_MAIL")
	private String comMail;

	/** The com mobile mail. */
	@Column(name = "COM_MOBILE_MAIL")
	private String comMobileMail;

	/** The out cd. */
	@Column(name = "OUT_CD")
	private String outCd;

	/** The adopt type. */
	@Basic(optional = false)
	@Column(name = "ADOPT_TYPE")
	private short adoptType;

	/** The retire pay str. */
	@Column(name = "RETIRE_PAY_STR")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate retirePayStr;

	/** The regular com. */
	@Basic(optional = false)
	@Column(name = "REGULAR_COM")
	private short regularCom;

	/** The quit procedure. */
	@Column(name = "QUIT_PROCEDURE")
	private String quitProcedure;

	/** The firing reason. */
	@Column(name = "FIRING_REASON")
	private String firingReason;

	/** The quit remark. */
	@Column(name = "QUIT_REMARK")
	private String quitRemark;

	/** The quit firing atr. */
	@Basic(optional = false)
	@Column(name = "QUIT_FIRING_ATR")
	private String quitFiringAtr;

	/** The quit firing reason atr. */
	@Basic(optional = false)
	@Column(name = "QUIT_FIRING_REASON_ATR")
	private String quitFiringReasonAtr;

	/** The firing reason A. */
	@Column(name = "FIRING_REASON_A")
	private String firingReasonA;

	/** The firing reason B. */
	@Column(name = "FIRING_REASON_B")
	private String firingReasonB;

	/** The firing reason C. */
	@Column(name = "FIRING_REASON_C")
	private String firingReasonC;

	/** The firing reason D. */
	@Column(name = "FIRING_REASON_D")
	private String firingReasonD;

	/** The firing reason E. */
	@Column(name = "FIRING_REASON_E")
	private String firingReasonE;

	/** The firing reason F. */
	@Column(name = "FIRING_REASON_F")
	private String firingReasonF;

	/** The gp 01. */
	@Column(name = "GP01")
	private String gp01;

	/** The gp 02. */
	@Column(name = "GP02")
	private String gp02;

	/** The gp 03. */
	@Column(name = "GP03")
	private String gp03;

	/** The gp 04. */
	@Column(name = "GP04")
	private String gp04;

	/** The gp 05. */
	@Column(name = "GP05")
	private String gp05;

	/** The gp 06. */
	@Column(name = "GP06")
	private String gp06;

	/** The gp 07. */
	@Column(name = "GP07")
	private String gp07;

	/** The gp 08. */
	@Column(name = "GP08")
	private String gp08;

	/** The gp 09. */
	@Column(name = "GP09")
	private String gp09;

	/** The gp 10. */
	@Column(name = "GP10")
	private String gp10;

	/**
	 * Instantiates a new pcpmt person com.
	 */
	public PcpmtPersonCom() {
	}

	/**
	 * Instantiates a new pcpmt person com.
	 *
	 * @param pcpmtPersonComPK the pcpmt person com PK
	 */
	public PcpmtPersonCom(PcpmtPersonComPK pcpmtPersonComPK) {
		this.pcpmtPersonComPK = pcpmtPersonComPK;
	}

	/**
	 * Instantiates a new pcpmt person com.
	 *
	 * @param ccd the ccd
	 * @param pid the pid
	 * @param histId the hist id
	 */
	public PcpmtPersonCom(String ccd, String pid, String histId) {
		this.pcpmtPersonComPK = new PcpmtPersonComPK(ccd, pid, histId);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (pcpmtPersonComPK != null ? pcpmtPersonComPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof PcpmtPersonCom)) {
			return false;
		}
		PcpmtPersonCom other = (PcpmtPersonCom) object;
		if ((this.pcpmtPersonComPK == null && other.pcpmtPersonComPK != null)
				|| (this.pcpmtPersonComPK != null && !this.pcpmtPersonComPK.equals(other.pcpmtPersonComPK))) {
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
		return "entity.PcpmtPersonCom[ pcpmtPersonComPK=" + pcpmtPersonComPK + " ]";
	}

}
