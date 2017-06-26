/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.service;

import java.math.BigDecimal;
import java.util.Set;

import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnValue;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRounding;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.InsuranceRateItem;

public interface HealthInsuranceAvgearnService {

	/**
	 * Validate required item.
	 *
	 * @param healthInsuranceAvgearn the health insurance avgearn
	 */
	void validateRequiredItem(HealthInsuranceAvgearn healthInsuranceAvgearn);

	/**
	 * Update health insurance rate avg earn.
	 *
	 * @param healthInsuranceRate the health insurance rate
	 */
	void updateHealthInsuranceRateAvgEarn(HealthInsuranceRate healthInsuranceRate);

	/**
	 * Calculate avgearn value.
	 *
	 * @param roundingMethods the rounding methods
	 * @param masterRate the master rate
	 * @param rateItems the rate items
	 * @param isPersonal the is personal
	 * @return the health insurance avgearn value
	 */
	HealthInsuranceAvgearnValue calculateAvgearnValue(
			Set<HealthInsuranceRounding> roundingMethods, BigDecimal masterRate,
			Set<InsuranceRateItem> rateItems, boolean isPersonal);
}
