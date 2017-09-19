/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.command.employment;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class EmpRemoveCommandHandler.
 */
@Stateless
public class EmpRemoveCommandHandler extends CommandHandler<EmpRemoveCommand>{

	/** The repository. */
	@Inject
	private EmploymentRepository repository;
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<EmpRemoveCommand> context) {
		// Get Company Id
		String companyId = AppContexts.user().companyId();
		
		// Get Command
		EmpRemoveCommand command = context.getCommand();
		
		// Remove Employment
		this.repository.remove(companyId, command.getEmploymentCode());
	}

	
}
