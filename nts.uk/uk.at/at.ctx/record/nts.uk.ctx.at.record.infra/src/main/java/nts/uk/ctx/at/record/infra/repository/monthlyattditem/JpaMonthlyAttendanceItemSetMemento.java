/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.monthlyattditem;

import nts.uk.ctx.at.record.dom.monthlyattditem.MonthlyAttendanceItemAtr;
import nts.uk.ctx.at.record.dom.monthlyattditem.MonthlyAttendanceItemSetMemento;
import nts.uk.ctx.at.record.infra.entity.monthlyattditem.KrcmtMonAttendanceItem;
import nts.uk.ctx.at.record.infra.entity.monthlyattditem.KrcmtMonAttendanceItemPK;
import nts.uk.ctx.at.shared.dom.attendance.AttendanceName;
import nts.uk.ctx.at.shared.dom.attendance.UseSetting;

/**
 * The Class JpaMonthlyAttendanceItemSetMemento.
 */
public class JpaMonthlyAttendanceItemSetMemento implements MonthlyAttendanceItemSetMemento {

	/** The entity. */
	private KrcmtMonAttendanceItem entity;

	/**
	 * Instantiates a new jpa monthly attendance item set memento.
	 *
	 * @param entity the entity
	 */
	public JpaMonthlyAttendanceItemSetMemento(KrcmtMonAttendanceItem entity) {
		this.entity = entity;
	}

	/**
	 * Instantiates a new jpa monthly attendance item set memento.
	 */
	public JpaMonthlyAttendanceItemSetMemento() {
		this.entity = new KrcmtMonAttendanceItem(new KrcmtMonAttendanceItemPK());
	}

	/**
	 * Sets the company id.
	 *
	 * @param companyId the new company id
	 */
	@Override
	public void setCompanyId(String companyId) {
		this.entity.getKrcmtMonAttendanceItemPK().setCid(companyId);
	}

	/**
	 * Sets the attendance item id.
	 *
	 * @param itemId the new attendance item id
	 */
	@Override
	public void setAttendanceItemId(int itemId) {
		this.entity.getKrcmtMonAttendanceItemPK().setMAtdItemId(itemId);
	}

	/**
	 * Sets the attendance name.
	 *
	 * @param name the new attendance name
	 */
	@Override
	public void setAttendanceName(AttendanceName name) {
		this.entity.setMAtdItemName(name.v());
	}

	/**
	 * Sets the display number.
	 *
	 * @param number the new display number
	 */
	@Override
	public void setDisplayNumber(int number) {
		this.entity.setDispNo(number);
	}

	/**
	 * Sets the user can update atr.
	 *
	 * @param canUpdateAtr the new user can update atr
	 */
	@Override
	public void setUserCanUpdateAtr(UseSetting canUpdateAtr) {
		this.entity.setIsAllowChange(canUpdateAtr.value);
	}

	/**
	 * Sets the monthly attendance atr.
	 *
	 * @param atr the new monthly attendance atr
	 */
	@Override
	public void setMonthlyAttendanceAtr(MonthlyAttendanceItemAtr atr) {
		this.entity.setMAtdItemAtr(atr.value);
	}

	/**
	 * Sets the name line feed position.
	 *
	 * @param nameLine the new name line feed position
	 */
	@Override
	public void setNameLineFeedPosition(int nameLine) {
		this.entity.setLineBreakPosName(nameLine);
	}

}
