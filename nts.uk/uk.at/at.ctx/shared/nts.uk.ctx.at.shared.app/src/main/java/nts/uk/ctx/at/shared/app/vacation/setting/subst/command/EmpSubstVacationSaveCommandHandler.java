/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.vacation.setting.subst.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.SubstVacationSetting;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class ComSubstVacationSaveCommandHandler.
 */
@Stateless
public class EmpSubstVacationSaveCommandHandler
		extends CommandHandler<EmpSubstVacationSaveCommand> {

	/** The repository. */
	@Inject
	private EmpSubstVacationRepository repository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<EmpSubstVacationSaveCommand> context) {
		// get user login
		LoginUserContext loginUserContext = AppContexts.user();

		// get company code user login
		String companyId = loginUserContext.companyId();

		// Get Command
		EmpSubstVacationSaveCommand command = context.getCommand();

		// Update VacationAcquisitionRule
		Optional<EmpSubstVacation> optEmpSubstVacation = this.repository.findById(companyId,
				command.getContractTypeCode());

		// Check is managed, keep old values when is not managed
		if (optEmpSubstVacation.isPresent() && command.getIsManage() == ManageDistinct.NO.value) {
			SubstVacationSetting setting = optEmpSubstVacation.get().getSetting();
			command.setAllowPrepaidLeave(setting.getAllowPrepaidLeave().value);
			command.setExpirationDate(setting.getExpirationDate().value);
		}

		// Convert data
		EmpSubstVacation empSubstVacation = command.toDomain(companyId);

		// Check exist
		if (optEmpSubstVacation.isPresent()) {
			// Update into db
			this.repository.update(empSubstVacation);
		} else {
			// Insert into db
			this.repository.insert(empSubstVacation);
		}
	}

}
