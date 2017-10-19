/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace.info;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

/**
 * The Interface WorkplaceInfoRepository.
 */
public interface WorkplaceInfoRepository {

    /**
     * Adds the.
     *
     * @param workplaceInfo the workplace info
     */
    void add(WorkplaceInfo workplaceInfo);
    
	/**
	 * Update.
	 *
	 * @param workplaceInfo the workplace info
	 */
	void update(WorkplaceInfo workplaceInfo);

	/**
     * Removes the.
     *
     * @param companyId the company id
     * @param workplaceId the workplace id
     * @param historyId the history id
     */
    void remove(String companyId, String workplaceId, String historyId);
	
	/**
	 * Find by wkp id.
	 *
	 * @param wkpId the wkp id
	 * @return the optional
	 */
	Optional<WorkplaceInfo> find(String companyId, String wkpId, String historyId);

	/**
	 * Find by wkp cd.
	 *
	 * @param wpkCode the wpk code
	 * @param date the date
	 * @return the list
	 */
	List<WorkplaceInfo> findByWkpCd(String companyId, String wpkCode, GeneralDate baseDate);

	/**
	 * Find by wkp id.
	 *
	 * @param companyId the company id
	 * @param wkpId the wpk id
	 * @param baseDate the base date
	 * @return the list
	 */
	Optional<WorkplaceInfo> findByWkpId(String wkpId, GeneralDate baseDate);
	
	/**
	 * Checks if is existed.
	 *
	 * @param companyId the company id
	 * @param newWkpCd the new wkp id
	 * @return true, if is existed
	 */
	boolean isExistedWkpCd(String companyId, String newWkpCd);
}
