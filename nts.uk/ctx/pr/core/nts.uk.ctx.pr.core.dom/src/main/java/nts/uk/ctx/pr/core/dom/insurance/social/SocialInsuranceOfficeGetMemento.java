/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social;

import nts.uk.ctx.pr.core.dom.insurance.Address1;
import nts.uk.ctx.pr.core.dom.insurance.Address2;
import nts.uk.ctx.pr.core.dom.insurance.AddressKana1;
import nts.uk.ctx.pr.core.dom.insurance.AddressKana2;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.OfficeName;
import nts.uk.ctx.pr.core.dom.insurance.PhoneNumber;
import nts.uk.ctx.pr.core.dom.insurance.PicName;
import nts.uk.ctx.pr.core.dom.insurance.PicPosition;
import nts.uk.ctx.pr.core.dom.insurance.PotalCode;
import nts.uk.ctx.pr.core.dom.insurance.ShortName;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Interface SocialInsuranceOfficeMemento.
 */
public interface SocialInsuranceOfficeGetMemento {

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	String getCompanyCode();

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	OfficeCode getCode();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	OfficeName getName();

	/**
	 * Gets the short name.
	 *
	 * @return the short name
	 */
	ShortName getShortName();

	/**
	 * Gets the pic name.
	 *
	 * @return the pic name
	 */
	PicName getPicName();

	/**
	 * Gets the pic position.
	 *
	 * @return the pic position
	 */
	PicPosition getPicPosition();

	/**
	 * Gets the potal code.
	 *
	 * @return the potal code
	 */
	PotalCode getPotalCode();

	/**
	 * Gets the address 1 st.
	 *
	 * @return the address 1 st
	 */
	Address1 getAddress1st();

	/**
	 * Gets the address 2 nd.
	 *
	 * @return the address 2 nd
	 */
	Address2 getAddress2nd();

	/**
	 * Gets the kana address 1 st.
	 *
	 * @return the kana address 1 st
	 */
	AddressKana1 getKanaAddress1st();

	/**
	 * Gets the kana address 2 nd.
	 *
	 * @return the kana address 2 nd
	 */
	AddressKana2 getKanaAddress2nd();

	/**
	 * Gets the phone number.
	 *
	 * @return the phone number
	 */
	// TODO: TelephoneNo
	PhoneNumber getPhoneNumber();

	/**
	 * Gets the health insu office ref code 1 st.
	 *
	 * @return the health insu office ref code 1 st
	 */
	String getHealthInsuOfficeRefCode1st();

	/**
	 * Gets the health insu office ref code 2 nd.
	 *
	 * @return the health insu office ref code 2 nd
	 */
	String getHealthInsuOfficeRefCode2nd();

	/**
	 * Gets the pension office ref code 1 st.
	 *
	 * @return the pension office ref code 1 st
	 */
	String getPensionOfficeRefCode1st();

	/**
	 * Gte pension office ref code 2 nd.
	 *
	 * @return the string
	 */
	String getPensionOfficeRefCode2nd();

	/**
	 * Gets the welfare pension fund code.
	 *
	 * @return the welfare pension fund code
	 */
	String getWelfarePensionFundCode();

	/**
	 * Gets the office pension fund code.
	 *
	 * @return the office pension fund code
	 */
	String getOfficePensionFundCode();

	/**
	 * Gets the health insu city code.
	 *
	 * @return the health insu city code
	 */
	String getHealthInsuCityCode();

	/**
	 * Gets the health insu office sign.
	 *
	 * @return the health insu office sign
	 */
	String getHealthInsuOfficeSign();

	/**
	 * Gets the pension city code.
	 *
	 * @return the pension city code
	 */
	String getPensionCityCode();

	/**
	 * Gets the pension office sign.
	 *
	 * @return the pension office sign
	 */
	String getPensionOfficeSign();

	/**
	 * Gets the health insu office code.
	 *
	 * @return the health insu office code
	 */
	String getHealthInsuOfficeCode();

	/**
	 * Gets the health insu asso code.
	 *
	 * @return the health insu asso code
	 */
	String getHealthInsuAssoCode();

	/**
	 * Gets the memo.
	 *
	 * @return the memo
	 */
	Memo getMemo();

}
