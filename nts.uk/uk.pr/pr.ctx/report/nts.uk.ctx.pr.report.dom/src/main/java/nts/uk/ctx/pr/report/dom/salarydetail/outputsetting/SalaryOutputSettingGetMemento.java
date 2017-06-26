/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import java.util.List;

/**
 * The Interface SalaryOutputSettingGetMemento.
 */
public interface SalaryOutputSettingGetMemento {

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	 String getCompanyCode();

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	 SalaryOutputSettingCode getCode();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	 SalaryOutputSettingName getName();

	/**
	 * Gets the category settings.
	 *
	 * @return the category settings
	 */
	 List<SalaryCategorySetting> getCategorySettings();
}
