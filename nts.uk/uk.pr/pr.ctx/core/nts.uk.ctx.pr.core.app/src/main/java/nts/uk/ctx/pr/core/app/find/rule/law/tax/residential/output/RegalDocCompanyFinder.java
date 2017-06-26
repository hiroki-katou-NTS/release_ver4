package nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output.dto.RegalDocCompanyDto;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.RegalDocCompanyRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class RegalDocCompanyFinder {
	@Inject
	private RegalDocCompanyRepository repository;
	/**
	 * Find All Regal Doc Company by CompanyCode
	 * @return
	 */
	public List<RegalDocCompanyDto> findAll() {
		String companyCode = AppContexts.user().companyCode();

		List<RegalDocCompanyDto> allRegalDocCompany = this.repository.findAll(companyCode).stream()
				.map(c -> RegalDocCompanyDto.fromDomain(c)).collect(Collectors.toList());
		return allRegalDocCompany;

	}
}
