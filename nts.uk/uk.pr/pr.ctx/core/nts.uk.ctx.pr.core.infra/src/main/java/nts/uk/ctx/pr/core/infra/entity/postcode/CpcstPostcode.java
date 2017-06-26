/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.postcode;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * The Class CpcstPostcode.
 */
@Data
@Entity
@Table(name = "CPCST_POSTCODE")
public class CpcstPostcode implements Serializable {

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

	/** The id. */
	@Id
	@Basic(optional = false)
	@Column(name = "ID")
	private String id;

	/** The local gov code. */
	@Basic(optional = false)
	@Column(name = "LOCAL_GOV_CODE")
	private String localGovCode;

	/** The postcode. */
	@Basic(optional = false)
	@Column(name = "POSTCODE")
	private String postcode;

	/** The prefecture name kn. */
	@Column(name = "PREFECTURE_NAME_KN")
	private String prefectureNameKn;

	/** The municipality name kn. */
	@Column(name = "MUNICIPALITY_NAME_KN")
	private String municipalityNameKn;

	/** The town name kn. */
	@Column(name = "TOWN_NAME_KN")
	private String townNameKn;

	/** The prefecture name. */
	@Column(name = "PREFECTURE_NAME")
	private String prefectureName;

	/** The municipality name. */
	@Column(name = "MUNICIPALITY_NAME")
	private String municipalityName;

	/** The town name. */
	@Column(name = "TOWN_NAME")
	private String townName;

	/**
	 * Instantiates a new cpcst postcode.
	 */
	public CpcstPostcode() {
		super();
	}

	/**
	 * Instantiates a new cpcst postcode.
	 *
	 * @param id
	 *            the id
	 */
	public CpcstPostcode(String id) {
		this.id = id;
	}

	/**
	 * Instantiates a new cpcst postcode.
	 *
	 * @param id
	 *            the id
	 * @param exclusVer
	 *            the exclus ver
	 * @param localGovCode
	 *            the local gov code
	 * @param postcode
	 *            the postcode
	 */
	public CpcstPostcode(String id, int exclusVer, String localGovCode, String postcode) {
		this.id = id;
		this.exclusVer = exclusVer;
		this.localGovCode = localGovCode;
		this.postcode = postcode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof CpcstPostcode)) {
			return false;
		}
		CpcstPostcode other = (CpcstPostcode) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

}
