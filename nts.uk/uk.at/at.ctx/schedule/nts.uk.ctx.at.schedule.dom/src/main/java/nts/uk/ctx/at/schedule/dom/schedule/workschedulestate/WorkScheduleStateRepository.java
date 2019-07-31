package nts.uk.ctx.at.schedule.dom.schedule.workschedulestate;

import java.util.List;

import nts.arc.time.GeneralDate;

/**
 * 
 * @author sonnh1
 *
 */
public interface WorkScheduleStateRepository {
	/**
	 * find all data of WorkScheduleState
	 * 
	 * @return List WorkScheduleState
	 */
	public List<WorkScheduleState> findAll();

	/**
	 * update ScheduleEditState
	 * 
	 * @param domain
	 * @return
	 */
	public void updateScheduleEditState(WorkScheduleState domain);

	/**
	 * find by date and EmployeeId
	 * 
	 * @param sId
	 * @param date
	 * @return
	 */
	public List<WorkScheduleState> findByDateAndEmpId(String sId, GeneralDate date);
	
	/**
	 * データがある場合更新する、データがない場合追加する
	 * @param domain
	 */
	void updateOrInsert(WorkScheduleState domain);
	
	void updateOrInsert(List<WorkScheduleState> lstState);
}
