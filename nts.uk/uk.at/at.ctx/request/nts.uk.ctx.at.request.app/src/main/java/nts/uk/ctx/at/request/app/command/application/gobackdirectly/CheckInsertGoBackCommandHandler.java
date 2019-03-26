package nts.uk.ctx.at.request.app.command.application.gobackdirectly;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.service.GoBackDirectlyRegisterService;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class CheckInsertGoBackCommandHandler extends CommandHandler<InsertApplicationGoBackDirectlyCommand>{
	@Inject
	private GoBackDirectlyRegisterService goBackDirectlyRegisterService;
	@Override
	protected void handle(CommandHandlerContext<InsertApplicationGoBackDirectlyCommand> context) {
		String companyId = AppContexts.user().companyId();
		InsertApplicationGoBackDirectlyCommand command = context.getCommand();		
		//get new Application Item
		Application_New newApp = Application_New.firstCreate(
				companyId, 
				EnumAdaptor.valueOf(command.appCommand.getPrePostAtr(), PrePostAtr.class),  
				command.appCommand.getApplicationDate(),
				EnumAdaptor.valueOf(command.appCommand.getApplicationType(), ApplicationType.class), 
				command.appCommand.getEnteredPersonSID(),
				new AppReason(command.appCommand.getApplicationReason()));
		// get new GoBack Direct Item
		GoBackDirectly newGoBack = new GoBackDirectly(
				companyId, 
				newApp.getAppID(),
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
		goBackDirectlyRegisterService.checkBeforRegister(newGoBack, newApp, command.isCheckOver1Year());
	}

}
