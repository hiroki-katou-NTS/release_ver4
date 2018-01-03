/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRoundingSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexWorkTimeSet;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class JpaFlexTimeZoneRoundingGetMemento.
 */
public class JpaFlexTimeZoneRoundingSetMemento implements TimeZoneRoundingSetMemento {
	
	/** The entity. */
	private KshmtFlexWorkTimeSet entity;
	
	/**
	 * Instantiates a new jpa flex time zone rounding set memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexTimeZoneRoundingSetMemento(KshmtFlexWorkTimeSet entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRoundingSetMemento#
	 * setRounding(nts.uk.ctx.at.shared.dom.common.timerounding.
	 * TimeRoundingSetting)
	 */
	@Override
	public void setRounding(TimeRoundingSetting rdSet) {
		if (rdSet != null) {
			this.entity.setUnit(rdSet.getRoundingTime().value);
			this.entity.setRounding(rdSet.getRounding().value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRoundingSetMemento#
	 * setStart(nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setStart(TimeWithDayAttr start) {
		this.entity.setTimeStr(start.valueAsMinutes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRoundingSetMemento#
	 * setEnd(nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setEnd(TimeWithDayAttr end) {
		this.entity.setTimeEnd(end.valueAsMinutes());
	}

}
