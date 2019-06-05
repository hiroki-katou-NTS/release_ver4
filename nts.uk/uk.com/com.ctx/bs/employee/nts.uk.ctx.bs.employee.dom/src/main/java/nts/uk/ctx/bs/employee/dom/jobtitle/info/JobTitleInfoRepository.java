/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.jobtitle.info;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;

/**
 * The Interface JobTitleInfoRepository.
 */
public interface JobTitleInfoRepository {

	/**
	 * Adds the.
	 *
	 * @param jobTitleInfo the job title info
	 */
	void add(JobTitleInfo jobTitleInfo);

	/**
	 * Update.
	 *
	 * @param jobTitleInfo the job title info
	 */
	void update(JobTitleInfo jobTitleInfo);

	/**
	 * Removes the.
	 *
	 * @param companyId the company id
	 * @param jobTitleId the job title id
	 * @param historyId the history id
	 */
	void remove(String companyId, String jobTitleId, String historyId);

	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @param jobTitleId the job title id
	 * @param historyId the history id
	 * @return the optional
	 */
	Optional<JobTitleInfo> find(String companyId, String jobTitleId, String historyId);

	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @param jobTitleId the job title id
	 * @param baseDate the base date
	 * @return the optional
	 */
	Optional<JobTitleInfo> find(String companyId, String jobTitleId, GeneralDate baseDate);
	
	/**
	 * Find job title code.
	 *
	 * @param companyId the company id
	 * @param jobTitleId the job title id
	 * @return the optional
	 */
	Optional<JobTitleCode> findJobTitleCode(String companyId, String jobTitleId);

	/**
	 * Find.
	 *
	 * @param jobTitleId the job title id
	 * @param baseDate the base date
	 * @return the optional
	 */
	Optional<JobTitleInfo> find(String jobTitleId, GeneralDate baseDate);

	/**
	 * Find by job code.
	 *
	 * @param companyId the company id
	 * @param jobTitleCode the job title code
	 * @return the optional
	 */
	Optional<JobTitleInfo> findByJobCode(String companyId, String jobTitleCode);

	/**
	 * Checks if is sequence master used.
	 *
	 * @param companyId the company id
	 * @param sequenceCode the sequence code
	 * @return true, if is sequence master used
	 */
	boolean isSequenceMasterUsed(String companyId, String sequenceCode);

	/**
	 * Find all.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @return the list
	 */
	List<JobTitleInfo> findAll(String companyId, GeneralDate baseDate);

	/**
	 * Checks if is job title code exist.
	 *
	 * @param companyId the company id
	 * @param jobTitleCode the job title code
	 * @return true, if is job title code exist
	 */
	boolean isJobTitleCodeExist(String companyId, String jobTitleCode);
	
	
	/**
	 * Find.
	 *
	 * @param companyId the company id
	 * @param jobIds the job ids
	 * @param baseDate the base date
	 * @return the list
	 */
	List<JobTitleInfo> findByIds(String companyId, List<String> jobIds, GeneralDate baseDate);
	
	List<JobTitleInfo> findByIds(List<String> jobIds, GeneralDate baseDate);
	
	/**
	 * Find by ids.
	 *
	 * @param companyId the company id
	 * @param jobIds the job ids
	 * @param baseDates the base dates
	 * @return the map
	 */
	Map<GeneralDate, List<JobTitleInfo>> findByIds(String companyId, List<String> jobIds,
			List<GeneralDate> baseDates);

	List<JobTitleInfo> findByJobIds(String companyId, List<String> jobIds, String historyId);
	
}
