/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.flowset;

import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlTimeSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.flowset.KshmtOtTimeZone;

/**
 * The Class JpaFlowOTTimeSettingSetMemento.
 */
public class JpaFlowOTTimeSettingSetMemento implements FlTimeSetMemento {

	/** The entity. */
	private KshmtOtTimeZone entity; 
	
	/**
	 * Instantiates a new jpa flow OT time setting set memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlowOTTimeSettingSetMemento(KshmtOtTimeZone entity) {
		super();
		this.entity = entity;
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlTimeSetMemento#setRouding(nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting)
	 */
	@Override
	public void setRouding(TimeRoundingSetting trSet) {
		this.entity.setUnit(trSet.getRoundingTime().value);
		this.entity.setRounding(trSet.getRounding().value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlTimeSetMemento#setElapsedTime(nts.uk.ctx.at.shared.dom.common.time.AttendanceTime)
	 */
	@Override
	public void setElapsedTime(AttendanceTime at) {
		this.entity.setPassageTime(at.valueAsMinutes());
	}

}
