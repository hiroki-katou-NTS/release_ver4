package nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository;

//import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
//import nts.uk.ctx.at.shared.dom.yearholidaygrant.service.Period;

public interface BusinessTypeEmpOfHistoryRepository {

	/**
	 * insert BusinessTypeOfEmployeeHistory
	 * Merge KRCMT_BUS_TYPE_HIST To KRCMT_BUS_TYPE_SYAIN  because response
	 * new Insert Method ↓
	 *       Class      : here
	 *       MethodName : addToMerge
	 */
//	void add(String companyId,String employeeId,String historyId, GeneralDate startDate,GeneralDate endDate);
	void addToMerge(String companyId, String employeeId, String historyId, GeneralDate startDate, GeneralDate endDate, String businessTypeCode);
	
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
