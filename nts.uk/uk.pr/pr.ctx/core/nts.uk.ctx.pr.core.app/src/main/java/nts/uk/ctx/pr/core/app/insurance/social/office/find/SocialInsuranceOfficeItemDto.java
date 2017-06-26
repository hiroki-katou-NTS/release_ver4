/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.office.find;

import lombok.Builder;
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
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class SocialInsuranceOfficeItemDto.
 */
@Builder
public class SocialInsuranceOfficeItemDto implements SocialInsuranceOfficeSetMemento {

	/** The company code. */
	public String companyCode;

	/** The code. */
	public String code;

	/** The name. */
	public String name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setCompanyCode(java.lang.String)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
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
		this.code = code.v();
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
		this.name = name.v();
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @sees
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeSetMemento#
	 * setPensionOfficeSign(java.lang.String)
	 */
	@Override
	public void setPensionOfficeSign(OfficeSign pensionOfficeSign) {

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

	}
}
