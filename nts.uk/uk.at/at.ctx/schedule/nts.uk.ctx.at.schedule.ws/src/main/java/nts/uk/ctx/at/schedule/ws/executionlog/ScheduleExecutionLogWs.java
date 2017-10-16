/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.ws.executionlog;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.arc.task.AsyncTaskInfo;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommandHandler;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionRespone;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleExecutionLogAddCommand;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleExecutionLogAddCommandHandler;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleExecutionLogSaveRespone;
import nts.uk.ctx.at.schedule.app.find.executionlog.ScheduleCreateContentFinder;
import nts.uk.ctx.at.schedule.app.find.executionlog.ScheduleCreatorFinder;
import nts.uk.ctx.at.schedule.app.find.executionlog.ScheduleErrorLogFinder;
import nts.uk.ctx.at.schedule.app.find.executionlog.ScheduleExecutionLogFinder;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.PeriodObject;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.ScheduleCreateContentDto;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.ScheduleCreatorDto;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.ScheduleErrorLogDto;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.ScheduleExecutionLogDto;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.ScheduleExecutionLogInfoDto;
import nts.uk.ctx.at.schedule.app.find.executionlog.export.ExeErrorLogExportService;

/**
 * The Class ScheduleExecutionLogWs.
 */
@Path("at/schedule/exelog")
@Produces(MediaType.APPLICATION_JSON)
public class ScheduleExecutionLogWs extends WebService {

	/** The schedule execution log finder. */
	@Inject
	private ScheduleExecutionLogFinder scheduleExecutionLogFinder;
	
	/** The add. */
	@Inject
	private ScheduleCreateContentFinder scheduleCreateContentFinder;
	
	/** The schedule creator finder. */
	@Inject
	private ScheduleCreatorFinder scheduleCreatorFinder;
	
	/** The schedule error log finder. */
	@Inject
	private ScheduleErrorLogFinder scheduleErrorLogFinder;
	
	/** The add. */
	@Inject
	private ScheduleExecutionLogAddCommandHandler add;
	
	/** The execution. */
	@Inject
	private ScheduleCreatorExecutionCommandHandler execution;

	@Inject
	private ExeErrorLogExportService exeErrorLogExportService;

	/**
	 * Find all exe log.
	 *
	 * @return the schedule execution log dto
	 */
	@POST
	@Path("findAll")
	public List<ScheduleExecutionLogDto> findAllExeLog(PeriodObject periodObj) {
		return this.scheduleExecutionLogFinder.findByDate(periodObj);
	}
	
	/**
	 * Find by id.
	 *
	 * @param executionId the execution id
	 * @return the schedule execution log dto
	 */
	@POST
	@Path("findById/{executionId}")
	public ScheduleExecutionLogDto findById(@PathParam("executionId") String executionId) {
		return this.scheduleExecutionLogFinder.findById(executionId);
	}
	
	/**
	 * Find info by id.
	 *
	 * @param executionId the execution id
	 * @return the schedule execution log info dto
	 */
	@POST
	@Path("findInfoById/{executionId}")
	public ScheduleExecutionLogInfoDto findInfoById(@PathParam("executionId") String executionId) {
		return this.scheduleExecutionLogFinder.findInfoById(executionId);
	}
 
	/**
	 * Find create content by exe id.
	 *
	 * @param executionId the execution id
	 * @return the schedule create content dto
	 */
	@POST
	@Path("createContent/{executionId}")
	public ScheduleCreateContentDto findCreateContentByExeId(@PathParam("executionId") String executionId) {
		return this.scheduleCreateContentFinder.findByExecutionId(executionId);
	}
	
	/**
	 * Find all creator.
	 *
	 * @param executionId the execution id
	 * @return the list
	 */
	@POST
	@Path("findAllCreator/{executionId}")
	public List<ScheduleCreatorDto> findAllCreator(@PathParam("executionId") String executionId) {
		return this.scheduleCreatorFinder.findAllByExeId(executionId);
	}
	
	/**
	 * Find all error.
	 *
	 * @param executionId the execution id
	 * @return the list
	 */
	@POST
	@Path("findAllError/{executionId}")
	public List<ScheduleErrorLogDto> findAllError(@PathParam("executionId") String executionId) {
		return this.scheduleErrorLogFinder.findAllByExeId(executionId);
	}
	
	/**
	 * Export error.
	 *
	 * @param executionId the execution id
	 */
	@POST
	@Path("error/export/{executionId}")
	public ExportServiceResult exportError(@PathParam("executionId") String executionId) {
		return this.exeErrorLogExportService.start(executionId);
	}
	
	/**
	 * Save.
	 *
	 * @param command the command
	 * @return the java type result
	 */
	@POST
	@Path("add")
	public JavaTypeResult<ScheduleExecutionLogSaveRespone> add(
			ScheduleExecutionLogAddCommand command) {
		return new JavaTypeResult<ScheduleExecutionLogSaveRespone>(this.add.handle(command));
	}
	
	/**
	 * Execution.
	 *
	 * @param command the command
	 * @return the schedule creator execution respone
	 */
	@POST
	@Path("execution")
	public ScheduleCreatorExecutionRespone execution(ScheduleCreatorExecutionCommand command) {
		AsyncTaskInfo taskInfor = this.execution.handle(command);
		ScheduleCreatorExecutionRespone respone = new ScheduleCreatorExecutionRespone();
		respone.setExecuteId(command.getExecutionId());
		respone.setTaskInfor(taskInfor);
		return respone;

	}
	
}
