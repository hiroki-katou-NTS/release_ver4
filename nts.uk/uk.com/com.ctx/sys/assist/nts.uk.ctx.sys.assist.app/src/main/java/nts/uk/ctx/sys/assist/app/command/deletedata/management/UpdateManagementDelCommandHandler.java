package nts.uk.ctx.sys.assist.app.command.deletedata.management;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting.SaveErrorLogDeleteResult;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.OperatingCondition;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateManagementDelCommandHandler extends CommandHandler<ManagementDelCommand> {

	@Inject
	private ManagementDeletionRepository repository;
	@Inject
	private SaveErrorLogDeleteResult saveErrorLogDeleteResult;

	@Override
	protected void handle(CommandHandlerContext<ManagementDelCommand> context) {
		ManagementDelCommand updateCommand = context.getCommand();
		repository.setInterruptDeleting(updateCommand.getDelId(), updateCommand.getIsInterruptedFlg(),
				OperatingCondition.valueOf(updateCommand.getOperatingCondition()));

		// ドメインモデル「データ削除の結果ログ」へ追加する
		saveErrorLogDeleteResult.saveErrorWhenInterruptProcessing(updateCommand.getDelId(), AppContexts.user().companyId());
		// ドメインモデル「データ削除の保存結果」を更新する
		saveErrorLogDeleteResult.saveEndResultDelInterrupt(updateCommand.getDelId());
	}
}
