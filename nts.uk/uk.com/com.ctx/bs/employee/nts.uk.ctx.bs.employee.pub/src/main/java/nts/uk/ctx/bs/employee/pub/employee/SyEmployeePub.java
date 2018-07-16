/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pub.employee;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface EmployeePub.
 */
public interface SyEmployeePub {

	/**
	 * Find by wpk ids.
	 *
	 * @param companyId the company id
	 * @param workplaceIds the workplace ids
	 * @param baseDate the base date
	 * @return the list
	 */
	List<EmployeeExport> findByWpkIds(String companyId, List<String> workplaceIds, GeneralDate baseDate);

	/**
	 * Gets the concurrent employee.
	 *
	 * @param companyId the company id
	 * @param jobId the job id
	 * @param baseDate the base date
	 * @return the concurrent employee
	 */
	// RequestList77
	List<ConcurrentEmployeeExport> getConcurrentEmployee(String companyId, String jobId, GeneralDate baseDate);

	/**
	 * Find by emp id.
	 *
	 * @param sId the emp id
	 * @return the employee export
	 */
	// RequestList #1-2
	EmployeeBasicInfoExport findBySId(String sId);

	/**
	 * Find by emp ids.
	 * 社員IDのListを基に社員情報のListを返してほしいです。
	 * @param sIds the s ids
	 * @return the list
	 */
	// RequestList126
	List<EmployeeBasicInfoExport> findBySIds(List<String> sIds);
	
	
	/** Get list employees in the workplace by baseDate and employeeID
	 * @param sid
	 * @param baseDate
	 * @return
	 */
	//  RequestList #243
	List<String> GetListSid(String sid , GeneralDate baseDate);
	
	
	/**
	 * Get List EmployeeCode 
	 * @param sid
	 * @param basedate
	 * @return
	 */
	// RequestList #212
	List<String> getEmployeeCode(String sid , GeneralDate basedate);
	
	/**
	 * Gets the sdata mng info.
	 *
	 * @param sid the sid
	 * @return the sdata mng info
	 */
	// Redmine #87534
	Optional<EmployeeDataMngInfoExport> getSdataMngInfo(String sid);
	
	/**
	 * Find by emp ids.
	 * 社員ID（List）から社員コードと表示名を取得
	 * @param sIds the sids
	 * @return the list
	 */
	// RequestList228
	List<EmployeeInfoExport> getByListSid(List<String> sIds);
	
	
	/**
	 * Get List EmployeeId By WorkPlace and Employment and TemporaryLeaveAbsenceHistory
	 * @param sIds
	 * @return
	 */
	// RequestList335
	List<String> getListEmpByWkpAndEmpt(List<String> wkps , List<String> lstempts , DatePeriod dateperiod);
	
	/**
	 * Get List EmployeeId By CompanyId
	 * @param sIds
	 * @return
	 */
	// RequestList52
	List<EmpOfLoginCompanyExport> getListEmpOfLoginCompany(String cid);
	
	/**
	 * Find by emp id.
	 *
	 * @param sId the emp id
	 * @return the employee export
	 */
	// RequestList377
	EmployeeBasicExport getEmpBasicBySId(String sId);
	
	/**
	 * RequestList 
	 * @param sid
	 * @return status of Employee
	 */
	StatusOfEmployeeExport getStatusOfEmployee(String sid);
	
	/**
	 * 社員が削除されたかを取得			
	 * RequestList248
	 * @param sid
	 * @return
	 */
	boolean isEmployeeDelete(String sid);
	
	/**
	 * @param lstSid
	 * @return
	 */
	// RequestList61
	List<EmpInfoExport> getEmpInfo(List<String> lstSid);
	
}
