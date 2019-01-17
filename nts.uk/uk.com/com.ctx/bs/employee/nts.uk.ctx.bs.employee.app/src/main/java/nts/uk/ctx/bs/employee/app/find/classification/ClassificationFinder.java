/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.classification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.i18n.I18NText;
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
	 * Find classification.
	 *
	 * @return the 
	 */
	public ClassificationFindDto findClassificationByCode(String code){
		
		ClassificationFindDto dto = new ClassificationFindDto();
		
		// get login info
		LoginUserContext loginUserContext = AppContexts.user();
		
		// get company id
		String companyId = loginUserContext.companyId();

		// get all management category
		Optional<Classification> optClassification = this.repository.findClassification(companyId, code);
		
		if (!optClassification.isPresent()) {
			dto.setName(code + I18NText.getText("KMF004_163"));
			return dto;
		}
		
		
		optClassification.get().saveToMemento(dto);
		
		return dto;
	}
	
	 /** Find all.
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
