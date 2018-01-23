/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.worktimeset;

import java.util.List;
import java.util.Optional;

/**
 * The Interface WorkTimeSettingRepository.
 */
public interface WorkTimeSettingRepository {

	/**
	 * Find by company id.
	 *
	 * @param companyId the company id
	 * @return the list
	 */
	public List<WorkTimeSetting> findByCompanyId(String companyId);

	/**
	 * Find by codes.
	 *
	 * @param companyId the company id
	 * @param codes the codes
	 * @return the list
	 */
	public List<WorkTimeSetting> findByCodes(String companyId, List<String> codes);

	/**
	 * Find by code.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 * @return the optional
	 */
	public Optional<WorkTimeSetting> findByCode(String companyId, String workTimeCode);

	/**
	 * Find with condition.
	 *
	 * @param companyId the company id
	 * @param condition the condition
	 * @return the list
	 */
	public List<WorkTimeSetting> findWithCondition(String companyId, WorkTimeSettingCondition condition);
	
	/**
	 * Gets the list work time set by list code.
	 *
	 * @param workTimeCodes the work time codes
	 * @return the list work time set by list code
	 */
	public List<WorkTimeSetting> getListWorkTimeSetByListCode(String companyId, List<String> workTimeCodes);

	/**
	 * Insert.
	 *
	 * @param domain the domain
	 */
	public void add(WorkTimeSetting domain);

	/**
	 * Update.
	 *
	 * @param domain the domain
	 */
	public void update(WorkTimeSetting domain);

	/**
	 * Removes the.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 */
	public void remove(String companyId,String workTimeCode);
}
