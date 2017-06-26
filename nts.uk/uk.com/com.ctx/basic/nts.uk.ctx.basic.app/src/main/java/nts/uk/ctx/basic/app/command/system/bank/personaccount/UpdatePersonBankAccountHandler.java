package nts.uk.ctx.basic.app.command.system.bank.personaccount;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccount;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccountRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
@Transactional
public class UpdatePersonBankAccountHandler extends CommandHandler<UpdatePersonBankAccountCommand> {
	
	@Inject
	private PersonBankAccountRepository personBankAccountRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdatePersonBankAccountCommand> context) {
        UpdatePersonBankAccountCommand command = context.getCommand();
        String companyCode = AppContexts.user().companyCode();
        
        PersonBankAccount domain = new PersonBankAccount(
        		companyCode,
        		command.getPersonID(),
        	    command.getHistId(),
        		command.getStartYearMonth(),
        		command.getEndYearMonth(),
        		command.getUseSet1(),
        		command.getUseSet2(),
        		command.getUseSet3(),
        		command.getUseSet4(),
        		command.getUseSet5());
        domain.validate();
        personBankAccountRepository.update(domain);
	}

}
