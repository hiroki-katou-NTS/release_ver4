/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.certification;

import java.io.Serializable;

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
 * The Class QwtmtWagetableCertify.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_CERTIFY")
public class QwtmtWagetableCertify extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable certify PK. */
	@EmbeddedId
	protected QwtmtWagetableCertifyPK qwtmtWagetableCertifyPK;

	/** The qcemt certification. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "CERTIFY_CD", referencedColumnName = "CERT_CD", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QcemtCertification qcemtCertification;

	/** The qwtmt wagetable certify G. */
	@JoinColumns({
			@JoinColumn(name = "CCD", referencedColumnName = "CCD", insertable = false, updatable = false),
			@JoinColumn(name = "CERTIFY_GROUP_CD", referencedColumnName = "CERTIFY_GROUP_CD", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private QwtmtWagetableCertifyG qwtmtWagetableCertifyG;

	/**
	 * Instantiates a new qwtmt wagetable certify.
	 */
	public QwtmtWagetableCertify() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable certify.
	 *
	 * @param qwtmtWagetableCertifyPK
	 *            the qwtmt wagetable certify PK
	 */
	public QwtmtWagetableCertify(QwtmtWagetableCertifyPK qwtmtWagetableCertifyPK) {
		this.qwtmtWagetableCertifyPK = qwtmtWagetableCertifyPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable certify.
	 *
	 * @param ccd
	 *            the ccd
	 * @param certifyGroupCd
	 *            the certify group cd
	 * @param certifyCd
	 *            the certify cd
	 */
	public QwtmtWagetableCertify(String ccd, String certifyGroupCd, String certifyCd) {
		this.qwtmtWagetableCertifyPK = new QwtmtWagetableCertifyPK(ccd, certifyGroupCd, certifyCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableCertifyPK != null ? qwtmtWagetableCertifyPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableCertify)) {
			return false;
		}
		QwtmtWagetableCertify other = (QwtmtWagetableCertify) object;
		if ((this.qwtmtWagetableCertifyPK == null && other.qwtmtWagetableCertifyPK != null)
				|| (this.qwtmtWagetableCertifyPK != null
						&& !this.qwtmtWagetableCertifyPK.equals(other.qwtmtWagetableCertifyPK))) {
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
		return this.qwtmtWagetableCertifyPK;
	}

}
