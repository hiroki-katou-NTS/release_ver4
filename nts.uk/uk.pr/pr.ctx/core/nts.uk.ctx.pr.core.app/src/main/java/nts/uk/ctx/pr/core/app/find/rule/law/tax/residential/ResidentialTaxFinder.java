package nts.uk.ctx.pr.core.app.find.rule.law.tax.residential;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
/**
 * 
 * @author lanlt
 *
 */
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.ResidentialTaxRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ResidentialTaxFinder {
	@Inject
	private ResidentialTaxRepository resiTaxRepository;

	// SEL1 get data by companyCode login
	public List<ResidentialTaxDto> getAllResidentialTax() {
		String companyCode = AppContexts.user().companyCode();
		List<ResidentialTaxDto> allResidential = this.resiTaxRepository.getAllResidentialTax(companyCode).stream()
				.map(c -> ResidentialTaxDto.fromDomain(c)).collect(Collectors.toList());
		return allResidential;
	}

	// SEL_1 where CCD= 0000
	public List<ResidentialTaxDto> getAllResidentialTax(String companyCode) {
		List<ResidentialTaxDto> allResidential = this.resiTaxRepository.getAllResidentialTax(companyCode).stream()
				.map(c -> ResidentialTaxDto.fromDomain(c)).collect(Collectors.toList());
		return allResidential;
	}

	// SEL2 get data by companyCode, resiTaxCode, resiTaxReportCode, purpose:
	// check obj is not use or use in order to delete
	public List<ResidentialTaxDto> getAllResidentialTax(String resiTaxCode, String resiTaxReportCode) {
		String companyCode = AppContexts.user().companyCode();
		List<ResidentialTaxDto> allResidential = this.resiTaxRepository
				.getAllResidentialTax(companyCode, resiTaxCode, resiTaxReportCode).stream()
				.map(c -> ResidentialTaxDto.fromDomain(c)).collect(Collectors.toList());
		return allResidential;
	}

	// SEL3 get detail of residential Tax CCD=0000
	public Optional<ResidentialTaxDetailDto> getResidentialTax(String companyCode, String resiTaxCode) {

		return this.resiTaxRepository.getResidentialTax(companyCode, resiTaxCode)
				.map(c -> ResidentialTaxDetailDto.fromDomain(c));

	}

	// SEL3 get detail of residential Tax CCD!=0000
	public Optional<ResidentialTaxDetailDto> getResidentialTax(String resiTaxCode) {
		String companyCode = "";
		if (AppContexts.user() != null) {
			companyCode = AppContexts.user().companyCode();
			return this.resiTaxRepository.getResidentialTax(companyCode, resiTaxCode)
					.map(c -> ResidentialTaxDetailDto.fromDomain(c));
		} else {
			return null;
		}
	}

/*	// SEL 5
	public List<String> getAllResiTax(String companyCode, String resiTaxReportCode) {

		return (List<String>) this.resiTaxRepository.getAllResidentialTax(companyCode, resiTaxReportCode).stream()
				.collect(Collectors.toList());
	}*/

}
