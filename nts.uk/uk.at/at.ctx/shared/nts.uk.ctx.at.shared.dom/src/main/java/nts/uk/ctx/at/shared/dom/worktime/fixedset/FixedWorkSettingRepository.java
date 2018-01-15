/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import java.util.Optional;

/**
 * The Interface FixedWorkSettingRepository.
 */
public interface FixedWorkSettingRepository {

	/**
	 * Adds the.
	 *
	 * @param domain the domain
	 */
	void add(FixedWorkSetting domain);
	
	/**
	 * Update.
	 *
	 * @param domain the domain
	 */
	void update(FixedWorkSetting domain);
	
	/**
	 * Removes the.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 */
	void remove(String companyId, String workTimeCode);
	
	/**
	 * Find by key.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 * @return the optional
	 */
	Optional<FixedWorkSetting> findByKey(String companyId, String workTimeCode);
}
