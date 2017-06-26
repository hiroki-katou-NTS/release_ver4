package nts.uk.ctx.basic.app.command.system.bank.branch;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranch;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranchRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * update branch command handler
 * @author sonnh
 *
 */
@Stateless
@Transactional
public class UpdateBranchCommandHandler extends CommandHandler<UpdateBranchCommand> {
	@Inject
	private BankBranchRepository bankBranchRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateBranchCommand> context) {
		UpdateBranchCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		
		// create from java type
		BankBranch domain = BankBranch.createFromJavaType(companyCode, command.getBranchId(), command.getBankCode(), command.getBranchCode(), command.getBranchName(), command.getBranchKnName(), command.getMemo());
		
		// validate
		domain.validate();
		
		// update bank branch
		bankBranchRepository.update(domain);	
	} 
	
	
}
