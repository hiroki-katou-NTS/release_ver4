/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.limit.find;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimitRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class HealthAvgEarnLimitFinder.
 */
@Stateless
public class HealthAvgEarnLimitFinder {

	/** The repository. */
	@Inject
	private HealthAvgEarnLimitRepository repository;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<HealthAvgEarnLimitDto> findAll() {
		// Get the current company code.
		String companyCode = AppContexts.user().companyCode();

		// Get list HealthAvgEarnLimit.
		return repository.findAll(companyCode).stream()
				.map(HealthAvgEarnLimitDto::fromDomain).collect(Collectors.toList());
	}
}
