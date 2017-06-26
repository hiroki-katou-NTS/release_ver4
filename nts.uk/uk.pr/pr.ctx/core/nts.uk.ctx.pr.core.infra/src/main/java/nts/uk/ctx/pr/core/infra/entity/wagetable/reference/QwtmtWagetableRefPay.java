/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.reference;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QwtmtWagetableRefPay.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_REF_PAY")
public class QwtmtWagetableRefPay extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable ref pay PK. */
	@EmbeddedId
	protected QwtmtWagetableRefPayPK qwtmtWagetableRefPayPK;

	/** The ref pay name. */
	@Basic(optional = false)
	@Column(name = "REF_PAY_NAME")
	private String refPayName;

	/** The wage ref ctg atr. */
	@Basic(optional = false)
	@Column(name = "WAGE_REF_CTG_ATR")
	private short wageRefCtgAtr;

	/** The wage ref item cd. */
	@Basic(optional = false)
	@Column(name = "WAGE_REF_ITEM_CD")
	private String wageRefItemCd;

	/**
	 * Instantiates a new qwtmt wagetable ref pay.
	 */
	public QwtmtWagetableRefPay() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable ref pay.
	 *
	 * @param qwtmtWagetableRefPayPK
	 *            the qwtmt wagetable ref pay PK
	 */
	public QwtmtWagetableRefPay(QwtmtWagetableRefPayPK qwtmtWagetableRefPayPK) {
		this.qwtmtWagetableRefPayPK = qwtmtWagetableRefPayPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable ref pay.
	 *
	 * @param ccd
	 *            the ccd
	 * @param refPayNo
	 *            the ref pay no
	 */
	public QwtmtWagetableRefPay(String ccd, String refPayNo) {
		this.qwtmtWagetableRefPayPK = new QwtmtWagetableRefPayPK(ccd, refPayNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableRefPayPK != null ? qwtmtWagetableRefPayPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableRefPay)) {
			return false;
		}
		QwtmtWagetableRefPay other = (QwtmtWagetableRefPay) object;
		if ((this.qwtmtWagetableRefPayPK == null && other.qwtmtWagetableRefPayPK != null)
				|| (this.qwtmtWagetableRefPayPK != null
						&& !this.qwtmtWagetableRefPayPK.equals(other.qwtmtWagetableRefPayPK))) {
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
		return this.qwtmtWagetableRefPayPK;
	}

}
