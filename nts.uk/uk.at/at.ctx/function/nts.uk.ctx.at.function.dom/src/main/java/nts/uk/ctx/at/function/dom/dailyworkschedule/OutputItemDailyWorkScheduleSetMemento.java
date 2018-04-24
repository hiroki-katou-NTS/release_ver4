/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.dom.dailyworkschedule;

import java.util.List;

/**
 * The Interface OutputItemDailyWorkScheduleSetMemento.
 */
public interface OutputItemDailyWorkScheduleSetMemento {
	
	/**
	 * Sets the company ID.
	 *
	 * @param companyID the new company ID
	 */
	void setCompanyID(String companyID);
	
	/**
	 * Sets the item code.
	 *
	 * @param itemCode the new item code
	 */
	void setItemCode(OutputItemSettingCode itemCode);
	
	/**
	 * Sets the item name.
	 *
	 * @param itemName the new item name
	 */
	void setItemName(OutputItemSettingName itemName);
	
	/**
	 * Sets the lst displayed attendance.
	 *
	 * @param lstDisplayAttendance the new lst displayed attendance
	 */
	void setLstDisplayedAttendance(List<AttendanceItemsDisplay> lstDisplayAttendance);
	
	/**
	 * Sets the lst remark content.
	 *
	 * @param lstRemarkContent the new lst remark content
	 */
	void setLstRemarkContent(List<PrintRemarksContent> lstRemarkContent);
	
	/**
	 * Sets the zone name.
	 *
	 * @param zoneName the new zone name
	 */
	void setZoneName(NameWorkTypeOrHourZone zoneName);
}
