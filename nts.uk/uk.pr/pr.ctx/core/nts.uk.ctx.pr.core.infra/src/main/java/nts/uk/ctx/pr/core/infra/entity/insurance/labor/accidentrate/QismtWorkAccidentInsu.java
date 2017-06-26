/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.businesstype.QismtBusinessType;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QismtWorkAccidentInsu.
 */
@Getter
@Setter
@Entity
@Table(name = "QISMT_WORK_ACCIDENT_INSU")
public class QismtWorkAccidentInsu extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qismt work accident insu PK. */
	@EmbeddedId
	protected QismtWorkAccidentInsuPK qismtWorkAccidentInsuPK;

	/** The str ym. */
	@Column(name = "STR_YM")
	private int strYm;

	/** The end ym. */
	@Column(name = "END_YM")
	private int endYm;

	/** The wa insu rate. */
	@Column(name = "WA_INSU_RATE")
	private BigDecimal waInsuRate;

	/** The wa insu round. */
	@Column(name = "WA_INSU_ROUND")
	private int waInsuRound;

	/** The qismt business type. */
	@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private QismtBusinessType qismtBusinessType;

	/**
	 * Instantiates a new qismt work accident insu.
	 */
	public QismtWorkAccidentInsu() {
		super();
	}

	/**
	 * Instantiates a new qismt work accident insu.
	 *
	 * @param qismtWorkAccidentInsuPK
	 *            the qismt work accident insu PK
	 */
	public QismtWorkAccidentInsu(QismtWorkAccidentInsuPK qismtWorkAccidentInsuPK) {
		this.qismtWorkAccidentInsuPK = qismtWorkAccidentInsuPK;
	}

	/**
	 * Instantiates a new qismt work accident insu.
	 *
	 * @param ccd
	 *            the ccd
	 * @param histId
	 *            the hist id
	 * @param waInsuCd
	 *            the wa insu cd
	 */
	public QismtWorkAccidentInsu(String ccd, String histId, int waInsuCd) {
		this.qismtWorkAccidentInsuPK = new QismtWorkAccidentInsuPK(ccd, histId, waInsuCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qismtWorkAccidentInsuPK != null ? qismtWorkAccidentInsuPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtWorkAccidentInsu)) {
			return false;
		}
		QismtWorkAccidentInsu other = (QismtWorkAccidentInsu) object;
		if ((this.qismtWorkAccidentInsuPK == null && other.qismtWorkAccidentInsuPK != null)
				|| (this.qismtWorkAccidentInsuPK != null
						&& !this.qismtWorkAccidentInsuPK.equals(other.qismtWorkAccidentInsuPK))) {
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
		return this.qismtWorkAccidentInsuPK;
	}

}
