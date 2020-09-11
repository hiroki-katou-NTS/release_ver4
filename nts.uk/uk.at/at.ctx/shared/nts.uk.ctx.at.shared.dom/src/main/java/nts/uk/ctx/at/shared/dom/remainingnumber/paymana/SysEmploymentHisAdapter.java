/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.remainingnumber.paymana;

import java.util.Optional;

import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;

public interface SysEmploymentHisAdapter {

	/**
	 * Find S job hist by sid.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	// RequestList31
	Optional<SEmpHistoryImport> findSEmpHistBySid(String companyId, String employeeId, GeneralDate baseDate);
	Optional<SEmpHistoryImport> findSEmpHistBySidRequire(CacheCarrier cacheCarrier, String companyId, String employeeId, GeneralDate baseDate);
	
}
