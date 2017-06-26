/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.salarydetail;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class QlsptPaylstFormDetail.
 */
@Getter
@Setter
@Entity
@Table(name = "QLSPT_PAYLST_FORM_DETAIL")
public class QlsptPaylstFormDetail implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qlspt paylst form detail PK. */
	@EmbeddedId
	protected QlsptPaylstFormDetailPK qlsptPaylstFormDetailPK;

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

	/** The disp order. */
	@Basic(optional = false)
	@Column(name = "DISP_ORDER")
	private int dispOrder;

	/** The qlspt paylst form head. */
	@JoinColumns({ @JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "FORM_CD", referencedColumnName = "FORM_CD", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QlsptPaylstFormHead qlsptPaylstFormHead;

	/**
	 * Instantiates a new qlspt paylst form detail.
	 */
	public QlsptPaylstFormDetail() {
		super();
	}

	/**
	 * Instantiates a new qlspt paylst form detail.
	 *
	 * @param qlsptPaylstFormDetailPK
	 *            the qlspt paylst form detail PK
	 */
	public QlsptPaylstFormDetail(QlsptPaylstFormDetailPK qlsptPaylstFormDetailPK) {
		this.qlsptPaylstFormDetailPK = qlsptPaylstFormDetailPK;
	}

	/**
	 * Instantiates a new qlspt paylst form detail.
	 *
	 * @param qlsptPaylstFormDetailPK
	 *            the qlspt paylst form detail PK
	 * @param exclusVer
	 *            the exclus ver
	 * @param dispOrder
	 *            the disp order
	 */
	public QlsptPaylstFormDetail(QlsptPaylstFormDetailPK qlsptPaylstFormDetailPK, int exclusVer, int dispOrder) {
		this.qlsptPaylstFormDetailPK = qlsptPaylstFormDetailPK;
		this.exclusVer = exclusVer;
		this.dispOrder = dispOrder;
	}

	/**
	 * Instantiates a new qlspt paylst form detail.
	 *
	 * @param ccd
	 *            the ccd
	 * @param formCd
	 *            the form cd
	 * @param ctgAtr
	 *            the ctg atr
	 * @param aggregateAtr
	 *            the aggregate atr
	 * @param itemAgreCd
	 *            the item agre cd
	 */
	public QlsptPaylstFormDetail(String ccd, String formCd, int ctgAtr, int aggregateAtr, String itemAgreCd) {
		this.qlsptPaylstFormDetailPK = new QlsptPaylstFormDetailPK(ccd, formCd, ctgAtr, aggregateAtr, itemAgreCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qlsptPaylstFormDetailPK != null ? qlsptPaylstFormDetailPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptPaylstFormDetail)) {
			return false;
		}
		QlsptPaylstFormDetail other = (QlsptPaylstFormDetail) object;
		if ((this.qlsptPaylstFormDetailPK == null && other.qlsptPaylstFormDetailPK != null)
				|| (this.qlsptPaylstFormDetailPK != null
						&& !this.qlsptPaylstFormDetailPK.equals(other.qlsptPaylstFormDetailPK))) {
			return false;
		}
		return true;
	}
}
