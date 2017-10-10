package nts.uk.ctx.at.request.app.command.application.common;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.ProcessCancel;
import nts.uk.shr.com.context.AppContexts;
@Stateless
@Transactional
public class UpdateApplicationCancelHandler extends CommandHandler<UpdateApplicationCommonCmd> {

	
	@Inject
	private ProcessCancel processCancelRepo;

	@Override
	protected void handle(CommandHandlerContext<UpdateApplicationCommonCmd> context) {
		String companyID = AppContexts.user().companyId();
		//12
		processCancelRepo.detailScreenCancelProcess(companyID,context.getCommand().getAppId());
		
	}

}
