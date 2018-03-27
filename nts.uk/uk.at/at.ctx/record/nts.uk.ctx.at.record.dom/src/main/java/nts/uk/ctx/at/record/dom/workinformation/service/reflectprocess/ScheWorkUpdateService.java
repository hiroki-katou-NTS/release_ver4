package nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess;

import java.util.Map;

import nts.arc.time.GeneralDate;

/**
 * 反映処理
 * @author do_dt
 *
 */
public interface ScheWorkUpdateService {
	/**
	 * 予定勤種・就時の反映
	 * @param para
	 * scheUpdate: true: 予定勤種就時を反映, false: 勤種就時を反映
	 */
	public void updateWorkTimeType(ReflectParameter para, boolean scheUpdate);
	/**
	 * 予定時刻の反映
	 * @param data
	 */
	public void updateStartTimeOfReflect(TimeReflectParameter data);
	/**
	 * 開始時刻の反映
	 * @param data
	 */
	public void updateReflectStartEndTime(TimeReflectParameter para);
	/**
	 * 残業時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param mapOvertime
	 */
	public void reflectOffOvertime(String employeeId, GeneralDate dateData, Map<Integer, Integer> mapOvertime);
	/**
	 * 所定外深夜時間の反映
	 * @param employeeId
	 * @param dateData
	 * @param timeNight
	 */
	public void updateTimeShiftNight(String employeeId, GeneralDate dateData, Integer timeNight);
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
	public void updateFlexTime(String employeeId, GeneralDate dateData, Integer flexTime);

}
