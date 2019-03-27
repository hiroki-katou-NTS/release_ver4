/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace.config.info;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistory;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface WorkplaceConfigInfoRepository.
 */
public interface WorkplaceConfigInfoRepository {

	/**
	 * Adds the.
	 *
	 * @param wkpConfigInfo the wkp config info
	 */
	void add(WorkplaceConfigInfo wkpConfigInfo);

	/**
	 * Update.
	 *
	 * @param wkpConfigInfo the wkp config info
	 */
	void update(WorkplaceConfigInfo wkpConfigInfo);

	/**
	 * Removes the wkp hierarchy.
	 *
	 * @param companyId the company id
	 * @param historyId the history id
	 * @param wkpId the wkp id
	 */
	void removeWkpHierarchy(String companyId, String historyId, String wkpId);

	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @param historyId the history id
	 * @return the optional
	 */
	Optional<WorkplaceConfigInfo> find(String companyId, String historyId);
	
	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @return the optional
	 */
//	Optional<WorkplaceConfigInfo> find(String companyId, GeneralDate baseDate);

	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @param historyId the history id
	 * @param wkpId the wkp id
	 * @return the optional
	 */
	Optional<WorkplaceConfigInfo> find(String companyId, String historyId, String wkpId);

	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @param wkpId the wkp id
	 * @return the optional
	 */
	Optional<WorkplaceConfigInfo> find(String companyId, GeneralDate baseDate, String wkpId);

	/**
	 * Find with child.
	 *
	 * @param companyId the company id
	 * @param historyId the history id
	 * @param wkpId the wkp id
	 * @return the optional
	 */
	Optional<WorkplaceConfigInfo> findAllByParentWkpId(String companyId, GeneralDate baseDate, String parentWkpId);
	
	/**
	 * Find all parent by wkp id.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @param wkpId the wkp id
	 * @return the optional
	 */
	Optional<WorkplaceConfigInfo> findAllParentByWkpId(String companyId, GeneralDate baseDate, String wkpId);
	
	Map<WorkplaceConfigHistory, List<WorkplaceConfigInfo>> findAllParentByWkpId(String companyId, DatePeriod baseDate, List<String> wkpId);
	
	/**
	 * Find by wkp ids at time.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @param wkpIds the wkp ids
	 * @return the list
	 */
	List<WorkplaceConfigInfo> findByWkpIdsAtTime(String companyId, GeneralDate baseDate, List<String> wkpIds);
	
	/**
	 * Find by history ids and wpl ids.
	 *
	 * @param companyId the company id
	 * @param historyIds the history ids
	 * @param workplaceIds the workplace ids -> if null or emplty, return all workplace config info.
	 * @return the list
	 */
	List<WorkplaceConfigInfo> findByHistoryIdsAndWplIds(String companyId, List<String> historyIds, List<String> workplaceIds);

	/**
	 * Find by history ids.
	 *
	 * @param companyId the company id
	 * @param historyIds the history ids
	 * @return the list
	 */
	List<WorkplaceConfigInfo> findByHistoryIds(String companyId, List<String> historyIds);
	
	/**
	 * Find all parent by wkp id.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @param wkpId the wkp id
	 * @param isSortAscHierarchyCd the is sort asc hierarchy cd
	 * @return the optional
	 */
	Optional<WorkplaceConfigInfo> findAllParentByWkpId(String companyId,
			GeneralDate baseDate, String wkpId, boolean isSortAscHierarchyCd);
	
	/**
	 * Update workplace config info.
	 *
	 * @param companyId the company id
	 * @param historyId the history id
	 * @param wkpId the wkp id
	 * @param hierarchyCd the hierarchy cd
	 */
	void updateWorkplaceConfigInfo(String companyId, String historyId, String wkpId,String hierarchyCd);
}
