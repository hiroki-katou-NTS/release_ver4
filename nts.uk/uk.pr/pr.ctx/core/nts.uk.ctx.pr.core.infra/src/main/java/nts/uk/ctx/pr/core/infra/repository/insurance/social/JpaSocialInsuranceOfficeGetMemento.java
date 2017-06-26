/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social;

import java.math.BigDecimal;

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
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.QismtSocialInsuOffice;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class JpaSocialInsuranceOfficeGetMemento.
 */
public class JpaSocialInsuranceOfficeGetMemento implements SocialInsuranceOfficeGetMemento {

	/** The type value. */
	private QismtSocialInsuOffice typeValue;

	/**
	 * Instantiates a new jpa social insurance office get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaSocialInsuranceOfficeGetMemento(QismtSocialInsuOffice typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQismtSocialInsuOfficePK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getCode()
	 */
	@Override
	public OfficeCode getCode() {
		return new OfficeCode(this.typeValue.getQismtSocialInsuOfficePK().getSiOfficeCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getName()
	 */
	@Override
	public OfficeName getName() {
		return new OfficeName(this.typeValue.getSiOfficeName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getShortName()
	 */
	@Override
	public ShortName getShortName() {
		return new ShortName(this.typeValue.getSiOfficeAbName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getHealthInsuOfficeRefCode1st()
	 */
	@Override
	public OfficeRefCode1 getHealthInsuOfficeRefCode1st() {
		return new OfficeRefCode1(this.typeValue.getHealthInsuOfficeRefno1());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getHealthInsuOfficeRefCode2nd()
	 */
	@Override
	public OfficeRefCode2 getHealthInsuOfficeRefCode2nd() {
		return new OfficeRefCode2(this.typeValue.getHealthInsuOfficeRefno2());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getPensionOfficeRefCode1st()
	 */
	@Override
	public OfficeRefCode1 getPensionOfficeRefCode1st() {
		return new OfficeRefCode1(this.typeValue.getPensionOfficeRefno1());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getPensionOfficeRefCode2nd()
	 */
	@Override
	public OfficeRefCode2 getPensionOfficeRefCode2nd() {
		return new OfficeRefCode2(this.typeValue.getPensionOfficeRefno2());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getWelfarePensionFundCode()
	 */
	@Override
	public WelfarePensionFundCode getWelfarePensionFundCode() {
		if (this.typeValue.getPensionFundNo() == null || this.typeValue.getPensionFundNo().equals("")) {
			return null;
		}
		return new WelfarePensionFundCode(new BigDecimal(this.typeValue.getPensionFundNo()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getOfficePensionFundCode()
	 */
	@Override
	public OfficePensionFundCode getOfficePensionFundCode() {
		return new OfficePensionFundCode(this.typeValue.getPensionFundOfficeNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getHealthInsuCityCode()
	 */
	@Override
	public CityCode getHealthInsuCityCode() {
		return new CityCode(this.typeValue.getHealthInsuCityMark());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getHealthInsuOfficeSign()
	 */
	@Override
	public OfficeSign getHealthInsuOfficeSign() {
		return new OfficeSign(this.typeValue.getHealthInsuOfficeMark());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getPensionCityCode()
	 */
	@Override
	public CityCode getPensionCityCode() {
		return new CityCode(this.typeValue.getPensionCityMark());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getPensionOfficeSign()
	 */
	@Override
	public OfficeSign getPensionOfficeSign() {
		return new OfficeSign(this.typeValue.getPensionOfficeMark());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getHealthInsuOfficeCode()
	 */
	@Override
	public HealthInsuOfficeCode getHealthInsuOfficeCode() {
		if (this.typeValue.getHealthInsuOfficeNo() == null||this.typeValue.getHealthInsuOfficeNo().equals("")) {
			return null;
		} else {
			return new HealthInsuOfficeCode(new BigDecimal(this.typeValue.getHealthInsuOfficeNo()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getHealthInsuAssoCode()
	 */
	@Override
	public HealthInsuAssoCode getHealthInsuAssoCode() {
		return new HealthInsuAssoCode(this.typeValue.getHealthInsuAssoNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento#
	 * getMemo()
	 */
	@Override
	public Memo getMemo() {
		return new Memo(this.typeValue.getMemo());
	}

}
