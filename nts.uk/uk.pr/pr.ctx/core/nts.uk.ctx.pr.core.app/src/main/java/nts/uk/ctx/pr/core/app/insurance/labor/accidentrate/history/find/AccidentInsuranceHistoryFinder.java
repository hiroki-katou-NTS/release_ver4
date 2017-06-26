/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.history.find;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.find.dto.AccidentInsuranceRateHistoryFindDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class AccidentInsuranceHistoryF‌inder.
 */
@Stateless
public class AccidentInsuranceHistoryFinder {

	/** The accident insurance rate repository. */
	@Inject
	private AccidentInsuranceRateRepository repository;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<AccidentInsuranceRateHistoryFindDto> findAll() {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// call repository find
		List<AccidentInsuranceRate> data = this.repository.findAll(companyCode);

		// set to output
		List<AccidentInsuranceRateHistoryFindDto> dataOutput = data.stream().map(history -> {
			AccidentInsuranceRateHistoryFindDto dto = new AccidentInsuranceRateHistoryFindDto();
			history.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());

		return dataOutput;
	}

	/**
	 * Find.
	 *
	 * @param historyId
	 *            the history id
	 * @return the accident insurance rate history find out dto
	 */
	public AccidentInsuranceRateHistoryFindDto find(String historyId) {

		// call repository find
		Optional<AccidentInsuranceRate> data = this.repository.findById(historyId);

		AccidentInsuranceRateHistoryFindDto dataOutput = new AccidentInsuranceRateHistoryFindDto();

		// check exist data
		if (data.isPresent()) {
			data.get().saveToMemento(dataOutput);
		}

		return dataOutput;
	}

}
