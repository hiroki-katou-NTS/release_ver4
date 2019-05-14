package nts.uk.ctx.at.function.ws.processexecution;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import lombok.val;
import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;
import nts.arc.task.AsyncTaskInfo;
import nts.arc.task.AsyncTaskInfoRepository;
import nts.gul.util.value.MutableValue;
//import nts.uk.ctx.at.function.app.command.processexecution.ExecuteProcessExecCommandHandler;
import nts.uk.ctx.at.function.app.command.processexecution.ExecuteProcessExecutionCommand;
import nts.uk.ctx.at.function.app.command.processexecution.ExecuteProcessExecutionCommandHandler;
import nts.uk.ctx.at.function.app.command.processexecution.RemoveProcessExecutionCommand;
import nts.uk.ctx.at.function.app.command.processexecution.RemoveProcessExecutionCommandHandler;
import nts.uk.ctx.at.function.app.command.processexecution.SaveExecutionTaskSettingCommand;
import nts.uk.ctx.at.function.app.command.processexecution.SaveExecutionTaskSettingCommandHandler;
import nts.uk.ctx.at.function.app.command.processexecution.SaveProcessExecutionCommand;
import nts.uk.ctx.at.function.app.command.processexecution.SaveProcessExecutionCommandHandler;
import nts.uk.ctx.at.function.app.command.processexecution.TerminateProcessExecutionCommand;
import nts.uk.ctx.at.function.app.command.processexecution.TerminateProcessExecutionCommandHandler;
import nts.uk.ctx.at.function.app.find.processexecution.ExecutionTaskSettingFinder;
import nts.uk.ctx.at.function.app.find.processexecution.ProcessExecutionFinder;
import nts.uk.ctx.at.function.app.find.processexecution.ProcessExecutionLogFinder;
import nts.uk.ctx.at.function.app.find.processexecution.ProcessExecutionLogHistoryFinder;
import nts.uk.ctx.at.function.app.find.processexecution.dto.ExecItemEnumDto;
import nts.uk.ctx.at.function.app.find.processexecution.dto.ExecutionTaskSettingDto;
import nts.uk.ctx.at.function.app.find.processexecution.dto.ProcessExecutionDateParam;
import nts.uk.ctx.at.function.app.find.processexecution.dto.ProcessExecutionDto;
import nts.uk.ctx.at.function.app.find.processexecution.dto.ProcessExecutionLogDto;
import nts.uk.ctx.at.function.app.find.processexecution.dto.ProcessExecutionLogHistoryDto;
import nts.uk.ctx.at.function.ws.processexecution.batchserver.BatchTaskResult;
import nts.uk.shr.com.communicate.PathToWebApi;
import nts.uk.shr.com.communicate.batch.BatchServer;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

@Path("at/function/processexec")
@Produces("application/json")
public class ProcessExecutionWebService extends WebService {
	
	/* Finder */
	@Inject
	private ProcessExecutionFinder procExecFinder;
	
	@Inject
	private ProcessExecutionLogFinder execLogFinder;
	
	@Inject
	private ExecutionTaskSettingFinder execSettingFinder;
	
	@Inject
	private ProcessExecutionLogHistoryFinder logHistFinder;
	
	/* Handler */
	@Inject
	private SaveProcessExecutionCommandHandler saveProcExecHandler;
	
	@Inject
	private RemoveProcessExecutionCommandHandler removeProcExecHandler;
	
	@Inject
	private SaveExecutionTaskSettingCommandHandler saveExecSettingHandler;
	
	/* Executor */
	@Inject
	private ExecuteProcessExecutionCommandHandler execHandler;
	
	@Inject
	private TerminateProcessExecutionCommandHandler termHandler;
	
	/** The i18n. */
	@Inject
	private I18NResourcesForUK i18n;
	
	@Inject
	private BatchServer batchServer;
	
	@Inject
	private AsyncTaskInfoRepository asyncTaskInfoRepository;
	
