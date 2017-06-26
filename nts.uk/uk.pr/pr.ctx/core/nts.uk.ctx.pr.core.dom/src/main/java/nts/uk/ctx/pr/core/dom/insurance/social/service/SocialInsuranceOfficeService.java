/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.service;

import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;

/**
 * The Interface SocialInsuranceOfficeService.
 */
public interface SocialInsuranceOfficeService {

	/**
	 * Validate required item.
	 *
	 * @param office the office
	 */
	void validateRequiredItem(SocialInsuranceOffice office);

	/**
	 * Check duplicate code.
	 *
	 * @param office the office
	 */
	void checkDuplicateCode(SocialInsuranceOffice office);

}
