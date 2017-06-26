/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class CertifyGroupAddCommandHandler.
 */
@Stateless
public class CertifyGroupDeleteCommandHandler extends CommandHandler<CertifyGroupDeleteCommand> {

	/** The certify group repository. */
	@Inject
	private CertifyGroupRepository repository;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<CertifyGroupDeleteCommand> context) {

		// get info login user companyCode
		String companyCode = AppContexts.user().companyCode();

		// get command
		CertifyGroupDeleteCommand command = context.getCommand();

		// call server remove
		this.repository.remove(companyCode, command.getCertifyGroupDeleteDto().getGroupCode());
	}

}
