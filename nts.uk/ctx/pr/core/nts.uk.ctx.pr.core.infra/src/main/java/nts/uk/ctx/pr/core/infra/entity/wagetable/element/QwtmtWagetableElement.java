/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.element;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.infra.entity.wagetable.QwtmtWagetableHead;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHist;

/**
 * The Class QwtmtWagetableElement.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_ELEMENT")
public class QwtmtWagetableElement implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable element PK. */
	@EmbeddedId
	protected QwtmtWagetableElementPK qwtmtWagetableElementPK;

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
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The demension type. */
	@Basic(optional = false)
	@Column(name = "DEMENSION_TYPE")
	private Integer demensionType;

	/** The demension ref no. */
	@Basic(optional = false)
	@Column(name = "DEMENSION_REF_NO")
	private String demensionRefNo;

	/** The qwtmt wagetable head. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QwtmtWagetableHead qwtmtWagetableHead;

	/** The qwtmt wagetable ele hist. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "DEMENSION_NO", referencedColumnName = "DEMENSION_NO", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL)
	private List<QwtmtWagetableEleHist> qwtmtWagetableEleHistList;

	/**
	 * Instantiates a new qwtmt wagetable element.
	 */
	public QwtmtWagetableElement() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable element.
	 *
	 * @param qwtmtWagetableElementPK
	 *            the qwtmt wagetable element PK
	 */
	public QwtmtWagetableElement(QwtmtWagetableElementPK qwtmtWagetableElementPK) {
		this.qwtmtWagetableElementPK = qwtmtWagetableElementPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable element.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param demensionNo
	 *            the demension no
	 */
	public QwtmtWagetableElement(String ccd, String wageTableCd, Integer demensionNo) {
		this.qwtmtWagetableElementPK = new QwtmtWagetableElementPK(ccd, wageTableCd, demensionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableElementPK != null ? qwtmtWagetableElementPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableElement)) {
			return false;
		}
		QwtmtWagetableElement other = (QwtmtWagetableElement) object;
		if ((this.qwtmtWagetableElementPK == null && other.qwtmtWagetableElementPK != null)
				|| (this.qwtmtWagetableElementPK != null
						&& !this.qwtmtWagetableElementPK.equals(other.qwtmtWagetableElementPK))) {
			return false;
		}
		return true;
	}
}
