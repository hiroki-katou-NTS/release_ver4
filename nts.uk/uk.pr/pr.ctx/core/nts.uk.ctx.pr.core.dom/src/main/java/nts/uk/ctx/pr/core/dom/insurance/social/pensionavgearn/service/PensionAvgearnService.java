/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.service;

import java.util.List;

import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate;

/**
 * The Interface PensionAvgearnService.
 */
public interface PensionAvgearnService {

	/**
	 * Validate required item.
	 *
	 * @param pensionAvgearn the pension avgearn
	 */
	void validateRequiredItem(PensionAvgearn pensionAvgearn);

	/**
	 * Calculate list pension avgearn.
	 *
	 * @param pensionRate the pension rate
	 * @return the list
	 */
	List<PensionAvgearn> calculateListPensionAvgearn(PensionRate pensionRate);

}
