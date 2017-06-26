/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social;

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
import nts.uk.shr.com.primitive.Memo;

/**
 * The Interface SocialInsuranceOfficeMemento.
 */
public interface SocialInsuranceOfficeSetMemento {

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	void setCompanyCode(String companyCode);

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	void setCode(OfficeCode code);

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	void setName(OfficeName name);

	/**
	 * Sets the short name.
	 *
	 * @param shortName the new short name
	 */
	void setShortName(ShortName shortName);

	/**
	 * Sets the pic name.
	 *
	 * @param picName the new pic name
	 */
	void setPicName(PicName picName);

	/**
	 * Sets the pic position.
	 *
	 * @param picPosition the new pic position
	 */
	void setPicPosition(PicPosition picPosition);

	/**
	 * Sets the potal code.
	 *
	 * @param potalCode the new potal code
	 */
	void setPotalCode(PotalCode potalCode);

	/**
	 * Sets the address 1 st.
	 *
	 * @param address1st the new address 1 st
	 */
	void setAddress1st(Address1 address1st);

	/**
	 * Sets the address 2 nd.
	 *
	 * @param address2nd the new address 2 nd
	 */
	void setAddress2nd(Address2 address2nd);

	/**
	 * Sets the kana address 1 st.
	 *
	 * @param kanaAddress1st the new kana address 1 st
	 */
	void setKanaAddress1st(AddressKana1 kanaAddress1st);

	/**
	 * Sets the kana address 2 nd.
	 *
	 * @param kanaAddress2nd the new kana address 2 nd
	 */
	void setKanaAddress2nd(AddressKana2 kanaAddress2nd);

	/**
	 * Sets the phone number.
	 *
	 * @param phoneNumber the new phone number
	 */
	void setPhoneNumber(PhoneNumber phoneNumber);

	/**
	 * Sets the health insu office ref code 1 st.
	 *
	 * @param healthInsuOfficeRefCode1st the new health insu office ref code 1 st
	 */
	void setHealthInsuOfficeRefCode1st(OfficeRefCode1 healthInsuOfficeRefCode1st);

	/**
	 * Sets the health insu office ref code 2 nd.
	 *
	 * @param healthInsuOfficeRefCode2nd the new health insu office ref code 2 nd
	 */
	void setHealthInsuOfficeRefCode2nd(OfficeRefCode2 healthInsuOfficeRefCode2nd);

	/**
	 * Sets the pension office ref code 1 st.
	 *
	 * @param pensionOfficeRefCode1st the new pension office ref code 1 st
	 */
	void setPensionOfficeRefCode1st(OfficeRefCode1 pensionOfficeRefCode1st);

	/**
	 * Sets the pension office ref code 2 nd.
	 *
	 * @param pensionOfficeRefCode2nd the new pension office ref code 2 nd
	 */
	void setPensionOfficeRefCode2nd(OfficeRefCode2 pensionOfficeRefCode2nd);

	/**
	 * Sets the welfare pension fund code.
	 *
	 * @param welfarePensionFundCode the new welfare pension fund code
	 */
	void setWelfarePensionFundCode(WelfarePensionFundCode welfarePensionFundCode);

	/**
	 * Sets the office pension fund code.
	 *
	 * @param officePensionFundCode the new office pension fund code
	 */
	void setOfficePensionFundCode(OfficePensionFundCode officePensionFundCode);

	/**
	 * Sets the health insu city code.
	 *
	 * @param healthInsuCityCode the new health insu city code
	 */
	void setHealthInsuCityCode(CityCode healthInsuCityCode);

	/**
	 * Sets the health insu office sign.
	 *
	 * @param healthInsuOfficeSign the new health insu office sign
	 */
	void setHealthInsuOfficeSign(OfficeSign healthInsuOfficeSign);

	/**
	 * Sets the pension city code.
	 *
	 * @param pensionCityCode the new pension city code
	 */
	void setPensionCityCode(CityCode pensionCityCode);

	/**
	 * Sets the pension office sign.
	 *
	 * @param pensionOfficeSign the new pension office sign
	 */
	void setPensionOfficeSign(OfficeSign pensionOfficeSign);

	/**
	 * Sets the health insu office code.
	 *
	 * @param healthInsuOfficeCode the new health insu office code
	 */
	void setHealthInsuOfficeCode(HealthInsuOfficeCode healthInsuOfficeCode);

	/**
	 * Sets the health insu asso code.
	 *
	 * @param healthInsuAssoCode the new health insu asso code
	 */
	void setHealthInsuAssoCode(HealthInsuAssoCode healthInsuAssoCode);

	/**
	 * Sets the memo.
	 *
	 * @param memo the new memo
	 */
	void setMemo(Memo memo);

}
