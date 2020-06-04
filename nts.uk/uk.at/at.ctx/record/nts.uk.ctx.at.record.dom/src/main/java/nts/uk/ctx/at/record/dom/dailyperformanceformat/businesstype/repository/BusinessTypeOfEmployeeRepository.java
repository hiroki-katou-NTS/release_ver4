package nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployee;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface BusinessTypeOfEmployeeRepository {
	/**
	 * find all by list businessTypeCodes
	 * 
	 * @param businessTypeCodes
	 * @return List BusinessTypeOfEmployee
	 */
	List<BusinessTypeOfEmployee> findAllByListCode(List<String> businessTypeCodes);

	/**
	 * insert BusinessTypeOfEmployee
	 * 
	 * @param businessTypeOfEmployee
	 */
//	void insert(BusinessTypeOfEmployee businessTypeOfEmployee);

	/**
	 * update BusinessTypeOfEmployee
	 * 
	 * @param businessTypeOfEmployee
	 */
	void update(BusinessTypeOfEmployee businessTypeOfEmployee);

	/**
	 * delete BusinessTypeOfEmployee
	 * 
	 * @param businessTypeOfEmployee
	 */
	void delete(String historyId);
	/**
	 * find by HistoryID
	 * @param historyId
	 * @return BusinessTypeOfEmployee
	 */
	Optional<BusinessTypeOfEmployee> findByHistoryId(String historyId);
	
	/**
	 * find all by list employeeId, date
	 * 
	 * @param businessTypeCodes
	 * @return List BusinessTypeOfEmployee
	 */
	List<BusinessTypeOfEmployee> findAllByEmpAndDate(List<String> employeeIds, DatePeriod date);
}
