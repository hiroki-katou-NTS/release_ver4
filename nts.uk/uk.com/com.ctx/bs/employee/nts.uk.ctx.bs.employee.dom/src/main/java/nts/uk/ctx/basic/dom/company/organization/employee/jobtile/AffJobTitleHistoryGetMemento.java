/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.dom.company.organization.employee.jobtile;

import nts.uk.ctx.basic.dom.common.history.Period;

/**
 * The Interface JobTitleHistoryGetMemento.
 */
public interface AffJobTitleHistoryGetMemento {

	
	/**
	 * Gets the period.
	 *
	 * @return the period
	 */
	Period getPeriod();
	
	
	/**
	 * Gets the employee id.
	 *
	 * @return the employee id
	 */
	String getEmployeeId();
	
	
	/**
	 * Gets the job title id.
	 *
	 * @return the job title id
	 */
	PositionId getJobTitleId();
}
