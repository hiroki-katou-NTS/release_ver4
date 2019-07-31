package nts.uk.ctx.at.schedule.dom.appreflectprocess.service;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.servicedto.TimeReflectScheDto;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;

public interface UpdateScheCommonAppRelect {
	/**
	 * 勤務予定基本情報
	 * 勤務種類=INPUT．勤務種類コード
	 * 就業時間帯=INPUT．就業時間帯コード
	 * @param employeeId
	 * @param baseDate
	 */
	public void updateScheWorkTimeType(BasicSchedule scheData, List<WorkScheduleState> lstState, String workTypeCode, String workTimeCode);
	/**
	 * 勤務種類の反映
	 * @param sid
	 * @param baseDate
	 * @param workTypeCode
	 */
	public void updateScheWorkType(BasicSchedule scheData, List<WorkScheduleState> lstState, String workTypeCode);
	/**
	 * 就業時間帯の反映
	 * @param sid
	 * @param baseDate
	 * @param workTimeCode
	 */
	public void updateScheWorkTime(BasicSchedule scheData, List<WorkScheduleState> lstState, String workTimeCode);
	/**
	 * domain 「勤務予定基本情報」、「勤務予定項目状態」を更新する
	 * @param startTimeDto
	 */
	public void updateStartTimeRflect(TimeReflectScheDto timeDto, BasicSchedule scheData, List<WorkScheduleState> lstState);
	void setStateData(BasicSchedule scheData, List<WorkScheduleState> lstState, int itemId);
	
}
