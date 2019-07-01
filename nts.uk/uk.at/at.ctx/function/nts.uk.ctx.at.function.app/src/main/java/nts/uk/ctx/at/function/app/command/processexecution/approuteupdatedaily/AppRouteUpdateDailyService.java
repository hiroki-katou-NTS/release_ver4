package nts.uk.ctx.at.function.app.command.processexecution.approuteupdatedaily;

import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
/**
 * 承認ルート更新（日次）
 * @author tutk
 *
 */
public interface AppRouteUpdateDailyService {
	public boolean checkAppRouteUpdateDaily( String execId, ProcessExecution procExec,
			ProcessExecutionLog procExecLog);
}
