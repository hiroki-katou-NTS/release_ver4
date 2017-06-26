/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.element;

import java.io.Serializable;
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

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.infra.entity.wagetable.QwtmtWagetableHead;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHist;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QwtmtWagetableElement.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_ELEMENT")
public class QwtmtWagetableElement extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable element PK. */
	@EmbeddedId
	protected QwtmtWagetableElementPK qwtmtWagetableElementPK;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.qwtmtWagetableElementPK;
	}
}
