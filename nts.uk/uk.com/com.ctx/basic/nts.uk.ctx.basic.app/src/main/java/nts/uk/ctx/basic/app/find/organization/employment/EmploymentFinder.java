package nts.uk.ctx.basic.app.find.organization.employment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.organization.employment.EmploymentRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class EmploymentFinder {
	@Inject
	private EmploymentRepository repository;

	
	
	/**
	 * find all employments by company code
	 * @param companyCode
	 * @return
	 */
	public List<EmploymentDto> getAllEmployment(){
		String companyCode = AppContexts.user().companyCode();
		return this.repository.findAllEmployment(companyCode)
				.stream().map(employ -> EmploymentDto.fromDomain(employ)).collect(Collectors.toList());
	}
	/**
	 * find employment by company code and employment code
	 * @param companyCode
	 * @param employmentCode
	 * @return
	 */
	public Optional<EmploymentDto> getEmployment(String employmentCode){
		String companyCode = AppContexts.user().companyCode();
		return this.repository.findEmployment(companyCode, employmentCode).map(employ -> EmploymentDto.fromDomain(employ));		
	}
	/**
	 * find employment by company code and display flag
	 * @param companyCode
	 * @param displayFlg
	 * @return
	 */
	public Optional<EmploymentDto> findEmpByDisplayFlg(){
		String companyCode = AppContexts.user().companyCode();
		return this.repository.findEmploymnetByDisplayFlg(companyCode).map(employ -> EmploymentDto.fromDomain(employ));
	}
	
	
} 
