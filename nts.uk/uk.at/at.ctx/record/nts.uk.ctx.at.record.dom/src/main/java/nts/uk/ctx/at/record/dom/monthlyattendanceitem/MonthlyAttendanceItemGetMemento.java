/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.monthlyattendanceitem;

import nts.uk.ctx.at.record.dom.dailyattendanceitem.enums.UseSetting;
import nts.uk.ctx.at.record.dom.dailyattendanceitem.primitivevalue.AttendanceName;

/**
 * The Interface MonthlyAttendanceItemGetMemento.
 */
public interface MonthlyAttendanceItemGetMemento {

	/**
	 * Gets the company id.
	 *
	 * @return the company id
	 */
	String getCompanyId();

	/**
	 * Gets the attendance item id.
	 *
	 * @return the attendance item id
	 */
	int getAttendanceItemId();

	/**
	 * Gets the attendance name.
	 *
	 * @return the attendance name
	 */
	AttendanceName getAttendanceName();

	/**
	 * Gets the display number.
	 *
	 * @return the display number
	 */
	int getDisplayNumber();

	/**
	 * Gets the user can update atr.
	 *
	 * @return the user can update atr
	 */
	UseSetting getUserCanUpdateAtr();

	/**
	 * Gets the monthly attendance atr.
	 *
	 * @return the monthly attendance atr
	 */
	MonthlyAttendanceItemAtr getMonthlyAttendanceAtr();

	/**
	 * Gets the name line feed position.
	 *
	 * @return the name line feed position
	 */
	int getNameLineFeedPosition();

}
