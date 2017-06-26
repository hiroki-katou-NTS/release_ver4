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
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.service.UnemployeeInsuranceRateService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class UnemployeeInsuranceRateUpdateCommandHandler.
 */
@Stateless
public class UnemployeeInsuranceRateUpdateCommandHandler
	extends CommandHandler<UnemployeeInsuranceRateUpdateCommand> {

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
	protected void handle(CommandHandlerContext<UnemployeeInsuranceRateUpdateCommand> context) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// get command
		UnemployeeInsuranceRateUpdateCommand command = context.getCommand();

		// to domain
		UnemployeeInsuranceRate insuranceRate = command.toDomain(companyCode);

		// validate
		insuranceRate.validate();
		this.service.validateDateRangeUpdate(insuranceRate);

		// call get by id
		Optional<UnemployeeInsuranceRate> data = this.repository
			.findById(insuranceRate.getCompanyCode(), insuranceRate.getHistoryId());

		// get <= start
		if (data.isPresent()) {
			Optional<UnemployeeInsuranceRate> dataPrevious = this.repository.findBetweenUpdate(
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
