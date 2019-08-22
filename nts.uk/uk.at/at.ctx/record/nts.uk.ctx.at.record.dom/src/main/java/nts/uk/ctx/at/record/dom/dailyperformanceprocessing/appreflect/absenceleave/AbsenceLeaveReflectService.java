package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.absenceleave;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonReflectParameter;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;

public interface AbsenceLeaveReflectService {
	/**
	 * 振休申請
	 * @param parama
	 */
	public void reflectAbsenceLeave(CommonReflectParameter param, boolean isPre);
	/**
	 * 予定勤種就時開始終了の反映
	 * @param param
	 */
	public void reflectScheStartEndTime(CommonReflectParameter param, boolean isPre, IntegrationOfDaily dailyInfor);
	/**
	 * 就業時間帯が反映できるか
	 * @param employeeId
	 * @param dateData
	 * @param workTime
	 * @return
	 */
	public WorkTimeIsRecordReflect checkReflectWorktime(String employeeId, GeneralDate dateData, String workTime);
	/**
	 * 予定開始終了時刻が反映できるか
	 * @param employeeId
	 * @param baseDate
	 * @param workStype
	 * @param startTime
	 * @param endTime
	 * @param workTimeCode
	 * @return
	 */
	public StartEndTimeIsRecordReflect checkReflectScheStartEndTime(String employeeId, GeneralDate baseDate, WorkStyle workStype,
			Integer startTime, Integer endTime, String workTimeCode);
	/**
	 * 勤種就時開始終了の反映
	 * @param param
	 */
	public void reflectRecordStartEndTime(CommonReflectParameter param, IntegrationOfDaily dailyInfor);
	/**
	 * 開始時刻が反映できるか
	 * @param employeeId
	 * @param baseDate
	 * @param frameNo
	 * @return
	 */
	public boolean checkReflectRecordStartEndTime(String employeeId, GeneralDate baseDate, Integer frameNo, boolean isAttendence,
			Optional<TimeLeavingOfDailyPerformance> optTimeLeaving);
}
