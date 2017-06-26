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
 * The Class QlsptPaylstAggreDetail.
 */
@Getter
@Setter
@Entity
@Table(name = "QLSPT_PAYLST_AGGRE_DETAIL")
public class QlsptPaylstAggreDetail implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qlspt paylst aggre detail PK. */
	@EmbeddedId
	protected QlsptPaylstAggreDetailPK qlsptPaylstAggreDetailPK;

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

	/** The qlspt paylst aggre head. */
	@JoinColumns({ @JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "AGGREGATE_CD", referencedColumnName = "AGGREGATE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "CTG_ATR", referencedColumnName = "CTG_ATR", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QlsptPaylstAggreHead qlsptPaylstAggreHead;

	/**
	 * Instantiates a new qlspt paylst aggre detail.
	 */
	public QlsptPaylstAggreDetail() {
		super();
	}

	/**
	 * Instantiates a new qlspt paylst aggre detail.
	 *
	 * @param qlsptPaylstAggreDetailPK
	 *            the qlspt paylst aggre detail PK
	 */
	public QlsptPaylstAggreDetail(QlsptPaylstAggreDetailPK qlsptPaylstAggreDetailPK) {
		this.qlsptPaylstAggreDetailPK = qlsptPaylstAggreDetailPK;
	}

	/**
	 * Instantiates a new qlspt paylst aggre detail.
	 *
	 * @param qlsptPaylstAggreDetailPK
	 *            the qlspt paylst aggre detail PK
	 * @param exclusVer
	 *            the exclus ver
	 */
	public QlsptPaylstAggreDetail(QlsptPaylstAggreDetailPK qlsptPaylstAggreDetailPK, int exclusVer) {
		this.qlsptPaylstAggreDetailPK = qlsptPaylstAggreDetailPK;
		this.exclusVer = exclusVer;
	}

	/**
	 * Instantiates a new qlspt paylst aggre detail.
	 *
	 * @param ccd
	 *            the ccd
	 * @param aggregateCd
	 *            the aggregate cd
	 * @param ctgAtr
	 *            the ctg atr
	 * @param itemCd
	 *            the item cd
	 */
	public QlsptPaylstAggreDetail(String ccd, String aggregateCd, int ctgAtr, String itemCd) {
		this.qlsptPaylstAggreDetailPK = new QlsptPaylstAggreDetailPK(ccd, aggregateCd, ctgAtr, itemCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qlsptPaylstAggreDetailPK != null ? qlsptPaylstAggreDetailPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptPaylstAggreDetail)) {
			return false;
		}
		QlsptPaylstAggreDetail other = (QlsptPaylstAggreDetail) object;
		if ((this.qlsptPaylstAggreDetailPK == null && other.qlsptPaylstAggreDetailPK != null)
				|| (this.qlsptPaylstAggreDetailPK != null
						&& !this.qlsptPaylstAggreDetailPK.equals(other.qlsptPaylstAggreDetailPK))) {
			return false;
		}
		return true;
	}

}
