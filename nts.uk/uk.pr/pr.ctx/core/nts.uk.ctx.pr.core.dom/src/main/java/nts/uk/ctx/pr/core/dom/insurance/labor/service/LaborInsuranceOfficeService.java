/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.service;

import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOffice;

/**
 * The Interface LaborInsuranceOfficeService.
 */
public interface LaborInsuranceOfficeService {

	/**
	 * Validate required item.
	 *
	 * @param office the office
	 */
	void validateRequiredItem(LaborInsuranceOffice office);

	/**
	 * Check duplicate code.
	 *
	 * @param office the office
	 */
	void checkDuplicateCode(LaborInsuranceOffice office);

}
