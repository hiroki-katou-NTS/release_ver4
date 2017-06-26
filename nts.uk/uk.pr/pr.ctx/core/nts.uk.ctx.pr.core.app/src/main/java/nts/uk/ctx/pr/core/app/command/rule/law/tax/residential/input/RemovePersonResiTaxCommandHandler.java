package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input.PersonResiTaxRepository;
import nts.uk.shr.com.context.AppContexts;
@RequestScoped
public class RemovePersonResiTaxCommandHandler extends CommandHandler<RemovePersonResiTaxCommand> {
	@Inject
	private PersonResiTaxRepository personResiTaxRepository;
	
	@Override
	protected void handle(CommandHandlerContext<RemovePersonResiTaxCommand> context) {
		RemovePersonResiTaxCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		for(String item: command.getPersonId()){
			personResiTaxRepository.remove(companyCode, item, command.getYearKey());
		}
	}
}
