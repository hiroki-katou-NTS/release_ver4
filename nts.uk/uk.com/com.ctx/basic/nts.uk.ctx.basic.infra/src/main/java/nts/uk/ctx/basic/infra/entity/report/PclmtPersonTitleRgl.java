/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.report;

import java.io.Serializable;
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
 * The Class PclmtPersonTitleRgl.
 */
@Setter
@Getter
@Entity
@Table(name = "PCLMT_PERSON_TITLE_RGL")
public class PclmtPersonTitleRgl implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The pclmt person title rgl PK. */
	@EmbeddedId
	protected PclmtPersonTitleRglPK pclmtPersonTitleRglPK;

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
	@Temporal(TemporalType.TIMESTAMP)
	private Date strD;

	/** The end D. */
	@Basic(optional = false)
	@Column(name = "END_D")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endD;

	/** The exp D. */
	@Basic(optional = false)
	@Column(name = "EXP_D")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expD;

	/** The jobcd. */
	@Basic(optional = false)
	@Column(name = "JOBCD")
	private String jobcd;

	/** The issue date. */
	@Column(name = "ISSUE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date issueDate;

	/** The issue sts. */
	@Basic(optional = false)
	@Column(name = "ISSUE_STS")
	private short issueSts;

	/** The remark. */
	@Column(name = "REMARK")
	private String remark;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (pclmtPersonTitleRglPK != null ? pclmtPersonTitleRglPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof PclmtPersonTitleRgl)) {
			return false;
		}
		PclmtPersonTitleRgl other = (PclmtPersonTitleRgl) object;
		if ((this.pclmtPersonTitleRglPK == null && other.pclmtPersonTitleRglPK != null)
				|| (this.pclmtPersonTitleRglPK != null
						&& !this.pclmtPersonTitleRglPK.equals(other.pclmtPersonTitleRglPK))) {
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
		return "entity.PclmtPersonTitleRgl[ pclmtPersonTitleRglPK=" + pclmtPersonTitleRglPK + " ]";
	}

}
