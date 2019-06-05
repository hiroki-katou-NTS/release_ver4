/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pub.jobtitle;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;

/**
 * The Interface JobtitlePub.
 */
public interface SyJobTitlePub {

	/**
	 * Find job title by sid.
	 *
	 * @param employeeId the employee id
	 * @return the list
	 */
	// RequestList17
	List<JobTitleExport> findJobTitleBySid(String employeeId);

	/**
	 * Find job title by sid.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList33
	Optional<EmployeeJobHistExport> findBySid(String employeeId, GeneralDate baseDate);

	/**
	 * Find job title by position id.
	 *
	 * @param companyId the company id
	 * @param jobId the position id
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList67-1
	Optional<JobTitleExport> findByJobId(String companyId, String jobId, GeneralDate baseDate);

	/**
	 * Find by base date.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList74
	List<JobTitleExport> findAll(String companyId, GeneralDate baseDate);

	/**
	 * Find S job hist by S id.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList #??? -> NamPT pls add on
	Optional<EmployeeJobHistExport> findSJobHistBySId(String employeeId, GeneralDate baseDate);
	/**
	 * Find list S job hist by list S id.
	 *
	 * @param employeeIds
	 * @param baseDate the base date
	 * @return the list EmployeeJobHistExport
	 */
	List<EmployeeJobHistExport> findSJobHistByListSId(List<String> employeeIds, GeneralDate baseDate);
	List<EmployeeJobHistExport> findSJobHistByListSIdV2(List<String> employeeIds, GeneralDate baseDate);
	
	/**
	 * Find by ids.
	 *
	 * @param companyId the company id
	 * @param jobIds the job ids
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList158
	List<SimpleJobTitleExport> findByIds(String companyId,List<String> jobIds, GeneralDate baseDate);
	
	/**
	 * Find job title by sid.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList From  LamDT
	Optional<AffJobTitleHistoryExport> gerBySidAndBaseDate(String employeeId, GeneralDate baseDate);
	

	/**
	 * 社員IDと基準日から職位情報を取得
	 * @param sid
	 * @param baseDate
	 * @return
	 */
	// RequestList 297
	Optional<AffJobTitleBasicExport> getBySidAndBaseDate(String sid, GeneralDate baseDate);
	
	/**
	 * Gets the job title map.
	 *
	 * @param companyId the company id
	 * @param jobIds the job ids
	 * @param baseDate the base date
	 * @return the job title map
	 */
	Map<Pair<String, GeneralDate>, Pair<String, String>> getJobTitleMapIdBaseDateName(String companyId,List<String> jobIds, List<GeneralDate> baseDates);

	List<JobTitleInfoExport> findByJobIds(String companyId, List<String> jobIds, String historyId);
	
	List<SequenceMasterExport> findAllSequen(String companyId, String sequenceCode);

}
