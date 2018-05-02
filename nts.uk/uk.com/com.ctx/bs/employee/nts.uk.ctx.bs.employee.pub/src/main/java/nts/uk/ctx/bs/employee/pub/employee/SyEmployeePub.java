/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pub.employee;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

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
	
}
