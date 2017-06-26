/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate;

/**
 * The Interface UnemployeeInsuranceRateItemMemento.
 */
public interface UnemployeeInsuranceRateItemSetMemento {

	/**
	 * Sets the career group.
	 *
	 * @param careerGroup the new career group
	 */
	void setCareerGroup(CareerGroup careerGroup);

	/**
	 * Sets the company setting.
	 *
	 * @param companySetting the new company setting
	 */
	void setCompanySetting(UnemployeeInsuranceRateItemSetting companySetting);

	/**
	 * Sets the personal setting.
	 *
	 * @param personalSetting the new personal setting
	 */
	void setPersonalSetting(UnemployeeInsuranceRateItemSetting personalSetting);
}
