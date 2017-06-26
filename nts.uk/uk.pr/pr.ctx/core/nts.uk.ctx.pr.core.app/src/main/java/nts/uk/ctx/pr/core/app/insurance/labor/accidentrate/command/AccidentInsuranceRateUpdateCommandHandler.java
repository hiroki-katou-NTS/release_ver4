/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
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
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.service.AccidentInsuranceRateService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class AccidentInsuranceRateUpdateCommandHandler.
 */
@Stateless
public class AccidentInsuranceRateUpdateCommandHandler
	extends CommandHandler<AccidentInsuranceRateUpdateCommand> {

	/** The accident insurance rate repo. */
	@Inject
	private AccidentInsuranceRateRepository repository;

	/** The accident insurance rate service. */
	@Inject
	private AccidentInsuranceRateService service;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<AccidentInsuranceRateUpdateCommand> context) {

		// get user login
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// getCommand
		AccidentInsuranceRateUpdateCommand command = context.getCommand();

		// to domain
		AccidentInsuranceRate insuranceRate = command.toDomain(companyCode);

		// validate domain
		insuranceRate.validate();

		// validate input
		this.service.validateDateRangeUpdate(insuranceRate);

		// get first by update
		Optional<AccidentInsuranceRate> data = this.repository.findBetweenUpdate(
			insuranceRate.getCompanyCode(), insuranceRate.getApplyRange().getStartMonth(),
			insuranceRate.getHistoryId());

		// exist data
		if (data.isPresent()) {
			data.get().setEnd(insuranceRate.getApplyRange().getStartMonth().previousMonth());
			this.repository.update(data.get());
		}

		// connection service update
		this.repository.update(insuranceRate);
	}

}
