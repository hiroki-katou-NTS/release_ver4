/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.worktimeset;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;


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
	
	public Map<String, String> getCodeNameByListWorkTimeCd(String companyId, List<String> listWorkTimeCode);

	/**
	 * Find by codes.
	 *
	 * @param companyId the company id
	 * @param codes the codes
	 * @return the list
	 */
	public List<WorkTimeSetting> findByCodes(String companyId, List<String> codes);
	
	/**
	 * Find by codes with No master
	 *
	 * @param companyId the company id
	 * @param codes the codes
	 * @return the list
	 */
	public List<WorkTimeSetting> findByCodesWithNoMaster(String companyID, List<String> codes);

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
	 * @param companyId the company id
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
	
	/**
	 * Find active items.
	 *
	 * @param companyId the company id
	 * @param displayAtr the display atr
	 * @param abolishAtr the abolish atr
	 * @return the list
	 */
	public List<WorkTimeSetting> findActiveItems(String companyId);
	
	/**
	 * Find by code and abolish condition.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 * @param abolishAtr the abolish atr
	 * @return the optional
	 */
	public Optional<WorkTimeSetting> findByCodeAndAbolishCondition(String companyId, String workTimeCode,AbolishAtr abolishAtr);
	
}
