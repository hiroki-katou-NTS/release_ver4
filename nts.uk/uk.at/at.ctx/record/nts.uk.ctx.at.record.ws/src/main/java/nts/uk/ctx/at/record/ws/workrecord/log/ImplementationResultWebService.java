package nts.uk.ctx.at.record.ws.workrecord.log;

//import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.val;
import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;
import nts.arc.task.AsyncTaskInfo;
import nts.arc.task.AsyncTaskInfoRepository;
import nts.gul.util.value.MutableValue;
import nts.uk.ctx.at.record.app.command.workrecord.log.AddEmpCalSumAndTargetCommandHandler;
import nts.uk.ctx.at.record.app.command.workrecord.log.AddEmpCalSumAndTargetCommandResult;
import nts.uk.ctx.at.record.app.command.workrecord.log.CheckProcessCommand;
import nts.uk.ctx.at.record.app.command.workrecord.log.AddEmpCalSumAndTargetCommand;
import nts.uk.ctx.at.record.app.command.workrecord.log.CheckProcessCommandHandler;
import nts.uk.ctx.at.record.app.command.workrecord.log.UpdateDailyLogStateCommandHandler;
import nts.uk.ctx.at.record.app.command.workrecord.log.UpdateExecutionTimeCommand;
import nts.uk.ctx.at.record.app.command.workrecord.log.UpdateExecutionTimeCommandHandler;
import nts.uk.ctx.at.record.app.find.log.ImplementationResultFinder;
//import nts.uk.ctx.at.record.app.find.log.dto.PersonInfoErrMessageLogDto;
import nts.uk.ctx.at.record.app.find.log.dto.PersonInfoErrMessageLogResultDto;
import nts.uk.ctx.at.record.app.find.log.dto.ScreenImplementationResultDto;
import nts.uk.ctx.at.record.ws.workrecord.log.batchserver.ImplResultDto;
import nts.uk.shr.com.communicate.PathToWebApi;
import nts.uk.shr.com.communicate.batch.BatchServer;

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
	
	@Inject
	private UpdateExecutionTimeCommandHandler updateExecutionTimeCommandHandler;
	
	@Inject
	private UpdateDailyLogStateCommandHandler updateDailyLogStateCommandHandler;
	
	@Inject
	private BatchServer batchServer;

	@Inject
	private AsyncTaskInfoRepository asyncTaskInfoRepository;
	
	@POST
	@Path("addEmpCalSumAndTarget")
	public AddEmpCalSumAndTargetCommandResult addEmpCalSumAndTarget(AddEmpCalSumAndTargetCommand command) {
		return addEmpCalSumAndTargetCommandHandler.handle(command);
	}

	@POST
	@Path("checkprocess")
	public AsyncTaskInfo executeTask(CheckProcessCommand command) {
		MutableValue<AsyncTaskInfo> result = new MutableValue<>();
		
		if (this.batchServer.exists()) {
			System.out.println("Call batch service  !");
			val webApi = this.batchServer.webApi(PathToWebApi.at("/batch/task-result"), CheckProcessCommand.class, ImplResultDto.class);
			this.batchServer.request(webApi, c -> c.entity(command)
					.succeeded(x -> {
						String taskId = x.getId();
						AsyncTaskInfo taskInfo = asyncTaskInfoRepository.find(taskId).get();
						result.set(taskInfo);
			}).failed(f -> {
				throw new RuntimeException(f.toString());
			})
					);
		} else {
			System.out.println("No call batch service  !");
			result.set(queryExecutionStatusCommandHandler.handle(command));
		}
		return result.get();
	}

	@POST
	@Path("getErrorMessageInfo")
	public PersonInfoErrMessageLogResultDto getByEmpCalAndSumExecLogID(ScreenImplementationResultDto screenImplementationResultDto) {
		PersonInfoErrMessageLogResultDto data = null;
		if(screenImplementationResultDto.getEmployeeID() == null || screenImplementationResultDto.getEmployeeID().isEmpty()) {
			data = implementationResultFinder.getScreenImplementationResult(screenImplementationResultDto); 
		} else {
			data = implementationResultFinder.getScreenImplementationResultWithEmployees(screenImplementationResultDto); 			
		}
		return data;
	}
	
	@POST
	@Path("updateExcutionTime")
	public void updateExcutionTime(UpdateExecutionTimeCommand command) {
		this.updateExecutionTimeCommandHandler.handle(command);
	}
	
	@POST
	@Path("updateLogState")
	public JavaTypeResult<String> updateLogState(String empCalAndSumExecLogID) {
		return new JavaTypeResult<String>(this.updateDailyLogStateCommandHandler.handle(empCalAndSumExecLogID));
	}

}
