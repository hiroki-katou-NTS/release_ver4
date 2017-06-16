/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.employment.statutory.worktime.employment;

import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.shared.DeformationLaborSetting;
import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.shared.FlexSetting;
import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.shared.NormalSetting;

/**
 * The Interface CompanySettingSetMemento.
 */
public interface EmploymentWtSettingSetMemento {

	/**
	 * Sets the flex setting.
	 *
	 * @param flexSetting the new flex setting
	 */
	void setFlexSetting(FlexSetting flexSetting);

	/**
	 * Sets the deformation labor setting.
	 *
	 * @param deformationLaborSetting the new deformation labor setting
	 */
	void setDeformationLaborSetting(DeformationLaborSetting deformationLaborSetting);

	/**
	 * Sets the year.
	 *
	 * @param year the new year
	 */
	void setYear(Year year);

	/**
	 * Sets the company id.
	 *
	 * @param companyId the new company id
	 */
	void setCompanyId(CompanyId companyId);

	/**
	 * Sets the normal setting.
	 *
	 * @param normalSetting the new normal setting
	 */
	void setNormalSetting(NormalSetting normalSetting);

	/**
	 * Sets the employment code.
	 *
	 * @param employmentCode the new employment code
	 */
	void setEmploymentCode(String employmentCode);
}
