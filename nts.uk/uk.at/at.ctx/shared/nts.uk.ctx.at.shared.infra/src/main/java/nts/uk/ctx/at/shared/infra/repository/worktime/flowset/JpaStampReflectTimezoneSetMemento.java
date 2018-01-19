/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.flowset;

import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.infra.entity.worktime.flowset.KshmtFlowStampReflect;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class JpaStampReflectTimezoneSetMemento.
 */
public class JpaStampReflectTimezoneSetMemento implements StampReflectTimezoneSetMemento {

	/** The entity. */
	private KshmtFlowStampReflect entity;

	/**
	 * Instantiates a new jpa stamp reflect timezone set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaStampReflectTimezoneSetMemento(KshmtFlowStampReflect entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneSetMemento#
	 * setWorkNo(nts.uk.ctx.at.shared.dom.worktime.common.WorkNo)
	 */
	@Override
	public void setWorkNo(WorkNo workNo) {
		this.entity.getKshmtFlowStampReflectPK().setWorkNo(workNo.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneSetMemento#
	 * setClassification(nts.uk.ctx.at.shared.dom.worktime.common.
	 * GoLeavingWorkAtr)
	 */
	@Override
	public void setClassification(GoLeavingWorkAtr classification) {
		this.entity.setAttendAtr(classification.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneSetMemento#
	 * setEndTime(nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setEndTime(TimeWithDayAttr endTime) {
		this.entity.setEndClock(endTime.valueAsMinutes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneSetMemento#
	 * setStartTime(nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setStartTime(TimeWithDayAttr startTime) {
		this.entity.setStrClock(startTime.valueAsMinutes());
	}

}
