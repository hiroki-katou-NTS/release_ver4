package nts.uk.ctx.at.shared.dom.worktime.algorithm.caltimediff;

import nts.uk.ctx.at.shared.dom.schedule.basicschedule.JoggingWorkTime;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 時差勤務時間の計算
 * @author trungtran
 *
 */
public interface CalculateTimeDiffService {
	/**
	 * 時差勤務時間を計算する Calculate the time difference working hours
	 */
	JoggingWorkTime caculateJoggingWorkTime(String workTimeCode,TimeWithDayAttr scheduleStartClock);
}
