/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.jobtitle;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

/**
 * The Interface JobTitleRepository.
 */
public interface JobTitleRepository {

	/**
	 * Adds the.
	 *
	 * @param jobTitle the job title
	 */
	void add(JobTitle jobTitle);

	/**
	 * Update.
	 *
	 * @param jobTitle the job title
	 */
	void update(JobTitle jobTitle);

    /**
     * Removes the.
     *
     * @param companyId the company id
     * @param jobTitleId the job title id
     */
    void remove(String companyId, String jobTitleId);
	
    /**
     * Removes the history.
     *
     * @param companyId the company id
     * @param jobTitleId the job title id
     * @param historyId the history id
     */
    void removeHistory(String companyId, String jobTitleId, String historyId);
    
    /**
     * Find by job title id.
     *
     * @param companyId the company id
     * @param jobTitleId the job title id
     * @return the optional
     */
    Optional<JobTitle> findByJobTitleId(String companyId, String jobTitleId);
    
    /**
     * Find by history id.
     *
     * @param companyId the company id
     * @param historyId the history id
     * @return the optional
     */
    Optional<JobTitle> findByHistoryId(String companyId, String historyId);
    
    /**
     * Find by base date.
     *
     * @param companyId the company id
     * @param jobTitleId the job title id
     * @param baseDate the base date
     * @return the optional
     */
    Optional<JobTitle> findByBaseDate(String companyId, String jobTitleId, GeneralDate baseDate);
    
    /**
     * Find all.
     *
     * @param companyId the company id
     * @param baseDate the base date
     * @return the list
     */
    List<JobTitle> findAll(String companyId, GeneralDate baseDate);

    // fix bug 
	List<JobTitle> findAllById(String companyId, List<String> positionIds, GeneralDate baseDate);
    
}
