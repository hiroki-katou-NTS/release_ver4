/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit;

import java.util.List;

/**
 * The Interface HealthAvgEarnLimitRepository.
 */
public interface HealthAvgEarnLimitRepository {

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<HealthAvgEarnLimit> findAll(String companyCode);

}
