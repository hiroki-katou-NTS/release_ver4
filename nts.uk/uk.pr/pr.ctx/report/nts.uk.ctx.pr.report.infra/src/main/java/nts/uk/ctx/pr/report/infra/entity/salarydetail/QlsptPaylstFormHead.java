/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.salarydetail;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QlsptPaylstFormHead.
 */
@Setter
@Getter
@Entity
@Table(name = "QLSPT_PAYLST_FORM_HEAD")
public class QlsptPaylstFormHead implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qlspt paylst form head PK. */
	@EmbeddedId
	protected QlsptPaylstFormHeadPK qlsptPaylstFormHeadPK;

	/** The ins date. */
	@Column(name = "INS_DATE")
	@Temporal(TemporalType.TIME)
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
	@Temporal(TemporalType.TIME)
	private Date updDate;

	/** The upd ccd. */
	@Column(name = "UPD_CCD")
	private String updCcd;

	/** The upd scd. */
	@Column(name = "UPD_SCD")
	private String updScd;

	/** The exclus ver. */
	@Basic(optional = false)
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The form name. */
	@Column(name = "FORM_NAME")
	private String formName;

	/** The qlspt paylst form detail list. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qlsptPaylstFormHead", orphanRemoval = true)
	private List<QlsptPaylstFormDetail> qlsptPaylstFormDetailList;

	/**
	 * Instantiates a new qlspt paylst form head.
	 */
	public QlsptPaylstFormHead() {
		super();
	}

	/**
	 * Instantiates a new qlspt paylst form head.
	 *
	 * @param qlsptPaylstFormHeadPK
	 *            the qlspt paylst form head PK
	 */
	public QlsptPaylstFormHead(QlsptPaylstFormHeadPK qlsptPaylstFormHeadPK) {
		this.qlsptPaylstFormHeadPK = qlsptPaylstFormHeadPK;
	}

	/**
	 * Instantiates a new qlspt paylst form head.
	 *
	 * @param qlsptPaylstFormHeadPK
	 *            the qlspt paylst form head PK
	 * @param exclusVer
	 *            the exclus ver
	 */
	public QlsptPaylstFormHead(QlsptPaylstFormHeadPK qlsptPaylstFormHeadPK, int exclusVer) {
		this.qlsptPaylstFormHeadPK = qlsptPaylstFormHeadPK;
		this.exclusVer = exclusVer;
	}

	/**
	 * Instantiates a new qlspt paylst form head.
	 *
	 * @param ccd
	 *            the ccd
	 * @param formCd
	 *            the form cd
	 */
	public QlsptPaylstFormHead(String ccd, String formCd) {
		this.qlsptPaylstFormHeadPK = new QlsptPaylstFormHeadPK(ccd, formCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qlsptPaylstFormHeadPK != null ? qlsptPaylstFormHeadPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptPaylstFormHead)) {
			return false;
		}
		QlsptPaylstFormHead other = (QlsptPaylstFormHead) object;
		if ((this.qlsptPaylstFormHeadPK == null && other.qlsptPaylstFormHeadPK != null)
				|| (this.qlsptPaylstFormHeadPK != null
						&& !this.qlsptPaylstFormHeadPK.equals(other.qlsptPaylstFormHeadPK))) {
			return false;
		}
		return true;
	}
}
