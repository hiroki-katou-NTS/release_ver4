/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.adapter.employee;

import java.util.List;

import nts.arc.time.GeneralDate;

/**
 * The Interface EmployeeInfoAdapter.
 */
public interface EmployeeInfoAdapter {
	
	/**
	 * Gets the employees at work by base date.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @return the employees at work by base date
	 */
	List<EmployeeInfoDtoImport> getEmployeesAtWorkByBaseDate(String companyId , GeneralDate baseDate);
}
