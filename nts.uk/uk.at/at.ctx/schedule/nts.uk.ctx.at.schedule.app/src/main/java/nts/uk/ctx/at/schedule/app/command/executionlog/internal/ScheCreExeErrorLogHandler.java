/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.executionlog.internal;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogGetMemento;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogRepository;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

/**
 * The Class ScheCreExeErrorLogHandler.
 */
@Stateless
public class ScheCreExeErrorLogHandler {
	
	/** The internationalization. */
	@Inject
	private I18NResourcesForUK internationalization;
	
	
	/** The schedule error log repository. */
	@Inject
	private ScheduleErrorLogRepository scheduleErrorLogRepository;
	
	
	/**
	 * Adds the error.
	 *
	 * @param command the command
	 * @param employeeId the employee id
	 * @param messageId the message id
	 */
	public void addError(ScheduleCreatorExecutionCommand command, String employeeId, String messageId) {
		// check exist error
		if (!this.checkExistError(command, employeeId)) {
			this.scheduleErrorLogRepository.add(this.toScheduleErrorLog(command, employeeId, messageId));
		}
	}
	
	/**
	 * Check exist error.
	 *
	 * @param command the command
	 * @param employeeId the employee id
	 * @return true, if successful
	 */
	public boolean checkExistError(ScheduleCreatorExecutionCommand command, String employeeId) {
		List<ScheduleErrorLog> errorLogs = this.scheduleErrorLogRepository
				.findByEmployeeId(command.getContent().getExecutionId(), employeeId);

		// check empty list log error
		if (CollectionUtil.isEmpty(errorLogs)) {
			return false;
		}
		return true;
	}
	/**
	 * To schedule error log.
	 *
	 * @param command the command
	 * @param employeeId the employee id
	 * @param messageId the message id
	 * @return the schedule error log
	 */
	private ScheduleErrorLog toScheduleErrorLog(ScheduleCreatorExecutionCommand command, String employeeId,
			String messageId) {
		return new ScheduleErrorLog(new ScheduleErrorLogGetMementoImpl(command,employeeId,messageId)); 
	}
	
	
	
	/**
	 * The Class ScheduleErrorLogGetMementoImpl.
	 */
	class ScheduleErrorLogGetMementoImpl implements ScheduleErrorLogGetMemento{
		
		/** The command. */
		private ScheduleCreatorExecutionCommand command;
		
		/** The employee id. */
		private String employeeId;
		
		/** The message id. */
		private String messageId;
		

		/**
		 * Instantiates a new schedule error log get memento impl.
		 *
		 * @param command the command
		 * @param employeeId the employee id
		 * @param messageId the message id
		 */
		public ScheduleErrorLogGetMementoImpl(ScheduleCreatorExecutionCommand command, String employeeId,
				String messageId) {
			this.command = command;
			this.employeeId = employeeId;
			this.messageId = messageId;
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogGetMemento#getErrorContent()
		 */
		@Override
		public String getErrorContent() {
			return messageId + " " + internationalization.localize(messageId).get();
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogGetMemento#getExecutionId()
		 */
		@Override
		public String getExecutionId() {
			return command.getExecutionId();
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogGetMemento#getDate()
		 */
		@Override
		public GeneralDate getDate() {
			return command.getToDate();
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogGetMemento#getEmployeeId()
		 */
		@Override
		public String getEmployeeId() {
			return employeeId;
		}
		
	}
	
}