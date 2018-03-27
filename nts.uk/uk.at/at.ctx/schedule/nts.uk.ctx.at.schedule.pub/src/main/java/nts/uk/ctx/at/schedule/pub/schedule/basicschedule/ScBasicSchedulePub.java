/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.pub.schedule.basicschedule;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

/**
 * The Interface ScBasicSchedulePub.
 */
public interface ScBasicSchedulePub {

	/**
	 * Find by id.
	 * RequestList4
	 * 
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	public Optional<ScBasicScheduleExport> findById(String employeeId, GeneralDate baseDate);
	
	/**
	 * Find work schedule break time (勤務予定休憩時間帯)
	 * RequestList351
	 * 
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	public List<ScWorkBreakTimeExport> findWorkBreakTime(String employeeId, GeneralDate baseDate);
}
