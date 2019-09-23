package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

//import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.AsyncTaskService;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.CurrentExecutionStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.OverallErrorDetail;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogHistory;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogHistRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogManageRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExeStateOfCalAndSum;
@Stateless
public class TerminateProcessExecutionAutoCommandHandler extends AsyncCommandHandler<TerminateProcessExecutionCommand> {

	@Inject
	private ProcessExecutionLogRepository procExecLogRepo;
	
	@Inject
	private EmpCalAndSumExeLogRepository empCalSumRepo;
	
	@Inject
	private ProcessExecutionLogManageRepository processExecLogManRepo;
	
	@Inject
	private AsyncTaskService service;
	
	@Inject
	private ProcessExecutionLogHistRepository processExecutionLogHistRepo;

	//終了ボタン押下時処理
	@Override
	public void handle(CommandHandlerContext<TerminateProcessExecutionCommand> context) {
//		val asyncContext = context.asAsync();
		TerminateProcessExecutionCommand command = context.getCommand();
		String execItemCd = command.getExecItemCd();
		String companyId = command.getCompanyId();
		String taskTerminate = command.getTaskTerminate();
		//ドメインモデル「更新処理自動実行管理」を取得する
		Optional<ProcessExecutionLogManage> processExecLogManOpt = this.processExecLogManRepo.getLogByCIdAndExecCd(companyId, execItemCd);
		if(!processExecLogManOpt.isPresent()){
			return;
		}
		// ドメインモデル「更新処理自動実行管理」．全体の終了状態 をチェックする
		if (processExecLogManOpt.get().getOverallStatus().isPresent()) {
			if (processExecLogManOpt.get().getOverallStatus().get() == EndStatus.SUCCESS
					|| processExecLogManOpt.get().getOverallStatus().get() == EndStatus.FORCE_END) {
				return;
			}
		}
		
		ProcessExecutionLogManage processExecLogMan =processExecLogManOpt.get();
		
		//「待機中」 or 「無効」の場合
		if(processExecLogMan.getCurrentStatus().value==1 || processExecLogMan.getCurrentStatus().value==2){
//			dataSetter.setData("currentStatusIsOneOrTwo", "Msg_1102");
			return;
		}
		
		// ドメインモデル「更新処理自動実行ログ」を取得する
		ProcessExecutionLog procExecLog = null;
		Optional<ProcessExecutionLog> procExecLogOpt =
							this.procExecLogRepo.getLog(companyId, execItemCd);
		if (!procExecLogOpt.isPresent()) {
			return;
		}
		
		procExecLog = procExecLogOpt.get();
		
		//アルゴリズム[全体エラー状況確認処理]を実行する
		//ErrorConditionOutput errorCondition = overallErrorProcess.overallErrorProcess(procExecLog);
		/*
		 * ドメインモデル「更新処理自動実行管理」を更新する
		 * 
		 *	【終了タイプ　＝　1（F画面終了ボタン）の場合】
		 *	全体の終了状態　＝　終了中
		 *	強制終了の原因　＝　画面から強制終了しました。
		 *	【終了タイプ　＝　0（終了時刻）の場合】
		 *	強制終了の原因　＝　更新処理の途中で終了時刻を超過したため中断しました。
		 *	全体の終了状態　＝　終了中
		 */	
		int execType = command.getExecType();
		if (execType == 1) {
			processExecLogMan.setOverallStatus(EndStatus.CLOSING);
			processExecLogMan.setOverallError(OverallErrorDetail.TERMINATED);
		} else if (execType == 0) {
			processExecLogMan.setOverallStatus(EndStatus.CLOSING);
			processExecLogMan.setOverallError(OverallErrorDetail.EXCEED_TIME);
		}
		processExecLogMan.setCurrentStatus(CurrentExecutionStatus.WAITING);
		this.processExecLogManRepo.update(processExecLogMan);
		
		/*【登録内容】
		・会社ID　＝　取得した会社ID
		・実行ID　＝　「更新処理自動実行ログ」.実行ID
		・コード　＝　取得した更新処理自動実行項目コード
		・前回実行日時　＝　「更新処理自動実行管理」.前回実行日時
		・各処理の終了状態(List)　＝　「更新処理自動実行ログ」.各処理の終了状態（List）
		・全体の終了状態　＝　「更新処理自動実行管理」.全体の終了状態
		・全体のエラー詳細　＝　「更新処理自動実行管理」.全体のエラー詳細
		・各処理の期間　＝　「更新処理自動実行ログ」.各処理の期間
		*/
		procExecLog.getTaskLogList().stream().sorted((x,y)->x.getProcExecTask().value-y.getProcExecTask().value).collect(Collectors.toList());
		ProcessExecutionLogHistory processExecutionLogHistory = new ProcessExecutionLogHistory(
				new ExecutionCode(execItemCd), 
				companyId, 
				processExecLogMan.getOverallError(), 
				processExecLogMan.getOverallStatus().get(), 
				processExecLogMan.getLastExecDateTime(),
				!procExecLog.getEachProcPeriod().isPresent()?null : procExecLog.getEachProcPeriod().get(), 
				procExecLog.getTaskLogList(), 
				procExecLog.getExecId(),
				processExecLogMan.getLastEndExecDateTime(),
				processExecLogMan.getErrorSystem(),
				processExecLogMan.getErrorBusiness()) ;
		//ドメインモデル「更新処理自動実行ログ履歴」を追加する
		processExecutionLogHistRepo.insert(processExecutionLogHistory);
		String execId = procExecLog.getExecId();
		//スケジュールの作成の処理が完了しているか確認する
		//TODO NO3  fixed da tao schedule
		/*
		if(execType == 1){ // dung cho NO3 o tren
			dataSetter.setData("interupt", "true");
		}
		*/
		ProcessExecutionTask statusStop = null;
		for(ExecutionTaskLog task : procExecLog.getTaskLogList()) {
		//procExecLog.getTaskLogList().forEach(task ->{
			if (task.getProcExecTask().value == ProcessExecutionTask.SCH_CREATION.value) {
				
				if (task.getStatus() == null || !task.getStatus().isPresent()) {
					if(taskTerminate!=null&& !"".equals(taskTerminate)){
						service.requestToCancel(taskTerminate);	
					}
					System.out.println("update trang thai dung xu ly tao schdule cua anh Son");
					this.interupt(execId, ExeStateOfCalAndSum.START_INTERRUPTION.value);
					statusStop = task.getProcExecTask();
					//break;
				}
			}
			 //日別作成の処理が完了しているか確認する
				else if (task.getProcExecTask().value == ProcessExecutionTask.DAILY_CREATION.value) {
				if (task.getStatus() == null || !task.getStatus().isPresent()) {
					if(taskTerminate!=null&& !"".equals(taskTerminate)){
						service.requestToCancel(taskTerminate);	
					}
					//TU_DEBUG
					System.out.println("update trang thai dung xu ly tao data cua anh Nam");
					this.interupt(execId, ExeStateOfCalAndSum.START_INTERRUPTION.value);
					statusStop = task.getProcExecTask();
//					break;
				}
			//日別計算の処理が完了しているか確認する	
			} else if (task.getProcExecTask().value == ProcessExecutionTask.DAILY_CALCULATION.value) {
				if (task.getStatus() == null || !task.getStatus().isPresent()) {
					if(taskTerminate!=null&& !"".equals(taskTerminate)){
						service.requestToCancel(taskTerminate);
						}
					//TU_DEBUG
					System.out.println("update trang thai dung xu ly tinh toan data");
					this.interupt(execId, ExeStateOfCalAndSum.START_INTERRUPTION.value);
					statusStop = task.getProcExecTask();
//					break;
				}
			//承認結果反映の処理が完了しているか確認する	
			} else if (task.getProcExecTask().value == ProcessExecutionTask.RFL_APR_RESULT.value) {
				if (task.getStatus() == null || !task.getStatus().isPresent()) {
					if(taskTerminate!=null&& !"".equals(taskTerminate)){
						service.requestToCancel(taskTerminate);
						}
					this.interupt(execId, ExeStateOfCalAndSum.START_INTERRUPTION.value);
					statusStop = task.getProcExecTask();
//					break;
				}
			//月別集計の処理が完了しているか確認する	
			} else if (task.getProcExecTask().value == ProcessExecutionTask.MONTHLY_AGGR.value) {
				if (task.getStatus() == null || !task.getStatus().isPresent()) {
					if(taskTerminate!=null && !"".equals(taskTerminate)){
						service.requestToCancel(taskTerminate);
					}
					this.interupt(execId, ExeStateOfCalAndSum.START_INTERRUPTION.value);
					statusStop = task.getProcExecTask();
//					break;
				}
			//アラーム抽出を中断する
			} else if (task.getProcExecTask().value == ProcessExecutionTask.AL_EXTRACTION.value) {
				if (task.getStatus() == null || !task.getStatus().isPresent()) {
					if(taskTerminate!=null && !"".equals(taskTerminate)){
						service.requestToCancel(taskTerminate);
					}
					this.interupt(execId, ExeStateOfCalAndSum.START_INTERRUPTION.value);
					statusStop = task.getProcExecTask();
//					break;
				}
			} else{
//				if(execType == 1){ 
//					dataSetter.setData("interupt", "true");
//				}
			} 
		}
		/*
		 * ドメインモデル「就業計算と集計実行ログ」を更新する
		 * 
		 * 【条件】
		 * ID　＝　取得した実行ID
		 * 会社ID　＝　取得した会社ID
		 * 
		 * 【更新内容】
		 * 就業計算と集計実行ログ．実行状況 ← 中断終了
		 */
//		this.interupt(execId, ExeStateOfCalAndSum.END_INTERRUPTION.value);
		//ドメインモデル「更新処理自動実行ログ履歴」を更新する
		for(ExecutionTaskLog task : processExecutionLogHistory.getTaskLogList()) {
			if(task.getStatus() == null || !task.getStatus().isPresent()) {
				if(task.getProcExecTask() == statusStop) {
					task.setStatus(Optional.of(EndStatus.FORCE_END));
					for(ExecutionTaskLog taskAfter  : processExecutionLogHistory.getTaskLogList()) {
						if(taskAfter.getProcExecTask().value >task.getProcExecTask().value) {
							taskAfter.setStatus(Optional.of(EndStatus.NOT_IMPLEMENT));
						}
					}
					
					break;
				}
			}
		}
		
		this.processExecutionLogHistRepo.update(processExecutionLogHistory);;
	}
	
	/**
	 * ドメインモデル「就業計算と集計実行ログ」を更新する
	 * @param execId
	 * @param status
	 */
	//日別作成を中断する
	private void interupt(String execId, int status) {
		/* ドメインモデル「就業計算と集計実行ログ」を更新する
		 * 
		 * 【条件】
		 * ID　＝　実行ID
		 * 会社ID　＝　取得した会社ID
		 * 
		 * 【更新内容】
		 * 就業計算と集計実行ログ．実行状況 ← 中断開始
		 */
		this.empCalSumRepo.updateStatus(execId, status);
	}
}
