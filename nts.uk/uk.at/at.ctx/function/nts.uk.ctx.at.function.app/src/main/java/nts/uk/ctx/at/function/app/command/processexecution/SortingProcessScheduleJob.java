package nts.uk.ctx.at.function.app.command.processexecution;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.uk.shr.com.communicate.PathToWebApi;
import nts.uk.shr.com.communicate.batch.BatchServer;
//import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.uk.shr.com.task.schedule.ExecutionContext;
import nts.uk.shr.com.task.schedule.UkScheduledJob;

@Stateless
public class SortingProcessScheduleJob extends UkScheduledJob {

	@Inject
	private SortingProcessCommandHandler sortingProcessCommandHandler;

	@Inject
	private BatchServer batchServer;

	@Override
	protected void execute(ExecutionContext context) {

		String companyId = context.scheduletimeData().getString("companyId");
		String execItemCd = context.scheduletimeData().getString("execItemCd");
		ScheduleExecuteCommand s = new ScheduleExecuteCommand();
		s.setCompanyId(companyId);
		s.setExecItemCd(execItemCd);
		s.setNextDate(context.nextFireTime());

		if (this.batchServer.exists()) {
			System.out.println("Sent a request SortingProcessCommandHandler to Batch Server");
			val webApi = this.batchServer.webApi(PathToWebApi.at("/batch/sorting/process"),
					ScheduleExecuteCommand.class);
			this.batchServer.request(webApi, c -> c.entity(s));
		} else {
			System.out.println("SortingProcessCommandHandler is executed on AP Server");
			this.sortingProcessCommandHandler.handle(s);
		}

	}

}
