/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social;

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
 * The Class QismtSocialInsuOffice.
 */
@Getter
@Setter
@Entity
@Table(name = "QISMT_SOCIAL_INSU_OFFICE")
public class QismtSocialInsuOffice extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qismt social insu office PK. */
	@EmbeddedId
	protected QismtSocialInsuOfficePK qismtSocialInsuOfficePK;

	/** The si office name. */
	@Basic(optional = false)
	@Column(name = "SI_OFFICE_NAME")
	private String siOfficeName;

	/** The si office ab name. */
	@Column(name = "SI_OFFICE_AB_NAME")
	private String siOfficeAbName;

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

	/** The health insu office refno 1. */
	@Column(name = "HEALTH_INSU_OFFICE_REFNO1")
	private String healthInsuOfficeRefno1;

	/** The health insu office refno 2. */
	@Column(name = "HEALTH_INSU_OFFICE_REFNO2")
	private String healthInsuOfficeRefno2;

	/** The pension office refno 1. */
	@Column(name = "PENSION_OFFICE_REFNO1")
	private String pensionOfficeRefno1;

	/** The pension office refno 2. */
	@Column(name = "PENSION_OFFICE_REFNO2")
	private String pensionOfficeRefno2;

	/** The pension fund no. */
	@Column(name = "PENSION_FUND_NO")
	private String pensionFundNo;

	/** The pension fund office no. */
	@Column(name = "PENSION_FUND_OFFICE_NO")
	private String pensionFundOfficeNo;

	/** The health insu city mark. */
	@Column(name = "HEALTH_INSU_CITY_MARK")
	private String healthInsuCityMark;

	/** The health insu office mark. */
	@Column(name = "HEALTH_INSU_OFFICE_MARK")
	private String healthInsuOfficeMark;

	/** The pension city mark. */
	@Column(name = "PENSION_CITY_MARK")
	private String pensionCityMark;

	/** The pension office mark. */
	@Column(name = "PENSION_OFFICE_MARK")
	private String pensionOfficeMark;

	/** The health insu office no. */
	@Column(name = "HEALTH_INSU_OFFICE_NO")
	private String healthInsuOfficeNo;

	/** The health insu asso no. */
	@Column(name = "HEALTH_INSU_ASSO_NO")
	private String healthInsuAssoNo;

	/** The memo. */
	@Column(name = "MEMO")
	private String memo;

	/**
	 * Instantiates a new qismt social insu office.
	 */
	public QismtSocialInsuOffice() {
		super();
	}

	/**
	 * Instantiates a new qismt social insu office.
	 *
	 * @param qismtSocialInsuOfficePK
	 *            the qismt social insu office PK
	 */
	public QismtSocialInsuOffice(QismtSocialInsuOfficePK qismtSocialInsuOfficePK) {
		this.qismtSocialInsuOfficePK = qismtSocialInsuOfficePK;
	}

	/**
	 * Instantiates a new qismt social insu office.
	 *
	 * @param ccd
	 *            the ccd
	 * @param siOfficeCd
	 *            the si office cd
	 */
	public QismtSocialInsuOffice(String ccd, String siOfficeCd) {
		this.qismtSocialInsuOfficePK = new QismtSocialInsuOfficePK(ccd, siOfficeCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qismtSocialInsuOfficePK != null ? qismtSocialInsuOfficePK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QismtSocialInsuOffice)) {
			return false;
		}
		QismtSocialInsuOffice other = (QismtSocialInsuOffice) object;
		if ((this.qismtSocialInsuOfficePK == null && other.qismtSocialInsuOfficePK != null)
				|| (this.qismtSocialInsuOfficePK != null
						&& !this.qismtSocialInsuOfficePK.equals(other.qismtSocialInsuOfficePK))) {
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
		return this.qismtSocialInsuOfficePK;
	}

}
