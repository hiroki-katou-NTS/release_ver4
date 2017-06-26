package nts.uk.ctx.pr.core.app.find.retirement.payitem;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.retirement.payitem.dto.RetirementPayItemDto;
import nts.uk.ctx.pr.core.dom.retirement.payitem.RetirementPayItemRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class RetirementPayItemFinder {
	@Inject
	private RetirementPayItemRepository retirementPayItemRepository;
	
	/**
	 * find retirement payment item by company code 
	 * @return list retirement payment item by company code
	 */
	public List<RetirementPayItemDto> findByCompanyCode(){
		String companyCode = AppContexts.user().companyCode() ;
		return this.retirementPayItemRepository.findByCompanyCode(companyCode)
				.stream()
				.map(x -> new RetirementPayItemDto(
						x.getCompanyCode(),
						x.getCategory().value,
						x.getItemCode(),
						x.getItemName(),
						x.getPrintName().v(),
						x.getEnglishName(),
						x.getFullName(),
						x.getMemo().v()))
				.collect(Collectors.toList());
	}
}
