package nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess;

import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
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
	public void updateWorkTimeType(ReflectParameter para, boolean scheUpdate);
	
	public IntegrationOfDaily updateWorkTimeTypeHoliwork(ReflectParameter para, boolean scheUpdate, IntegrationOfDaily dailyData);
	/**
	 * 予定時刻の反映
	 * @param data
	 */
	public void updateScheStartEndTime(TimeReflectPara data);
	
	public IntegrationOfDaily updateScheStartEndTimeHoliday(TimeReflectPara data, IntegrationOfDaily dailyData);
	/**
	 * 開始時刻の反映, 終了時刻を反映
	 * @param data
	 */
	public void updateRecordStartEndTimeReflect(TimeReflectPara data);
	
	public void updateRecordStartEndTimeReflectRecruitment(TimeReflectPara data, TimeLeavingOfDailyPerformance timeLeavingOfDailyData);
	/**
	 * 残業時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param mapOvertime
	 * @param isPre: true 事前申請、false 事後申請
	 */
	public void reflectOffOvertime(String employeeId, GeneralDate dateData, Map<Integer, Integer> mapOvertime, boolean isPre);
	/**
	 * 所定外深夜時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param timeNight
	 * @param isPre : true 事前申請、false 事後申請
	 */
	public void updateTimeShiftNight(String employeeId, GeneralDate dateData, Integer timeNight, boolean isPre);
	
	public IntegrationOfDaily updateTimeShiftNightHoliday(String employeeId, GeneralDate dateData, Integer timeNight, boolean isPre, IntegrationOfDaily dailyData);
	/**
	 * 休出時間(深夜)の反映
	 * @param employeeId
	 * @param dateData
	 */
	public void updateBreakNight(String employeeId, GeneralDate dateData);
	/**
	 * フレックス時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param flexTime
	 */
	public void updateFlexTime(String employeeId, GeneralDate dateData, Integer flexTime, boolean isPre);
	/**
	 * 勤務種類
	 * @param employeeId
	 * @param dateData
	 * @param workTypeCode
	 * @param scheUpdate true: 予定勤務種類, false: 勤務種類
	 */
	public void updateRecordWorkType(String employeeId, GeneralDate dateData, String workTypeCode, boolean scheUpdate);
	/**
	 * 休出時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param worktimeFrame
	 * @param isPre
	 */
	public IntegrationOfDaily updateWorkTimeFrame(String employeeId, GeneralDate dateData, Map<Integer, Integer> worktimeFrame, boolean isPre, IntegrationOfDaily dailyData);
	/**
	 * 就時の反映
	 * @param employeeId
	 * @param dateData
	 * @param workTimeCode
	 * @param scheUpdate true: 予定就時の反映
	 */
	public void updateRecordWorkTime(String employeeId, GeneralDate dateData, String workTimeCode, boolean scheUpdate);
	/**
	 * 振替時間(休出)の反映
	 * @param employeeId
	 * @param dateData
	 * @param transferTimeFrame
	 */
	public void updateTransferTimeFrame(String employeeId, GeneralDate dateData, Map<Integer, Integer> transferTimeFrame);

}
