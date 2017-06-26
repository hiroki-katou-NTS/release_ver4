/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.office.find;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;

/**
 * The Class SocialInsuranceOfficeFinder.
 */
@Stateless
public class SocialInsuranceOfficeFinder {

	/** The social insurance office repository. */
	@Inject
	private SocialInsuranceOfficeRepository socialInsuranceOfficeRepo;

	/**
	 * Find.
	 *
	 * @param officeCode
	 *            the office code
	 * @return the optional
	 */
	public Optional<SocialInsuranceOfficeDto> find(String companyCode, OfficeCode officeCode) {
		Optional<SocialInsuranceOffice> socialInsuranceOffice = socialInsuranceOfficeRepo
				.findByOfficeCode(companyCode, officeCode);
		SocialInsuranceOfficeDto dto = SocialInsuranceOfficeDto.builder().build();
		if (socialInsuranceOffice.isPresent()) {
			socialInsuranceOffice.get().saveToMemento(dto);
		}
		return Optional.ofNullable(dto);
	}

	/**
	 * Find all.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the list
	 */
	public List<SocialInsuranceOfficeItemDto> findAll(String companyCode) {
		List<SocialInsuranceOffice> lstSocialInsuranceOffice = socialInsuranceOfficeRepo.findAll(companyCode);
		return lstSocialInsuranceOffice.stream()
				.map(domain -> {
					SocialInsuranceOfficeItemDto dto = SocialInsuranceOfficeItemDto.builder()
							.build();
					domain.saveToMemento(dto);
					return dto;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Find all detail.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the list
	 */
	public List<SocialInsuranceOfficeDto> findAllDetail(String companyCode) {
		List<SocialInsuranceOffice> lstSocialInsuranceOffice = socialInsuranceOfficeRepo.findAll(companyCode);
		return lstSocialInsuranceOffice.stream()
				.map(domain -> {
					SocialInsuranceOfficeDto dto = SocialInsuranceOfficeDto.builder()
							.build();
					domain.saveToMemento(dto);
					return dto;
				}).collect(Collectors.toList());
	}
}
