/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.shortworktime;

import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Interface ShortWorkTimeHistorySetMemento.
 */
public interface SWorkTimeHistSetMemento {

	/**
	 * Sets the employee id.
	 *
	 * @param empId the new employee id
	 */
	void setEmployeeId(String empId);
	
	/**
	 * Sets the history item.
	 *
	 * @param historyItem the new history item
	 */
	void setHistoryItem(DateHistoryItem historyItem);
}
