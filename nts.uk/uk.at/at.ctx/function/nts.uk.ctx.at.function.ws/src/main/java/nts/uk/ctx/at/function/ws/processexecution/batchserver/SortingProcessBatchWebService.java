package nts.uk.ctx.at.function.ws.processexecution.batchserver;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.function.app.command.processexecution.ScheduleExecuteCommand;
import nts.uk.ctx.at.function.app.command.processexecution.SortingProcessCommandHandler;

@Path("batch/sorting")
@Produces("application/json")
public class SortingProcessBatchWebService {

	@Inject
	private SortingProcessCommandHandler sortingProcessCommandHandler;
	
	@POST
	@Path("process")
	public void processSort(ScheduleExecuteCommand command) {
		System.out.println("SortingProcessCommandHandler is executed on Batch Server");
		this.sortingProcessCommandHandler.handle(command);
	}
	
}
