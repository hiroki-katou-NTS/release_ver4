package nts.uk.ctx.at.record.ws.workrecord.log;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.arc.task.AsyncTaskInfo;
import nts.uk.ctx.at.record.app.command.workrecord.log.AddEmpCalSumAndTargetCommandHandler;
import nts.uk.ctx.at.record.app.command.workrecord.log.AddEmpCalSumAndTargetCommandResult;
import nts.uk.ctx.at.record.app.command.workrecord.log.CheckProcessCommand;
import nts.uk.ctx.at.record.app.command.workrecord.log.AddEmpCalSumAndTargetCommand;
import nts.uk.ctx.at.record.app.command.workrecord.log.CheckProcessCommandHandler;
import nts.uk.ctx.at.record.app.find.log.ImplementationResultFinder;
import nts.uk.ctx.at.record.app.find.log.dto.PersonInfoErrMessageLogDto;
import nts.uk.ctx.at.record.app.find.log.dto.ScreenImplementationResultDto;

/**
 * 
 * @author hieult
 *
 */
@Path("/at/record/log")
@Produces("application/json")
public class ImplementationResultWebService extends WebService {

	@Inject
	private ImplementationResultFinder implementationResultFinder;

	@Inject
	private CheckProcessCommandHandler queryExecutionStatusCommandHandler;

	@Inject
	private AddEmpCalSumAndTargetCommandHandler addEmpCalSumAndTargetCommandHandler;

	@POST
	@Path("addEmpCalSumAndTarget")
	public AddEmpCalSumAndTargetCommandResult addEmpCalSumAndTarget(AddEmpCalSumAndTargetCommand command) {
		return addEmpCalSumAndTargetCommandHandler.handle(command);
	}

	@POST
	@Path("checkprocess")
	public AsyncTaskInfo executeTask(CheckProcessCommand command) {
		return queryExecutionStatusCommandHandler.handle(command);
	}

	@POST
	@Path("getErrorMessageInfo")
	public List<PersonInfoErrMessageLogDto> getByEmpCalAndSumExecLogID(ScreenImplementationResultDto screenImplementationResultDto) {
		List<PersonInfoErrMessageLogDto> data = implementationResultFinder.getScreenImplementationResult(screenImplementationResultDto); 
		return data;
	}

}
