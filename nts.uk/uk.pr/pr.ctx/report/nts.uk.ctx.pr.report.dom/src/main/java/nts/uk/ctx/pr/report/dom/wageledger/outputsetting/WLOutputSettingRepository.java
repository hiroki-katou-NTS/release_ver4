/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubject;

/**
 * The Interface WLOutputSettingRepository.
 */
public interface WLOutputSettingRepository {
	
	
	/**
	 * Creates the.
	 *
	 * @param outputSetting the output setting
	 */
	void create(WLOutputSetting outputSetting);
	
	/**
	 * Update.
	 *
	 * @param outputSetting the output setting
	 */
	void update(WLOutputSetting outputSetting);
	
	/**
	 * Removes the.
	 *
	 * @param outputSetting the output setting
	 */
	void remove(String companyCode, WLOutputSettingCode code);
	
	/**
	 * Find.
	 *
	 * @param code the code
	 * @param companyCode the company code
	 * @return the WL output setting
	 */
	Optional<WLOutputSetting> findByCode(String companyCode, WLOutputSettingCode code);
	
	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<WLOutputSetting> findAll(String companyCode);
	
	/**
	 * Checks if is exist.
	 *
	 * @param code the code
	 * @return true, if is exist
	 */
	boolean isExist(String companyCode, WLOutputSettingCode code);
	
	/**
	 * Removes the aggregate item used.
	 *
	 * @param itemSubject the item subject
	 */
	void removeAggregateItemUsed(WLItemSubject itemSubject);
}
