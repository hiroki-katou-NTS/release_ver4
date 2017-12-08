/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.fixedset;

import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTimeSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexHaFixRest;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexHaFixRestPK;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class JpaFlexOffdayDeductionTimeSetMemento.
 */
public class JpaFlexHADeductionTimeSetMemento implements DeductionTimeSetMemento{
	
	/** The entity. */
	private KshmtFlexHaFixRest entity;
	
	/**
	 * Instantiates a new jpa flex offday deduction time set memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexHADeductionTimeSetMemento(KshmtFlexHaFixRest entity) {
		super();
		if(entity.getKshmtFlexHaFixRestPK() == null){
			entity.setKshmtFlexHaFixRestPK(new KshmtFlexHaFixRestPK());
		}
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.DeductionTimeSetMemento#setStart
	 * (nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setStart(TimeWithDayAttr start) {
		this.entity.setStrTime(start.valueAsMinutes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.DeductionTimeSetMemento#setEnd(
	 * nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setEnd(TimeWithDayAttr end) {
		this.entity.setEndTime(end.valueAsMinutes());
	}
	

}
