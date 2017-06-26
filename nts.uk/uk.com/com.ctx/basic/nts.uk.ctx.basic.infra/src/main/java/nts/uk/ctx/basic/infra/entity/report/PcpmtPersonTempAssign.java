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
 * The Class PcpmtPersonTempAssign.
 */
@Setter
@Getter
@Entity
@Table(name = "PCPMT_PERSON_TEMP_ASSIGN")
public class PcpmtPersonTempAssign implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The pcpmt person temp assign PK. */
	@EmbeddedId
	protected PcpmtPersonTempAssignPK pcpmtPersonTempAssignPK;
	
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
	
	/** The str D. */
	@Basic(optional = false)
	@Column(name = "STR_D")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate strD;
	
	/** The end D. */
	@Basic(optional = false)
	@Column(name = "END_D")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate endD;
	
	/** The issue date. */
	@Column(name = "ISSUE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate issueDate;
	
	/** The issue sts. */
	@Basic(optional = false)
	@Column(name = "ISSUE_STS")
	private short issueSts;
	
	/** The temp assign atr. */
	@Basic(optional = false)
	@Column(name = "TEMP_ASSIGN_ATR")
	private short tempAssignAtr;
	
	/** The additional atr. */
	@Basic(optional = false)
	@Column(name = "ADDITIONAL_ATR")
	private short additionalAtr;
	
	/** The temp assign ccd. */
	@Basic(optional = false)
	@Column(name = "TEMP_ASSIGN_CCD")
	private String tempAssignCcd;
	
	/** The assign company name. */
	@Column(name = "ASSIGN_COMPANY_NAME")
	private String assignCompanyName;
	
	/** The assign remark. */
	@Column(name = "ASSIGN_REMARK")
	private String assignRemark;
	
	/** The assign country. */
	@Column(name = "ASSIGN_COUNTRY")
	private String assignCountry;
	
	/** The assign postal. */
	@Column(name = "ASSIGN_POSTAL")
	private String assignPostal;
	
	/** The assign address 1. */
	@Column(name = "ASSIGN_ADDRESS1")
	private String assignAddress1;
	
	/** The assign address 2. */
	@Column(name = "ASSIGN_ADDRESS2")
	private String assignAddress2;
	
	/** The kn assign address 1. */
	@Column(name = "KN_ASSIGN_ADDRESS1")
	private String knAssignAddress1;
	
	/** The kn assign address 2. */
	@Column(name = "KN_ASSIGN_ADDRESS2")
	private String knAssignAddress2;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (pcpmtPersonTempAssignPK != null ? pcpmtPersonTempAssignPK.hashCode() : 0);
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof PcpmtPersonTempAssign)) {
			return false;
		}
		PcpmtPersonTempAssign other = (PcpmtPersonTempAssign) object;
		if ((this.pcpmtPersonTempAssignPK == null && other.pcpmtPersonTempAssignPK != null)
				|| (this.pcpmtPersonTempAssignPK != null
						&& !this.pcpmtPersonTempAssignPK.equals(other.pcpmtPersonTempAssignPK))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.PcpmtPersonTempAssign[ pcpmtPersonTempAssignPK=" + pcpmtPersonTempAssignPK + " ]";
	}

}
