/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.adapter.syworkplace;

import java.util.List;

import nts.arc.time.GeneralDate;

/**
 * The Interface SyWorkplaceAdapter.
 */
public interface GwSyWorkplaceAdapter {

	/**
	 * Find by sid.
	 *
	 * @param companyId the company id
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	List<SWkpHistImport> findBySid(String companyId, String employeeId, GeneralDate baseDate);
}
