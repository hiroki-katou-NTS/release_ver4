package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.absenceleave;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;

/**
 * 振休申請の反映
 * @author dudt
 *
 */
public interface AbsenceLeaveReflectSche {
	/**
	 * 振休申請の反映
	 * @param param
	 * @return
	 */
	public void absenceLeaveReflect(CommonReflectParamSche param);
	/**
	 * 時刻の反映
	 * @param employeeId
	 * @param baseDate
	 * @param workTypeCode
	 * @param startTime
	 * @param endTime
	 */
	public void absenceLeaveStartEndTimeReflect(CommonReflectParamSche param);
	/**
	 * 就業時間帯が反映できるか
	 * @param employeeId
	 * @param baseDate
	 * @param worktimeCode
	 * @return
	 */
	public WorkTimeIsReflect workTimeIsReflect(String employeeId, GeneralDate baseDate, String worktimeCode);
	/**
	 * 開始終了時刻が反映できるか
	 * @param employeeId
	 * @param baseDate
	 * @param workStype 出勤休日区分
	 * @param startTime
	 * @param endTime
	 * @param workTimeCode
	 * @return
	 */
	public StartEndTimeIsReflect startEndTimeIsReflect(String employeeId, GeneralDate baseDate, 
			WorkStyle workStype, Integer startTime, Integer endTime, String workTimeCode);

}
