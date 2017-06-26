package nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.input;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input.PersonResiTaxRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
@RequestScoped
public class PersonResiTaxFinder {
	@Inject
	private PersonResiTaxRepository personResiTaxRepository;
	
	public List<PersonResiTaxDto> findAll(int yearKey){
		LoginUserContext login =  AppContexts.user();
		List<PersonResiTaxDto> result = this.personResiTaxRepository.findAll(login.companyCode(), login.personId(), yearKey)
				.stream().map(x -> {return PersonResiTaxDto.fromDomain(x);})
				.collect(Collectors.toList());
		return result;
	}
	
	//SEL_5
	public List<String> findByResidenceCode(String residenceCode,int yearKey){
		LoginUserContext login =  AppContexts.user();
		List<?> result = this.personResiTaxRepository.findByResidenceCode(login.companyCode(), residenceCode, yearKey)
				.stream()
				.collect(Collectors.toList());
		List<String> lstPersonId= new ArrayList<String>();
		result.forEach(item->lstPersonId.add(item.toString()));
		return lstPersonId;
	}
}
