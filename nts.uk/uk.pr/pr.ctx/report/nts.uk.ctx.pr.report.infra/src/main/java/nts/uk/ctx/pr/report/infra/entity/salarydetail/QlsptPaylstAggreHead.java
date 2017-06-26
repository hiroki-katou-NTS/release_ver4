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
 * The Class QlsptPaylstAggreHead.
 */
@Getter
@Setter
@Entity
@Table(name = "QLSPT_PAYLST_AGGRE_HEAD")
public class QlsptPaylstAggreHead implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qlspt paylst aggre head PK. */
	@EmbeddedId
	protected QlsptPaylstAggreHeadPK qlsptPaylstAggreHeadPK;

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

	/** The aggregate name. */
	@Column(name = "AGGREGATE_NAME")
	private String aggregateName;

	/** The qlspt paylst aggre detail list. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qlsptPaylstAggreHead", orphanRemoval = true)
	private List<QlsptPaylstAggreDetail> qlsptPaylstAggreDetailList;

	/**
	 * Instantiates a new qlspt paylst aggre head.
	 */
	public QlsptPaylstAggreHead() {
		super();
	}

	/**
	 * Instantiates a new qlspt paylst aggre head.
	 *
	 * @param qlsptPaylstAggreHeadPK
	 *            the qlspt paylst aggre head PK
	 */
	public QlsptPaylstAggreHead(QlsptPaylstAggreHeadPK qlsptPaylstAggreHeadPK) {
		this.qlsptPaylstAggreHeadPK = qlsptPaylstAggreHeadPK;
	}

	/**
	 * Instantiates a new qlspt paylst aggre head.
	 *
	 * @param qlsptPaylstAggreHeadPK
	 *            the qlspt paylst aggre head PK
	 * @param exclusVer
	 *            the exclus ver
	 */
	public QlsptPaylstAggreHead(QlsptPaylstAggreHeadPK qlsptPaylstAggreHeadPK, int exclusVer) {
		this.qlsptPaylstAggreHeadPK = qlsptPaylstAggreHeadPK;
		this.exclusVer = exclusVer;
	}

	/**
	 * Instantiates a new qlspt paylst aggre head.
	 *
	 * @param ccd
	 *            the ccd
	 * @param aggregateCd
	 *            the aggregate cd
	 * @param ctgAtr
	 *            the ctg atr
	 */
	public QlsptPaylstAggreHead(String ccd, String aggregateCd, int ctgAtr) {
		this.qlsptPaylstAggreHeadPK = new QlsptPaylstAggreHeadPK(ccd, aggregateCd, ctgAtr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qlsptPaylstAggreHeadPK != null ? qlsptPaylstAggreHeadPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptPaylstAggreHead)) {
			return false;
		}
		QlsptPaylstAggreHead other = (QlsptPaylstAggreHead) object;
		if ((this.qlsptPaylstAggreHeadPK == null && other.qlsptPaylstAggreHeadPK != null)
			|| (this.qlsptPaylstAggreHeadPK != null
				&& !this.qlsptPaylstAggreHeadPK.equals(other.qlsptPaylstAggreHeadPK))) {
			return false;
		}
		return true;
	}
}
