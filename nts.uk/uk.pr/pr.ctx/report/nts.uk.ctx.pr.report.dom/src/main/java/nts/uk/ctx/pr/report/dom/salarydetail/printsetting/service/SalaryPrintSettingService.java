/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.printsetting.service;

import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSetting;

/**
 * The Interface SalaryPrintSettingService.
 */
public interface SalaryPrintSettingService {

	/**
	 * Validate required item.
	 *
	 * @param salaryPrintSetting the salary print setting
	 */
	void validateRequiredItem(SalaryPrintSetting salaryPrintSetting);

	/**
	 * Validate hrchy count.
	 *
	 * @param setting the setting
	 */
	void validateSelection(SalaryPrintSetting setting);
}
