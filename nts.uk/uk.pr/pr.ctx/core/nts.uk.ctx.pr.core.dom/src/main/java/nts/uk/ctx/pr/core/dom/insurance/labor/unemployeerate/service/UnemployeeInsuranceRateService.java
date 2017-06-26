/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.service;

import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;

/**
 * The Interface UnemployeeInsuranceRateService.
 */
public interface UnemployeeInsuranceRateService {

	/**
	 * Validate required item.
	 *
	 * @param rate the rate
	 */
	void validateRequiredItem(UnemployeeInsuranceRate rate);

	/**
	 * Validate date range.
	 *
	 * @param rate the rate
	 */
	void validateDateRange(UnemployeeInsuranceRate rate);

	/**
	 * Validate date range update.
	 *
	 * @param rate the rate
	 */
	void validateDateRangeUpdate(UnemployeeInsuranceRate rate);
}
