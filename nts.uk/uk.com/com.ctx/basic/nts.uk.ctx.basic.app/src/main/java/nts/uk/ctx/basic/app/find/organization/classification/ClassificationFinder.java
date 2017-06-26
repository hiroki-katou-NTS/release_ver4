package nts.uk.ctx.basic.app.find.organization.classification;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.organization.classification.Classification;
import nts.uk.ctx.basic.dom.organization.classification.ClassificationRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ClassificationFinder {

	@Inject
	private ClassificationRepository classificationRepository;

	/**
	 * get All Classification
	 * @return List<ClassificationDto>
	 */
	public List<ClassificationDto> getAllClassification() {
		String companyCode = AppContexts.user().companyCode();
		return classificationRepository.findAll(companyCode).stream().map(e -> 
			ClassificationDto.convertToDto(e)
		).collect(Collectors.toList());
	}
}
