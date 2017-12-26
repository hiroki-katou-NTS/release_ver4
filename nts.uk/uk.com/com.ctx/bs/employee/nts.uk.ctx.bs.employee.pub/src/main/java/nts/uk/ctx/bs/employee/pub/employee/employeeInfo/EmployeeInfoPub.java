/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pub.employee.employeeInfo;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

/**
 * The Interface EmployeePub.
 */
public interface EmployeeInfoPub {

	/**
	 * Find Employee by companyId,employeeCode For request No.18
	 *
	 */
	Optional<EmployeeInfoDtoExport> getEmployeeInfo(String companyId, String employeeCode);

	/**
	 * Get List Employee by companyId,baseDate For request No.60
	 *
	 */

	List<EmployeeInfoDtoExport> getEmployeesAtWorkByBaseDate(String companyId, GeneralDate baseDate);

	/**
	 * Get List Employee Infomation For request No.126 param : sid : employeeId
	 *
	 */
	//List<EmpBasicInfoExport> getListEmpBasicInfo(List<String> sid);

	/**
	 * Get Employee Info By Pid.
	 * Requets List No.124
	 * @param pid
	 * @return
	 */
	List<EmpInfoExport> getEmpInfoByPid(String pid);
	
	
	/**
	 * Find Employee by companyId,Pid
	 * RequestList No. 
	 */
	Optional<EmployeeInfoDto> getEmployeeInfoByCidPid(String companyId, String personId);

}
