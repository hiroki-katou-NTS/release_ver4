/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.personallaborcondition;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface PersonalLaborConditionSetMemento {
	
	/**
	 * Sets the schedule management atr.
	 *
	 * @param scheduleManagementAtr the new schedule management atr
	 */
	public void setScheduleManagementAtr(UseAtr scheduleManagementAtr);
	
	
	/**
	 * Sets the holiday add time set.
	 *
	 * @param holidayAddTimeSet the new holiday add time set
	 */
	public void setHolidayAddTimeSet(BreakdownTimeDay holidayAddTimeSet);
	
	
	/**
	 * Sets the work category.
	 *
	 * @param workCategory the new work category
	 */
	public void setWorkCategory(PersonalWorkCategory workCategory);
	
	
	/**
	 * Sets the work day of week.
	 *
	 * @param workDayOfWeek the new work day of week
	 */
	public void setWorkDayOfWeek(PersonalDayOfWeek workDayOfWeek);
	
	
	/**
	 * Sets the period.
	 *
	 * @param period the new period
	 */
	public void setPeriod(DatePeriod period);
	
	
	/**
	 * Sets the employee id.
	 *
	 * @param employeeId the new employee id
	 */
	public void setEmployeeId(String employeeId);
	
	
	/**
	 * Sets the automatic emboss set atr.
	 *
	 * @param automaticEmbossSetAtr the new automatic emboss set atr
	 */
	public void setAutomaticEmbossSetAtr(UseAtr automaticEmbossSetAtr);

}
