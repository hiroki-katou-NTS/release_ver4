/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.history;

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
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QwtmtWagetableHist.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_HIST")
public class QwtmtWagetableHist extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable hist PK. */
	@EmbeddedId
	protected QwtmtWagetableHistPK qwtmtWagetableHistPK;

	/** The str ym. */
	@Basic(optional = false)
	@Column(name = "STR_YM")
	private int strYm;

	/** The end ym. */
	@Basic(optional = false)
	@Column(name = "END_YM")
	private int endYm;

	/** The qwtmt wagetable head. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false) })
	@ManyToOne
	private QwtmtWagetableHead qwtmtWagetableHead;

	/** The qwtmt wagetable ele hist list. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QwtmtWagetableEleHist> qwtmtWagetableEleHistList;

	/** The qwtmt wagetable mny list. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "WAGE_TABLE_CD", referencedColumnName = "WAGE_TABLE_CD", insertable = false, updatable = false),
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QwtmtWagetableMny> qwtmtWagetableMnyList;

	/**
	 * Instantiates a new qwtmt wagetable hist.
	 */
	public QwtmtWagetableHist() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable hist.
	 *
	 * @param qwtmtWagetableHistPK
	 *            the qwtmt wagetable hist PK
	 */
	public QwtmtWagetableHist(QwtmtWagetableHistPK qwtmtWagetableHistPK) {
		this.qwtmtWagetableHistPK = qwtmtWagetableHistPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable hist.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param histId
	 *            the hist id
	 */
	public QwtmtWagetableHist(String ccd, String wageTableCd, String histId) {
		this.qwtmtWagetableHistPK = new QwtmtWagetableHistPK(ccd, wageTableCd, histId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableHistPK != null ? qwtmtWagetableHistPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableHist)) {
			return false;
		}
		QwtmtWagetableHist other = (QwtmtWagetableHist) object;
		if ((this.qwtmtWagetableHistPK == null && other.qwtmtWagetableHistPK != null)
				|| (this.qwtmtWagetableHistPK != null
						&& !this.qwtmtWagetableHistPK.equals(other.qwtmtWagetableHistPK))) {
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
		return this.qwtmtWagetableHistPK;
	}
}
