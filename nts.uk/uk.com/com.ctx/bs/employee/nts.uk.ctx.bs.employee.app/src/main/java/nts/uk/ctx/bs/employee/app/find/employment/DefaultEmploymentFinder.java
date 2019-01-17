/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.employment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.i18n.I18NText;
import nts.gul.collection.CollectionUtil;
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

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.app.find.employment.EmploymentFinder#findByCodes(java.util.List)
	 */
	@Override
	public List<EmploymentDto> findByCodes(List<String> empCodes) {
		// Get Company Id
		String companyId = AppContexts.user().companyId();
		
		// Get All Employment
		List<Employment> empList = this.repository.findByEmpCodes(companyId, empCodes);
		
		// Save to Memento
		return empList.stream().map(employment -> {
			EmploymentDto dto = new EmploymentDto();
			dto.setCode(employment.getEmploymentCode().v());
			dto.setName(employment.getEmploymentName().v());
			return dto;
		}).collect(Collectors.toList());
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.app.find.employment.EmploymentFinder#findByCodesWithNull(java.util.List)
	 */
	@Override
	public List<EmploymentDto> findByCodesWithNull(List<String> empCodes) {
		// Get Company Id
		
		List<EmploymentDto> result = new ArrayList<EmploymentDto>();
		
		String companyId = AppContexts.user().companyId();
		if(CollectionUtil.isEmpty(empCodes)){
			return result;
		}
		
		empCodes.forEach(code->{
			Optional<Employment> optEmp = this.repository.findEmployment(companyId, code);
			String itemName ;
			if(optEmp.isPresent()){
				itemName = optEmp.get().getEmploymentName().v();
			}else{
				itemName = code+ I18NText.getText("KMF004_163");
			}
			
			result.add(new EmploymentDto(code, itemName));
		});
		return result;
	}
	
	
}
