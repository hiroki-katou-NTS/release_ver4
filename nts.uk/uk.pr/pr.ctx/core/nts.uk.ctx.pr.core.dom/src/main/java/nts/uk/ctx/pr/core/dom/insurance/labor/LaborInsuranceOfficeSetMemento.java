/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
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
public interface LaborInsuranceOfficeSetMemento {

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
	 * Sets the city sign.
	 *
	 * @param citySign the new city sign
	 */
	void setCitySign(CitySign citySign);

	/**
	 * Sets the office mark.
	 *
	 * @param officeMark the new office mark
	 */
	void setOfficeMark(OfficeMark officeMark);

	/**
	 * Sets the office no A.
	 *
	 * @param officeNoA the new office no A
	 */
	void setOfficeNoA(OfficeNoA officeNoA);

	/**
	 * Sets the office no B.
	 *
	 * @param officeNoB the new office no B
	 */
	void setOfficeNoB(OfficeNoB officeNoB);

	/**
	 * Sets the office no C.
	 *
	 * @param officeNoC the new office no C
	 */
	void setOfficeNoC(OfficeNoC officeNoC);

	/**
	 * Sets the memo.
	 *
	 * @param memo the new memo
	 */
	void setMemo(Memo memo);

}
