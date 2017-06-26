/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.service;

import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate;

/**
 * The Interface UnemployeeInsuranceRateService.
 */
public abstract class PensionRateService extends SimpleHistoryBaseService<SocialInsuranceOffice, PensionRate> {

	/**
	 * Validate required item.
	 *
	 * @param rate the rate
	 */
	public abstract void validateRequiredItem(PensionRate rate);
}
