/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface WorkplaceHistorySetMemento.
 */
public interface WorkplaceHistorySetMemento {

	/**
	 * Sets the history id.
	 *
	 * @param historyId the new history id
	 */
	public void setHistoryId(HistoryId historyId);

	/**
	 * Sets the period.
	 *
	 * @param period the new period
	 */
	public void setPeriod(DatePeriod period);
}
