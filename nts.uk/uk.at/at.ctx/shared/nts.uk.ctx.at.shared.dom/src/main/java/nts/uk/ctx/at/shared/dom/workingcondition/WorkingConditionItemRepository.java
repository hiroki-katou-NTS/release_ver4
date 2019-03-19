/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.workingcondition;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface WorkingConditionItemRepository.
 */
public interface WorkingConditionItemRepository {
	
	/**
	 * Gets the by list sid and monthly pattern not null.
	 *
	 * @param employeeIds the employee ids
	 * @param monthlyPatternCodes the monthly pattern codes
	 * @return the by list sid and monthly pattern not null
	 */
	List<WorkingConditionItem> getByListSidAndMonthlyPatternNotNull(List<String> employeeIds, List<String> monthlyPatternCodes);

	/**
	 * Gets the by history id.
	 *
	 * @param historyId the history id
	 * @return the by history id
	 */
	Optional<WorkingConditionItem> getByHistoryId(String historyId);

	/**
	 * Find working condition item by pers work cat.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	Optional<WorkingConditionItem> getBySidAndStandardDate(String employeeId, GeneralDate baseDate);

	/**
	 * Find working condition item by pers work cat.
	 *
	 * @param employeeIds the employees id
	 * @param baseDate the base date
	 * @return the optional
	 */
	List<WorkingConditionItemCustom> getBySidsAndStandardDate(List<String> employeeIds, GeneralDate baseDate);
	
	/**
	 * Gets the by sids and base date.
	 *
	 * @param sIds the s ids
	 * @param baseDate the base date
	 * @return the by sids and base date
	 */
	List<WorkingConditionItem> getBySidsAndDatePeriod(List<String> sIds, DatePeriod datePeriod);

	/**
	 * Find working condition item by pers work cat.
	 *
	 * @param employeeId the employee id
	 * @param datePeriod the date period
	 * @return the list
	 */
	List<WorkingConditionItem> getBySidAndPeriodOrderByStrD(String employeeId, DatePeriod datePeriod);
	
	/**
	 * Gets the by sid and hist id.
	 *
	 * @param employeeId the employee id
	 * @param historyId the history id
	 * @return the by sid and hist id
	 */
	Optional<WorkingConditionItem> getBySid(String employeeId);

	/**
	 * Adds the.
	 *
	 * @param item the item
	 */
	void add(WorkingConditionItem item);

	/**
	 * Update.
	 *
	 * @param item the item
	 */
	void update(WorkingConditionItem item);

	/**
	 * Delete.
	 *
	 * @param historyId the history id
	 */
	void delete(String historyId);
	
	/**
	 * Delete monthly pattern.
	 *
	 * @param historyId the history id
	 */
	void deleteMonthlyPattern(String historyId);
	
	/**
	 * Update monthly pattern.
	 *
	 * @param historyId the history id
	 * @param monthlyPattern the monthly pattern
	 */
	void updateMonthlyPattern(String historyId, MonthlyPatternCode monthlyPattern);

	/**
	 * Copy last monthly pattern setting.
	 *
	 * @param sourceSid the source sid
	 * @param destSid the dest sid
	 */
	boolean copyLastMonthlyPatternSetting(String sourceSid, List<String> destSid);
	
	/**
	 * Gets the by sid and hist id.
	 *
	 * @param employeeId the employee id
	 * @param histId the hist id
	 * @return the by sid and hist id
	 */
	Optional<WorkingConditionItem> getBySidAndHistId(String employeeId, String histId);
	
	/**
	 * Gets the list with period by sid and date period 
	 * @param employeeId
	 * @param datePeriod
	 * @param max maxdatePeriod
	 * @param min mindatePeriod
	 * @return the by sid ,hist id and data Period 
	 */
	WorkingConditionWithDataPeriod getBySidAndPeriodOrderByStrDWithDatePeriod(Map<String,DatePeriod> param,GeneralDate max,GeneralDate min);
		
	
	/**
	 * Update WorkingCondition
	 * trong trường hợp chia đôi category WorkingCondition
	 * @param item the item
	 */
	void updateWorkCond2(WorkingConditionItem item);

	/**
	 * Gets the list with period by sid and date period 
	 * @param map of employeeId and date
	 * @return the by sid ,generalDate and data 
	 */
	Map<String, Map<GeneralDate, WorkingConditionItem>> getBySidAndPeriod(Map<String, Set<GeneralDate>> params);
	
	/**
	 * Gets the last working cond item.
	 *
	 * @param employeeIds the employee ids
	 * @return the last working cond item
	 */
	List<WorkingConditionItem> getLastWorkingCondItem(List<String> employeeIds);
	
}
