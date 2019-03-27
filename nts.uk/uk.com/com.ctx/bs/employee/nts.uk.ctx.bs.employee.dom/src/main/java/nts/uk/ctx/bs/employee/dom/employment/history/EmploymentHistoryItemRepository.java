package nts.uk.ctx.bs.employee.dom.employment.history;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentInfo;
import nts.uk.ctx.bs.employee.dom.employment.EmpmInfo;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface EmploymentHistoryItemRepository {
	
	
	Optional<EmploymentInfo> getDetailEmploymentHistoryItem(String companyId, String sid, GeneralDate date);
	
	/**
	 * get with historyId
	 * @param historyId
	 * @return
	 */
	Optional<EmploymentHistoryItem> getByHistoryId(String historyId);
	
	
	/**
	 * get with historyId
	 * @param historyId
	 * @return
	 */
	List<EmploymentHistoryItem> getByListHistoryId(List<String> historyIds);
	
	/**
	 * Add employment
	 * @param domain
	 */
	void add(EmploymentHistoryItem domain);
	
	/**
	 * Update employment
	 * @param domain
	 */
	void update(EmploymentHistoryItem domain);
	
	/**
	 * Delete employment
	 * @param domain
	 */
	void delete(String histId);
	
	/**
	 * Search employee.
	 *
	 * @param baseDate the base date
	 * @param employmentCodes the employment codes
	 * @return the list
	 */
	List<EmploymentHistoryItem> searchEmployee(GeneralDate baseDate, List<String> employmentCodes );

	/**
	 * Search employee.
	 *
	 * @param employeeIds the employee ids
	 * @param baseDate the base date
	 * @param employmentCodes the employment codes
	 * @return the list
	 */
	List<EmploymentHistoryItem> searchEmployee(GeneralDate baseDate, List<String> employeeIds, 
			 List<String> employmentCodes );

	/**
	 * Search employment of sids.
	 *
	 * @param employeeIds the employee ids
	 * @param baseDate the base date
	 * @return the list
	 */
	List<EmploymentHistoryItem> searchEmploymentOfSids(List<String> employeeIds,
			GeneralDate baseDate);
	
	/**
	 * 
	 * @param basedate
	 * @param employeeId
	 * @return
	 */
	List<EmploymentHistoryItem> getEmploymentByEmpIdAndDate(GeneralDate basedate, String employeeId);
	
	/**
	 * Get employment item by employee ID
	 * @param employeeId
	 * @return List EmploymentHistoryOfEmployee
	 */
	List<EmploymentHistoryOfEmployee> getEmploymentBySID(String employeeId);
	
	/**
	 * Search employee.
	 *
	 * @param baseDate the base date
	 * @param employmentCodes the employment codes
	 * @return the list
	 */
	//List<EmploymentHistoryItem> getListEmptByListCodeAndDatePeriod(DatePeriod datePeriod, List<String> employmentCodes );
	
	List<String> getLstSidByListCodeAndDatePeriod(DatePeriod datePeriod, List<String> employmentCodes );
	
	/**
	 * @author lanlt
	 * getEmploymentHistoryItem
	 * @param cid
	 * @param baseDate
	 * @return
	 */
	List<EmploymentHistoryItem> getEmploymentHistoryItem(String cid, GeneralDate baseDate);
	/**
	 * @author hoatt
	 * key: sid, value: EmploymentInfo
	 * @param companyId
	 * @param lstSID
	 * @param date
	 * @return
	 */
	Map<String, EmpmInfo> getLstDetailEmpHistItem(String companyId, List<String> lstSID, GeneralDate date);

	/**
	 * @author lanlt
	 * @param sids
	 * @param employmentCodes
	 * @param dateRange
	 * @return
	 */
	List<EmploymentHistoryOfEmployee> getEmploymentBySID(List<String> sids, List<String> employmentCodes, DatePeriod dateRange);
}
