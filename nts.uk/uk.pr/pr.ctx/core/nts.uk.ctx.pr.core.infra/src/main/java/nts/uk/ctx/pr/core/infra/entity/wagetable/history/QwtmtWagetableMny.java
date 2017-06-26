/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.history;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QwtmtWagetableMny.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_MNY")
public class QwtmtWagetableMny extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable mny PK. */
	@EmbeddedId
	protected QwtmtWagetableMnyPK qwtmtWagetableMnyPK;

	/** The value mny. */
	@Basic(optional = false)
	@Column(name = "VALUE_MNY")
	private long valueMny;

	/** The qwtmt wagetable hist. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false) })
	@ManyToOne(cascade = CascadeType.ALL)
	private QwtmtWagetableHist qwtmtWagetableHist;

	/**
	 * Instantiates a new qwtmt wagetable mny.
	 */
	public QwtmtWagetableMny() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable mny.
	 *
	 * @param qwtmtWagetableMnyPK
	 *            the qwtmt wagetable mny PK
	 */
	public QwtmtWagetableMny(QwtmtWagetableMnyPK qwtmtWagetableMnyPK) {
		this.qwtmtWagetableMnyPK = qwtmtWagetableMnyPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable mny.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param histId
	 *            the hist id
	 * @param element1Id
	 *            the element 1 id
	 * @param element2Id
	 *            the element 2 id
	 * @param element3Id
	 *            the element 3 id
	 */
	public QwtmtWagetableMny(String ccd, String wageTableCd, String histId, String element1Id,
			String element2Id, String element3Id) {
		this.qwtmtWagetableMnyPK = new QwtmtWagetableMnyPK(ccd, wageTableCd, histId, element1Id,
				element2Id, element3Id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableMnyPK != null ? qwtmtWagetableMnyPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableMny)) {
			return false;
		}
		QwtmtWagetableMny other = (QwtmtWagetableMny) object;
		if ((this.qwtmtWagetableMnyPK == null && other.qwtmtWagetableMnyPK != null)
				|| (this.qwtmtWagetableMnyPK != null
						&& !this.qwtmtWagetableMnyPK.equals(other.qwtmtWagetableMnyPK))) {
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
		return this.qwtmtWagetableMnyPK;
	}
}
