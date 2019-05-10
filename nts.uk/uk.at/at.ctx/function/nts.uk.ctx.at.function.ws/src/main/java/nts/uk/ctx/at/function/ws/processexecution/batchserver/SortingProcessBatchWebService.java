package nts.uk.ctx.at.function.ws.processexecution.batchserver;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.extern.slf4j.Slf4j;
import nts.arc.scoped.ScopedContext;
import nts.arc.scoped.request.thread.ThreadRequestContextHolder;
import nts.arc.scoped.session.thread.ThreadSessionContextHolder;
import nts.gul.serialize.ObjectSerializer;
import nts.uk.ctx.at.function.app.command.processexecution.AsyncSortingProcessCommandHandler;
import nts.uk.ctx.at.function.app.command.processexecution.ScheduleExecuteCommand;

@Path("batch/sorting")
@Produces("application/json")
@Slf4j
public class SortingProcessBatchWebService {

	@Inject
	private AsyncSortingProcessCommandHandler sortingProcessCommandHandler;
	
	@POST
	@Path("process")
	public void processSort(ScheduleExecuteCommand.ForBatchServer command) {
		log.info("SortingProcessCommandHandler is executed on Batch Server");
		
		String[] contexts = command.getContexts().split("\t");
		ScopedContext session = ObjectSerializer.restore(contexts[0]);
		ScopedContext request = ObjectSerializer.restore(contexts[1]);
		ThreadSessionContextHolder.instance().set(session);
		ThreadRequestContextHolder.instance().set(request);
		
		this.sortingProcessCommandHandler.handle(command.getCommand());
	}
	
}
