/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SalaryOutputSettingRemoveCommandHandler.
 */
@Stateless
public class SalaryOutputSettingRemoveCommandHandler extends CommandHandler<SalaryOutputSettingRemoveCommand> {

	/** The repository. */
	@Inject
	private SalaryOutputSettingRepository repository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<SalaryOutputSettingRemoveCommand> context) {
		this.repository.remove(AppContexts.user().companyCode(), context.getCommand().getCode());
	}

}
