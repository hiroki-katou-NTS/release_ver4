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

/**
 * The Class CertifyGroupAddCommandHandler.
 */
@Stateless
public class CertifyGroupAddCommandHandler extends CommandHandler<CertifyGroupAddCommand> {

	/** The repository. */
	@Inject
	private CertifyGroupRepository repository;

	/** The service. */
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
	protected void handle(CommandHandlerContext<CertifyGroupAddCommand> context) {

		// get login user info companyCode
		String companyCode = AppContexts.user().companyCode();

		// get command
		CertifyGroupAddCommand command = context.getCommand();

		// to domain
		CertifyGroup certifyGroup = command.toDomain(companyCode);

		// validate domain
		certifyGroup.validate();

		// validate check ....
		this.service.validateRequiredItem(certifyGroup);
		this.service.checkDuplicateCode(certifyGroup);
		this.service.checkCertificationIsBelong(certifyGroup);

		// add to server database
		this.repository.add(certifyGroup);
	}

}
