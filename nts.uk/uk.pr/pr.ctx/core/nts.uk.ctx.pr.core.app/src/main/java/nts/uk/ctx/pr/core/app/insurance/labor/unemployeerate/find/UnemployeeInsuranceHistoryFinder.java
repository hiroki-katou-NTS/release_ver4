/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find.dto.UnemployeeInsuranceHistoryFindDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class UnemployeeInsuranceHistoryFinder.
 */
@Stateless
public class UnemployeeInsuranceHistoryFinder {

	/** The repository. */
	@Inject
	private UnemployeeInsuranceRateRepository repository;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<UnemployeeInsuranceHistoryFindDto> findAll() {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// call finder
		List<UnemployeeInsuranceRate> data = this.repository.findAll(companyCode);

		// save to data object
		List<UnemployeeInsuranceHistoryFindDto> dataOutput = data.stream().map(insuranceRate -> {
			UnemployeeInsuranceHistoryFindDto dto = new UnemployeeInsuranceHistoryFindDto();
			insuranceRate.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());

		return dataOutput;
	}

	/**
	 * Find.
	 *
	 * @param historyId the history id
	 * @return the unemployee insurance history find dto
	 */
	public UnemployeeInsuranceHistoryFindDto find(String historyId) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// call finder
		Optional<UnemployeeInsuranceRate> data = this.repository.findById(companyCode, historyId);
		UnemployeeInsuranceHistoryFindDto dto = new UnemployeeInsuranceHistoryFindDto();

		// not exist value
		if (!data.isPresent()) {
			return null;
		}

		// save data object
		data.get().saveToMemento(dto);
		return dto;

	}

}
