/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.history;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElement;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QwtmtWagetableEleHist.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_ELE_HIST")
public class QwtmtWagetableEleHist extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable ele hist PK. */
	@EmbeddedId
	protected QwtmtWagetableEleHistPK qwtmtWagetableEleHistPK;

	/** The demension lower limit. */
	@Column(name = "DEMENSION_LOWER_LIMIT")
	private BigDecimal demensionLowerLimit;

	/** The demension upper limit. */
	@Column(name = "DEMENSION_UPPER_LIMIT")
	private BigDecimal demensionUpperLimit;

	/** The demension interval. */
	@Column(name = "DEMENSION_INTERVAL")
	private BigDecimal demensionInterval;

	/** The qwtmt wagetable element. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "DEMENSION_NO", referencedColumnName = "DEMENSION_NO", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QwtmtWagetableElement qwtmtWagetableElement;

	/** The qwtmt wagetable cd list. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false),
			@JoinColumn(name = "DEMENSION_NO", referencedColumnName = "DEMENSION_NO", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QwtmtWagetableCd> qwtmtWagetableCdList;

	/** The qwtmt wagetable num list. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false),
			@JoinColumn(name = "DEMENSION_NO", referencedColumnName = "DEMENSION_NO", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("qwtmtWagetableNumPK.elementNumNo asc")
	private List<QwtmtWagetableNum> qwtmtWagetableNumList;

	/**
	 * Instantiates a new qwtmt wagetable ele hist.
	 */
	public QwtmtWagetableEleHist() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable ele hist.
	 *
	 * @param qwtmtWagetableEleHistPK
	 *            the qwtmt wagetable ele hist PK
	 */
	public QwtmtWagetableEleHist(QwtmtWagetableEleHistPK qwtmtWagetableEleHistPK) {
		this.qwtmtWagetableEleHistPK = qwtmtWagetableEleHistPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable ele hist.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param histId
	 *            the hist id
	 * @param demensionNo
	 *            the demension no
	 */
	public QwtmtWagetableEleHist(String ccd, String wageTableCd, String histId,
			Integer demensionNo) {
		this.qwtmtWagetableEleHistPK = new QwtmtWagetableEleHistPK(ccd, wageTableCd, histId,
				demensionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableEleHistPK != null ? qwtmtWagetableEleHistPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableEleHist)) {
			return false;
		}
		QwtmtWagetableEleHist other = (QwtmtWagetableEleHist) object;
		if ((this.qwtmtWagetableEleHistPK == null && other.qwtmtWagetableEleHistPK != null)
				|| (this.qwtmtWagetableEleHistPK != null
						&& !this.qwtmtWagetableEleHistPK.equals(other.qwtmtWagetableEleHistPK))) {
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
		return this.qwtmtWagetableEleHistPK;
	}
}
