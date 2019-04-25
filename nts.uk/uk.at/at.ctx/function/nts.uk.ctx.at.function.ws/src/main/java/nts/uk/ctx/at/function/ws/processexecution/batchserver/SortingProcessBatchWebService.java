package nts.uk.ctx.at.function.ws.processexecution.batchserver;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.extern.slf4j.Slf4j;
import nts.uk.ctx.at.function.app.command.processexecution.ScheduleExecuteCommand;
import nts.uk.ctx.at.function.app.command.processexecution.SortingProcessCommandHandler;

@Path("batch/sorting")
@Produces("application/json")
@Slf4j
public class SortingProcessBatchWebService {

	@Inject
	private SortingProcessCommandHandler sortingProcessCommandHandler;
	
	@POST
	@Path("process")
	public void processSort(ScheduleExecuteCommand command) {
		log.info("SortingProcessCommandHandler is executed on Batch Server");
		this.sortingProcessCommandHandler.handle(command);
	}
	
}
