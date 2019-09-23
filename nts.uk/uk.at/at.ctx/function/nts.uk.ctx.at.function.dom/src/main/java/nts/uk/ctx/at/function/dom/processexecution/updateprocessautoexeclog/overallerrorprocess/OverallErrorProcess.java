package nts.uk.ctx.at.function.dom.processexecution.updateprocessautoexeclog.overallerrorprocess;

import javax.ejb.Stateless;

import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
/**
 * 全体エラー状況確認処理
 * @author tutk
 *
 */
@Stateless
public class OverallErrorProcess {
	
	public ErrorConditionOutput overallErrorProcess(ProcessExecutionLog procExecLog) {
		boolean isHasErrorSystem = false;
		boolean isHasErrorBussiness = false;
		//【INPUT「更新処理自動実行ログ」．各処理の終了状態．システムエラー状態】をチェックする
		for(ExecutionTaskLog taskLog : procExecLog.getTaskLogList()) {
			if(taskLog.getErrorSystem() != null && taskLog.getErrorSystem() == true) {
				isHasErrorSystem = true;
				break;
			}
		}
		//【INPUT「更新処理自動実行ログ」．各処理の終了状態．業務エラー状態】をチェックする
		for(ExecutionTaskLog taskLog : procExecLog.getTaskLogList()) {
			if(taskLog.getErrorBusiness() != null && taskLog.getErrorBusiness() == true) {
				isHasErrorBussiness = true;
				break;
			}
		}
		return new ErrorConditionOutput(isHasErrorBussiness,isHasErrorSystem);
	}
}
