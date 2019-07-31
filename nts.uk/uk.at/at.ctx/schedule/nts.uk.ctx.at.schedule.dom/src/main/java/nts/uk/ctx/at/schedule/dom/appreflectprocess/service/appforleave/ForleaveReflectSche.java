package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.appforleave;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange.WorkChangecommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;

/**
 * (休暇申請)勤務予定への反映
 * @author do_dt
 *
 */
public interface ForleaveReflectSche {
	/**
	 * (休暇申請)勤務予定への反映
	 * @param reflectParam
	 */
	public void forlearveReflectSche(WorkChangecommonReflectParamSche reflectParam);
	/**
	 * 時刻の反映
	 * @param employeeId
	 * @param dateData
	 * @param workTypeCode
	 */
	public void reflectTime(BasicSchedule scheData, List<WorkScheduleState> lstState, String workTypeCode, Integer startTime, Integer endTime);

}
