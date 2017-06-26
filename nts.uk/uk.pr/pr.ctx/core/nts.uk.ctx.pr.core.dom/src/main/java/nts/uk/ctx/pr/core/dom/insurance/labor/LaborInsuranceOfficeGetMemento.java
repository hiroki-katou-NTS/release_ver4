/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor;

import nts.uk.ctx.pr.core.dom.insurance.Address1;
import nts.uk.ctx.pr.core.dom.insurance.Address2;
import nts.uk.ctx.pr.core.dom.insurance.AddressKana1;
import nts.uk.ctx.pr.core.dom.insurance.AddressKana2;
import nts.uk.ctx.pr.core.dom.insurance.CitySign;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.OfficeMark;
import nts.uk.ctx.pr.core.dom.insurance.OfficeName;
import nts.uk.ctx.pr.core.dom.insurance.OfficeNoA;
import nts.uk.ctx.pr.core.dom.insurance.OfficeNoB;
import nts.uk.ctx.pr.core.dom.insurance.OfficeNoC;
import nts.uk.ctx.pr.core.dom.insurance.PhoneNumber;
import nts.uk.ctx.pr.core.dom.insurance.PicName;
import nts.uk.ctx.pr.core.dom.insurance.PicPosition;
import nts.uk.ctx.pr.core.dom.insurance.PotalCode;
import nts.uk.ctx.pr.core.dom.insurance.ShortName;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class LaborInsuranceOfficeMemento.
 *
 * @param <T>
 *            the generic type
 */
public interface LaborInsuranceOfficeGetMemento {

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
	 * Gets the city sign.
	 *
	 * @return the city sign
	 */
	CitySign getCitySign();

	/**
	 * Gets the office mark.
	 *
	 * @return the office mark
	 */
	OfficeMark getOfficeMark();

	/**
	 * Gets the office no A.
	 *
	 * @return the office no A
	 */
	OfficeNoA getOfficeNoA();

	/**
	 * Gets the office no B.
	 *
	 * @return the office no B
	 */
	OfficeNoB getOfficeNoB();

	/**
	 * Gets the office no C.
	 *
	 * @return the office no C
	 */
	OfficeNoC getOfficeNoC();

	/**
	 * Gets the memo.
	 *
	 * @return the memo
	 */
	Memo getMemo();

}
