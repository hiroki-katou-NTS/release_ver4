/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.difftimeset;

import java.util.Optional;

/**
 * The Interface DiffTimeWorkSettingRepository.
 */
public interface DiffTimeWorkSettingRepository {

	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 * @return the optional
	 */
	Optional<DiffTimeWorkSetting> find(String companyId, String workTimeCode);

}
