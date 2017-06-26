/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.report;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class ReportPbsmtPersonBase.
 */
@Getter
@Setter
@Entity
@Table(name = "PBSMT_PERSON_BASE")
public class PbsmtPersonBase implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
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
    
    /** The pid. */
    @Id
    @Basic(optional = false)
    @Column(name = "PID")
    private String pid;
    
    /** The name official. */
    @Basic(optional = false)
    @Column(name = "NAME_OFFICIAL")
    private String nameOfficial;
    
    /** The kn name official. */
    @Column(name = "KN_NAME_OFFICIAL")
    private String knNameOfficial;
    
    /** The name E. */
    @Column(name = "NAME_E")
    private String nameE;
    
    /** The kn name E. */
    @Column(name = "KN_NAME_E")
    private String knNameE;
    
    /** The name O. */
    @Column(name = "NAME_O")
    private String nameO;
    
    /** The kn name O. */
    @Column(name = "KN_NAME_O")
    private String knNameO;
    
    /** The name B. */
    @Basic(optional = false)
    @Column(name = "NAME_B")
    private String nameB;
    
    /** The kn name B. */
    @Column(name = "KN_NAME_B")
    private String knNameB;
    
    /** The name maiden. */
    @Column(name = "NAME_MAIDEN")
    private String nameMaiden;
    
    /** The kn name maiden. */
    @Column(name = "KN_NAME_MAIDEN")
    private String knNameMaiden;
    
    /** The gov app name. */
    @Basic(optional = false)
    @Column(name = "GOV_APP_NAME")
    private String govAppName;
    
    /** The birth date. */
    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;
    
    /** The gendar. */
    @Basic(optional = false)
    @Column(name = "GENDAR")
    private short gendar;
    
    /** The private cellphone. */
    @Column(name = "PRIVATE_CELLPHONE")
    private String privateCellphone;
    
    /** The private mail. */
    @Column(name = "PRIVATE_MAIL")
    private String privateMail;
    
    /** The private mobile mail. */
    @Column(name = "PRIVATE_MOBILE_MAIL")
    private String privateMobileMail;
    
    /** The blood type. */
    @Column(name = "BLOOD_TYPE")
    private Short bloodType;
    
    /** The interim flg. */
    @Basic(optional = false)
    @Column(name = "INTERIM_FLG")
    private short interimFlg;
    
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PbsmtPersonBase other = (PbsmtPersonBase) obj;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		return true;
	}
}
