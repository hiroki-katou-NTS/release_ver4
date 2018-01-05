/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.dom.adapter.workplace;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

/**
 * The Interface WorkplaceAdapter.
 */
public interface WorkplaceAdapter {

	/**
	 * Find list wkp id by base date.
	 *
	 * @param baseDate the base date
	 * @return the list
	 */
	List<String> findListWkpIdByBaseDate(GeneralDate baseDate);

	/**
	 * Find wkp by base date and employee id.
	 *
	 * @param baseDate the base date
	 * @param employeeId the employee id
	 * @return the aff workplace hist import
	 */
	Optional<AffWorkplaceHistImport> findWkpByBaseDateAndEmployeeId(GeneralDate baseDate, String employeeId);
	
	
	/**
	 * Find list workplace id by cid and wkp id and base date.
	 *
	 * @param companyId the company id
	 * @param workplaceId the workplace id
	 * @param baseDate the base date
	 * @return the list
	 */
	List<String> findListWorkplaceIdByCidAndWkpIdAndBaseDate(String companyId, String workplaceId,
			GeneralDate baseDate);
	
	/**
	 * 
	 * @param workplaceId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<String> findListSIdByCidAndWkpIdAndPeriod(String workplaceId, GeneralDate startDate, GeneralDate endDate);

}
