/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.history.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.service.AccidentInsuranceRateService;

/**
 * The Class AccidentInsuranceHistoryUpdateCommandHandler.
 */
@Stateless
public class AccidentInsuranceHistoryUpdateCommandHandler
		extends CommandHandler<AccidentInsuranceHistoryUpdateCommand> {

	/** The accident insurance rate repository. */
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
	protected void handle(CommandHandlerContext<AccidentInsuranceHistoryUpdateCommand> context) {

		// get command
		AccidentInsuranceHistoryUpdateCommand command = context.getCommand();

		Optional<AccidentInsuranceRate> data = this.repository.findById(command.getHistoryId());

		// exist data
		if (data.isPresent()) {
			AccidentInsuranceRate insuranceRate = data.get();
			insuranceRate.setStart(YearMonth.of(command.getStartMonth()));
			insuranceRate.setEnd(YearMonth.of(command.getEndMonth()));
			// validate
			insuranceRate.validate();
			this.service.validateDateRangeUpdate(insuranceRate);

			// call get by id
			Optional<AccidentInsuranceRate> dataFirst = this.repository
					.findById(insuranceRate.getHistoryId());

			if (!dataFirst.isPresent()) {
				return;
			}
			// get <= start
			Optional<AccidentInsuranceRate> dataPrevious = this.repository.findBetweenUpdate(
					insuranceRate.getCompanyCode(), data.get().getApplyRange().getStartMonth(),
					data.get().getHistoryId());

			// update end year month start previous
			if (dataPrevious.isPresent()) {
				dataPrevious.get()
						.setEnd(insuranceRate.getApplyRange().getStartMonth().previousMonth());
				this.repository.update(dataPrevious.get());
			}

			// update value
			this.repository.update(insuranceRate);
		}
	}

}
