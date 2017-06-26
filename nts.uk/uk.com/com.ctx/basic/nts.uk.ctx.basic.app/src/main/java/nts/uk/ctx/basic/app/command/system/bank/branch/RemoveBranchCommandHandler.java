package nts.uk.ctx.basic.app.command.system.bank.branch;

/**
 * remove branch command handler
 */
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranchRepository;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonBankAccountRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class RemoveBranchCommandHandler extends CommandHandler<RemoveBranchCommand> {
	@Inject
	private BankBranchRepository bankBranchRepo;
	
	@Inject
	private PersonBankAccountRepository personBankAccountRepository;
	
	@Override
	protected void handle(CommandHandlerContext<RemoveBranchCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		RemoveBranchCommand command = context.getCommand();
	    List<String> branchBranchIdList = new ArrayList<String>();
	    branchBranchIdList.add(command.getBranchId().toString());
	    // check exists bank branch
	    if(personBankAccountRepository.checkExistsBranchAccount(companyCode, branchBranchIdList)){
	    	throw new BusinessException("ER008"); // ER008
	    }
	    // remove bank branch
		bankBranchRepo.remove(companyCode,command.getBranchId());	
	}

}