	/**
	 * Gets the enum.
	 *
	 * @return the enum
	 */
	@POST
	@Path("getEnum")
	public ExecItemEnumDto getEnum() {
		return ExecItemEnumDto.init(i18n);
	}
	
	@POST
	@Path("getProcExecList")
	public List<ProcessExecutionDto> getProcExecList() {
		return this.procExecFinder.findAll();
	}
	
	@POST
	@Path("saveProcExec")
	public JavaTypeResult<String> add(SaveProcessExecutionCommand command) {
		return new JavaTypeResult<String>(this.saveProcExecHandler.handle(command));
	}
	
	@POST
	@Path("removeProcExec")
	public void remove(RemoveProcessExecutionCommand command) {
		this.removeProcExecHandler.handle(command);
	}
	
	@POST
	@Path("getExecSetting/{execItemCd}")
	public ExecutionTaskSettingDto getExecutionSetting(@PathParam("execItemCd") String execItemCd) {
		return this.execSettingFinder.find(execItemCd);
	}
	
	@POST
	@Path("saveExecSetting")
	public JavaTypeResult<String> saveExecutionSetting(SaveExecutionTaskSettingCommand command) {
		return new JavaTypeResult<String>(this.saveExecSettingHandler.handle(command));
	}
	
	@POST
	@Path("getProcExecLogList")
	public List<ProcessExecutionLogDto> getProcExecLogList() {
		return this.execLogFinder.findAll();
	}
	
	@POST
	@Path("execute")
	public AsyncTaskInfo execute(ExecuteProcessExecutionCommand command) {
		
		MutableValue<AsyncTaskInfo> result = new MutableValue<>();
		
		if (this.batchServer.exists()) {
			System.out.println("Call batch service  !");
			val webApi = this.batchServer.webApi(PathToWebApi.at("/batch/batch-execute"), 
					ExecuteProcessExecutionCommand.class, BatchTaskResult.class);
			this.batchServer.request(webApi, c -> c.entity(command)
					.succeeded(x -> {
						String taskId = x.getId();
						AsyncTaskInfo taskInfo = asyncTaskInfoRepository.find(taskId).get();
						result.set(taskInfo);
			})
					.failed(f -> {
						throw new RuntimeException(f.toString());
					}));
		} else {
			System.out.println("No call batch service !");
			result.set(this.execHandler.handle(command));
			
		}
		
		return result.get();
	}
	
	@POST
	@Path("terminate")
	public void terminate(TerminateProcessExecutionCommand command) {
//		MutableValue<AsyncTaskInfo> result = new MutableValue<>();
//		
//		if (this.batchServer.exists()) {
//			val webApi = this.batchServer.webApi(PathToWebApi.at("/batch/batch-terminate"), 
//					TerminateProcessExecutionCommand.class, BatchTaskResult.class);
//			this.batchServer.request(webApi, c -> c.entity(command)
//					.succeeded(x -> {
//						String taskId = x.getId();
//						AsyncTaskInfo taskInfo = asyncTaskInfoRepository.find(taskId).get();
//						result.set(taskInfo);
//			}));
//		} else {
//			this.termHandler.handle(command);
//		}
		this.termHandler.handle(command);
		//return result.get();
	}
	
	@POST
	@Path("getLogHistory/{execItemCd}/{execId}")
	public ProcessExecutionLogHistoryDto getProcessExecutionLogHistory(@PathParam("execItemCd") String execItemCd, @PathParam("execId") String execId) {
		String companyId = AppContexts.user().companyId();
		return this.logHistFinder.find(companyId, execItemCd, execId);
	}
	@POST
	@Path("getLogHistoryBySystemDates/{execItemCd}")
	public List<ProcessExecutionLogHistoryDto> getLogHistoryBySystemDates(@PathParam("execItemCd") String execItemCd){
		return logHistFinder.findList(execItemCd);
	}
	@POST
	@Path("findListDateRange")
	public List<ProcessExecutionLogHistoryDto> findListDateRange(ProcessExecutionDateParam Param){
		return logHistFinder.findListDateRange(Param.getExecItemCd(), Param.getStartDate(),Param.getEndDate());
	}
}
