/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.workrule.closure;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistoryRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class ClosureSaveCommandHandler.
 */
@Stateless
public class ClosureSaveCommandHandler extends CommandHandler<ClosureSaveCommand> {
	
	/** The repository. */
	@Inject
	private ClosureRepository closureRepo;
	
	/** The repository history. */
	@Inject
	private ClosureHistoryRepository closureHistoryRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<ClosureSaveCommand> context) {
		
		// get user login
		LoginUserContext loginUserContext = AppContexts.user();
		
		//get company id
		String companyId = loginUserContext.companyId();
		
		// get command
		ClosureSaveCommand command = context.getCommand();
		
		Optional<ClosureHistory> beginClosureHistory = this.closureHistoryRepo
				.findByHistoryBegin(companyId, command.getClosureId());
		
		Optional<ClosureHistory> endClosureHistory = this.closureHistoryRepo
				.findByHistoryLast(companyId, command.getClosureId());
		// check (min start month) <= closure month <= (max end month) 
		if (beginClosureHistory.isPresent() && endClosureHistory.isPresent()
				&& command.getUseClassification() == 1
				&& (command.getMonth() > endClosureHistory.get().getEndYearMonth().v() || command
						.getMonth() < beginClosureHistory.get().getStartYearMonth().v())) {

			throw new BusinessException("Msg_241");
		}
		// to domain
		Closure domain = command.toDomain(companyId);
		
		this.closureRepo.update(domain);
	}

}
