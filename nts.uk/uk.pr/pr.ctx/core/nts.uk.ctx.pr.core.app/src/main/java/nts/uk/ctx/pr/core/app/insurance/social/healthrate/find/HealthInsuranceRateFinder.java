/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthrate.find;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class HealthInsuranceRateFinder.
 */
@Stateless
public class HealthInsuranceRateFinder {

	/** The health insurance rate repository. */
	@Inject
	private HealthInsuranceRateRepository healthInsuranceRateRepository;

	/** The social insurance office repository. */
	@Inject
	private SocialInsuranceOfficeRepository socialInsuranceOfficeRepository;

	/**
	 * Find.
	 *
	 * @param id
	 *            the id
	 * @return the optional
	 */
	public Optional<HealthInsuranceRateDto> find(String id) {
		Optional<HealthInsuranceRate> healthInsuranceRate = healthInsuranceRateRepository
				.findById(id);
		HealthInsuranceRateDto dto = HealthInsuranceRateDto.builder().build();
		if (healthInsuranceRate.isPresent()) {
			healthInsuranceRate.get().saveToMemento(dto);
		}
		return Optional.ofNullable(dto);
	}

	/**
	 * Find all history.
	 *
	 * @return the list
	 */
	public List<HealthInsuranceOfficeItemDto> findAllHistory() {

		String companyCode = AppContexts.user().companyCode();

		List<SocialInsuranceOffice> listOffice = socialInsuranceOfficeRepository
				.findAll(companyCode);

		List<HealthInsuranceRate> listHealth = healthInsuranceRateRepository.findAll(companyCode);

		// group health same office code
		Map<OfficeCode, List<HealthInsuranceHistoryItemDto>> historyMap = listHealth.stream()
				.collect(Collectors.groupingBy(HealthInsuranceRate::getOfficeCode,
						Collectors.mapping((res) -> {
							return new HealthInsuranceHistoryItemDto(res.getHistoryId(),
									res.getApplyRange().getStartMonth().v().toString(),
									res.getApplyRange().getEndMonth().v().toString());
						}, Collectors.toList())));

		return listOffice.stream()
				.map(item -> new HealthInsuranceOfficeItemDto(item.getCode().v(),
						item.getName().v(), historyMap.get(item.getCode())))
				.collect(Collectors.toList());
	}
}
