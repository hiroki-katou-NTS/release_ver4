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

@Stateless
public class UnemployeeInsuranceRateAddCommandHandler
	extends CommandHandler<UnemployeeInsuranceRateAddCommand> {

	/** CompanyRepository */
	@Inject
	private UnemployeeInsuranceRateRepository repository;

	/** The unemployee insurance rate service. */
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
	protected void handle(CommandHandlerContext<UnemployeeInsuranceRateAddCommand> context) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// get command
		UnemployeeInsuranceRateAddCommand command = context.getCommand();

		// to domain
		UnemployeeInsuranceRate insuranceRate = command.toDomain(companyCode);

		// validate
		insuranceRate.validate();
		this.service.validateDateRange(insuranceRate);

		// find first data
		Optional<UnemployeeInsuranceRate> dataFisrt = this.repository
			.findFirstData(insuranceRate.getCompanyCode());

		// check exist
		if (dataFisrt.isPresent()) {
			dataFisrt.get().setEnd(insuranceRate.getApplyRange().getStartMonth().previousMonth());
			this.repository.update(dataFisrt.get());
		}

		// call repository add (insert database)
		this.repository.add(insuranceRate.copyWithDate(insuranceRate.getStart()));
	}

}
