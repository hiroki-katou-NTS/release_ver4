/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.executionlog;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateful;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.schedule.app.find.executionlog.ScheduleExecutionLogFinder;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionDateTime;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleCreator;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleCreatorRepository;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class ScheduleCreatorExecutionCommandHandler.
 */
@Stateful
public class ScheduleCreatorExecutionCommandHandler
		extends AsyncCommandHandler<ScheduleCreatorExecutionCommand> {

    /** The schedule execution log repository. */
    @Inject
    private ScheduleExecutionLogRepository scheduleExecutionLogRepository;
    
    /** The finder. */
    @Inject
    private ScheduleExecutionLogFinder finder;
    
    /** The schedule creator repository. */
    @Inject
    private ScheduleCreatorRepository scheduleCreatorRepository;
    
    /** The default value. */
    private final Integer DEFAULT_VALUE = 0;
        
    /** The total record. */
    private final String TOTAL_RECORD = "TOTAL_RECORD";
    
    /** The success cnt. */
    private final String SUCCESS_CNT = "SUCCESS_CNT";
    
    /** The fail cnt. */
    private final String FAIL_CNT = "FAIL_CNT";
	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.AsyncCommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<ScheduleCreatorExecutionCommand> context) {
		
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();
		
		val asyncTask = context.asAsync();
		TaskDataSetter setter = asyncTask.getDataSetter();
		ScheduleCreatorExecutionCommand command = context.getCommand();

		String executionId = command.getExecutionId();

		Optional<ScheduleExecutionLog> optionalScheduleExecutionLog = this.scheduleExecutionLogRepository
				.findById(companyId, executionId);
		
		// check exist data
		if(optionalScheduleExecutionLog.isPresent()){
			ScheduleExecutionLog domain = optionalScheduleExecutionLog.get();
			domain.setExecutionDateTime(
					new ExecutionDateTime(GeneralDateTime.now(), GeneralDateTime.now()));
			this.scheduleExecutionLogRepository.update(domain);
		}

		List<ScheduleCreator> scheduleCreators =  this.scheduleCreatorRepository.findAll(executionId);
		
		setter.setData(TOTAL_RECORD, scheduleCreators.size());
		setter.setData(SUCCESS_CNT, DEFAULT_VALUE);
		setter.setData(FAIL_CNT, DEFAULT_VALUE);
		scheduleCreators.forEach(domain->{
			domain.updateToCreated();
			this.scheduleCreatorRepository.update(domain);
			setter.updateData(SUCCESS_CNT, this.finder.findInfoById(executionId).getTotalNumberCreated());
		});
	

	}
	
}
