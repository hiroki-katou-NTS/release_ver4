/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.CareerGroup;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItemSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItemSetting;

/**
 * The Class UnemployeeInsuranceRateItemFindOutDto.
 */

/**
 * Gets the personal setting.
 *
 * @return the personal setting
 */
@Getter
@Setter
public class UnemployeeInsuranceRatetemFindDto implements UnemployeeInsuranceRateItemSetMemento {

	/** The career group. */
	private Integer careerGroup;

	/** The company setting. */
	private UnemployeeInsuranceRateSettingFindDto companySetting;

	/** The personal setting. */
	private UnemployeeInsuranceRateSettingFindDto personalSetting;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetMemento#setCareerGroup(nts.uk.ctx.pr.core.
	 * dom.insurance.labor.unemployeerate.CareerGroup)
	 */
	@Override
	public void setCareerGroup(CareerGroup careerGroup) {
		this.careerGroup = careerGroup.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetMemento#setCompanySetting(nts.uk.ctx.pr.
	 * core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetting)
	 */
	@Override
	public void setCompanySetting(UnemployeeInsuranceRateItemSetting companySetting) {
		if (this.companySetting == null) {
			this.companySetting = new UnemployeeInsuranceRateSettingFindDto();
		}
		this.companySetting.setRate(companySetting.getRate());
		this.companySetting.setRoundAtr(companySetting.getRoundAtr().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetMemento#setPersonalSetting(nts.uk.ctx.pr.
	 * core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateItemSetting)
	 */
	@Override
	public void setPersonalSetting(UnemployeeInsuranceRateItemSetting personalSetting) {
		if (this.personalSetting == null) {
			this.personalSetting = new UnemployeeInsuranceRateSettingFindDto();
		}
		this.personalSetting.setRate(personalSetting.getRate());
		this.personalSetting.setRoundAtr(personalSetting.getRoundAtr().value);
	}

}
