/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.employment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.app.find.employment.dto.EmploymentDto;
import nts.uk.ctx.bs.employee.app.find.employment.dto.EmploymentFindDto;
import nts.uk.ctx.bs.employee.dom.employment.Employment;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class DefaultEmploymentFinder.
 */
@Stateless
public class DefaultEmploymentFinder implements EmploymentFinder {

	/** The repository. */
	@Inject
	private EmploymentRepository repository;
	
	/* (non-Javadoc)
	 * @see nts.uk.shr.find.employment.EmploymentFinder#findAll()
	 */
	public List<EmploymentDto> findAll() {

		// Get Login User Info
		LoginUserContext loginUserContext = AppContexts.user();

		// Get Company Id
		String companyId = loginUserContext.companyId();

		// Get All Employment
		List<Employment> empList = this.repository.findAll(companyId);

		// Save to Memento
		return empList.stream().map(employment -> {
			EmploymentDto dto = new EmploymentDto();
			dto.setCode(employment.getEmploymentCode().v());
			dto.setName(employment.getEmploymentName().v());
			return dto;
		}).collect(Collectors.toList());
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.shr.find.employment.EmploymentFinder#findByCode(java.lang.String)
	 */
	@Override
	public EmploymentFindDto findByCode(String employmentCode) {
		String companyId = AppContexts.user().companyId();
		EmploymentFindDto dto = new EmploymentFindDto();
		Optional<Employment> employment = this.repository.findEmployment(companyId, employmentCode);
		if (!employment.isPresent()) {
			return null;
		}
		dto.setCode(employmentCode);
		dto.setName(employment.get().getEmploymentName().v());
		dto.setEmpExternalCode(employment.get().getEmpExternalCode());
		dto.setMemo(employment.get().getMemo());
		return dto;
	}
}
