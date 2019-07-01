package nts.uk.ctx.at.function.app.command.processexecution.approuteupdatemonthly;

import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;

public interface AppRouteUpdateMonthlyService {
	public boolean checkAppRouteUpdateMonthly( String execId, ProcessExecution procExec,
			ProcessExecutionLog procExecLog);
}
