package nts.uk.ctx.bs.employee.dom.employment.history;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface EmploymentHistoryRepository {
	/**
	 * Get employment history by employee id
	 * @param sid
	 * @return
	 */
	Optional<EmploymentHistory> getByEmployeeId(String cid, String sid);
	
	/**
	 * Get employment history by employee id with descending
	 * @param cid
	 * @param sid
	 * @return
	 */
	Optional<EmploymentHistory> getByEmployeeIdDesc(String cid, String sid);
	
	/**
	 * get with employeeId
	 * startDate <= standardDate <= endDate 
	 * @param employeeId
	 * @param standardDate
	 * @return
	 */
	Optional<DateHistoryItem> getByEmployeeIdAndStandardDate(String employeeId, GeneralDate standardDate);
	
	/**
	 * get with employeeId
	 * startDate <= standardDate <= endDate 
	 * @param employeeId
	 * @param standardDate
	 * @return
	 */
	List<DateHistoryItem> getByEmployeeId(String employeeId);
	
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
	
	// query from RequetsList 264
	List<EmploymentHistory> getByListSid(List<String> employeeIds  ,  DatePeriod datePeriod);
	
	
	/**
	 * @author lanlt
	 * getEmploymentHistoryItem
	 * @param historyId
	 * @param employmentCode
	 * @return
	 */
	Optional<EmploymentHistory> getEmploymentHistory(String historyId, String employmentCode);
	/**
	 * @author hoatt
	 * get with employeeId
	 * startDate <= standardDate <= endDate 
	 * @param employeeId
	 * @param standardDate
	 * @return
	 */
	Map<String, DateHistItem> getBySIdAndate(List<String> lstSID, GeneralDate standardDate);
}
