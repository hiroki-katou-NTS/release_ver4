/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.classification;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.app.find.classification.dto.ClassificationFindDto;
import nts.uk.ctx.bs.employee.dom.classification.Classification;
import nts.uk.ctx.bs.employee.dom.classification.ClassificationRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class ClassificationFinder.
 */
@Stateless
public class ClassificationFinder {
	
	/** The repository. */
	@Inject
	private ClassificationRepository repository;
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<ClassificationFindDto> findAll(){
		
		// get login info
		LoginUserContext loginUserContext = AppContexts.user();
		
		// get company id
		String companyId = loginUserContext.companyId();

		// get all management category
		List<Classification> managementCategories = this.repository
			.getAllManagementCategory(companyId);
		
		// to domain
		return managementCategories.stream().map(category -> {
			ClassificationFindDto dto = new ClassificationFindDto();
			category.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}
}
