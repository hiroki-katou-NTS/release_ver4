/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.find;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.find.dto.AccidentInsuranceRateFindDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateRepository;

/**
 * The Class HistoryAccidentInsuranceRateF‌inder.
 */
@Stateless
public class AccidentInsuranceRateFinder {

	/** The accident insurance rate repository. */
	@Inject
	private AccidentInsuranceRateRepository repository;

	/**
	 * Find.
	 *
	 * @param historyAccidentInsuranceRateFindInDto
	 *            the history accident insurance rate find in dto
	 * @return the history accident insurance rate dto
	 */
	public AccidentInsuranceRateFindDto find(String historyId) {

		// call repository finder
		AccidentInsuranceRateFindDto dto = new AccidentInsuranceRateFindDto();

		// find by id
		Optional<AccidentInsuranceRate> data = this.repository.findById(historyId);

		// exist data finder
		if (data.isPresent()) {
			data.get().saveToMemento(dto);
		}

		return dto;
	}

}
