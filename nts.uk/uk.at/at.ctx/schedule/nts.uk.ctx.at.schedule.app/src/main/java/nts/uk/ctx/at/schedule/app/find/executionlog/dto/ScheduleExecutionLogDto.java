package nts.uk.ctx.at.schedule.app.find.executionlog.dto;

import lombok.Getter;
import nts.uk.ctx.at.schedule.dom.executionlog.CompletionStatus;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContent;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionDateTime;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogSetMemento;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.workrule.closure.Period;

/**
 * The Class ScheduleExecutionLogDto.
 */
@Getter
public class ScheduleExecutionLogDto implements ScheduleExecutionLogSetMemento {

	/** The company id. */
	public String companyId;

	/** The completion status. */
	public String completionStatus;

	/** The execution id. */
	public String executionId;

	/** The execution content. */
	public ExecutionContentDto executionContent;

	/** The execution date time. */
	public ExecutionDateTime executionDateTime;

	/** The execution employee id. */
	public String executionEmployeeId;

	/** The period. */
	public Period period;
	
	//imported class
	/** The employee code. */
	public String employeeCode;
	
	/** The employee name. */
	public String employeeName;
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogSetMemento#setCompanyId(nts.uk.ctx.at.shared.dom.common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		this.companyId = companyId.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogSetMemento#setCompletionStatus(nts.uk.ctx.at.schedule.dom.executionlog.CompletionStatus)
	 */
	@Override
	public void setCompletionStatus(CompletionStatus completionStatus) {
		this.completionStatus = completionStatus.description;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogSetMemento#setExecutionId(java.lang.String)
	 */
	@Override
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogSetMemento#setExecutionContent(nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContent)
	 */
	@Override
	public void setExecutionContent(ExecutionContent executionContent) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogSetMemento#setExecutionDateTime(nts.uk.ctx.at.schedule.dom.executionlog.ExecutionDateTime)
	 */
	@Override
	public void setExecutionDateTime(ExecutionDateTime executionDateTime) {
		this.executionDateTime = executionDateTime;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogSetMemento#setExecutionEmployeeId(java.lang.String)
	 */
	@Override
	public void setExecutionEmployeeId(String executionEmployeeId) {
		this.executionEmployeeId = executionEmployeeId;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogSetMemento#setPeriod(nts.uk.ctx.at.shared.dom.workrule.closure.Period)
	 */
	@Override
	public void setPeriod(Period period) {
		this.period = period;
	}
	
	/**
	 * Sets the employee code.
	 *
	 * @param employeeCode the new employee code
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	/**
	 * Sets the employee name.
	 *
	 * @param employeeName the new employee name
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
}
