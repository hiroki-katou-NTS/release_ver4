package nts.uk.ctx.at.request.app.command.application.common;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterRemand;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class RemandApplicationHandler extends CommandHandlerWithResult<RemandCommand, String>{
	
	@Inject
	private DetailBeforeUpdate detailBeforeUpdate;
	
	@Inject
	private DetailAfterRemand detailAfterRemand;
	
	@Override
	protected String handle(CommandHandlerContext<RemandCommand> context) {
		String companyID =  AppContexts.user().companyId();
		RemandCommand remandCommand = context.getCommand();
		
		// 11-1.詳細画面差し戻し前の処理
		detailBeforeUpdate.exclusiveCheck(companyID, remandCommand.getAppID(), remandCommand.getVersion());
		
		return detailAfterRemand.doRemand(companyID, remandCommand.getAppID(), remandCommand.getVersion(), remandCommand.getOrder(), remandCommand.getReturnReason());
	}

}
