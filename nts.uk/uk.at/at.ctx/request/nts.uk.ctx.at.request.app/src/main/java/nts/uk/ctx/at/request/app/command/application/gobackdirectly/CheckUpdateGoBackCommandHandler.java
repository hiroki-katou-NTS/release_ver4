package nts.uk.ctx.at.request.app.command.application.gobackdirectly;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.service.GoBackDirectlyUpdateService;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class CheckUpdateGoBackCommandHandler extends CommandHandler<InsertApplicationGoBackDirectlyCommand>{
	
	@Inject
	private GoBackDirectlyUpdateService goBackDirectlyUpdateService;
	
	@Override
	protected void handle(CommandHandlerContext<InsertApplicationGoBackDirectlyCommand> context) {
		String companyId = AppContexts.user().companyId();
		InsertApplicationGoBackDirectlyCommand command = context.getCommand();		
		//get new Application Item
		String appID = command.getAppCommand().getApplicationID();
		// get new GoBack Direct Item
		GoBackDirectly newGoBack = new GoBackDirectly(
				companyId, 
				appID,
				command.goBackCommand.workTypeCD, 
				command.goBackCommand.siftCD, 
				command.goBackCommand.workChangeAtr,
				command.goBackCommand.goWorkAtr1, 
				command.goBackCommand.backHomeAtr1,
				command.goBackCommand.workTimeStart1, 
				command.goBackCommand.workTimeEnd1,
				command.goBackCommand.workLocationCD1, 
				command.goBackCommand.goWorkAtr2,
				command.goBackCommand.backHomeAtr2, 
				command.goBackCommand.workTimeStart2,
				command.goBackCommand.workTimeEnd2, 
				command.goBackCommand.workLocationCD2);
		//勤務を変更する
		//直行直帰登録前チェック 
		goBackDirectlyUpdateService.checkErrorBeforeUpdate(newGoBack, companyId, appID, command.getAppCommand().getVersion());
		
	}

}
