/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class UnemployeeInsuranceRateDeleteCommandHandler.
 */
@Stateless
public class UnemployeeInsuranceRateDeleteCommandHandler
	extends CommandHandler<UnemployeeInsuranceRateDeleteCommand> {

	/** The unemployee insurance rate repository. */
	@Inject
	private UnemployeeInsuranceRateRepository repository;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<UnemployeeInsuranceRateDeleteCommand> context) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// get command
		UnemployeeInsuranceRateDeleteCommand command = context.getCommand();

		// get first data (remove)
		Optional<UnemployeeInsuranceRate> data = this.repository.findFirstData(companyCode);

		// remove data by exist and equal code
		if (data.isPresent() && data.get().getHistoryId().equals(command.getCode())) {
			this.repository.remove(companyCode, command.getCode(), command.getVersion());

			// find second data (remove => first)
			Optional<UnemployeeInsuranceRate> dataFirst = this.repository.findFirstData(companyCode);

			// update second data
			if (dataFirst.isPresent()) {
				dataFirst.get().setMaxDate();
				this.repository.update(dataFirst.get());
			}
		}
	}

}
