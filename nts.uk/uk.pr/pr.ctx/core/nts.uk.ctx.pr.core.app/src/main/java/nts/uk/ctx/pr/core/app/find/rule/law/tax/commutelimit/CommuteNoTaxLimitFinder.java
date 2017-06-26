package nts.uk.ctx.pr.core.app.find.rule.law.tax.commutelimit;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuteNoTaxLimit;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuteNoTaxLimitRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CommuteNoTaxLimitFinder {

	@Inject
	private CommuteNoTaxLimitRepository repository;

	public List<CommuteNoTaxLimitDto> getListCommuteNoTaxLimitByCompanyCode() {
		String companyCode = AppContexts.user().companyCode();
		return this.repository.getCommuteNoTaxLimitByCompanyCode(companyCode).stream()
				.map(item -> CommuteNoTaxLimitDto.getValuesFromDomain(item)).collect(Collectors.toList());
	}

	public CommuteNoTaxLimitDto getCommuteNoTaxLimit(String taxLimitCode) {
		String companyCode = AppContexts.user().companyCode();
		Optional<CommuteNoTaxLimit> exitCommuteNoTaxLimit = this.repository.getCommuteNoTaxLimit(companyCode,
				taxLimitCode);
		if(!exitCommuteNoTaxLimit.isPresent()){
			return null;
		}
		return CommuteNoTaxLimitDto.getValuesFromDomain(exitCommuteNoTaxLimit.get());
	}
}
