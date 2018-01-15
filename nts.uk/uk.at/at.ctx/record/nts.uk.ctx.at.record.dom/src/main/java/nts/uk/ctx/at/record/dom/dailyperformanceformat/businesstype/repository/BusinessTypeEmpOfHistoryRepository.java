package nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;

public interface BusinessTypeEmpOfHistoryRepository {
	/**
	 * find all
	 * @param cId companyId
	 * @param sId employeeId
	 * @return BusinessTypeOfEmployeeHistory
	 */
	BusinessTypeOfEmployeeHistory findAll(String cId, String sId);

	/**
	 * insert BusinessTypeOfEmployeeHistory
	 * 
	 * @param domain:
	 *            BusinessTypeOfEmployeeHistory
	 */
	void add(String companyId,String employeeId,String historyId, GeneralDate startDate,GeneralDate endDate);

	/**
	 * update BusinessTypeOfEmployeeHistory
	 * 
	 * @param domain:
	 *            BusinessTypeOfEmployeeHistory
	 */
	void update(String companyId,String employeeId,String historyId, GeneralDate startDate,GeneralDate endDate);

	/**
	 * delete BusinessTypeOfEmployeeHistory
	 * 
	 * @param domain:
	 *            BusinessTypeOfEmployeeHistory
	 */
	void delete(String historyId);
	/**
	 * find by base date and employeeId
	 * @param baseDate
	 * @param sId employeeId
	 * @return BusinessTypeOfEmployeeHistory
	 */
	Optional<BusinessTypeOfEmployeeHistory> findByBaseDate(GeneralDate baseDate,String sId);
	/**
	 * find by employeeId
	 * @param sId employeeId
	 * @return BusinessTypeOfEmployeeHistory
	 */
	Optional<BusinessTypeOfEmployeeHistory> findByEmployee(String cid, String sId);
	
	/**
	 * find by employeeId
	 * @param sId employeeId
	 * @return BusinessTypeOfEmployeeHistory
	 */
	Optional<BusinessTypeOfEmployeeHistory> findByEmployeeDesc(String cid, String sId);
	
	/**
	 * find by historyId
	 * @param historyId
	 * @return BusinessTypeOfEmployeeHistory
	 */
	Optional<BusinessTypeOfEmployeeHistory> findByHistoryId(String historyId);

}
