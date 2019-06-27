package nts.uk.ctx.sys.assist.app.command.datarestoration;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryMngRepository;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryOperatingCondition;
import nts.uk.ctx.sys.assist.dom.datarestoration.RecoveryStorageService;

@Stateless
public class RecoveryStogareAsysnCommandHandler extends CommandHandler<PerformDataRecoveryCommand> {
	@Inject
	private RecoveryStorageService recoveryStorageService;
	@Inject
	private DataRecoveryMngRepository repoDataRecoveryMng;

	@Override
	public void handle(CommandHandlerContext<PerformDataRecoveryCommand> context) {
		PerformDataRecoveryCommand performDataCommand = context.getCommand();
		String dataRecoveryProcessId = performDataCommand.getRecoveryProcessingId();
		// サーバー復旧処理
		try {
			recoveryStorageService.recoveryStorage(dataRecoveryProcessId);
		} catch (Exception e) {
			repoDataRecoveryMng.updateByOperatingCondition(dataRecoveryProcessId, DataRecoveryOperatingCondition.ABNORMAL_TERMINATION);
		}
	}
}
