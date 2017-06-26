/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.history.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.service.UnemployeeInsuranceRateService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class UnemployeeInsuranceHistoryUpdateCommandHandler.
 */
@Stateless
public class UnemployeeInsuranceHistoryUpdateCommandHandler
	extends CommandHandler<UnemployeeInsuranceHistoryUpdateCommand> {

	/** The repository. */
	@Inject
	private UnemployeeInsuranceRateRepository repository;

	/** The service. */
	@Inject
	private UnemployeeInsuranceRateService service;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<UnemployeeInsuranceHistoryUpdateCommand> context) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// get command
		UnemployeeInsuranceHistoryUpdateCommand command = context.getCommand();

		// find data
		Optional<UnemployeeInsuranceRate> data = this.repository.findById(companyCode,
			command.getHistoryId());

		// exist data
		if (data.isPresent()) {
			UnemployeeInsuranceRate insuranceRate = data.get();
			insuranceRate.setApplyRange(MonthRange.range(YearMonth.of(command.getStartMonth()),
				YearMonth.of(command.getEndMonth())));

			// validate
			insuranceRate.validate();
			this.service.validateDateRangeUpdate(insuranceRate);

			// call get by id
			Optional<UnemployeeInsuranceRate> dataFirst = this.repository
				.findById(insuranceRate.getCompanyCode(), insuranceRate.getHistoryId());

			// get <= start
			if (!dataFirst.isPresent()) {
				return;
			}
			Optional<UnemployeeInsuranceRate> dataPrevious = this.repository.findBetweenUpdate(
				insuranceRate.getCompanyCode(), dataFirst.get().getApplyRange().getStartMonth(),
				dataFirst.get().getHistoryId());

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
