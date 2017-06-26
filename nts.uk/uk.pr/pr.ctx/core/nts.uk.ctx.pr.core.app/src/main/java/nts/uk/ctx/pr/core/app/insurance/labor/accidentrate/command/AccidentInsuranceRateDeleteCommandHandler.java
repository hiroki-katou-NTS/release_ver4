/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class AccidentInsuranceRateDeleteCommandHandler.
 */
@Stateless
public class AccidentInsuranceRateDeleteCommandHandler
		extends CommandHandler<AccidentInsuranceRateDeleteCommand> {

	/** The repository. */
	@Inject
	private AccidentInsuranceRateRepository repository;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<AccidentInsuranceRateDeleteCommand> context) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// get command
		AccidentInsuranceRateDeleteCommand command = context.getCommand();

		// get first data (remove)
		Optional<AccidentInsuranceRate> data = this.repository.findFirstData(companyCode);

		// exist data remove
		if (data.isPresent() && data.get().getHistoryId()
				.equals(command.getAccidentInsuranceRateDeleteDto().getCode())) {

			// history first
			this.repository.remove(command.getAccidentInsuranceRateDeleteDto().getCode());

			// get first data (update)
			Optional<AccidentInsuranceRate> dataFirst = this.repository.findFirstData(companyCode);

			// exist data update
			if (dataFirst.isPresent()) {
				dataFirst.get().setMaxDate();
				this.repository.update(dataFirst.get());
			}
		}
	}

}
