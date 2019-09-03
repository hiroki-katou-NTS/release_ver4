/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.model.employee;

import java.util.List;

import nts.arc.time.GeneralDate;

/**
 * The Interface EmployeeAuthAdapter.
 */
public interface EmployeeAuthAdapter {
	
	/**
	 * Narrow emp list by reference range.
	 *
	 * @param sIds the s ids
	 * @param roleType the role type
	 * @return the list
	 */
	public List<String> narrowEmpListByReferenceRange(List<String> sIds, Integer roleType, GeneralDate referenceDate);
}
