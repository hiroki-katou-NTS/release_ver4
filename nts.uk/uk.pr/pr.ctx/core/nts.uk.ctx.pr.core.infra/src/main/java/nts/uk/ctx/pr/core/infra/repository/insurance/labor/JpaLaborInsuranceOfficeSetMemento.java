/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor;

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
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.QismtLaborInsuOffice;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.QismtLaborInsuOfficePK;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class JpaLaborInsuranceOfficeSetMemento.
 */
public class JpaLaborInsuranceOfficeSetMemento implements LaborInsuranceOfficeSetMemento {

	/** The type value. */
	private QismtLaborInsuOffice typeValue;

	/**
	 * Instantiates a new jpa labor insurance office set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaLaborInsuranceOfficeSetMemento(QismtLaborInsuOffice typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeSetMemento#
	 * setCompanyCode(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		QismtLaborInsuOfficePK pk = new QismtLaborInsuOfficePK();
		pk.setCcd(companyCode);
		this.typeValue.setQismtLaborInsuOfficePK(pk);
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
		QismtLaborInsuOfficePK pk = this.typeValue.getQismtLaborInsuOfficePK();
		pk.setLiOfficeCd(code.v());
		this.typeValue.setQismtLaborInsuOfficePK(pk);
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
		this.typeValue.setLiOfficeName(name.v());
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
		this.typeValue.setLiOfficeAbName(shortName.v());
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
		this.typeValue.setPresidentName(picName.v());
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
		this.typeValue.setPresidentTitle(picPosition.v());
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
		this.typeValue.setPostal(potalCode.v());
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
		this.typeValue.setAddress1(address1st.v());
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
		this.typeValue.setAddress2(address2nd.v());
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
		this.typeValue.setKnAddress1(kanaAddress1st.v());
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
		this.typeValue.setKnAddress2(kanaAddress2nd.v());
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
		this.typeValue.setMemo(memo.v());
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
		this.typeValue.setTelNo(phoneNumber.v());

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
		this.typeValue.setCitySign(citySign.v());
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
		this.typeValue.setOfficeMark(officeMark.v());
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
		this.typeValue.setOfficeNoA(officeNoA.v());

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
		this.typeValue.setOfficeNoB(officeNoB.v());

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
		this.typeValue.setOfficeNoC(officeNoC.v());

	}

}
