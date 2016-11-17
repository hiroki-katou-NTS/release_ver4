package nts.uk.ctx.core.app.personalinfo.wage.find;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.uk.ctx.pr.proto.dom.personalinfo.wage.wagename.PersonalWageNameRepository;

@RequestScoped
public class PersonalWageNameFinder {
	@Inject
	private PersonalWageNameRepository repository;
	
	public List<PersonalWageNameDto> getPersonalWageNames(String companyCode, int categoryAtr){
		return this.repository.getPersonalWageName(companyCode, categoryAtr).stream()
				.map(item -> PersonalWageNameDto.fromDomain(item))
				.collect(Collectors.toList());
	}
}
