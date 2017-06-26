package nts.uk.ctx.basic.dom.organization.position;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface PositionRepository {

	/**
	 * Find All Jobtitle
	 * 
	 * @param companyCode
	 * @param historyId
	 * @return
	 */
	List<JobTitle> findAllJobTitle(String companyCode, String historyId);

	/**
	 * Add JobTitle
	 * 
	 * @param jobTitle
	 */
	void add(JobTitle jobTitle);

	/**
	 * Update JobTitle
	 * 
	 * @param jobTitle
	 */
	void update(JobTitle jobTitle);

	/**
	 * Delete JobTitle
	 * 
	 * @param companyCode
	 * @param jobCode
	 * @param historyId
	 */
	void delete(String companyCode, String historyId, JobCode jobCode);

	/**
	 * Delete JobTitle By HistoryId
	 * 
	 * @param companyCode
	 * @param historyId
	 */
	void deleteJobTitleByHisId(String companyCode, String historyId);

	/**
	 * Add History
	 * 
	 * @param history
	 */
	void addHistory(JobHistory history);

	/**
	 * Update History
	 * 
	 * @param history
	 */
	void updateHistory(JobHistory history);

	/**
	 * Delete History
	 * 
	 * @param companyCode
	 * @param historyId
	 */
	void deleteHist(String companyCode, String historyId);

	/**
	 * Get All History
	 * 
	 * @param companyCode
	 * @return
	 */
	List<JobHistory> getAllHistory(String companyCode);

	/**
	 * Find All JobTitle Ref
	 * 
	 * @param companyCode
	 * @param historyId
	 * @param jobCode
	 * @return
	 */
	List<JobTitleRef> findAllJobTitleRef(String companyCode, String historyId, String jobCode);

	/**
	 * Find All JobTitle Ref By JobHistory
	 * 
	 * @param companyCode
	 * @param historyId
	 * @return
	 */
	List<JobTitleRef> findAllJobTitleRefByJobHist(String companyCode, String historyId);

	/**
	 * Find Single JobTitle Ref
	 * 
	 * @param companyCode
	 * @param historyId
	 * @param jobCode
	 * @return
	 */
	Optional<JobTitleRef> findSingleJobTitleRef(String companyCode, String historyId, String jobCode);

	/**
	 * Find Single History
	 * 
	 * @param companyCode
	 * @param historyId
	 * @return
	 */
	Optional<JobHistory> findSingleHistory(String companyCode, String historyId);

	/**
	 * Get History By End Date
	 * 
	 * @param companyCode
	 * @param endDate
	 * @return
	 */
	Optional<JobHistory> getHistoryByEdate(String companyCode, GeneralDate endDate);

	/**
	 * Add JobTitle Ref
	 * 
	 * @param jobTitleRef
	 */
	void addJobTitleRef(List<JobTitleRef> jobTitleRef);

	/**
	 * Get JobTitle By Code
	 * 
	 * @param companyCode
	 * @param historyId
	 * @param jobCode
	 * @return
	 */
	Optional<JobTitle> getJobTitleByCode(String companyCode, String historyId, String jobCode);

	/**
	 * Get All Auth
	 * 
	 * @param companyCode
	 * @param historyId
	 * @param jobCode
	 * @param authScopeAtr
	 * @return
	 */
	List<JobRefAuth> getAllAuth(String companyCode, String historyId, String jobCode, String authScopeAtr);

	/**
	 * Add List JobTitle
	 * 
	 * @param lstPositionNow
	 */
	void add(List<JobTitle> lstPositionNow);

	/**
	 * Update JobTitle Ref
	 * 
	 * @param refInfor
	 */
	void updateRef(List<JobTitleRef> refInfor);

	/**
	 * Delete JobTitle Ref
	 * 
	 * @param companyCode
	 * @param historyId
	 * @param jobCode
	 */
	void deleteJobTitleRef(String companyCode, String historyId, String jobCode);
	
	/**
	 * Delete JobTitle Ref By JobHist
	 * 
	 * @param companyCode
	 * @param historyId
	 */
	void deleteJobTitleRefByJobHist(String companyCode, String historyId);

	Optional<AuthorizationLevel> findAuth(String companyCode, String authScopeAtr);
}
