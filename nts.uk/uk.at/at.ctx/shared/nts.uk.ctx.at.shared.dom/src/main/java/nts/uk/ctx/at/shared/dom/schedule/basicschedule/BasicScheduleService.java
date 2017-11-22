package nts.uk.ctx.at.shared.dom.schedule.basicschedule;

import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author sonnh1
 *
 */
public interface BasicScheduleService {

	/**
	 * Return state of error checking process: nothing or throw error
	 * 
	 * @param workTypeCode
	 * @param workTimeCode
	 */
	void checkPairWorkTypeWorkTime(String workTypeCode, String workTimeCode);

	/**
	 * Check needed of Work Time setting
	 * 
	 * @param workTypeCode
	 * @return SetupType
	 */
	SetupType checkNeededOfWorkTimeSetting(String workTypeCode);

	/**
	 * Check required of input type
	 * 
	 * @param dayType
	 * @return SetupType
	 */
	SetupType checkRequiredOfInputType(WorkTypeClassification workTypeClass);

	/**
	 * Check work day
	 * 
	 * @param workTypeCode
	 * @return WorkStyle
	 */
	WorkStyle checkWorkDay(String workTypeCode);

	/**
	 * Check required
	 * 
	 * @param morningWorkStyle
	 * @param afternoonWorkStyle
	 * @return SetupType
	 */
	SetupType checkRequired(SetupType morningWorkStyle, SetupType afternoonWorkStyle);

	/**
	 * 時差勤務時間を計算する Calculate the time difference working hours
	 */
	JoggingWorkTime caculateJoggingWorkTime(String workTimeCode,TimeWithDayAttr scheduleStartClock);

	/**
	 * 開始時刻と終了時刻の逆転チェック
	 * 
	 * @param scheduleStartClock
	 * @param scheduleEndClock
	 * @return true is reverse
	 */
	boolean isReverseStartAndEndTime(TimeWithDayAttr scheduleStartClock, TimeWithDayAttr scheduleEndClock);
}
