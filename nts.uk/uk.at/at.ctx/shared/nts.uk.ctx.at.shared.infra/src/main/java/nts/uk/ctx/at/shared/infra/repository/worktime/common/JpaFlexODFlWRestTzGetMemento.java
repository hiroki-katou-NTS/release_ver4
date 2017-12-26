/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexOdRtSet;

/**
 * The Class JpaFlexODFlWRestTzGetMemento.
 */
public class JpaFlexODFlWRestTzGetMemento implements FlowWorkRestTimezoneGetMemento{
	
	/** The entity. */
	private KshmtFlexOdRtSet entity;
	

	/**
	 * Instantiates a new jpa flex OD fl W rest tz get memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexODFlWRestTzGetMemento(KshmtFlexOdRtSet entity) {
		super();
		this.entity = entity;
	}


	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneGetMemento#getFixRestTime()
	 */
	@Override
	public boolean getFixRestTime() {
		return BooleanGetAtr.getAtrByInteger(this.entity.getFixRestTime());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneGetMemento#getFixedRestTimezone()
	 */
	@Override
	public TimezoneOfFixedRestTimeSet getFixedRestTimezone() {
		return new TimezoneOfFixedRestTimeSet(new JpaFlexODTzOFRTimeSetGetMemento(this.entity
				.getKshmtFlexOdFixRest()));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneGetMemento#getFlowRestTimezone()
	 */
	@Override
	public FlowRestTimezone getFlowRestTimezone() {
		return new FlowRestTimezone(new JpaFlexODFlowRestTzGetMemento(this.entity));
	}

}
