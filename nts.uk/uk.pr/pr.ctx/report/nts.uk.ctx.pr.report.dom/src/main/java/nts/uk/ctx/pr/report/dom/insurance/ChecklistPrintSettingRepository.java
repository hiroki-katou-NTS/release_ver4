/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.insurance;

import java.util.Optional;

/**
 * The Interface ChecklistPrintSettingRepository.
 */
public interface ChecklistPrintSettingRepository {

	/**
	 * create.
	 *
	 * @param printSetting the print setting
	 */
	void create(ChecklistPrintSetting printSetting);
	
	/**
	 * Update.
	 *
	 * @param printSetting the print setting
	 */
	void update(ChecklistPrintSetting printSetting);

	/**
	 * Find by company code.
	 *
	 * @param companyCode the company code
	 * @return the checklist print setting
	 */
	Optional<ChecklistPrintSetting> findByCompanyCode(String companyCode);
}
