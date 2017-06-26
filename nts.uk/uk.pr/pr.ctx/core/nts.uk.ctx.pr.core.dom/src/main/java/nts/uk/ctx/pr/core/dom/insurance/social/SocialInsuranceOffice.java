/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.core.dom.base.simplehistory.Master;
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
 * The Class SocialInsuranceOffice.
 */
@Getter
public class SocialInsuranceOffice extends DomainObject implements Master {

	/** The company code. */
	private String companyCode;

	/** The code. */
	private OfficeCode code;

	/** The name. */
	private OfficeName name;

	/** The short name. */
	private ShortName shortName;

	/** The pic name. */
	private PicName picName;

	/** The pic position. */
	private PicPosition picPosition;

	/** The potal code. */
	private PotalCode potalCode;

	/** The address 1 st. */
	private Address1 address1st;

	/** The address 2 nd. */
	private Address2 address2nd;

	/** The kana address 1 st. */
	private AddressKana1 kanaAddress1st;

	/** The kana address 2 nd. */
	private AddressKana2 kanaAddress2nd;

	/** The phone number. */
	// TODO: TelephoneNo
	private PhoneNumber phoneNumber;

	/** The health insu office ref code 1 st. */
	private OfficeRefCode1 healthInsuOfficeRefCode1st;

	/** The health insu office ref code 2 nd. */
	private OfficeRefCode2 healthInsuOfficeRefCode2nd;

	/** The pension office ref code 1 st. */
	private OfficeRefCode1 pensionOfficeRefCode1st;

	/** The pension office ref code 2 nd. */
	private OfficeRefCode2 pensionOfficeRefCode2nd;

	/** The welfare pension fund code. */
	private WelfarePensionFundCode welfarePensionFundCode;

	/** The office pension fund code. */
	private OfficePensionFundCode officePensionFundCode;

	/** The health insu city code. */
	private CityCode healthInsuCityCode;

	/** The health insu office sign. */
	private OfficeSign healthInsuOfficeSign;

	/** The pension city code. */
	private CityCode pensionCityCode;

	/** The pension office sign. */
	private OfficeSign pensionOfficeSign;

	/** The health insu office code. */
	private HealthInsuOfficeCode healthInsuOfficeCode;

	/** The health insu asso code. */
	private HealthInsuAssoCode healthInsuAssoCode;

	/** The memo. */
	private Memo memo;

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new social insurance office.
	 *
	 * @param memento
	 *            the memento
	 */
	public SocialInsuranceOffice(SocialInsuranceOfficeGetMemento memento) {
		this.companyCode = memento.getCompanyCode();
		this.code = memento.getCode();
		this.name = memento.getName();
		this.shortName = memento.getShortName();
		this.picName = memento.getPicName();
		this.picPosition = memento.getPicPosition();
		this.potalCode = memento.getPotalCode();
		this.address1st = memento.getAddress1st();
		this.address2nd = memento.getAddress2nd();
		this.kanaAddress1st = memento.getKanaAddress1st();
		this.kanaAddress2nd = memento.getKanaAddress2nd();
		this.phoneNumber = memento.getPhoneNumber();
		this.healthInsuOfficeRefCode1st = memento.getHealthInsuOfficeRefCode1st();
		this.healthInsuOfficeRefCode2nd = memento.getHealthInsuOfficeRefCode2nd();
		this.pensionOfficeRefCode1st = memento.getPensionOfficeRefCode1st();
		this.pensionOfficeRefCode2nd = memento.getPensionOfficeRefCode2nd();
		this.welfarePensionFundCode = memento.getWelfarePensionFundCode();
		this.officePensionFundCode = memento.getOfficePensionFundCode();
		this.healthInsuCityCode = memento.getHealthInsuCityCode();
		this.healthInsuOfficeSign = memento.getHealthInsuOfficeSign();
		this.pensionCityCode = memento.getPensionCityCode();
		this.pensionOfficeSign = memento.getPensionOfficeSign();
		this.healthInsuOfficeCode = memento.getHealthInsuOfficeCode();
		this.healthInsuAssoCode = memento.getHealthInsuAssoCode();
		this.memo = memento.getMemo();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(SocialInsuranceOfficeSetMemento memento) {
		memento.setCompanyCode(this.companyCode);
		memento.setCode(this.code);
		memento.setName(this.name);
		memento.setShortName(this.shortName);
		memento.setPicName(this.picName);
		memento.setPicPosition(this.picPosition);
		memento.setPotalCode(this.potalCode);
		memento.setAddress1st(this.address1st);
		memento.setAddress2nd(this.address2nd);
		memento.setKanaAddress1st(this.kanaAddress1st);
		memento.setKanaAddress2nd(this.kanaAddress2nd);
		memento.setPhoneNumber(this.phoneNumber);
		memento.setHealthInsuOfficeRefCode1st(this.healthInsuOfficeRefCode1st);
		memento.setHealthInsuOfficeRefCode2nd(this.healthInsuOfficeRefCode2nd);
		memento.setPensionOfficeRefCode1st(this.pensionOfficeRefCode1st);
		memento.setPensionOfficeRefCode2nd(this.pensionOfficeRefCode2nd);
		memento.setWelfarePensionFundCode(this.welfarePensionFundCode);
		memento.setOfficePensionFundCode(this.officePensionFundCode);
		memento.setHealthInsuCityCode(this.healthInsuCityCode);
		memento.setHealthInsuOfficeSign(this.healthInsuOfficeSign);
		memento.setPensionCityCode(this.pensionCityCode);
		memento.setPensionOfficeSign(this.pensionOfficeSign);
		memento.setHealthInsuOfficeCode(this.healthInsuOfficeCode);
		memento.setHealthInsuAssoCode(this.healthInsuAssoCode);
		memento.setMemo(this.memo);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SocialInsuranceOffice))
			return false;
		SocialInsuranceOffice other = (SocialInsuranceOffice) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
