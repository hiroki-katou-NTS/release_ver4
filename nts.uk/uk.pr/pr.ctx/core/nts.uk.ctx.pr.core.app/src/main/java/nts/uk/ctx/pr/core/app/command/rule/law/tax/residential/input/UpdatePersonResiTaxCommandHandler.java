package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input.PersonResiTax;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input.PersonResiTaxRepository;
import nts.uk.shr.com.context.AppContexts;
@RequestScoped
public class UpdatePersonResiTaxCommandHandler extends CommandHandler<UpdatePersonResiTaxCommand> {
	@Inject
	PersonResiTaxRepository personResiTaxRepository;
	@Override
	protected void handle(CommandHandlerContext<UpdatePersonResiTaxCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		PersonResiTax personResiTax = context.getCommand().toDomain(companyCode);
		personResiTaxRepository.update(personResiTax);
	}
}
