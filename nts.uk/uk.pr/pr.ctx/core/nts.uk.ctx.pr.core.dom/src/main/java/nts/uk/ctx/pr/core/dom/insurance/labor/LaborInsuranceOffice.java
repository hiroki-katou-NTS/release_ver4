/******************************************************************

 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.insurance.labor;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
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
 * The Class LaborInsuranceOffice.
 */
@Getter
public class LaborInsuranceOffice extends DomainObject {

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
	private PhoneNumber phoneNumber;

	/** The city sign. */
	private CitySign citySign;

	/** The office mark. */
	private OfficeMark officeMark;

	/** The office no A. */
	private OfficeNoA officeNoA;

	/** The office no B. */
	private OfficeNoB officeNoB;

	/** The office no C. */
	private OfficeNoC officeNoC;

	/** The memo. */
	private Memo memo;

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new labor insurance office.
	 *
	 * @param memento
	 *            the memento
	 */
	public LaborInsuranceOffice(LaborInsuranceOfficeGetMemento memento) {
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
		this.citySign = memento.getCitySign();
		this.officeMark = memento.getOfficeMark();
		this.officeNoA = memento.getOfficeNoA();
		this.officeNoB = memento.getOfficeNoB();
		this.officeNoC = memento.getOfficeNoC();
		this.memo = memento.getMemo();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(LaborInsuranceOfficeSetMemento memento) {
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
		memento.setCitySign(this.citySign);
		memento.setOfficeMark(this.officeMark);
		memento.setOfficeNoA(this.officeNoA);
		memento.setOfficeNoB(this.officeNoB);
		memento.setOfficeNoC(this.officeNoC);
		memento.setMemo(this.memo);
	}

}
