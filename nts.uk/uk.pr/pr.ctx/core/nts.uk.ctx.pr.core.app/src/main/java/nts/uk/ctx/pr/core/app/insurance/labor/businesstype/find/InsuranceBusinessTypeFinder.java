/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.businesstype.find;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.insurance.labor.businesstype.find.dto.InsuranceBusinessTypeFindOutDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.InsuranceBusinessType;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.InsuranceBusinessTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class InsuranceBusinessTypeFinder.
 */
@Stateless
public class InsuranceBusinessTypeFinder {

	/** The insurance business type repository. */
	@Inject
	private InsuranceBusinessTypeRepository repository;

	/**
	 * Find all.
	 *
	 * @return the insurance business type find out dto
	 */
	public InsuranceBusinessTypeFindOutDto findAll() {

		// get user login
		LoginUserContext loginUserContext = AppContexts.user();

		// get company code by user login
		String companyCode = loginUserContext.companyCode();

		// find all data
		List<InsuranceBusinessType> data = this.repository.findAll(companyCode);

		// res data
		return InsuranceBusinessTypeFindOutDto.fromDomain(data);
	}
}
