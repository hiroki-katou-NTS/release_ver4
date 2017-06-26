package nts.uk.ctx.basic.app.command.organization.workplace;


import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class RemoveHistoryWkPCommandHandler extends CommandHandler<String> {

	@Inject
	private WorkPlaceRepository workPlaceRepository;

	@Override
	protected void handle(CommandHandlerContext<String> context) {
		String companyCode = AppContexts.user().companyCode();
		// check isExistHistory
		if (!workPlaceRepository.isExistHistory(companyCode, context.getCommand().toString())) {
			throw new BusinessException("ER06");
		}
		workPlaceRepository.removeHistory(companyCode, context.getCommand().toString());
		workPlaceRepository.removeMemoByHistId(companyCode, context.getCommand().toString());

	}

}
