package nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess;

import java.util.List;
import java.util.Map;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.OverTimeRecordAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;

/**
 * 反映処理
 * @author do_dt
 *
 */
public interface WorkUpdateService {
	/**
	 * 勤種・就時の反映
	 * @param para
	 * scheUpdate: true: 予定勤種就時を反映, false: 勤種就時を反映
	 */
	public WorkInfoOfDailyPerformance updateWorkTimeType(ReflectParameter para, boolean scheUpdate, WorkInfoOfDailyPerformance dailyInfo);
	
	public IntegrationOfDaily updateWorkTimeTypeHoliwork(ReflectParameter para, boolean scheUpdate, IntegrationOfDaily dailyData);
	/**
	 * 予定時刻の反映
	 * @param data
	 */
	public WorkInfoOfDailyPerformance updateScheStartEndTime(TimeReflectPara data, WorkInfoOfDailyPerformance dailyInfo);
	
	public IntegrationOfDaily updateScheStartEndTimeHoliday(TimeReflectPara data, IntegrationOfDaily dailyData);
	/**
	 * 開始時刻の反映, 終了時刻を反映
	 * @param data
	 */
	public TimeLeavingOfDailyPerformance updateRecordStartEndTimeReflect(TimeReflectPara data);
	
	public void updateTimeNotReflect(String employeeId, GeneralDate dateData);
	
	public TimeLeavingOfDailyPerformance updateRecordStartEndTimeReflectRecruitment(TimeReflectPara data);
	/**
	 * 残業時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param mapOvertime
	 * @param isPre: true 事前申請、false 事後申請
	 */
	public AttendanceTimeOfDailyPerformance reflectOffOvertime(String employeeId, GeneralDate dateData, Map<Integer, Integer> mapOvertime,
			boolean isPre, AttendanceTimeOfDailyPerformance attendanceTimeData);
	/**
	 * 所定外深夜時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param timeNight
	 * @param isPre : true 事前申請、false 事後申請
	 */
	public AttendanceTimeOfDailyPerformance updateTimeShiftNight(String employeeId, GeneralDate dateData, Integer timeNight, boolean isPre,
			AttendanceTimeOfDailyPerformance attendanceTimeData);
	
	public IntegrationOfDaily updateTimeShiftNightHoliday(String employeeId, GeneralDate dateData, Integer timeNight, boolean isPre, IntegrationOfDaily dailyData);
	/**
	 * 休出時間(深夜)の反映
	 * @param employeeId
	 * @param dateData
	 */
	public AttendanceTimeOfDailyPerformance updateBreakNight(String employeeId, GeneralDate dateData, AttendanceTimeOfDailyPerformance attendanceTimeData);
	/**
	 * フレックス時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param flexTime
	 */
	public AttendanceTimeOfDailyPerformance updateFlexTime(String employeeId, GeneralDate dateData, Integer flexTime, boolean isPre,
			AttendanceTimeOfDailyPerformance attendanceTimeData);
	/**
	 * 勤務種類
	 * @param employeeId
	 * @param dateData
	 * @param workTypeCode
	 * @param scheUpdate true: 予定勤務種類, false: 勤務種類
	 */
	public WorkInfoOfDailyPerformance updateRecordWorkType(String employeeId, GeneralDate dateData, String workTypeCode, boolean scheUpdate, WorkInfoOfDailyPerformance dailyPerfor);
	/**
	 * 休出時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param worktimeFrame
	 * @param isPre
	 * @param isRec: True: 振出申請、False：　休日出勤申請 fix bug 103077
	 */
	public IntegrationOfDaily updateWorkTimeFrame(String employeeId, GeneralDate dateData, Map<Integer, Integer> worktimeFrame, 
			boolean isPre, IntegrationOfDaily dailyData, boolean isRec);
	/**
	 * 就時の反映
	 * @param employeeId
	 * @param dateData
	 * @param workTimeCode
	 * @param scheUpdate true: 予定就時の反映
	 */
	public WorkInfoOfDailyPerformance updateRecordWorkTime(String employeeId, GeneralDate dateData, String workTimeCode, boolean scheUpdate,
			WorkInfoOfDailyPerformance dailyPerfor);
	/**
	 * 振替時間(休出)の反映
	 * @param employeeId
	 * @param dateData
	 * @param transferTimeFrame
	 */
	public AttendanceTimeOfDailyPerformance updateTransferTimeFrame(String employeeId, GeneralDate dateData, Map<Integer, Integer> transferTimeFrame, 
			AttendanceTimeOfDailyPerformance attendanceTimeData);
	/**
	 * 申請理由の反映
	 * @param sid
	 * @param appDate
	 * @param appReason
	 * @param overTimeAtr
	 */
	public void reflectReason(String sid, GeneralDate appDate, String appReason, OverTimeRecordAtr overTimeAtr);
	/**
	 * 事前残業の勤務項目
	 * @return
	 */
	public List<Integer> lstPreOvertimeItem();
	/**
	 * 事前休日出勤時間の項目ID
	 * @return
	 */
	List<Integer> lstPreWorktimeFrameItem();
	/**
	 * 事後休日出勤時間帯の項目ID
	 * @return
	 */
	List<Integer> lstAfterWorktimeFrameItem();
	/**
	 * 振替時間の項目ID
	 * @return
	 */
	List<Integer> lstTranfertimeFrameItem();
}
