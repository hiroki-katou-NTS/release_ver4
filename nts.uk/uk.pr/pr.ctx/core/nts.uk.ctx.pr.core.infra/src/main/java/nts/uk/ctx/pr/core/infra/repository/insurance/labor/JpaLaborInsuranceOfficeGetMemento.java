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
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.QismtLaborInsuOffice;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class JpaLaborInsuranceOfficeGetMemento.
 */
public class JpaLaborInsuranceOfficeGetMemento implements LaborInsuranceOfficeGetMemento {

	/** The type value. */
	private QismtLaborInsuOffice typeValue;

	/**
	 * Instantiates a new jpa labor insurance office get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaLaborInsuranceOfficeGetMemento(QismtLaborInsuOffice typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQismtLaborInsuOfficePK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getCode()
	 */
	@Override
	public OfficeCode getCode() {
		return new OfficeCode(this.typeValue.getQismtLaborInsuOfficePK().getLiOfficeCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getName()
	 */
	@Override
	public OfficeName getName() {
		return new OfficeName(this.typeValue.getLiOfficeName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getShortName()
	 */
	@Override
	public ShortName getShortName() {
		return new ShortName(this.typeValue.getLiOfficeAbName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getPicName()
	 */
	@Override
	public PicName getPicName() {
		return new PicName(this.typeValue.getPresidentName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getPicPosition()
	 */
	@Override
	public PicPosition getPicPosition() {
		return new PicPosition(this.typeValue.getPresidentTitle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getPotalCode()
	 */
	@Override
	public PotalCode getPotalCode() {
		return new PotalCode(this.typeValue.getPostal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getAddress1st()
	 */
	@Override
	public Address1 getAddress1st() {
		return new Address1(this.typeValue.getAddress1());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getAddress2nd()
	 */
	@Override
	public Address2 getAddress2nd() {
		return new Address2(this.typeValue.getAddress2());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getKanaAddress1st()
	 */
	@Override
	public AddressKana1 getKanaAddress1st() {
		return new AddressKana1(this.typeValue.getKnAddress1());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getKanaAddress2nd()
	 */
	@Override
	public AddressKana2 getKanaAddress2nd() {
		return new AddressKana2(this.typeValue.getKnAddress2());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getPhoneNumber()
	 */
	@Override
	public PhoneNumber getPhoneNumber() {
		return new PhoneNumber(this.typeValue.getTelNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getCitySign()
	 */
	@Override
	public CitySign getCitySign() {
		return new CitySign(this.typeValue.getCitySign());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getOfficeMark()
	 */
	@Override
	public OfficeMark getOfficeMark() {
		return new OfficeMark(this.typeValue.getOfficeMark());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getOfficeNoA()
	 */
	@Override
	public OfficeNoA getOfficeNoA() {
		return new OfficeNoA(this.typeValue.getOfficeNoA());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getOfficeNoB()
	 */
	@Override
	public OfficeNoB getOfficeNoB() {
		return new OfficeNoB(this.typeValue.getOfficeNoB());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getOfficeNoC()
	 */
	@Override
	public OfficeNoC getOfficeNoC() {
		return new OfficeNoC(this.typeValue.getOfficeNoC());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento#
	 * getMemo()
	 */
	@Override
	public Memo getMemo() {
		return new Memo(this.typeValue.getMemo());
	}

}
