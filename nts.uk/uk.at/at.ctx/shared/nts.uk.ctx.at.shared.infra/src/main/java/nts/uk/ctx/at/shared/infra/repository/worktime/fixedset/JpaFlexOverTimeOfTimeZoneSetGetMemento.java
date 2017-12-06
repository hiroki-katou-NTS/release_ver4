/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.fixedset;

import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OTFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.SettlementOrder;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexOtTimeSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexOtTimeSetPK;

/**
 * The Class JpaFlexOverTimeOfTimeZoneSetGetMemento.
 */
public class JpaFlexOverTimeOfTimeZoneSetGetMemento implements OverTimeOfTimeZoneSetGetMemento{
	
	/** The entity. */
	private KshmtFlexOtTimeSet entity;

	/**
	 * Instantiates a new jpa flex over time of time zone set get memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexOverTimeOfTimeZoneSetGetMemento(KshmtFlexOtTimeSet entity) {
		super();
		if(entity.getKshmtFlexOtTimeSetPK() == null){
			entity.setKshmtFlexOtTimeSetPK(new KshmtFlexOtTimeSetPK());
		}
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * OverTimeOfTimeZoneSetGetMemento#getWorkTimezoneNo()
	 */
	@Override
	public EmTimezoneNo getWorkTimezoneNo() {
		return new EmTimezoneNo(this.entity.getKshmtFlexOtTimeSetPK().getWorktimeNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * OverTimeOfTimeZoneSetGetMemento#getRestraintTimeUse()
	 */
	@Override
	public boolean getRestraintTimeUse() {
		return BooleanGetAtr.getAtrByInteger(this.entity.getTreatTimeWork());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * OverTimeOfTimeZoneSetGetMemento#getEarlyOTUse()
	 */
	@Override
	public boolean getEarlyOTUse() {
		return BooleanGetAtr.getAtrByInteger(this.entity.getTreatEarlyOtWork());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * OverTimeOfTimeZoneSetGetMemento#getTimezone()
	 */
	@Override
	public TimeZoneRounding getTimezone() {
		return new TimeZoneRounding(new JpaFlexOTTimeZoneRoundingGetMemento(this.entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * OverTimeOfTimeZoneSetGetMemento#getOTFrameNo()
	 */
	@Override
	public OTFrameNo getOTFrameNo() {
		return new OTFrameNo(this.entity.getOtFrameNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * OverTimeOfTimeZoneSetGetMemento#getLegalOTframeNo()
	 */
	@Override
	public OTFrameNo getLegalOTframeNo() {
		return new OTFrameNo(this.entity.getLegalOtFrameNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * OverTimeOfTimeZoneSetGetMemento#getSettlementOrder()
	 */
	@Override
	public SettlementOrder getSettlementOrder() {
		return new SettlementOrder(this.entity.getPayoffOrder());
	}

}
