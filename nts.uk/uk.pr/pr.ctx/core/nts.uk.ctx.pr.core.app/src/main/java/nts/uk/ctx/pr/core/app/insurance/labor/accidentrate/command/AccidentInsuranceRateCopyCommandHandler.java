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
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.service.AccidentInsuranceRateService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class AccidentInsuranceRateCopyCommandHandler.
 */
@Stateless
public class AccidentInsuranceRateCopyCommandHandler
		extends CommandHandler<AccidentInsuranceRateCopyCommand> {

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
	protected void handle(CommandHandlerContext<AccidentInsuranceRateCopyCommand> context) {

		// get user login
		LoginUserContext loginUserContext = AppContexts.user();

		// get company code user login
		String companyCode = loginUserContext.companyCode();

		// get command
		AccidentInsuranceRateCopyCommand command = context.getCommand();

		// get domain by action request
		AccidentInsuranceRate insuranceRate = AccidentInsuranceRate.createWithIntial(companyCode,
				YearMonth.of(command.getStartMonth()));

		// not copy default
		if (!command.isAddNew() && command.getHistoryIdCopy() != null
				&& command.getHistoryIdCopy().length() > 0) {

			// add new with start historyId
			Optional<AccidentInsuranceRate> data = this.repository
					.findById(command.getHistoryIdCopy());

			// Check exist.
			if (data.isPresent()) {
				insuranceRate = data.get();
				insuranceRate = insuranceRate.copyWithDate(YearMonth.of(command.getStartMonth()));
			}
		}

		// validate domain
		insuranceRate.setMaxDate();
		insuranceRate.validate();

		// validate input domain
		this.service.validateDateRange(insuranceRate);

		// get first data
		Optional<AccidentInsuranceRate> dataFirst = this.repository.findFirstData(companyCode);

		// Check exist
		if (dataFirst.isPresent()) {
			dataFirst.get().setEnd(insuranceRate.getApplyRange().getStartMonth().previousMonth());
			this.repository.update(dataFirst.get());
		}

		// connection repository running add
		this.repository.add(insuranceRate);
	}

}
