/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.find.dto;

import lombok.Getter;
import lombok.Setter;
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
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class LaborInsuranceOfficeFindDto.
 */

/**
 * Instantiates a new labor insurance office find dto.
 */
@Getter
@Setter
public class LaborInsuranceOfficeFindDto implements LaborInsuranceOfficeSetMemento {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/** The short name. */
	private String shortName;

	/** The pic name. */
	private String picName;

	/** The pic position. */
	private String picPosition;

	/** The potal code. */
	private String potalCode;

	/** The address 1 st. */
	private String address1st;

	/** The address 2 nd. */
	private String address2nd;

	/** The kana address 1 st. */
	private String kanaAddress1st;

	/** The kana address 2 nd. */
	private String kanaAddress2nd;

	/** The phone number. */
	private String phoneNumber;

	/** The city sign. */
	private String citySign;

	/** The office mark. */
	private String officeMark;

	/** The office no A. */
	private String officeNoA;

	/** The office no B. */
	private String officeNoB;

	/** The office no C. */
	private String officeNoC;

	/** The memo. */
	private String memo;

	/** The version. */
	private long version;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setCompanyCode(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setShortName(nts.uk.ctx.pr.core.dom.insurance.ShortName)
	 */
	@Override
	public void setShortName(ShortName shortName) {
		this.shortName = shortName.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setPicName(nts.uk.ctx.pr.core.dom.insurance.PicName)
	 */
	@Override
	public void setPicName(PicName picName) {
		this.picName = picName.v();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setPicPosition(nts.uk.ctx.pr.core.dom.insurance.PicPosition)
	 */
	@Override
	public void setPicPosition(PicPosition picPosition) {
		this.picPosition = picPosition.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setPotalCode(nts.uk.ctx.pr.core.dom.insurance.PotalCode)
	 */
	@Override
	public void setPotalCode(PotalCode potalCode) {
		this.potalCode = potalCode.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setAddress1st(nts.uk.ctx.pr.core.dom.insurance.Address)
	 */
	@Override
	public void setAddress1st(Address1 address1st) {
		this.address1st = address1st.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setAddress2nd(nts.uk.ctx.pr.core.dom.insurance.Address)
	 */
	@Override
	public void setAddress2nd(Address2 address2nd) {
		this.address2nd = address2nd.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setKanaAddress1st(nts.uk.ctx.pr.core.dom.insurance.KanaAddress)
	 */
	@Override
	public void setKanaAddress1st(AddressKana1 kanaAddress1st) {
		this.kanaAddress1st = kanaAddress1st.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setKanaAddress2nd(nts.uk.ctx.pr.core.dom.insurance.KanaAddress)
	 */
	@Override
	public void setKanaAddress2nd(AddressKana2 kanaAddress2nd) {
		this.kanaAddress2nd = kanaAddress2nd.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setMemo(nts.uk.shr.com.primitive.Memo)
	 */
	@Override
	public void setMemo(Memo memo) {
		this.memo = memo.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setPhoneNumber(nts.uk.ctx.pr.core.dom.insurance.PhoneNumber)
	 */
	@Override
	public void setPhoneNumber(PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber.v();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setCitySign(nts.uk.ctx.pr.core.dom.insurance.CitySign)
	 */
	@Override
	public void setCitySign(CitySign citySign) {
		this.citySign = citySign.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setOfficeMark(nts.uk.ctx.pr.core.dom.insurance.OfficeMark)
	 */
	@Override
	public void setOfficeMark(OfficeMark officeMark) {
		this.officeMark = officeMark.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setOfficeNoA(nts.uk.ctx.pr.core.dom.insurance.OfficeNoA)
	 */
	@Override
	public void setOfficeNoA(OfficeNoA officeNoA) {
		this.officeNoA = officeNoA.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setOfficeNoB(nts.uk.ctx.pr.core.dom.insurance.OfficeNoB)
	 */
	@Override
	public void setOfficeNoB(OfficeNoB officeNoB) {
		this.officeNoB = officeNoB.v();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setOfficeNoC(nts.uk.ctx.pr.core.dom.insurance.OfficeNoC)
	 */
	@Override
	public void setOfficeNoC(OfficeNoC officeNoC) {
		this.officeNoC = officeNoC.v();

	}
}
