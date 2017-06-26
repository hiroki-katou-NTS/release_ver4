package nts.uk.ctx.basic.app.command.company;

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
 * UpdateCompanyCommandHandler
 * 
 * @author lanlt
 *
 */
@Stateless
@Transactional
public class UpdateCompanyCommandHandler extends CommandHandler<UpdateCompanyCommand> {
	@Inject
	private CompanyRepository companyReposity;

	@Override
	protected void handle(CommandHandlerContext<UpdateCompanyCommand> context) {
		UpdateCompanyCommand update = context.getCommand();
		// error 26
		Company company = companyReposity.getCompanyDetail(update.getCompanyCode())
				.orElseThrow(() -> new BusinessException(new RawErrorMessage("更新対象のデータが存在しません。")));
		
		if (company.getCompanyCode().toString().equals(update.getCompanyCode())) {
			company = update.toDomain();
			companyReposity.update(company);
		}

	}

}
