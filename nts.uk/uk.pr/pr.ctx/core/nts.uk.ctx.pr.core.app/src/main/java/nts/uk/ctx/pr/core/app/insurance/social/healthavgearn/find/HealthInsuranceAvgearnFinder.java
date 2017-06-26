/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnRepository;

/**
 * The Class HealthInsuranceAvgearnFinder.
 */
@Stateless
public class HealthInsuranceAvgearnFinder {

	/** The repository. */
	@Inject
	private HealthInsuranceAvgearnRepository repository;

	/**
	 * Find.
	 *
	 * @param id
	 *            the id
	 * @return the list health insurance avgearn dto
	 */
	public HealthInsAvgearnsModel find(String id) {
		List<HealthInsuranceAvgearn> listDomain = repository.findById(id);

		// Map to list Dto.
		List<HealthInsuranceAvgearnDto> listDto = listDomain.stream().map(domain -> {
			HealthInsuranceAvgearnDto dto = new HealthInsuranceAvgearnDto();
			return dto.fromDomain(domain);
		}).collect(Collectors.toList());
		
		HealthInsAvgearnsModel listHealthInsuranceAvgearnDto = HealthInsAvgearnsModel
				.builder().listHealthInsuranceAvgearnDto(listDto).historyId(id).build();
		
		return listHealthInsuranceAvgearnDto;
	}
}
