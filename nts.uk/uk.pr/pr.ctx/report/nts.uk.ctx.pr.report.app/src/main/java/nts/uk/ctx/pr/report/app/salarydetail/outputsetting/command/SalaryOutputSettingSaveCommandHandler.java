/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingRepository;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.service.SalaryOutputSettingService;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SalaryOutputSettingSaveCommandHandler.
 */
@Stateless
public class SalaryOutputSettingSaveCommandHandler extends CommandHandler<SalaryOutputSettingSaveCommand> {

	/** The repository. */
	@Inject
	private SalaryOutputSettingRepository repository;

	/** The service. */
	@Inject
	private SalaryOutputSettingService service;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<SalaryOutputSettingSaveCommand> context) {
		SalaryOutputSettingSaveCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();

		// In case of adding.
		if (command.isCreateMode()) {
			// Check duplicate.
			service.checkDuplicateCode(companyCode, command.getCode());

			// Convert to domain & validate
			SalaryOutputSetting outputSetting = command.toDomain(companyCode);
			outputSetting.validate();
			service.validateRequiredItem(outputSetting);

			this.repository.create(outputSetting);
		}

		// In case of updating.
		else {
			// Check exist.
			if (repository.isExist(companyCode, command.getCode())) {
				// Convert to domain & validate
				SalaryOutputSetting outputSetting = command.toDomain(companyCode);
				outputSetting.validate();
				service.validateRequiredItem(outputSetting);

				this.repository.update(outputSetting);
			} else {
				throw new BusinessException("ER026");
			}
		}
	}

}
