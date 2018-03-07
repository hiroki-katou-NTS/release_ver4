/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.pub.schedulemanagementcontrol;

/**
 * The Interface ScheduleManagementControlPub.
 */
public interface ScheduleManagementControlPub {
	
	/**
	 * RequestList27
	 * 
	 * Checks if is schedule management atr.
	 *
	 * @param employeeId the employee id
	 * @return true, if is schedule management atr
	 */
	public boolean isScheduleManagementAtr(String employeeId);

}
