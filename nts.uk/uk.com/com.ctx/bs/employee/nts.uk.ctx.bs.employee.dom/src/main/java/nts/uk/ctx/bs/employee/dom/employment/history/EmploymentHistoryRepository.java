package nts.uk.ctx.bs.employee.dom.employment.history;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;

public interface EmploymentHistoryRepository {
	/**
	 * Get employment history by employee id
	 * @param sid
	 * @return
	 */
	Optional<EmploymentHistory> getByEmployeeId(String cid, String sid);
	
	/**
	 * get with employeeId
	 * startDate <= standardDate <= endDate 
	 * @param employeeId
	 * @param standardDate
	 * @return
	 */
	Optional<DateHistoryItem> getByEmployeeIdAndStandardDate(String employeeId, GeneralDate standardDate);
	
	/**
	 * get with historyId
	 * @param historyId
	 * @return
	 */
	Optional<DateHistoryItem> getByHistoryId(String historyId);
	
	/**
	 * Add employment history
	 * @param sid
	 * @param domain
	 */
	void add(String sid, DateHistoryItem domain);
	
	/**
	 * Update employment history
	 * @param itemToBeUpdated
	 */
	void update(DateHistoryItem itemToBeUpdated);
	
	/**
	 * Delete employment history
	 * @param histId
	 */
	void delete(String histId);
}
