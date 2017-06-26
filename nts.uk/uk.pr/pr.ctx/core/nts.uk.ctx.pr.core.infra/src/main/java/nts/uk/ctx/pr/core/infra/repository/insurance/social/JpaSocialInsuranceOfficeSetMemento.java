/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social;

import nts.uk.ctx.pr.core.dom.insurance.Address1;
import nts.uk.ctx.pr.core.dom.insurance.Address2;
import nts.uk.ctx.pr.core.dom.insurance.AddressKana1;
import nts.uk.ctx.pr.core.dom.insurance.AddressKana2;
import nts.uk.ctx.pr.core.dom.insurance.CityCode;
import nts.uk.ctx.pr.core.dom.insurance.HealthInsuAssoCode;
import nts.uk.ctx.pr.core.dom.insurance.HealthInsuOfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.OfficeName;
import nts.uk.ctx.pr.core.dom.insurance.OfficePensionFundCode;
import nts.uk.ctx.pr.core.dom.insurance.OfficeRefCode1;
import nts.uk.ctx.pr.core.dom.insurance.OfficeRefCode2;
import nts.uk.ctx.pr.core.dom.insurance.OfficeSign;
import nts.uk.ctx.pr.core.dom.insurance.PhoneNumber;
import nts.uk.ctx.pr.core.dom.insurance.PicName;
import nts.uk.ctx.pr.core.dom.insurance.PicPosition;
import nts.uk.ctx.pr.core.dom.insurance.PotalCode;
import nts.uk.ctx.pr.core.dom.insurance.ShortName;
import nts.uk.ctx.pr.core.dom.insurance.WelfarePensionFundCode;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.QismtSocialInsuOffice;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.QismtSocialInsuOfficePK;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class JpaSocialInsuranceOfficeSetMemento.
 */
public class JpaSocialInsuranceOfficeSetMemento implements SocialInsuranceOfficeSetMemento {

	/** The type value. */
	private QismtSocialInsuOffice typeValue;

