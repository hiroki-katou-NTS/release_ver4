/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.schedule.basicschedule;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.childcareschedule.ChildCareSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.personalfee.WorkSchedulePersonFee;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workschedulebreak.WorkScheduleBreak;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletime.WorkScheduleTime;

/**
 * The Interface BasicScheduleRepository.
 */
public interface BasicScheduleRepository {

	/**
	 * Get BasicSchedule by primary key
	 * 
	 * @param sId
	 * @param date
	 * @return Optional BasicSchedule
	 */
	Optional<BasicSchedule> find(String sId, GeneralDate date);

	/**
	 * Check exists BasicSchedule by primary key
	 * 
	 * @param sId
	 * @param date
	 * @return Optional BasicSchedule
	 */
	boolean isExists(String sId, GeneralDate date);
	
	/**
	 * insert Basic Schedule
	 * 
	 * @param bSchedule
	 */
	void insert(BasicSchedule bSchedule);

	/**
	 * update Basic Schedule
	 * 
	 * @param bSchedule
	 */
	void update(BasicSchedule bSchedule);
	
	
	/**
	 * Delete.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 */
	void delete(String employeeId, GeneralDate baseDate);
	
	/**
	 * Find child care by id.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the list
	 */
	public List<ChildCareSchedule> findChildCareById(String employeeId, GeneralDate baseDate); 
	
	/**
	 * Find person fee by id.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the list
	 */
	public List<WorkSchedulePersonFee> findPersonFeeById(String employeeId, GeneralDate baseDate); 
	
	/**
	 * Find work schedule break time (勤務予定休憩時間帯)
	 * 
	 * @param employeeId
	 * @param baseDate
	 * @return
	 */
	List<WorkScheduleBreak> findWorkBreakTime(String employeeId, GeneralDate baseDate);
}
