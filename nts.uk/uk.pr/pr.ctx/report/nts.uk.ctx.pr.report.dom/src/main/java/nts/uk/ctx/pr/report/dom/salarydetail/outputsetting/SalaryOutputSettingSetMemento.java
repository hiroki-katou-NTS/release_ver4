/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import java.util.List;

/**
 * The Interface SalaryOutputSettingSetMemento.
 */
public interface SalaryOutputSettingSetMemento {

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	 void setCompanyCode(String companyCode);

	/**
	 * Sets the code.
	 *
	 * @param salaryOutputSettingCode the new code
	 */
	 void setCode(SalaryOutputSettingCode salaryOutputSettingCode);

	/**
	 * Sets the name.
	 *
	 * @param salaryOutputSettingName the new name
	 */
	 void setName(SalaryOutputSettingName salaryOutputSettingName);

	/**
	 * Sets the category settings.
	 *
	 * @param listSalaryCategorySetting the new category settings
	 */
	 void setCategorySettings(List<SalaryCategorySetting> listSalaryCategorySetting);
}
