/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.singlesignon;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BundledBusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository;

/**
 * The Class SaveOtherSysAccountCommandHandler.
 */
@Stateless
public class SaveOtherSysAccountCommandHandler extends CommandHandler<SaveOtherSysAccountCommand> {
	
	/** The other sys account repository. */
	@Inject
	private OtherSysAccountRepository otherSysAccountRepository;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<SaveOtherSysAccountCommand> context) {
		
		// Get command
		SaveOtherSysAccountCommand command = context.getCommand();

		// check error domain
		boolean isError = false;
		BundledBusinessException exceptions = BundledBusinessException.newInstance();

		// check only company code and user name
		Optional<OtherSysAccount> opOtherSysAccount = otherSysAccountRepository.findByCompanyCodeAndUserName(
				command.getCompanyCode(), command.getUserName());

		// check condition
		if (opOtherSysAccount.isPresent()) {
			// Throw Exception
			isError = true;
			exceptions.addMessage("Msg_616");
			// Has error, throws message
		}
		if (isError) {
			exceptions.throwExceptions();
		} else {
						
			Optional<OtherSysAccount> opOtherSysAcc = otherSysAccountRepository.findByUserId(command.getUserId());

			if(opOtherSysAcc.isPresent()){
				// remove
				this.otherSysAccountRepository.remove(opOtherSysAcc.get().getUserId(), opOtherSysAcc.get().getCompanyCode(), opOtherSysAcc.get().getUserName());
			}
			
			// save domain
			OtherSysAccount otherSysAccount = new OtherSysAccount(command);

			this.otherSysAccountRepository.add(otherSysAccount);
		}
	}
	
	
	
	
	

}
