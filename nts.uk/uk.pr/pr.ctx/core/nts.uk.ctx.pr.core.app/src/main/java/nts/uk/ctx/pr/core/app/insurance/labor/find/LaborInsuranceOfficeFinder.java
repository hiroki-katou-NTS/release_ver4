/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.find;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.insurance.labor.find.dto.LaborInsuranceOfficeFindDto;
import nts.uk.ctx.pr.core.app.insurance.labor.find.dto.LaborInsuranceOfficeFindOutDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class LaborInsuranceOfficeFinder.
 */
@Stateless
public class LaborInsuranceOfficeFinder {

	/** The labor insurance office repository. */
	@Inject
	private LaborInsuranceOfficeRepository repository;

	/**
	 * Find by id.
	 *
	 * @param officeCode
	 *            the office code
	 * @return the labor insurance office find dto
	 */
	public LaborInsuranceOfficeFindDto findById(String officeCode) {

		// get login user info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companycode by user login
		String companyCode = loginUserContext.companyCode();

		// to Dto
		LaborInsuranceOfficeFindDto dataOuput = new LaborInsuranceOfficeFindDto();

		// call service find Id
		Optional<LaborInsuranceOffice> data = this.repository.findById(companyCode, officeCode);

		// value exist
		if (data.isPresent()) {
			data.get().saveToMemento(dataOuput);
			return dataOuput;
		}

		return null;
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<LaborInsuranceOfficeFindOutDto> findAll() {

		// get login user info
		LoginUserContext loginUserContext = AppContexts.user();

		// get company code by user login
		String companyCode = loginUserContext.companyCode();

		// find data
		List<LaborInsuranceOffice> data = this.repository.findAll(companyCode);

		// to Dto
		List<LaborInsuranceOfficeFindOutDto> dataOutput = data.stream().map(office -> {
			LaborInsuranceOfficeFindOutDto dto = new LaborInsuranceOfficeFindOutDto();
			dto.setCode(office.getCode().v());
			dto.setName(office.getName().v());
			return dto;
		}).collect(Collectors.toList());

		return dataOutput;
	}

}
