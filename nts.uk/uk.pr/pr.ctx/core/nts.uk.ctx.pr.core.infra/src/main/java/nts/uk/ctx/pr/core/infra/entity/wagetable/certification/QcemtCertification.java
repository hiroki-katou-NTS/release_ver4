/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.certification;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QcemtCertification.
 */
@Getter
@Setter
@Entity
@Table(name = "QCEMT_CERTIFICATION")
public class QcemtCertification extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qcemt certification PK. */
	@EmbeddedId
	protected QcemtCertificationPK qcemtCertificationPK;

	/** The name. */
	@Basic(optional = false)
	@Column(name = "NAME")
	private String name;

	/** The qwtmt wagetable certify list. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qcemtCertification")
	private List<QwtmtWagetableCertify> qwtmtWagetableCertifyList;

	/**
	 * Instantiates a new qcemt certification.
	 */
	public QcemtCertification() {
		super();
	}

	/**
	 * Instantiates a new qcemt certification.
	 *
	 * @param qcemtCertificationPK
	 *            the qcemt certification PK
	 */
	public QcemtCertification(QcemtCertificationPK qcemtCertificationPK) {
		this.qcemtCertificationPK = qcemtCertificationPK;
	}

	/**
	 * Instantiates a new qcemt certification.
	 *
	 * @param ccd
	 *            the ccd
	 * @param certCd
	 *            the cert cd
	 */
	public QcemtCertification(String ccd, String certCd) {
		this.qcemtCertificationPK = new QcemtCertificationPK(ccd, certCd);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.qcemtCertificationPK == null ? 0 : this.qcemtCertificationPK.hashCode());
		return hash;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QcemtCertification)) {
			return false;
		}
		QcemtCertification other = (QcemtCertification) object;
		if ((this.qcemtCertificationPK == null && other.qcemtCertificationPK != null)
				|| (this.qcemtCertificationPK != null
						&& !this.qcemtCertificationPK.equals(other.qcemtCertificationPK))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.qcemtCertificationPK;
	}

}
