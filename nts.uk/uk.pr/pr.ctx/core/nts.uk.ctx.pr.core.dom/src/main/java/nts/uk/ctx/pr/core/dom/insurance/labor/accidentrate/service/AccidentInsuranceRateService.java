/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.service;

import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;

/**
 * The Interface LaborInsuranceOfficeService.
 */
public interface AccidentInsuranceRateService {

	/**
	 * Validate required item.
	 *
	 * @param office the office
	 */
	void validateRequiredItem(AccidentInsuranceRate rate);

	/**
	 * Check duplicate code.
	 *
	 * @param office the office
	 */
	void validateDateRange(AccidentInsuranceRate rate);

	/**
	 * Validate date range update.
	 *
	 * @param rate the rate
	 */
	void validateDateRangeUpdate(AccidentInsuranceRate rate);

}
