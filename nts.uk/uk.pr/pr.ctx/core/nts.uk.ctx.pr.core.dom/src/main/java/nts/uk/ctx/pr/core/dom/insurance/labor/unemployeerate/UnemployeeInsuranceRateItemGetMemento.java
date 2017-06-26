/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate;

/**
 * The Interface UnemployeeInsuranceRateItemMemento.
 */
public interface UnemployeeInsuranceRateItemGetMemento {

	/**
	 * Gets the career group.
	 *
	 * @return the career group
	 */
	CareerGroup getCareerGroup();

	/**
	 * Gets the company setting.
	 *
	 * @return the company setting
	 */
	UnemployeeInsuranceRateItemSetting getCompanySetting();

	/**
	 * Gets the personal setting.
	 *
	 * @return the personal setting
	 */
	UnemployeeInsuranceRateItemSetting getPersonalSetting();

}
