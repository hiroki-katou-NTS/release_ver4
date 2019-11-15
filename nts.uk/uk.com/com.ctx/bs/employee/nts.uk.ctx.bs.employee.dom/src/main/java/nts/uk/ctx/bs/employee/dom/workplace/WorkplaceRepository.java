/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

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
     * Removes the wkp history.
     *
     * @param companyId the company id
     * @param workplaceId the workplace id
     * @param historyId the history id
     */
    void removeWkpHistory(String companyId, String workplaceId, String historyId);

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
    
    
    /**
     * Find workplace.
     *
     * @param companyId the company id
     * @param workplaceId the workplace id
     * @param baseDate the base date
     * @return the workplace
     */
    Workplace findWorkplace(String companyId, String workplaceId, GeneralDate baseDate);
    
    /**
     * Find workplaces.
     *
     * @param companyId the company id
     * @param workplaceIds the workplace ids
     * @param baseDate the base date
     * @return the list
     */
    List<Workplace> findWorkplaces(String companyId, List<String> workplaceIds, GeneralDate baseDate);
    
    /**
     * Find workplaces.
     *
     * @param workplaceIds the workplace ids
     * @param datePeriod the date period
     * @return the list
     */
    List<Workplace> findWorkplaces(List<String> workplaceIds, DatePeriod datePeriod);
    
	/**
	 * Find workplaces by date.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @return the list
	 */
	List<Workplace> findWorkplacesByDate(String companyId, GeneralDate baseDate);
	/**
	 * get list wkp by SDate
	 * @param companyId
	 * @param lstWkpId
	 * @param strDate
	 * @return
	 */
	void deleteWorkplacesBySDate(String companyId, String wkpId, GeneralDate strDate);
}
