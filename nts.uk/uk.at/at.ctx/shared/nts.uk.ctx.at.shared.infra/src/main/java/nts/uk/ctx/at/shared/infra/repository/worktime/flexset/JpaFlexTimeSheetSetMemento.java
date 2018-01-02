/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.flexset;

import nts.uk.ctx.at.shared.dom.worktime.flexset.TimeSheetSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexWorkSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexWorkSetPK;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class JpaFlexTimeSheetSetMemento.
 */
public class JpaFlexTimeSheetSetMemento implements TimeSheetSetMemento {
	
	/** The entity. */
	private KshmtFlexWorkSet entity;
	
	/**
	 * Instantiates a new jpa flex time sheet set memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexTimeSheetSetMemento(KshmtFlexWorkSet entity) {
		super();
		if(entity.getKshmtFlexWorkSetPK() == null){
			entity.setKshmtFlexWorkSetPK(new KshmtFlexWorkSetPK());
		}
		this.entity = entity;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.TimeSheetSetMemento#
	 * setStartTime(nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setStartTime(TimeWithDayAttr startTime) {
		this.entity.setCoreTimeStr(startTime.valueAsMinutes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.flexset.TimeSheetSetMemento#setEndTime(
	 * nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setEndTime(TimeWithDayAttr endTime) {
		this.entity.setCoreTimeEnd(endTime.valueAsMinutes());
	}

}
