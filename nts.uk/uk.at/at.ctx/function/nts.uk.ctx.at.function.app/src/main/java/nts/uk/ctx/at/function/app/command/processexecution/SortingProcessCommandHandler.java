package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
//import lombok.val;
//import nts.arc.layer.app.command.AsyncCommandHandler;
//import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDateTime;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.OverallErrorDetail;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogHistory;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskSettingRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogHistRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogManageRepository;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.ExecutionTaskSetting;
//import nts.uk.shr.com.task.schedule.UkJobScheduler;
@Stateless
@Slf4j
public class SortingProcessCommandHandler extends CommandHandler<ScheduleExecuteCommand> {
	@Inject
	private ExecuteProcessExecutionAutoCommandHandler execHandler;
	@Inject
	private ProcessExecutionLogManageRepository processExecLogManaRepo;
	@Inject
	private ProcessExecutionLogHistRepository processExecLogHistRepo;
	@Inject
	private ExecutionTaskSettingRepository execSettingRepo;
//	@Inject
//	private UkJobScheduler ukJobScheduler;
	@Override //振り分け処理
	protected void handle(CommandHandlerContext<ScheduleExecuteCommand> context) {
		ScheduleExecuteCommand command = context.getCommand();
		String companyId = command.getCompanyId();
		String execItemCd = command.getExecItemCd();
		GeneralDateTime nextDate = command.getNextDate();
		//ドメインモデル「実行タスク設定」を取得する
		Optional<ExecutionTaskSetting> executionTaskSettingOpt = execSettingRepo.getByCidAndExecCd(companyId, command.getExecItemCd());
		if(!executionTaskSettingOpt.isPresent()) {
			return;
		}
		//ドメインモデル「実行タスク設定.更新処理有効設定」をチェックする
		if(!executionTaskSettingOpt.get().isEnabledSetting()) {
			//無効の場合
			return;//フロー終了
		}
		log.info(":更新処理自動実行_START_"+command.getExecItemCd()+"_"+GeneralDateTime.now());
		//ドメインモデル「更新処理自動実行管理」取得する
		Optional<ProcessExecutionLogManage> logManageOpt = this.processExecLogManaRepo.getLogByCIdAndExecCd(companyId, execItemCd);
		if(!logManageOpt.isPresent()){
			return;
		}
		ProcessExecutionLogManage processExecutionLogManage = logManageOpt.get();
		//実行IDを新規採番する
		String execItemId = IdentifierUtil.randomUniqueId();
		//「実行中」
		if(processExecutionLogManage.getCurrentStatus().value==0){
			this.DistributionRegistProcess(companyId, execItemCd, execItemId,  nextDate);
			
		}
		//「待機中」	
		else if(processExecutionLogManage.getCurrentStatus().value==1){
			ExecuteProcessExecutionCommand executeProcessExecutionCommand = new ExecuteProcessExecutionCommand();
			executeProcessExecutionCommand.setCompanyId(companyId);
			executeProcessExecutionCommand.setExecItemCd(execItemCd);
			executeProcessExecutionCommand.setExecId(execItemId);
			executeProcessExecutionCommand.setExecType(0);
			executeProcessExecutionCommand.setNextFireTime(Optional.ofNullable(nextDate));
			//AsyncCommandHandlerContext<ExecuteProcessExecutionCommand> ctxNew = new AsyncCommandHandlerContext<ExecuteProcessExecutionCommand>(executeProcessExecutionCommand);
			this.execHandler.handle(executeProcessExecutionCommand);
		}
		
	}
	//振り分け登録処理
	private void DistributionRegistProcess(String companyId, String execItemCd,String execItemId, GeneralDateTime nextDate ){
		//ドメインモデル「更新処理自動実行管理」を更新する
		ProcessExecutionLogManage processExecutionLogManage = this.processExecLogManaRepo.getLogByCIdAndExecCd(companyId, execItemCd).get();
		processExecutionLogManage.setLastExecDateTimeEx(GeneralDateTime.now());
		processExecLogManaRepo.update(processExecutionLogManage);
		//ドメインモデル「更新処理自動実行ログ履歴」を新規登録する
		List<ExecutionTaskLog> taskLogList = new ArrayList<>();
		taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.SCH_CREATION ,Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
		taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.DAILY_CREATION ,Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
		taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.DAILY_CALCULATION ,Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
		taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.RFL_APR_RESULT ,Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
		taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.MONTHLY_AGGR ,Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
		ProcessExecutionLogHistory processExecutionLogHistory = new ProcessExecutionLogHistory(new ExecutionCode(execItemCd), companyId,OverallErrorDetail.NOT_FINISHED, EndStatus.ABNORMAL_END, GeneralDateTime.now(), null, taskLogList, execItemId);
		processExecLogHistRepo.insert(processExecutionLogHistory);
	
		
		//ドメインモデル「実行タスク設定」を更新する
		Optional<ExecutionTaskSetting> executionTaskSetOpt = this.execSettingRepo.getByCidAndExecCd(companyId, execItemCd);
		if(executionTaskSetOpt.isPresent() && nextDate!=null){
			ExecutionTaskSetting executionTaskSetting = executionTaskSetOpt.get();
			executionTaskSetting.setNextExecDateTime(nextDate);
			this.execSettingRepo.update(executionTaskSetting);
		}
	}

}
