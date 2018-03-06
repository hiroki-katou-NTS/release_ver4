/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew;

import java.util.Optional;

import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * The Interface WkpFlexSettingRepository.
 */
public interface WkpFlexSettingRepository {

	/**
	 * Find by cid and wkp id and year.
	 *
	 * @param cid the cid
	 * @param wkpId the wkp id
	 * @param year the year
	 * @return the optional
	 */
	Optional<WkpFlexSetting> findByCidAndWkpIdAndYear(String cid, String wkpId, Year year);

	/**
	 * Adds the.
	 *
	 * @param wkpFlexSetting the wkp flex setting
	 */
	void add(WkpFlexSetting wkpFlexSetting);

	/**
	 * Update.
	 *
	 * @param wkpFlexSetting the wkp flex setting
	 */
	void update(WkpFlexSetting wkpFlexSetting);

	/**
	 * Delete.
	 *
	 * @param wkpFlexSetting the wkp flex setting
	 */
	void delete(WkpFlexSetting wkpFlexSetting);

}
