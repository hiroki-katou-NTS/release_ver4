/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class PensionRateFinder.
 */
@Stateless
public class PensionRateFinder {

	/** The pension rate repo. */
	@Inject
	private PensionRateRepository pensionRateRepo;

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
	public Optional<PensionRateDto> find(String id) {
		Optional<PensionRate> pensionRate = this.pensionRateRepo.findById(id);

		PensionRateDto dto = PensionRateDto.builder().build();

		if (pensionRate.isPresent()) {
			pensionRate.get().saveToMemento(dto);
		}

		return Optional.ofNullable(dto);
	}

	/**
	 * Find all history.
	 *
	 * @return the list
	 */
	public List<PensionOfficeItemDto> findAllHistory() {
		String companyCode = AppContexts.user().companyCode();

		List<SocialInsuranceOffice> listOffice = socialInsuranceOfficeRepository
				.findAll(companyCode);

		List<PensionRate> listHealth = pensionRateRepo.findAll(companyCode);

		// group health same office code
		Map<OfficeCode, List<PensionHistoryItemDto>> historyMap = listHealth.stream().collect(
				Collectors.groupingBy(PensionRate::getOfficeCode, Collectors.mapping((res) -> {
					return new PensionHistoryItemDto(res.getHistoryId(),
							res.getApplyRange().getStartMonth().v().toString(),
							res.getApplyRange().getEndMonth().v().toString());
				}, Collectors.toList())));

		return listOffice
				.stream().map(item -> new PensionOfficeItemDto(item.getCode().v(),
						item.getName().v(), historyMap.get(item.getCode())))
				.collect(Collectors.toList());

	}
}
