/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.adapter.employment;

import java.util.Optional;

import nts.arc.time.GeneralDate;

/**
 * The Interface SyEmploymentAdapter.
 */
public interface GwSyEmploymentAdapter {
	
	/**
	 * Find S emp hist by sid.
	 *
	 * @param companyId the company id
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	Optional<SEmpHistImport> findSEmpHistBySid(String companyId, String employeeId, GeneralDate baseDate);
}
