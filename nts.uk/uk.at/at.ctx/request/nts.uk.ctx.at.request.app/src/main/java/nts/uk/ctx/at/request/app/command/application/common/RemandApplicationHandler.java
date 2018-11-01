package nts.uk.ctx.at.request.app.command.application.common;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterRemand;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.RemandCommand;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.MailSenderResult;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class RemandApplicationHandler extends CommandHandlerWithResult<RemandCommand, MailSenderResult>{
	
	@Inject
	private DetailBeforeUpdate detailBeforeUpdate;
	
	@Inject
	private DetailAfterRemand detailAfterRemand;
	//差し戻し実行
	@Override
	protected MailSenderResult handle(CommandHandlerContext<RemandCommand> context) {
		String companyID =  AppContexts.user().companyId();
		RemandCommand remandCommand = context.getCommand();
		List<String> lstAppID = remandCommand.getAppID();
		for (String appId : lstAppID) {
			//共通アルゴリズム「詳細画面差し戻し前の処理」を実行する-(THực hiện xử lý màn hình chi tiết trước khi refer back )
			// 11-1.詳細画面差し戻し前の処理
			detailBeforeUpdate.exclusiveCheck(companyID, appId, remandCommand.getVersion());
		}
		//共通アルゴリズム「詳細画面差し戻し後の処理」を実行する(xử lý màn hình chi tiết sau khi refer back)
		//11-2.詳細画面差し戻し後の処理
		return detailAfterRemand.doRemand(companyID, remandCommand);
	}
}
