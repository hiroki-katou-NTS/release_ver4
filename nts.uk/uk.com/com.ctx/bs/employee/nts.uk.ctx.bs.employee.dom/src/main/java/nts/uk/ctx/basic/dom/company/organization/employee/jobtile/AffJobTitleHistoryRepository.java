/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.dom.company.organization.employee.jobtile;

import java.util.List;

import nts.arc.time.GeneralDate;

/**
 * The Class JobTitleHistoryRepository.
 */
public interface AffJobTitleHistoryRepository {

	/**
	 * Search job title history.
	 *
	 * @param baseDate the base date
	 * @param positionIds the position ids
	 * @return the list
	 */
	List<AffJobTitleHistory> searchJobTitleHistory(GeneralDate baseDate, List<String> positionIds);

	/**
	 * Find all job title history.
	 *
	 * @param baseDate the base date
	 * @param employeeIds the employee ids
	 * @return the list
	 */
	List<AffJobTitleHistory> findAllJobTitleHistory(GeneralDate baseDate, List<String> employeeIds);

	/**
	 * Search job title history.
	 *
	 * @param employeeIds the employee ids
	 * @param baseDate the base date
	 * @param positionIds the position ids
	 * @return the list
	 */
	List<AffJobTitleHistory> searchJobTitleHistory(List<String> employeeIds,
			GeneralDate baseDate, List<String> positionIds);

	/**
	 * Find by sid.
	 *
	 * @param employeeId the employee id
	 * @return the list
	 */
	List<AffJobTitleHistory> findBySid(String employeeId);

	/**
	 * Find by sid.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the list
	 */
	List<AffJobTitleHistory> findBySid(String employeeId, GeneralDate baseDate);
}
