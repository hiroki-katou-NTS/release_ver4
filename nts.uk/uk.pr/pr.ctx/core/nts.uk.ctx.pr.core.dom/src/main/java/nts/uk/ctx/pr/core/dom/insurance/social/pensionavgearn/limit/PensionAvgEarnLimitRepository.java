/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit;

import java.util.List;

/**
 * The Interface PensionAvgEarnLimitRepository.
 */
public interface PensionAvgEarnLimitRepository {

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<PensionAvgEarnLimit> findAll(String companyCode);

}
