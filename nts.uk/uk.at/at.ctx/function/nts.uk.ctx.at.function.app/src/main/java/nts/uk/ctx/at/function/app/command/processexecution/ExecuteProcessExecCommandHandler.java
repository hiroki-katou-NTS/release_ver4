package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDateTime;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogManageRepository;
@Stateless
public class ExecuteProcessExecCommandHandler extends AsyncCommandHandler<ExecuteProcessExecutionCommand> {
	@Inject
	private ExecuteProcessExecutionCommandHandler execHandler;
	@Inject
	private ProcessExecutionLogManageRepository processExecLogManaRepo;
	@Override
	protected void handle(CommandHandlerContext<ExecuteProcessExecutionCommand> context) {
		val asyncContext = context.asAsync();
		val dataSetter = asyncContext.getDataSetter();
		ExecuteProcessExecutionCommand command = context.getCommand();
		String companyId = command.getCompanyId();
		String execItemCd = command.getExecItemCd();
		//ドメインモデル「更新処理自動実行管理」を取得する
		Optional<ProcessExecutionLogManage> logManageOpt = this.processExecLogManaRepo.getLogByCIdAndExecCd(companyId, execItemCd);
		if(!logManageOpt.isPresent()){
			return;
		}
		//取得したドメインモデル「更新処理自動実行管理」の現在の実行状態を確認する
		ProcessExecutionLogManage processExecutionLogManage = logManageOpt.get();
		//「実行中」 or 「無効」の場合
		if(processExecutionLogManage.getCurrentStatus().value==0||processExecutionLogManage.getCurrentStatus().value==2){
			//throw new BusinessException("Msg_1101");
			dataSetter.setData("message101", "Msg_1101");
		}
		//「待機中」の場合
		else{
			command.setExecId(IdentifierUtil.randomUniqueId());
			this.execHandler.handle(context);	
		}
	}

}