	/**
	 * Instantiates a new jpa social insurance office set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaSocialInsuranceOfficeSetMemento(QismtSocialInsuOffice typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setCompanyCode(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		QismtSocialInsuOfficePK QismtSocialInsuOfficePK = new QismtSocialInsuOfficePK();
		QismtSocialInsuOfficePK.setCcd(companyCode);
		this.typeValue.setQismtSocialInsuOfficePK(QismtSocialInsuOfficePK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setCode(nts.uk.ctx.pr.core.dom.insurance.OfficeCode)
	 */
	@Override
	public void setCode(OfficeCode code) {
		QismtSocialInsuOfficePK QismtSocialInsuOfficePK = this.typeValue
				.getQismtSocialInsuOfficePK();
		QismtSocialInsuOfficePK.setSiOfficeCd(code.v());
		this.typeValue.setQismtSocialInsuOfficePK(QismtSocialInsuOfficePK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setName(nts.uk.ctx.pr.core.dom.insurance.OfficeName)
	 */
	@Override
	public void setName(OfficeName name) {
		this.typeValue.setSiOfficeName(name.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setShortName(nts.uk.ctx.pr.core.dom.insurance.ShortName)
	 */
	@Override
	public void setShortName(ShortName shortName) {
		this.typeValue.setSiOfficeAbName(shortName.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setPicName(nts.uk.ctx.pr.core.dom.insurance.PicName)
	 */
	@Override
	public void setPicName(PicName picName) {
		this.typeValue.setPresidentName(picName.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setPicPosition(nts.uk.ctx.pr.core.dom.insurance.PicPosition)
	 */
	@Override
	public void setPicPosition(PicPosition picPosition) {
		this.typeValue.setPresidentTitle(picPosition.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setPotalCode(nts.uk.ctx.pr.core.dom.insurance.PotalCode)
	 */
	@Override
	public void setPotalCode(PotalCode potalCode) {
		this.typeValue.setPostal(potalCode.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setAddress1st(nts.uk.ctx.pr.core.dom.insurance.Address)
	 */
	@Override
	public void setAddress1st(Address1 address1st) {
		this.typeValue.setAddress1(address1st.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setAddress2nd(nts.uk.ctx.pr.core.dom.insurance.Address)
	 */
	@Override
	public void setAddress2nd(Address2 address2nd) {
		this.typeValue.setAddress2(address2nd.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setKanaAddress1st(nts.uk.ctx.pr.core.dom.insurance.KanaAddress)
	 */
	@Override
	public void setKanaAddress1st(AddressKana1 kanaAddress1st) {
		this.typeValue.setKnAddress1(kanaAddress1st.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setKanaAddress2nd(nts.uk.ctx.pr.core.dom.insurance.KanaAddress)
	 */
	@Override
	public void setKanaAddress2nd(AddressKana2 kanaAddress2nd) {
		this.typeValue.setKnAddress2(kanaAddress2nd.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setPhoneNumber(java.lang.String)
	 */
	@Override
	public void setPhoneNumber(PhoneNumber phoneNumber) {
		this.typeValue.setTelNo(phoneNumber.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setHealthInsuOfficeRefCode1st(java.lang.String)
	 */
	@Override
	public void setHealthInsuOfficeRefCode1st(OfficeRefCode1 healthInsuOfficeRefCode1st) {
		this.typeValue.setHealthInsuOfficeRefno1(healthInsuOfficeRefCode1st.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setHealthInsuOfficeRefCode2nd(java.lang.String)
	 */
	@Override
	public void setHealthInsuOfficeRefCode2nd(OfficeRefCode2 healthInsuOfficeRefCode2nd) {
		this.typeValue.setHealthInsuOfficeRefno2(healthInsuOfficeRefCode2nd.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setPensionOfficeRefCode1st(java.lang.String)
	 */
	@Override
	public void setPensionOfficeRefCode1st(OfficeRefCode1 pensionOfficeRefCode1st) {
		this.typeValue.setPensionOfficeRefno1(pensionOfficeRefCode1st.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setPensionOfficeRefCode2nd(java.lang.String)
	 */
	@Override
	public void setPensionOfficeRefCode2nd(OfficeRefCode2 pensionOfficeRefCode2nd) {
		this.typeValue.setPensionOfficeRefno2(pensionOfficeRefCode2nd.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setWelfarePensionFundCode(java.lang.String)
	 */
	@Override
	public void setWelfarePensionFundCode(WelfarePensionFundCode welfarePensionFundCode) {
		if (welfarePensionFundCode == null) {
			this.typeValue.setPensionFundNo("");
		} else {
			this.typeValue.setPensionFundNo(welfarePensionFundCode.v().toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setOfficePensionFundCode(java.lang.String)
	 */
	@Override
	public void setOfficePensionFundCode(OfficePensionFundCode officePensionFundCode) {
		this.typeValue.setPensionFundOfficeNo(officePensionFundCode.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setHealthInsuCityCode(java.lang.String)
	 */
	@Override
	public void setHealthInsuCityCode(CityCode healthInsuCityCode) {
		this.typeValue.setHealthInsuCityMark(healthInsuCityCode.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setHealthInsuOfficeSign(java.lang.String)
	 */
	@Override
	public void setHealthInsuOfficeSign(OfficeSign healthInsuOfficeSign) {
		this.typeValue.setHealthInsuOfficeMark(healthInsuOfficeSign.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setPensionCityCode(java.lang.String)
	 */
	@Override
	public void setPensionCityCode(CityCode pensionCityCode) {
		this.typeValue.setPensionCityMark(pensionCityCode.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setPensionOfficeSign(java.lang.String)
	 */
	@Override
	public void setPensionOfficeSign(OfficeSign pensionOfficeSign) {
		this.typeValue.setPensionOfficeMark(pensionOfficeSign.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setHealthInsuOfficeCode(java.lang.String)
	 */
	@Override
	public void setHealthInsuOfficeCode(HealthInsuOfficeCode healthInsuOfficeCode) {
		if (healthInsuOfficeCode == null) {
			this.typeValue.setHealthInsuOfficeNo("");
		} else {
			this.typeValue.setHealthInsuOfficeNo(healthInsuOfficeCode.v().toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setHealthInsuAssoCode(java.lang.String)
	 */
	@Override
	public void setHealthInsuAssoCode(HealthInsuAssoCode healthInsuAssoCode) {
		this.typeValue.setHealthInsuAssoNo(healthInsuAssoCode.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setMemo(nts.uk.shr.com.primitive.Memo)
	 */
	@Override
	public void setMemo(Memo memo) {
		this.typeValue.setMemo(memo.v());
	}

}
