/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElement;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHist;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QwtmtWagetableHead.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_HEAD")
public class QwtmtWagetableHead extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable head PK. */
	@EmbeddedId
	protected QwtmtWagetableHeadPK qwtmtWagetableHeadPK;

	/** The wage table name. */
	@Basic(optional = false)
	@Column(name = "WAGE_TABLE_NAME")
	private String wageTableName;

	/** The demension set. */
	@Basic(optional = false)
	@Column(name = "DEMENSION_SET")
	private int demensionSet;

	/** The memo. */
	@Column(name = "MEMO")
	private String memo;

	/** The wagetable element list. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("qwtmtWagetableElementPK.demensionNo ASC")
	private List<QwtmtWagetableElement> wagetableElementList;

	/** The wagetable hist list. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("strYm DESC")
	private List<QwtmtWagetableHist> wagetableHistList;

	/**
	 * Instantiates a new qwtmt wagetable head.
	 */
	public QwtmtWagetableHead() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable head.
	 *
	 * @param qwtmtWagetableHeadPK
	 *            the qwtmt wagetable head PK
	 */
	public QwtmtWagetableHead(QwtmtWagetableHeadPK qwtmtWagetableHeadPK) {
		this.qwtmtWagetableHeadPK = qwtmtWagetableHeadPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable head.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 */
	public QwtmtWagetableHead(String ccd, String wageTableCd) {
		this.qwtmtWagetableHeadPK = new QwtmtWagetableHeadPK(ccd, wageTableCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableHeadPK != null ? qwtmtWagetableHeadPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableHead)) {
			return false;
		}
		QwtmtWagetableHead other = (QwtmtWagetableHead) object;
		if ((this.qwtmtWagetableHeadPK == null && other.qwtmtWagetableHeadPK != null)
				|| (this.qwtmtWagetableHeadPK != null
						&& !this.qwtmtWagetableHeadPK.equals(other.qwtmtWagetableHeadPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.qwtmtWagetableHeadPK;
	}
}
