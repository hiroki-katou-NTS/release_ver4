/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthrate.service;

import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRate;

/**
 * The Interface UnemployeeInsuranceRateService.
 */
public abstract class HealthInsuranceRateService extends SimpleHistoryBaseService<SocialInsuranceOffice, HealthInsuranceRate>{

	/**
	 * Validate required item.
	 *
	 * @param rate the rate
	 */
	public abstract void validateRequiredItem(HealthInsuranceRate rate);
}
