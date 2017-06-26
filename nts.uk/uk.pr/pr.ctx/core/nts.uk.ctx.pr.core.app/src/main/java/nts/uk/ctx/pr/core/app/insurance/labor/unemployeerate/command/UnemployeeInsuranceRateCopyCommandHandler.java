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
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.service.UnemployeeInsuranceRateService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class UnemployeeInsuranceRateCopyCommandHandler.
 */
@Stateless
public class UnemployeeInsuranceRateCopyCommandHandler
	extends CommandHandler<UnemployeeInsuranceRateCopyCommand> {

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
	protected void handle(CommandHandlerContext<UnemployeeInsuranceRateCopyCommand> context) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// get command
		UnemployeeInsuranceRateCopyCommand command = context.getCommand();

		UnemployeeInsuranceRate insuranceRate = null;

		// add new
		if (command.isAddNew()) {
			insuranceRate = UnemployeeInsuranceRate.createWithIntial(companyCode,
				YearMonth.of(command.getStartMonth()));
		} else {
			// add new with start historyId
			Optional<UnemployeeInsuranceRate> dataAdd = this.repository.findById(companyCode,
				command.getHistoryIdCopy());

			// check exist data add
			if (dataAdd.isPresent()) {
				insuranceRate = dataAdd.get();
				insuranceRate = insuranceRate.copyWithDate(YearMonth.of(command.getStartMonth()));
			} else {
				insuranceRate = UnemployeeInsuranceRate.createWithIntial(companyCode,
					YearMonth.of(command.getStartMonth()));
			}
		}

		// validate
		insuranceRate.setMaxDate();
		insuranceRate.validate();
		this.service.validateDateRange(insuranceRate);

		// find first data
		Optional<UnemployeeInsuranceRate> dataFisrt = this.repository
			.findFirstData(insuranceRate.getCompanyCode());

		// check exist data first
		if (dataFisrt.isPresent()) {
			dataFisrt.get().setEnd(insuranceRate.getApplyRange().getStartMonth().previousMonth());
			this.repository.update(dataFisrt.get());
		}

		// call repository add (insert database)
		this.repository.add(insuranceRate);
	}

}
