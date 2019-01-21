/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.singlesignon;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class RemoveWindowAccountCommandHandler.
 */
@Stateless
public class RemoveWindowAccountCommandHandler extends CommandHandler<RemoveWindowAccountCommand> {

	/** The window account repository. */
	@Inject
	private WindowsAccountRepository windowAccountRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<RemoveWindowAccountCommand> context) {
		// Get command
		RemoveWindowAccountCommand command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		Optional<WindowsAccount> optWindowAcc = windowAccountRepository.findByEmployeeId(companyId,command.getEmployeeId());

		if(optWindowAcc.isPresent()) {
			optWindowAcc.get().getAccountInfos().forEach(wd -> {
				windowAccountRepository.remove(companyId, optWindowAcc.get().getEmployeeId(), wd.getNo());
			});
		}
	}

}
