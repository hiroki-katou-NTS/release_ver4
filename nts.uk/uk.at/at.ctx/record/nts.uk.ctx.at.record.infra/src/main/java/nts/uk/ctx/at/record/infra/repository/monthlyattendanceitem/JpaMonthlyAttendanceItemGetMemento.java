/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.monthlyattendanceitem;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.dailyattendanceitem.enums.UseSetting;
import nts.uk.ctx.at.record.dom.dailyattendanceitem.primitivevalue.AttendanceName;
import nts.uk.ctx.at.record.dom.monthlyattendanceitem.MonthlyAttendanceItemAtr;
import nts.uk.ctx.at.record.dom.monthlyattendanceitem.MonthlyAttendanceItemGetMemento;
import nts.uk.ctx.at.record.infra.entity.monthlyattendanceitem.KrcmtMonAttendanceItem;

/**
 * The Class JpaMonthlyAttendanceItemGetMemento.
 */
public class JpaMonthlyAttendanceItemGetMemento implements MonthlyAttendanceItemGetMemento {

	/** The entity. */
	private KrcmtMonAttendanceItem entity;

	/**
	 * Instantiates a new jpa monthly attendance item get memento.
	 *
	 * @param entity the entity
	 */
	public JpaMonthlyAttendanceItemGetMemento(KrcmtMonAttendanceItem entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.monthlyattendanceitem.
	 * MonthlyAttendanceItemGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return this.entity.getKrcmtMonAttendanceItemPK().getCid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.monthlyattendanceitem.
	 * MonthlyAttendanceItemGetMemento#getAttendanceItemId()
	 */
	@Override
	public int getAttendanceItemId() {
		return this.entity.getKrcmtMonAttendanceItemPK().getMAtdItemId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.monthlyattendanceitem.
	 * MonthlyAttendanceItemGetMemento#getAttendanceName()
	 */
	@Override
	public AttendanceName getAttendanceName() {
		return new AttendanceName(this.entity.getMAtdItemName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.monthlyattendanceitem.
	 * MonthlyAttendanceItemGetMemento#getDisplayNumber()
	 */
	@Override
	public int getDisplayNumber() {
		return this.entity.getDispNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.monthlyattendanceitem.
	 * MonthlyAttendanceItemGetMemento#getUserCanUpdateAtr()
	 */
	@Override
	public UseSetting getUserCanUpdateAtr() {
		return EnumAdaptor.valueOf(this.entity.getIsAllowChange(), UseSetting.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.monthlyattendanceitem.
	 * MonthlyAttendanceItemGetMemento#getMonthlyAttendanceAtr()
	 */
	@Override
	public MonthlyAttendanceItemAtr getMonthlyAttendanceAtr() {
		return MonthlyAttendanceItemAtr.valueOf(this.entity.getMAtdItemAtr());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.monthlyattendanceitem.
	 * MonthlyAttendanceItemGetMemento#getNameLineFeedPosition()
	 */
	@Override
	public int getNameLineFeedPosition() {
		return this.entity.getLineBreakPosName();
	}

}
