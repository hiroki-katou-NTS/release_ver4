/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.printsetting.command;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSettingRepository;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.service.SalaryPrintSettingService;

/**
 * The Class SalaryPrintSettingSaveCommandHandler.
 */
@Stateless
public class SalaryPrintSettingSaveCommandHandler extends CommandHandler<SalaryPrintSettingSaveCommand> {

	/** The repository. */
	@Inject
	private SalaryPrintSettingRepository repository;

	/** The service. */
	@Inject
	private SalaryPrintSettingService service;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<SalaryPrintSettingSaveCommand> context) {
		SalaryPrintSettingSaveCommand command = context.getCommand();
		SalaryPrintSetting salaryPrintSetting = new SalaryPrintSetting(command);

		// Validate
		service.validateRequiredItem(salaryPrintSetting);
		salaryPrintSetting.validate();
		service.validateSelection(salaryPrintSetting);

		repository.save(salaryPrintSetting);
	}
}
