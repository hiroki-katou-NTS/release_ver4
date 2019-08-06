package nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime.BreakTimeParam;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.OverTimeRecordAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;

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
	public void updateWorkTimeType(ReflectParameter para, boolean scheUpdate, IntegrationOfDaily dailyInfo);
	
	public IntegrationOfDaily updateWorkTimeTypeHoliwork(ReflectParameter para, boolean scheUpdate, IntegrationOfDaily dailyData);
	/**
	 * 予定時刻の反映
	 * @param data
	 */
	public void updateScheStartEndTime(TimeReflectPara data, IntegrationOfDaily dailyInfo);
	void editStateOfDailyPerformance(String sid, GeneralDate ymd, 
			List<EditStateOfDailyPerformance> lstEditState, List<Integer> lstItem);
	public IntegrationOfDaily updateScheStartEndTimeHoliday(TimeReflectPara data, IntegrationOfDaily dailyData);
	/**
	 * 開始時刻の反映, 終了時刻を反映
	 * @param data
	 */
	public void updateRecordStartEndTimeReflect(TimeReflectPara data, IntegrationOfDaily dailyData);
	
	public void updateTimeNotReflect(String employeeId, GeneralDate dateData);
	
	public void updateRecordStartEndTimeReflectRecruitment(TimeReflectPara data, IntegrationOfDaily dailyData);
	/**
	 * 残業時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param mapOvertime
	 * @param isPre: true 事前申請、false 事後申請
	 */
	public void reflectOffOvertime(String employeeId, GeneralDate dateData, Map<Integer, Integer> mapOvertime,
			boolean isPre, IntegrationOfDaily dailyData);
	/**
	 * 所定外深夜時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param timeNight
	 * @param isPre : true 事前申請、false 事後申請
	 */
	public void updateTimeShiftNight(String employeeId, GeneralDate dateData, Integer timeNight, boolean isPre,
			IntegrationOfDaily dailyInfor);
	
	public IntegrationOfDaily updateTimeShiftNightHoliday(String employeeId, GeneralDate dateData, Integer timeNight, boolean isPre, IntegrationOfDaily dailyData);
	/**
	 * 休出時間(深夜)の反映
	 * @param employeeId
	 * @param dateData
	 */
	public void updateBreakNight(String employeeId, GeneralDate dateData, IntegrationOfDaily dailyInfo);
	/**
	 * フレックス時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param flexTime
	 */
	public void updateFlexTime(String employeeId, GeneralDate dateData, Integer flexTime, boolean isPre,
			IntegrationOfDaily attendanceTimeData);
	/**
	 * 勤務種類
	 * @param employeeId
	 * @param dateData
	 * @param workTypeCode
	 * @param scheUpdate true: 予定勤務種類, false: 勤務種類
	 */
	public void updateRecordWorkType(String employeeId, GeneralDate dateData, String workTypeCode, boolean scheUpdate, IntegrationOfDaily dailyPerfor);
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
	public void updateRecordWorkTime(String employeeId, GeneralDate dateData, String workTimeCode, boolean scheUpdate,
			IntegrationOfDaily dailyPerfor);
	/**
	 * 振替時間(休出)の反映
	 * @param employeeId
	 * @param dateData
	 * @param transferTimeFrame
	 */
	public void updateTransferTimeFrame(String employeeId, GeneralDate dateData, Map<Integer, Integer> transferTimeFrame, 
			IntegrationOfDaily dailyPerfor);
	/**
	 * 申請理由の反映
	 * @param sid
	 * @param appDate
	 * @param appReason
	 * @param overTimeAtr
	 */
	public void reflectReason(String sid, GeneralDate appDate, String appReason, OverTimeRecordAtr overTimeAtr, IntegrationOfDaily dailyData);
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
	/**
	 * 事後残業の勤務項目
	 * @return
	 */
	List<Integer> lstAfterOvertimeItem();
	/**
	 * 残業枠時間．振替時間
	 * @return
	 */
	List<Integer> lstTransferTimeOtItem();
	
	public void updateBreakTime(Map<Integer, BreakTimeParam> mapBreakTimeFrame, boolean recordReflectBreakFlg, boolean isPre, IntegrationOfDaily daily);
	/**
	 * 休憩開始時刻
	 * @return
	 */
	List<Integer> lstBreakStartTime();
	/**
	 * 休憩終了時刻
	 * @return
	 */
	List<Integer> lstBreakEndTime();
	/**
	 * 予定休憩開始時刻
	 * @return
	 */
	List<Integer> lstScheBreakStartTime();
	/**
	 * 予定休憩終了時刻
	 * @return
	 */
	List<Integer> lstScheBreakEndTime();
	void cleanRecordTimeData(String employeeId, GeneralDate baseDate, IntegrationOfDaily dailyInfor);
	void cleanScheTime(String employeeId, GeneralDate baseDate, IntegrationOfDaily dailyInfor);
}
