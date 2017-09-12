/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace.config;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.bs.employee.dom.workplace.Period;

/**
 * The Class WorkplaceConfigHistory.
 */

@Getter
public class WorkplaceConfigHistory extends DomainObject{

	/** The history id. */
	//ID
	private String historyId;
	
	/** The period. */
	//期間
	private Period period;
	
	/**
	 * Instantiates a new workplace config history.
	 *
	 * @param memento the memento
	 */
	public WorkplaceConfigHistory(WorkplaceConfigHistoryGetMemento memento){
		this.historyId = memento.getHistoryId();
		this.period = memento.getPeriod();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkplaceConfigHistorySetMemento memento){
		memento.setHistoryId(this.historyId);
		memento.setPeriod(this.period);
	}
}
