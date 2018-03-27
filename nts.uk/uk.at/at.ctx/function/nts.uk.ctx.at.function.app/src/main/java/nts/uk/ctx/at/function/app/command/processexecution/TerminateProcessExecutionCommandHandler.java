package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.OverallErrorDetail;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExeStateOfCalAndSum;

@Stateless
public class TerminateProcessExecutionCommandHandler extends AsyncCommandHandler<TerminateProcessExecutionCommand> {

	@Inject
	private ProcessExecutionLogRepository procExecLogRepo;
	
	@Inject
	private EmpCalAndSumExeLogRepository empCalSumRepo;
	
	/**
	 * 終了処理を実行する
	 * 
	 * @param execType
	 * @param execItemCd
	 * @param companyId
	 */
	@Override
	protected void handle(CommandHandlerContext<TerminateProcessExecutionCommand> context) {
		TerminateProcessExecutionCommand command = context.getCommand();
		String execItemCd = command.getExecItemCd();
		String companyId = command.getCompanyId();
		String execId = command.getExecId();
		// ドメインモデル「更新処理自動実行ログ」を取得する
		ProcessExecutionLog procExecLog = null;
		Optional<ProcessExecutionLog> procExecLogOpt =
							this.procExecLogRepo.getLogByCIdAndExecCd(companyId, execItemCd, execId);
		if (!procExecLogOpt.isPresent()) {
			return;
		}
		procExecLog = procExecLogOpt.get();
		/*
		 * ドメインモデル「更新処理自動実行ログ」を取得する
		 * 
		 * 【終了タイプ　＝　1（F画面終了ボタン）の場合】
		 * 全体の終了状態　＝　強制終了
		 * 
		 * 【終了タイプ　＝　0（終了時刻）の場合】
		 * 全体の終了状態　＝　異常終了
		 * 全体のエラー詳細　＝　更新処理の途中で終了時刻を超過したため中断しました
		 */	
		int execType = command.getExecType();
		if (execType == 1) {
			procExecLog.setOverallStatus(EndStatus.FORCE_END);
		} else if (execType == 0) {
			procExecLog.setOverallStatus(EndStatus.ABNORMAL_END);
			procExecLog.setOverallError(OverallErrorDetail.EXCEED_TIME);
		}
		this.procExecLogRepo.update(procExecLog);
		
		/*
		 * 日別作成の処理が完了しているか確認する
		 */
		procExecLog.getTaskLogList().forEach(task ->{
			if (task.getProcExecTask().value == ProcessExecutionTask.DAILY_CREATION.value) {
				if (task.getStatus() == null) {
					this.interupt(execId, ExeStateOfCalAndSum.START_INTERRUPTION.value);
					return;
				}
			} else if (task.getProcExecTask().value == ProcessExecutionTask.DAILY_CALCULATION.value) {
				if (task.getStatus() == null) {
					this.interupt(execId, ExeStateOfCalAndSum.START_INTERRUPTION.value);
					return;
				}
			}
		});
		
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
		this.interupt(execId, ExeStateOfCalAndSum.END_INTERRUPTION.value);
	}
	
	/**
	 * ドメインモデル「就業計算と集計実行ログ」を更新する
	 * @param execId
	 * @param status
	 */
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
