/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.BreakFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSettingGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexHolSet;

/**
 * The Class JpaFlexODHDWTSheetGetMemento.
 */
public class JpaFlexODHDWTSheetGetMemento implements HDWorkTimeSheetSettingGetMemento{
	
	/** The entity. */
	private KshmtFlexHolSet entity;

	/**
	 * Instantiates a new jpa flex offday HDWT sheet get memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexODHDWTSheetGetMemento(KshmtFlexHolSet entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSettingGetMemento
	 * #getWorkTimeNo()
	 */
	@Override
	public Integer getWorkTimeNo() {
		return this.entity.getKshmtFlexHolSetPK().getWorktimeNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSettingGetMemento
	 * #getTimezone()
	 */
	@Override
	public TimeZoneRounding getTimezone() {
		return new TimeZoneRounding(new JpaFlexOffdayTZRoundGetMemento(this.entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSettingGetMemento
	 * #getIsLegalHolidayConstraintTime()
	 */
	@Override
	public boolean getIsLegalHolidayConstraintTime() {
		return BooleanGetAtr.getAtrByInteger(this.entity.getHolTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSettingGetMemento
	 * #getInLegalBreakFrameNo()
	 */
	@Override
	public BreakFrameNo getInLegalBreakFrameNo() {
		return new BreakFrameNo(this.entity.getHolFrameNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSettingGetMemento
	 * #getIsNonStatutoryDayoffConstraintTime()
	 */
	@Override
	public boolean getIsNonStatutoryDayoffConstraintTime() {
		return BooleanGetAtr.getAtrByInteger(this.entity.getOutHolTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSettingGetMemento
	 * #getOutLegalBreakFrameNo()
	 */
	@Override
	public BreakFrameNo getOutLegalBreakFrameNo() {
		return new BreakFrameNo(this.entity.getOutHolFrameNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSettingGetMemento
	 * #getIsNonStatutoryHolidayConstraintTime()
	 */
	@Override
	public boolean getIsNonStatutoryHolidayConstraintTime() {
		return BooleanGetAtr.getAtrByInteger(this.entity.getPubHolTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSettingGetMemento
	 * #getOutLegalPubHDFrameNo()
	 */
	@Override
	public BreakFrameNo getOutLegalPubHDFrameNo() {
		return new BreakFrameNo(this.entity.getPubHolFrameNo());
	}

}
