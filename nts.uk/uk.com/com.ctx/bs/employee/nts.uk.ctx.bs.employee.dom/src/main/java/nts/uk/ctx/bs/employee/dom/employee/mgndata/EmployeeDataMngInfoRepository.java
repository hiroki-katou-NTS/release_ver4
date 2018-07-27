/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.employee.mgndata;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface EmployeeDataMngInfoRepository {
	void add(EmployeeDataMngInfo domain);

	void update(EmployeeDataMngInfo domain);

	void updateAfterRemove(EmployeeDataMngInfo domain);

	void remove(EmployeeDataMngInfo domain);

	void remove(String sid, String pId);

	EmployeeDataMngInfo findById(String sid, String pId);
	// Lanlt code start

	Optional<EmployeeInfo> findById(String sid);

	List<EmployeeSimpleInfo> findByIds(List<String> lstId);

	Optional<EmployeeInfo> getDepartment(String departmentId, GeneralDate date);

	// Lanlt code end

	List<EmployeeDataMngInfo> findByEmployeeId(String sid);

	List<EmployeeDataMngInfo> findByPersonId(String pid);

	List<EmployeeDataMngInfo> findByCompanyId(String cid);
	
	List<Object[]> findByCompanyIdAndBaseDate(String cid, GeneralDate baseDate);

	Optional<EmployeeDataMngInfo> findByEmpId(String sId);

	// sonnlb code start

	List<EmployeeDataMngInfo> getEmployeeNotDeleteInCompany(String cId, String sCd);

	// sonnlb code end

	void updateRemoveReason(EmployeeDataMngInfo domain);

	List<EmployeeDataMngInfo> getListEmpToDelete(String cid);

	Optional<EmployeeDataMngInfo> findByEmployeCD(String empcode, String cid);

	// add by duongtv
	/**
	 * Find by list employee id.
	 *
	 * @param companyId
	 *            the company id
	 * @param employeeIds
	 *            the employee ids
	 * @return the list
	 */
	List<EmployeeDataMngInfo> findByListEmployeeId(String companyId, List<String> employeeIds);

	/**
	 * Find by list employee code.
	 *
	 * @param companyId
	 *            the company id
	 * @param employeeCodes
	 *            the employee codes
	 * @return the list
	 */
	List<EmployeeDataMngInfo> findByListEmployeeCode(String companyId, List<String> employeeCodes);

	/**
	 * Get List EmployeeDataMngInfo By List Sid
	 * 
	 * @param listSid
	 * @return
	 */
	List<EmployeeDataMngInfo> findByListEmployeeId(List<String> listSid);

	/**
	 * Get EmployeeDataMngInfo
	 * 
	 * @param cid
	 * @param pid
	 * @return
	 */
	Optional<EmployeeDataMngInfo> findByCidPid(String cid, String pid);

	/**
	 * Req No.125
	 * 
	 * @param cId
	 * @param sCd
	 * @return
	 */
	Optional<EmployeeDataMngInfo> getEmployeeByCidScd(String cId, String sCd);

	/**
	 * @param companyId
	 * @param startLetters
	 * @return return Optional<Value> if can get data Optional<empty> if it doesn't
	 *         match startLetters
	 */
	Optional<String> findLastEml(String companyId, String startLetters, int length);
	
	/**
	 * Get all Employee By CompanyId, Order by Scd ASC
	 * @param cid
	 * @return
	 */
	List<EmployeeDataMngInfo> getAllByCid(String cid);

}
