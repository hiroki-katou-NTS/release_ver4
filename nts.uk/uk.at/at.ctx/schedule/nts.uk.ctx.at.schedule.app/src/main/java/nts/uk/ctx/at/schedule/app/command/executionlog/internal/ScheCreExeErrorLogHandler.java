/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.executionlog.internal;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
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
	 * @param command
	 *            the command
	 * @param employeeId
	 *            the employee id
	 * @param messageId
	 *            the message id
	 */
	public void addError(ScheduleErrorLogGeterCommand command, String employeeId, String messageId) {
		// check exist error
		if (!this.checkExistErrorByKey(command, employeeId)) {
			this.scheduleErrorLogRepository.add(this.toScheduleErrorLog(command, employeeId, messageId));
		}
	}

	/**
	 * Adds the error.
	 *
	 * @param command
	 *            the command
	 * @param employeeId
	 *            the employee id
	 * @param messageId
	 *            the message id
	 */
	public void addError(ScheduleErrorLogGeterCommand command, String employeeId, String messageId, String paramMsg) {
		// check exist error
		if (!this.checkExistErrorByKey(command, employeeId)) {
			this.scheduleErrorLogRepository.add(this.toScheduleErrorLog(command, employeeId, messageId, paramMsg));
		}
	}

	/**
	 * Check exist error.
	 *
	 * @param command
	 *            the command
	 * @param employeeId
	 *            the employee id
	 * @return true, if successful
	 */
	public boolean checkExistError(ScheduleErrorLogGeterCommand command, String employeeId) {
		return this.scheduleErrorLogRepository.checkExistErrorByKey(command.getExecutionId(), employeeId, command.getToDate());

	}
	
	public boolean checkExistErrorByExeId(String executionId) {
		Integer errorLogs = this.scheduleErrorLogRepository.distinctErrorByExecutionId(executionId);
		// check empty list log error
		if (errorLogs>0) {
			return true;
		}
		return false;
	}
	
	public List<ScheduleErrorLog> getListError(ScheduleErrorLogGeterCommand command, String employeeId) {
		List<ScheduleErrorLog> errorLogs = this.scheduleErrorLogRepository.findByEmployeeId(command.getExecutionId(),
				employeeId);

		// check empty list log error
		if (CollectionUtil.isEmpty(errorLogs)) {
			return Collections.emptyList();
		}
		return errorLogs;
	}

	/**
	 * Check exist error.
	 *
	 * @param command
	 *            the command
	 * @param employeeId
	 *            the employee id
	 * @return true, if successful
	 */
	public boolean checkExistErrorByKey(ScheduleErrorLogGeterCommand command, String employeeId) {
		return this.scheduleErrorLogRepository.checkExistErrorByKey(command.getExecutionId(),
				employeeId, command.getToDate());
	}

	/**
	 * To schedule error log.
	 *
	 * @param command
	 *            the command
	 * @param employeeId
	 *            the employee id
	 * @param messageId
	 *            the message id
	 * @return the schedule error log
	 */
	private ScheduleErrorLog toScheduleErrorLog(ScheduleErrorLogGeterCommand command, String employeeId,
			String messageId) {
		return new ScheduleErrorLog(new ScheduleErrorLogGetMementoImpl(command, employeeId, messageId));
	}

	private ScheduleErrorLog toScheduleErrorLog(ScheduleErrorLogGeterCommand command, String employeeId,
			String messageId, String paramMsg) {
		return new ScheduleErrorLog(new ScheduleErrorLogGetMementoImpl(command, employeeId, messageId, paramMsg));
	}

	/**
	 * The Class ScheduleErrorLogGetMementoImpl.
	 */
	class ScheduleErrorLogGetMementoImpl implements ScheduleErrorLogGetMemento {

		/** The command. */
		private ScheduleErrorLogGeterCommand command;

		/** The employee id. */
		private String employeeId;

		/** The message id. */
		private String messageId;

		private String paramMsg;

		/**
		 * Instantiates a new schedule error log get memento impl.
		 *
		 * @param command
		 *            the command
		 * @param employeeId
		 *            the employee id
		 * @param messageId
		 *            the message id
		 */
		public ScheduleErrorLogGetMementoImpl(ScheduleErrorLogGeterCommand command, String employeeId,
				String messageId) {
			this.command = command;
			this.employeeId = employeeId;
			this.messageId = messageId;
		}

		public ScheduleErrorLogGetMementoImpl(ScheduleErrorLogGeterCommand command, String employeeId, String messageId,
				String paramMsg) {
			this.command = command;
			this.employeeId = employeeId;
			this.messageId = messageId;
			this.paramMsg = paramMsg;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogGetMemento#
		 * getErrorContent()
		 */
		@Override
		public String getErrorContent() {
			if (StringUtil.isNullOrEmpty(this.paramMsg, true)) {
				return internationalization.localize(messageId).get();
			}
			return internationalization.localize(messageId, paramMsg).get();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogGetMemento#
		 * getExecutionId()
		 */
		@Override
		public String getExecutionId() {
			return command.getExecutionId();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogGetMemento#
		 * getDate()
		 */
		@Override
		public GeneralDate getDate() {
			return command.getToDate();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogGetMemento#
		 * getEmployeeId()
		 */
		@Override
		public String getEmployeeId() {
			return employeeId;
		}

	}

}