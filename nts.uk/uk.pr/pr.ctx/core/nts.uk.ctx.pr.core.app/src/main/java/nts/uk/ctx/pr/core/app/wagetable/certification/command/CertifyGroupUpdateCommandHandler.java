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
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroup;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroupRepository;
import nts.uk.ctx.pr.core.dom.wagetable.certification.service.CertifyGroupService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class CertifyGroupUpdateCommandHandler.
 */
@Stateless
public class CertifyGroupUpdateCommandHandler extends CommandHandler<CertifyGroupUpdateCommand> {

	/** The certify group repository. */
	@Inject
	private CertifyGroupRepository repository;

	/** The certify group service. */
	@Inject
	private CertifyGroupService service;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<CertifyGroupUpdateCommand> context) {

		// get login user info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user
		String companyCode = loginUserContext.companyCode();

		// get command
		CertifyGroupUpdateCommand command = context.getCommand();

		// to domain by command
		CertifyGroup certifyGroup = command.toDomain(companyCode);

		// validate domain
		certifyGroup.validate();

		// validate
		this.service.validateRequiredItem(certifyGroup);

		// check duplicate
		this.service.checkCertificationIsBelong(certifyGroup);

		// update
		this.repository.update(certifyGroup);
	}

}
