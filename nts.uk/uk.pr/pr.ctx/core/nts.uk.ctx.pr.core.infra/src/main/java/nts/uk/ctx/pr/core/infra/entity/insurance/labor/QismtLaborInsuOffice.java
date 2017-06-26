/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor;

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
 * The Class QismtLaborInsuOffice.
 */
@Getter
@Setter
@Entity
@Table(name = "QISMT_LABOR_INSU_OFFICE")
public class QismtLaborInsuOffice extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qismt labor insu office PK. */
	@EmbeddedId
	protected QismtLaborInsuOfficePK qismtLaborInsuOfficePK;

	/** The li office name. */
	@Basic(optional = false)
	@Column(name = "LI_OFFICE_NAME")
	private String liOfficeName;

	/** The li office ab name. */
	@Column(name = "LI_OFFICE_AB_NAME")
	private String liOfficeAbName;

	/** The president name. */
	@Column(name = "PRESIDENT_NAME")
	private String presidentName;

	/** The president title. */
	@Column(name = "PRESIDENT_TITLE")
	private String presidentTitle;

	/** The postal. */
	@Column(name = "POSTAL")
	private String postal;

	/** The address 1. */
	@Column(name = "ADDRESS1")
	private String address1;

	/** The address 2. */
	@Column(name = "ADDRESS2")
	private String address2;

	/** The kn address 1. */
	@Column(name = "KN_ADDRESS1")
	private String knAddress1;

	/** The kn address 2. */
	@Column(name = "KN_ADDRESS2")
	private String knAddress2;

	/** The tel no. */
	@Column(name = "TEL_NO")
	private String telNo;

	/** The city sign. */
	@Column(name = "CITY_SIGN")
	private String citySign;

	/** The office mark. */
	@Column(name = "OFFICE_MARK")
	private String officeMark;

	/** The office no A. */
	@Column(name = "OFFICE_N0_A")
	private String officeNoA;

	/** The office no B. */
	@Column(name = "OFFICE_N0_B")
	private String officeNoB;

	/** The office no C. */
	@Column(name = "OFFICE_N0_C")
	private String officeNoC;

	/** The memo. */
	@Column(name = "MEMO")
	private String memo;

	/**
	 * Instantiates a new qismt labor insu office.
	 */
	public QismtLaborInsuOffice() {
		super();
	}

	/**
	 * Instantiates a new qismt labor insu office.
	 *
	 * @param qismtLaborInsuOfficePK
	 *            the qismt labor insu office PK
	 */
	public QismtLaborInsuOffice(QismtLaborInsuOfficePK qismtLaborInsuOfficePK) {
		this.qismtLaborInsuOfficePK = qismtLaborInsuOfficePK;
	}

	/**
	 * Instantiates a new qismt labor insu office.
	 *
	 * @param ccd
	 *            the ccd
	 * @param liOfficeCd
	 *            the li office cd
	 */
	public QismtLaborInsuOffice(String ccd, String liOfficeCd) {
		this.qismtLaborInsuOfficePK = new QismtLaborInsuOfficePK(ccd, liOfficeCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qismtLaborInsuOfficePK != null ? qismtLaborInsuOfficePK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtLaborInsuOffice)) {
			return false;
		}
		QismtLaborInsuOffice other = (QismtLaborInsuOffice) object;
		if ((this.qismtLaborInsuOfficePK == null && other.qismtLaborInsuOfficePK != null)
				|| (this.qismtLaborInsuOfficePK != null
						&& !this.qismtLaborInsuOfficePK.equals(other.qismtLaborInsuOfficePK))) {
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
		return this.qismtLaborInsuOfficePK;
	}

}
