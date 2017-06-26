/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.CareerGroup;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItem;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItemGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItemSetting;

/**
 * The Class UnemployeeInsuranceRateItemDto.
 */
@Getter
@Setter
public class UnemployeeInsuranceRateItemDto {
	/** The career group. */
	private Integer careerGroup;

	/** The company setting. */
	private UnemployeeInsuranceRateItemSettingDto companySetting;

	/** The personal setting. */
	private UnemployeeInsuranceRateItemSettingDto personalSetting;

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the unemployee insurance rate item
	 */
	public UnemployeeInsuranceRateItem toDomain(String companyCode) {
		UnemployeeInsuranceRateItemDto dto = this;
		return new UnemployeeInsuranceRateItem(new UnemployeeInsuranceRateItemGetMemento() {

			@Override
			public UnemployeeInsuranceRateItemSetting getPersonalSetting() {
				UnemployeeInsuranceRateItemSetting personalSetting = new UnemployeeInsuranceRateItemSetting();
				personalSetting.setRate(dto.personalSetting.getRate());
				personalSetting.setRoundAtr(RoundingMethod.valueOf(dto.personalSetting.getRoundAtr()));
				return personalSetting;
			}

			@Override
			public UnemployeeInsuranceRateItemSetting getCompanySetting() {
				UnemployeeInsuranceRateItemSetting companySetting = new UnemployeeInsuranceRateItemSetting();
				companySetting.setRate(dto.companySetting.getRate());
				companySetting.setRoundAtr(RoundingMethod.valueOf(dto.companySetting.getRoundAtr()));
				return companySetting;
			}

			@Override
			public CareerGroup getCareerGroup() {
				return CareerGroup.valueOf(dto.careerGroup);
			}
		});
	}
}
