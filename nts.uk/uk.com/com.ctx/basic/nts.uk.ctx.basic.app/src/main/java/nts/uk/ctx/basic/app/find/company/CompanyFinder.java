package nts.uk.ctx.basic.app.find.company;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.company.CompanyRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author lanlt
 *
 */
@Stateless
public class CompanyFinder {
	@Inject
	private CompanyRepository companyRepository;

	public List<CompanyDto> getAllCompanys() {
		return this.companyRepository.getAllCompanys().stream().map(item -> CompanyDto.fromDomain(item))
				.collect(Collectors.toList());
	}

	public Optional<CompanyDto> getCompany() {
		String companyCode = "";
		if (AppContexts.user() != null) {
			companyCode = AppContexts.user().companyCode();
		}
		return this.companyRepository.getCompanyDetail(companyCode).map(company -> CompanyDto.fromDomain(company));
	}

	public Optional<CompanyDto> getCompanyDetail(String companyCd) {
		if (companyCd.isEmpty() || companyCd == null) {
			companyCd = AppContexts.user().companyCode();
		}
		return this.companyRepository.getCompanyDetail(companyCd).map(company -> CompanyDto.fromDomain(company));
	}

	public Optional<CompanyDto> getCompanyByUserKtSet(int use_Kt_Set) {
		String companyCode = "";
		if (AppContexts.user() != null) {
			companyCode = AppContexts.user().companyCode();
		}

		return this.companyRepository.getCompanyByUserKtSet(companyCode, use_Kt_Set).map(c -> CompanyDto.fromDomain(c));
	}
}
