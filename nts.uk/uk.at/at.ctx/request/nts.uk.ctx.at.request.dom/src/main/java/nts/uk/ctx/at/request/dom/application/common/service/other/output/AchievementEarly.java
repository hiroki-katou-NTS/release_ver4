package nts.uk.ctx.at.request.dom.application.common.service.other.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * refactor 4
 * UKDesign.UniversalK.就業.KAF_申請.共通アルゴリズム.実績内容の取得.遅刻早退実績
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class AchievementEarly {
	
	/**
	 * 予定出勤時刻1
	 */
	private TimeWithDayAttr scheAttendanceTime1;
	
	/**
	 * 予定出勤時刻2
	 */
	private TimeWithDayAttr scheAttendanceTime2;
	
	/**
	 * 予定退勤時刻1
	 */
	private TimeWithDayAttr scheDepartureTime1;
	
	/**
	 * 予定退勤時刻2
	 */
	private TimeWithDayAttr scheDepartureTime2;
	
}
