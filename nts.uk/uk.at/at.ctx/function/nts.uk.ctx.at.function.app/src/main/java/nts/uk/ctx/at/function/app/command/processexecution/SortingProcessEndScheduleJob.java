package nts.uk.ctx.at.function.app.command.processexecution;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.uk.shr.com.task.schedule.ExecutionContext;
import nts.uk.shr.com.task.schedule.UkScheduledJob;

@Stateless
public class SortingProcessEndScheduleJob extends UkScheduledJob{

	@Inject
	private TerminateProcessExecutionCommandHandler terminateProcessExecutionCommandHandler;
	
	@Override
	protected void execute(ExecutionContext context) {
		/*
		String companyId = context.scheduletimeData().getString("companyId");
		String execItemCd = context.scheduletimeData().getString("execItemCd");
		TerminateProcessExecutionCommand t = new TerminateProcessExecutionCommand();
		t.setCompanyId(companyId);
		t.setExecItemCd(execItemCd);
		t.setExecType(0);
		AsyncCommandHandlerContext<TerminateProcessExecutionCommand> ctxRe = new AsyncCommandHandlerContext<TerminateProcessExecutionCommand>(t);
		this.terminateProcessExecutionCommandHandler.handle(ctxRe);
		*/
	}

}
