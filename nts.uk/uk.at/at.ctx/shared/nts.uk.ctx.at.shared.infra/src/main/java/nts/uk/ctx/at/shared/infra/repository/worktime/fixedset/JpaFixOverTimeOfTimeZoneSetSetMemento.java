/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.fixedset;

import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OTFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.SettlementOrder;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtFixedOtTimeSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtFixedOtTimeSetPK;

/**
 * The Class JpaFixOverTimeOfTimeZoneSetSetMemento.
 */
public class JpaFixOverTimeOfTimeZoneSetSetMemento implements OverTimeOfTimeZoneSetSetMemento {

	/** The entity. */
	private KshmtFixedOtTimeSet entity;

	/**
	 * Instantiates a new jpa fix over time of time zone set set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaFixOverTimeOfTimeZoneSetSetMemento(KshmtFixedOtTimeSet entity) {
		super();
		if (entity.getKshmtFixedOtTimeSetPK() == null) {
			entity.setKshmtFixedOtTimeSetPK(new KshmtFixedOtTimeSetPK());
		}
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetSetMemento#
	 * setWorkTimezoneNo(nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo)
	 */
	@Override
	public void setWorkTimezoneNo(EmTimezoneNo workTimezoneNo) {
		this.entity.getKshmtFixedOtTimeSetPK().setWorktimeNo(workTimezoneNo.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetSetMemento#
	 * setRestraintTimeUse(boolean)
	 */
	@Override
	public void setRestraintTimeUse(boolean restraintTimeUse) {
		this.entity.setTreatTimeWork(BooleanGetAtr.getAtrByBoolean(restraintTimeUse));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetSetMemento#
	 * setEarlyOTUse(boolean)
	 */
	@Override
	public void setEarlyOTUse(boolean earlyOTUse) {
		this.entity.setTreatEarlyOtWork(BooleanGetAtr.getAtrByBoolean(earlyOTUse));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetSetMemento#
	 * setTimezone(nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding)
	 */
	@Override
	public void setTimezone(TimeZoneRounding timezone) {
		this.entity.setTimeStr(timezone.getStart().valueAsMinutes());
		this.entity.setTimeEnd(timezone.getEnd().valueAsMinutes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetSetMemento#
	 * setOTFrameNo(nts.uk.ctx.at.shared.dom.worktime.common.OTFrameNo)
	 */
	@Override
	public void setOTFrameNo(OTFrameNo OTFrameNo) {
		this.entity.setOtFrameNo(OTFrameNo.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetSetMemento#
	 * setLegalOTframeNo(nts.uk.ctx.at.shared.dom.worktime.common.OTFrameNo)
	 */
	@Override
	public void setLegalOTframeNo(OTFrameNo legalOTframeNo) {
		this.entity.setLegalOtFrameNo(legalOTframeNo.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetSetMemento#
	 * setSettlementOrder(nts.uk.ctx.at.shared.dom.worktime.common.
	 * SettlementOrder)
	 */
	@Override
	public void setSettlementOrder(SettlementOrder settlementOrder) {
		this.entity.setPayoffOrder(settlementOrder.v());
	}

}
