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
 * The Class QwtmtWagetableRefCd.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_REF_CD")
public class QwtmtWagetableRefCd extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable ref cd PK. */
	@EmbeddedId
	protected QwtmtWagetableRefCdPK qwtmtWagetableRefCdPK;

	/** The ref cd name. */
	@Basic(optional = false)
	@Column(name = "REF_CD_NAME")
	private String refCdName;

	/** The wage ref value. */
	@Basic(optional = false)
	@Column(name = "WAGE_REF_VALUE")
	private String wageRefValue;

	/** The wage person table. */
	@Basic(optional = false)
	@Column(name = "WAGE_PERSON_TABLE")
	private String wagePersonTable;

	/** The wage person field. */
	@Basic(optional = false)
	@Column(name = "WAGE_PERSON_FIELD")
	private String wagePersonField;

	/** The wage person query. */
	@Column(name = "WAGE_PERSON_QUERY")
	private String wagePersonQuery;

	/**
	 * Instantiates a new qwtmt wagetable ref cd.
	 */
	public QwtmtWagetableRefCd() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable ref cd.
	 *
	 * @param qwtmtWagetableRefCdPK
	 *            the qwtmt wagetable ref cd PK
	 */
	public QwtmtWagetableRefCd(QwtmtWagetableRefCdPK qwtmtWagetableRefCdPK) {
		this.qwtmtWagetableRefCdPK = qwtmtWagetableRefCdPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable ref cd.
	 *
	 * @param ccd
	 *            the ccd
	 * @param refCdNo
	 *            the ref cd no
	 */
	public QwtmtWagetableRefCd(String ccd, String refCdNo) {
		this.qwtmtWagetableRefCdPK = new QwtmtWagetableRefCdPK(ccd, refCdNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableRefCdPK != null ? qwtmtWagetableRefCdPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableRefCd)) {
			return false;
		}
		QwtmtWagetableRefCd other = (QwtmtWagetableRefCd) object;
		if ((this.qwtmtWagetableRefCdPK == null && other.qwtmtWagetableRefCdPK != null)
				|| (this.qwtmtWagetableRefCdPK != null
						&& !this.qwtmtWagetableRefCdPK.equals(other.qwtmtWagetableRefCdPK))) {
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
		return this.qwtmtWagetableRefCdPK;
	}
}
