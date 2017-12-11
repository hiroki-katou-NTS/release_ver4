package nts.uk.ctx.bs.company.pub.company;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.company.dom.company.Company;
import nts.uk.ctx.bs.company.dom.company.CompanyInforNew;
import nts.uk.ctx.bs.company.dom.company.CompanyRepository;

@Stateless
public class CompanyPubImp implements ICompanyPub {

	@Inject
	private CompanyRepository repo;

	@Override
	public List<CompanyExport> getAllCompany() {

		return repo
				.getAllCompany().stream().map(item -> new CompanyExport(item.getCompanyCode().v(),
						item.getCompanyName().v(), item.getCompanyId(), item.getIsAbolition().value))
				.collect(Collectors.toList());

	}

	@Override
	public BeginOfMonthExport getBeginOfMonth(String cid) {

		BeginOfMonthExport result = new BeginOfMonthExport();
		Optional<Company> comOpt = repo.getComanyInfoByCid(cid);
		if (comOpt.isPresent()) {
			Company company = comOpt.get();
			result.setCid(company.getCompanyId());
			result.setStartMonth(company.getStartMonth().value);
		}
		return result;
	}

	/**
	 * for request list No.125
	 * @return Company Info
	 */
	@Override
	public CompanyExport getCompanyByCid(String cid) {
		Optional<CompanyInforNew> companyOpt = repo.getComanyByCid(cid);
		CompanyExport result = new CompanyExport();
		if (companyOpt.isPresent()) {
			CompanyInforNew company = companyOpt.get();
			result.setCompanyCode(company.getCompanyCode() == null ? "" : company.getCompanyCode().v());
			result.setCompanyId(company.getCompanyId());
			result.setCompanyName(company.getCompanyName() == null ? "" : company.getCompanyName().v());
			result.setIsAbolition(company.getIsAbolition().value);
		}
		return result;

	}
}
