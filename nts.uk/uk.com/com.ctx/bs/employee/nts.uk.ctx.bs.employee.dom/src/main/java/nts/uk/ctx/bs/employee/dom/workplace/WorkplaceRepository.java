/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace;

import java.util.List;
import java.util.Optional;

/**
 * The Interface WorkplaceRepository.
 */
public interface WorkplaceRepository {

	/**
	 * Adds the.
	 *
	 * @param wkp the wkp
	 * @return the string
	 */
	void add(Workplace wkp);

	/**
	 * Update.
	 *
	 * @param wkp the wkp
	 */
	void update(Workplace wkp);

	/**
	 * Removes the by wkp id.
	 *
	 * @param companyId the company id
	 * @param workplaceId the workplace id
	 */
    void removeByWkpId(String companyId, String workplaceId);

	/**
	 * Find all workplace.
	 *
	 * @param workplaceId the workplace id
	 * @return the list
	 */
	List<Workplace> findByWkpIds(List<String> workplaceIds);

	/**
	 * Find by workplace id.
	 *
	 * @param companyId the company id
	 * @param workplaceId the workplace id
	 * @return the optional
	 */
    Optional<Workplace> findByWorkplaceId(String companyId, String workplaceId);
    
    /**
     * Find by history id.
     *
     * @param companyId the company id
     * @param historyId the history id
     * @return the optional
     */
    Optional<Workplace> findByHistoryId(String companyId, String historyId);
}
