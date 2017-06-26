/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find.dto.UnemployeeInsuranceRateFindDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class UnemployeeInsuranceFinder.
 */
@Stateless
public class UnemployeeInsuranceFinder {

	/** The find. */
	@Inject
	private UnemployeeInsuranceRateRepository repository;

	/**
	 * Find by id.
	 *
	 * @param historyId
	 *            the history id
	 * @return the unemployee insurance rate find out dto
	 */
	public UnemployeeInsuranceRateFindDto findById(String historyId) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// call finder repository
		Optional<UnemployeeInsuranceRate> data = this.repository.findById(companyCode, historyId);

		// set output
		UnemployeeInsuranceRateFindDto dataOuput = new UnemployeeInsuranceRateFindDto();

		// exist value
		if (!data.isPresent()) {
			return null;
		}
		
		//res data
		data.get().saveToMemento(dataOuput);
		return dataOuput;
	}

}
