/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.history;

import java.io.Serializable;
import java.math.BigDecimal;

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
 * The Class QwtmtWagetableNum.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_NUM")
public class QwtmtWagetableNum extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable num PK. */
	@EmbeddedId
	protected QwtmtWagetableNumPK qwtmtWagetableNumPK;

	/** The element str. */
	@Basic(optional = false)
	@Column(name = "ELEMENT_STR")
	private BigDecimal elementStr;

	/** The element end. */
	@Basic(optional = false)
	@Column(name = "ELEMENT_END")
	private BigDecimal elementEnd;

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
	 * Instantiates a new qwtmt wagetable num.
	 */
	public QwtmtWagetableNum() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable num.
	 *
	 * @param qwtmtWagetableNumPK
	 *            the qwtmt wagetable num PK
	 */
	public QwtmtWagetableNum(QwtmtWagetableNumPK qwtmtWagetableNumPK) {
		this.qwtmtWagetableNumPK = qwtmtWagetableNumPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable num.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param histId
	 *            the hist id
	 * @param demensionNo
	 *            the demension no
	 * @param elementNumNo
	 *            the element num no
	 */
	public QwtmtWagetableNum(String ccd, String wageTableCd, String histId, Integer demensionNo,
			Integer elementNumNo) {
		this.qwtmtWagetableNumPK = new QwtmtWagetableNumPK(ccd, wageTableCd, histId, demensionNo,
				elementNumNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableNumPK != null ? qwtmtWagetableNumPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableNum)) {
			return false;
		}
		QwtmtWagetableNum other = (QwtmtWagetableNum) object;
		if ((this.qwtmtWagetableNumPK == null && other.qwtmtWagetableNumPK != null)
				|| (this.qwtmtWagetableNumPK != null
						&& !this.qwtmtWagetableNumPK.equals(other.qwtmtWagetableNumPK))) {
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
		return this.qwtmtWagetableNumPK;
	}
}
