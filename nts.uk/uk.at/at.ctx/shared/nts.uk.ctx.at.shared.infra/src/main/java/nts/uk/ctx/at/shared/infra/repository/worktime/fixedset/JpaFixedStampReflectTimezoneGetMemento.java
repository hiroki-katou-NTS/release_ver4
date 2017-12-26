/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.fixedset;

import java.math.BigDecimal;

import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtFixedStampReflect;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class JpaFixedStampReflectTimezoneGetMemento.
 */
public class JpaFixedStampReflectTimezoneGetMemento implements StampReflectTimezoneGetMemento {

	/** The entity. */
	private KshmtFixedStampReflect entity;
	
	/**
	 * Instantiates a new jpa fixed stamp reflect timezone get memento.
	 *
	 * @param entity the entity
	 */
	public JpaFixedStampReflectTimezoneGetMemento(KshmtFixedStampReflect entity) {
		this.entity = entity;
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneGetMemento#getWorkNo()
	 */
	@Override
	public WorkNo getWorkNo() {
		return new WorkNo(new BigDecimal(this.entity.getKshmtFixedStampReflectPK().getWorkNo()));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneGetMemento#getClassification()
	 */
	@Override
	public GoLeavingWorkAtr getClassification() {
		return GoLeavingWorkAtr.valueOf(this.entity.getAtr());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneGetMemento#getEndTime()
	 */
	@Override
	public TimeWithDayAttr getEndTime() {
		return new TimeWithDayAttr(this.entity.getEndTime());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneGetMemento#getStartTime()
	 */
	@Override
	public TimeWithDayAttr getStartTime() {
		return new TimeWithDayAttr(this.entity.getStartTime());
	}

}
