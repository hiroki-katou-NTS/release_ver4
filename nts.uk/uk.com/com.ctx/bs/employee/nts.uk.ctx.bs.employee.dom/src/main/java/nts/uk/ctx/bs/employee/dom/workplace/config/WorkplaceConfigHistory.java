/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace.config;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.GeneralHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class WorkplaceConfigHistory.
 */
@Getter
public class WorkplaceConfigHistory extends GeneralHistoryItem<WorkplaceConfigHistory, DatePeriod, GeneralDate> {

	/**
	 * Instantiates a new workplace config history.
	 *
	 * @param memento
	 *            the memento
	 */
	public WorkplaceConfigHistory(WorkplaceConfigHistoryGetMemento memento) {
		super(memento.getHistoryId(), memento.getPeriod());
	}
	
	public WorkplaceConfigHistory(String hisId, DatePeriod period) {
		super(hisId, period);
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WorkplaceConfigHistorySetMemento memento) {
		memento.setHistoryId(this.identifier());
		memento.setPeriod(this.span());
	}
}
