package nts.uk.ctx.basic.app.command.company;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.company.Company;
import nts.uk.ctx.basic.dom.company.CompanyRepository;

/**
 * AddCompanyCommandHandlers
 * 
 * @author lanlt
 *
 */
@Stateless
@Transactional
public class AddCompanyCommandHandler extends CommandHandler<AddCompanyCommand> {
	/** CompanyRepository */
	@Inject
	private CompanyRepository companyRepository;

	@Override
	protected void handle(CommandHandlerContext<AddCompanyCommand> context) {
		AddCompanyCommand addCompany = context.getCommand();

		Company company = addCompany.toDomain();
		Optional<Company> companyCheckExist;
		companyCheckExist = this.companyRepository.getCompanyDetail(addCompany.getCompanyCode());
		// notification error 005 check in account -> add account (sign up)
		if (companyCheckExist.isPresent()) {
			throw new BusinessException(new RawErrorMessage("入力した 会社コード は既に存在しています。\r\n。 " + "を確認してください 。"));
		} else {
			this.companyRepository.add(company);
		}
	}

}
