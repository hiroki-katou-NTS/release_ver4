package nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output.dto.ResimentTaxPaymentDataDto;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.ResidentTaxPaymentData;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.ResidentTaxPaymentDataRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author phongtq
 *
 */
@Stateless
public class ResimentTaxPaymentDataFinder {
	@Inject
	private ResidentTaxPaymentDataRepository repository;
	
	/**
	 * Find Resiment Tax Payment by ResimentTaxCode and YearMonth
	 * @param resimentTaxCode
	 * @param yearMonth
	 * @return
	 */
	public ResimentTaxPaymentDataDto find(String resimentTaxCode, int yearMonth) {
		String companyCode = AppContexts.user().companyCode();
		Optional<ResidentTaxPaymentData> domainOp = this.repository.find(companyCode, resimentTaxCode, yearMonth);
		if (!domainOp.isPresent()) {
			return null;
		}
		ResidentTaxPaymentData domain = domainOp.get();
		return new ResimentTaxPaymentDataDto(
				domain.getCode().v(), 
				domain.getYearMonth().v(), 
				domain.getTaxPayrollMoney().v(), 
				domain.getTaxBonusMoney().v(), 
				domain.getTaxOverdueMoney().v(),
				domain.getTaxDemandChargeMoney().v(), 
				domain.getAddress().v(), 
				domain.getDueDate(),
				domain.getStaffNo().v(),
				domain.getRetirementAmount().v(),
				domain.getCityTaxMoney().v(), 
				domain.getPrefectureTaxMoney().v()
				);
	}
}
