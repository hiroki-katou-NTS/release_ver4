/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.printsetting;

/**
 * The Interface SalaryPrintSettingRepository.
 */
public interface SalaryPrintSettingRepository {

	/**
	 * Find.
	 *
	 * @param companyCode the company code
	 * @return the salary print setting
	 */
	SalaryPrintSetting find(String companyCode);

	/**
	 * Save.
	 *
	 * @param salaryPrintSetting the salary print setting
	 */
	void save(SalaryPrintSetting salaryPrintSetting);
}
