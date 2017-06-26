/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.office.command;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
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
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeGetMemento;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class RegisterSocialOfficeCommand.
 */
@Getter
@Setter
public class RegisterSocialOfficeCommand extends SocialOfficeBaseCommand {

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the social insurance office
	 */
	public SocialInsuranceOffice toDomain(String companyCode) {
		RegisterSocialOfficeCommand command = this;
		SocialInsuranceOffice socialInsuranceOffice = new SocialInsuranceOffice(
				new SocialInsuranceOfficeGetMemento() {

					@Override
					public String getCompanyCode() {
						return companyCode;
					}

					@Override
					public OfficeCode getCode() {
						return new OfficeCode(command.getCode());
					}

					@Override
					public OfficeName getName() {
						return new OfficeName(command.getName());
					}

					@Override
					public ShortName getShortName() {
						return new ShortName(command.getShortName());
					}

					@Override
					public PicName getPicName() {
						return new PicName(command.getPicName());
					}

					@Override
					public PicPosition getPicPosition() {
						return new PicPosition(command.getPicPosition());
					}

					@Override
					public PotalCode getPotalCode() {
						return new PotalCode(command.getPotalCode());
					}

					@Override
					public Address1 getAddress1st() {
						return new Address1(command.getAddress1st());
					}

					@Override
					public Address2 getAddress2nd() {
						return new Address2(command.getAddress2nd());
					}

					@Override
					public AddressKana1 getKanaAddress1st() {
						return new AddressKana1(command.getKanaAddress1st());
					}

					@Override
					public AddressKana2 getKanaAddress2nd() {
						return new AddressKana2(command.getKanaAddress2nd());
					}

					@Override
					public PhoneNumber getPhoneNumber() {
						return new PhoneNumber(command.getPhoneNumber());
					}

					@Override
					public OfficeRefCode1 getHealthInsuOfficeRefCode1st() {
						return new OfficeRefCode1(command.getHealthInsuOfficeRefCode1st());
					}

					@Override
					public OfficeRefCode2 getHealthInsuOfficeRefCode2nd() {
						return new OfficeRefCode2(command.getHealthInsuOfficeRefCode2nd());
					}

					@Override
					public OfficeRefCode1 getPensionOfficeRefCode1st() {
						return new OfficeRefCode1(command.getPensionOfficeRefCode1st());
					}

					@Override
					public OfficeRefCode2 getPensionOfficeRefCode2nd() {
						return new OfficeRefCode2(command.getPensionOfficeRefCode2nd());
					}

					@Override
					public WelfarePensionFundCode getWelfarePensionFundCode() {
						if (command.getWelfarePensionFundCode() == null
								|| command.getWelfarePensionFundCode().equals("")) {
							return null;
						} else {
							return new WelfarePensionFundCode(new BigDecimal(command.getWelfarePensionFundCode()));
						}
					}

					@Override
					public OfficePensionFundCode getOfficePensionFundCode() {
						return new OfficePensionFundCode(command.getOfficePensionFundCode());
					}

					@Override
					public CityCode getHealthInsuCityCode() {
						return new CityCode(command.getHealthInsuCityCode());
					}

					@Override
					public OfficeSign getHealthInsuOfficeSign() {
						return new OfficeSign(command.getHealthInsuOfficeSign());
					}

					@Override
					public CityCode getPensionCityCode() {
						return new CityCode(command.getPensionCityCode());
					}

					@Override
					public OfficeSign getPensionOfficeSign() {
						return new OfficeSign(command.getPensionOfficeSign());
					}

					@Override
					public HealthInsuOfficeCode getHealthInsuOfficeCode() {
						if (command.getHealthInsuOfficeCode() == null || command.getHealthInsuOfficeCode().equals("")) {
							return null;
						} else {
							return new HealthInsuOfficeCode(new BigDecimal(command.getHealthInsuOfficeCode()));
						}
					}

					@Override
					public HealthInsuAssoCode getHealthInsuAssoCode() {
						return new HealthInsuAssoCode(command.getHealthInsuAssoCode());
					}

					@Override
					public Memo getMemo() {
						return new Memo(command.getMemo());
					}
				});

		return socialInsuranceOffice;
	}
}
