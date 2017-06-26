package nts.uk.ctx.basic.app.command.system.bank.branch;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranch;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranchRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * add branch command handler
 * @author sonnh
 *
 */
@Stateless
@Transactional
public class AddBranchCommandHandler extends CommandHandler<AddBranchCommand> {
	@Inject
	private BankBranchRepository bankBranchRepository; 
	
	@Override
	protected void handle(CommandHandlerContext<AddBranchCommand> context) {
		AddBranchCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		
		// Check exits bank branch
		boolean existsBranch = bankBranchRepository.checkExists(companyCode, command.getBankCode(), command.getBranchCode());
		if (existsBranch) {
			throw new BusinessException("ER005");
		}
		 // create from java type
		BankBranch domain =  BankBranch.newBranch(companyCode, command.getBankCode(), command.getBranchCode(), command.getBranchName(), command.getBranchKnName(), command.getMemo());
		
		// validate
		domain.validate();
		
		// add bank branch
		bankBranchRepository.add(domain);
	}

}
