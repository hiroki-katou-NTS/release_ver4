/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import java.util.List;

/**
 * The Interface SalaryOutputSettingRepository.
 */
public interface SalaryOutputSettingRepository {

	/**
	 * Creates the.
	 *
	 * @param setting the setting
	 */
	void create(SalaryOutputSetting setting);

	/**
	 * Update.
	 *
	 * @param setting the setting
	 */
	void update(SalaryOutputSetting setting);

	/**
	 * Removes the.
	 *
	 * @param companyCode the company code
	 * @param salaryOutputSettingCode the salary output setting code
	 */
	void remove(String companyCode, String salaryOutputSettingCode);

	/**
	 * Find by code.
	 *
	 * @param companyCode the company code
	 * @param salaryOutputSettingCode the salary output setting code
	 * @return the salary output setting
	 */
	SalaryOutputSetting findByCode(String companyCode, String salaryOutputSettingCode);

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<SalaryOutputSetting> findAll(String companyCode);

	/**
	 * Checks if is exist.
	 *
	 * @param companyCode the company code
	 * @param salaryOutputSettingCode the salary output setting code
	 * @return the boolean
	 */
	Boolean isExist(String companyCode, String salaryOutputSettingCode);
}
