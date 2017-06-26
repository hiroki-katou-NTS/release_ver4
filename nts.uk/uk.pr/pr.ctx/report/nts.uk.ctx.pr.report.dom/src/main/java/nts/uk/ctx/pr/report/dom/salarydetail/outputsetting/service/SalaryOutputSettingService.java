/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.service;

import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSetting;

/**
 * The Interface SalaryOutputSettingService.
 */
public interface SalaryOutputSettingService {
	
	/**
	 * Validate required item.
	 *
	 * @param salaryOutputSetting the salary output setting
	 */
	void validateRequiredItem(SalaryOutputSetting salaryOutputSetting);

	/**
	 * Check duplicate code.
	 *
	 * @param companyCode the company code
	 * @param salaryOutputSettingCode the salary output setting code
	 */
	void checkDuplicateCode(String companyCode, String salaryOutputSettingCode);
}
