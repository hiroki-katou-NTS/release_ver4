/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave2;

/**
 * The Interface CompensLeaveComSetRepository.
 */
public interface CompensLeaveComSetRepository {
	
	/**
	 * Insert.
	 *
	 * @param setting the setting
	 */
	void insert(CompensatoryLeaveComSetting setting);
	
	/**
	 * Update.
	 *
	 * @param setting the setting
	 */
	void update(CompensatoryLeaveComSetting setting);
	
	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @return the compensatory leave com setting
	 */
	CompensatoryLeaveComSetting find(String companyId);
}
