/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.workplace;

import nts.uk.ctx.bs.employee.dom.workplace.Period;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistoryGetMemento;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfig;

/**
 * The Class JpaWorkplaceConfigHistoryGetMemento.
 */
public class JpaWorkplaceConfigHistoryGetMemento implements WorkplaceConfigHistoryGetMemento {

	/** The entity. */
	private BsymtWkpConfig entity;
	
	/**
	 * Instantiates a new jpa workplace config history get memento.
	 *
	 * @param item the item
	 */
	public JpaWorkplaceConfigHistoryGetMemento(BsymtWkpConfig item) {
		this.entity = item;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistoryGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.entity.getBsymtWkpConfigPK().getHistoryId();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistoryGetMemento#getPeriod()
	 */
	@Override
	public Period getPeriod() {
		return new Period(this.entity.getStrD(),this.entity.getEndD());
	}
}
