package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.ClassificationAllotSettingHeaderRepository;

@Stateless
public class ClassificationAllotSettingHeaderFinder {

	@Inject
	private ClassificationAllotSettingHeaderRepository classificationAllotSettingHeaderRespository;
	
	public List<ClassificationAllotSettingHeaderDto>getAllClassificationAllotSettingHeader(String companyCode) {
		return this.classificationAllotSettingHeaderRespository.findAll(companyCode).stream()
				.map(m -> ClassificationAllotSettingHeaderDto.fromDomain(m))
				.collect(Collectors.toList());
		
	}
	
	/*public List<ClassificationAllotSettingHeaderDto>getHistory (String companyCode ,String historyId) {
		return this.classificationAllotSettingHeaderRespository.findbyHistoryId(companyCode, historyId).stream()
				.map( m -> ClassificationAllotSettingHeaderDto.fromDomain(m))
				.collect(Collectors.toList());
																
	}*/
	
}
