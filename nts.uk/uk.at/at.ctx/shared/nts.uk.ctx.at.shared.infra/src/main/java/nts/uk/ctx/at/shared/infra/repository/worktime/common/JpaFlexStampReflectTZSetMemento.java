/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezoneSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexStampReflect;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexStampReflectPK;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class JpaFlexStampReflectTZSetMemento.
 */
public class JpaFlexStampReflectTZSetMemento implements StampReflectTimezoneSetMemento{
	
	/** The entity. */
	private KshmtFlexStampReflect entity;
	
	/**
	 * Instantiates a new jpa flex stamp reflect TZ set memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexStampReflectTZSetMemento(KshmtFlexStampReflect entity) {
		super();
		if(entity.getKshmtFlexStampReflectPK() == null){
			entity.setKshmtFlexStampReflectPK(new KshmtFlexStampReflectPK());
		}
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.fixedset.StampReflectTimezoneSetMemento
	 * #setWorkNo(nts.uk.ctx.at.shared.dom.worktime.fixedset.WorkNo)
	 */
	@Override
	public void setWorkNo(WorkNo workNo) {
		this.entity.getKshmtFlexStampReflectPK().setWorkNo(workNo.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.fixedset.StampReflectTimezoneSetMemento
	 * #setClassification(nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * GoLeavingWorkAtr)
	 */
	@Override
	public void setClassification(GoLeavingWorkAtr classification) {
		this.entity.setAtr(classification.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.fixedset.StampReflectTimezoneSetMemento
	 * #setEndTime(nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setEndTime(TimeWithDayAttr endTime) {
		this.entity.setEndTime(endTime.valueAsMinutes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.fixedset.StampReflectTimezoneSetMemento
	 * #setStartTime(nts.uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setStartTime(TimeWithDayAttr startTime) {
		this.entity.setStartTime(startTime.valueAsMinutes());
	}
	
	

}
