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
 * The Class QwtmtWagetableCd.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_CD")
public class QwtmtWagetableCd extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable cd PK. */
	@EmbeddedId
	protected QwtmtWagetableCdPK qwtmtWagetableCdPK;

	/** The element id. */
	@Basic(optional = false)
	@Column(name = "ELEMENT_ID")
	private String elementId;

	/** The qwtmt wagetable element. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false),
			@JoinColumn(name = "DEMENSION_NO", referencedColumnName = "DEMENSION_NO", insertable = false, updatable = false) })
	@ManyToOne(cascade = CascadeType.ALL)
	private QwtmtWagetableEleHist qwtmtWagetableEleHist;

	/**
	 * Instantiates a new qwtmt wagetable cd.
	 */
	public QwtmtWagetableCd() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable cd.
	 *
	 * @param qwtmtWagetableCdPK
	 *            the qwtmt wagetable cd PK
	 */
	public QwtmtWagetableCd(QwtmtWagetableCdPK qwtmtWagetableCdPK) {
		this.qwtmtWagetableCdPK = qwtmtWagetableCdPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable cd.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param histId
	 *            the hist id
	 * @param demensionNo
	 *            the demension no
	 * @param elementCd
	 *            the element cd
	 */
	public QwtmtWagetableCd(String ccd, String wageTableCd, String histId, Integer demensionNo,
			String elementCd) {
		this.qwtmtWagetableCdPK = new QwtmtWagetableCdPK(ccd, wageTableCd, histId, demensionNo,
				elementCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableCdPK != null ? qwtmtWagetableCdPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableCd)) {
			return false;
		}
		QwtmtWagetableCd other = (QwtmtWagetableCd) object;
		if ((this.qwtmtWagetableCdPK == null && other.qwtmtWagetableCdPK != null)
				|| (this.qwtmtWagetableCdPK != null
						&& !this.qwtmtWagetableCdPK.equals(other.qwtmtWagetableCdPK))) {
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
		return this.qwtmtWagetableCdPK;
	}
}
