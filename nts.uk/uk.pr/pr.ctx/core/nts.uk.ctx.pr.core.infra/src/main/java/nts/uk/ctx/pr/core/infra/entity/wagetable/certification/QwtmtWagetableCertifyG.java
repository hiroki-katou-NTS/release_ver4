/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.certification;

import java.io.Serializable;
import java.util.List;

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
 * The Class QwtmtWagetableCertifyG.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_CERTIFY_G")
public class QwtmtWagetableCertifyG extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable certify GPK. */
	@EmbeddedId
	protected QwtmtWagetableCertifyGPK qwtmtWagetableCertifyGPK;

	/** The certify group name. */
	@Column(name = "CERTIFY_GROUP_NAME")
	private String certifyGroupName;

	/** The multi apply set. */
	@Column(name = "MULTI_APPLY_SET")
	private Integer multiApplySet;

	/** The qwtmt wagetable certify list. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qwtmtWagetableCertifyG", orphanRemoval = true)
	private List<QwtmtWagetableCertify> qwtmtWagetableCertifyList;

	/**
	 * Instantiates a new qwtmt wagetable certify G.
	 */
	public QwtmtWagetableCertifyG() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable certify G.
	 *
	 * @param qwtmtWagetableCertifyGPK
	 *            the qwtmt wagetable certify GPK
	 */
	public QwtmtWagetableCertifyG(QwtmtWagetableCertifyGPK qwtmtWagetableCertifyGPK) {
		this.qwtmtWagetableCertifyGPK = qwtmtWagetableCertifyGPK;
	}

	/**
	 * Instantiates a new qwtmt wagetable certify G.
	 *
	 * @param ccd
	 *            the ccd
	 * @param certifyGroupCd
	 *            the certify group cd
	 */
	public QwtmtWagetableCertifyG(String ccd, String certifyGroupCd) {
		this.qwtmtWagetableCertifyGPK = new QwtmtWagetableCertifyGPK(ccd, certifyGroupCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableCertifyGPK != null ? qwtmtWagetableCertifyGPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableCertifyG)) {
			return false;
		}
		QwtmtWagetableCertifyG other = (QwtmtWagetableCertifyG) object;
		if ((this.qwtmtWagetableCertifyGPK == null && other.qwtmtWagetableCertifyGPK != null)
				|| (this.qwtmtWagetableCertifyGPK != null
						&& !this.qwtmtWagetableCertifyGPK.equals(other.qwtmtWagetableCertifyGPK))) {
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
		return this.qwtmtWagetableCertifyGPK;
	}

}
