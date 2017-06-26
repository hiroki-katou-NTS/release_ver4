package nts.uk.ctx.basic.app.command.system.bank;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.Bank;
import nts.uk.ctx.basic.dom.system.bank.BankRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * update bank command handler
 * @author sonnh
 *
 */
@Stateless
@Transactional
public class UpdateBankCommandHandler extends CommandHandler<UpdateBankCommand>{
   
	@Inject
    private BankRepository bankRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateBankCommand> context) {
		UpdateBankCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		
		// check exists bank
		Optional<Bank> bank = bankRepository.find(companyCode, command.getBankCode());
		if (!bank.isPresent()) {
			throw new RuntimeException("Bank not found");
		}
		
		// create from java type 
		Bank domain = Bank.createFromJavaType(companyCode, command.getBankCode(), command.getBankName(), command.getBankNameKana(), command.getMemo());
		
		// validate
		domain.validate();
		
		// update bank
		bankRepository.update(domain);
	}
    
	
}
