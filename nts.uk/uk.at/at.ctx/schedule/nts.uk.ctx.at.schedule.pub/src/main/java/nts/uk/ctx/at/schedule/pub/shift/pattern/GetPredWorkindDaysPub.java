package nts.uk.ctx.at.schedule.pub.shift.pattern;

import java.util.Map;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 所定労働日数を取得する
 * @author shuichi_ishida
 */
public interface GetPredWorkindDaysPub {

	/**
	 * 指定期間の所定労働日数を取得する(大塚用)
	 * @param period 期間
	 * @return 所定労働日数
	 */
	// RequestList608
	public double byPeriod(DatePeriod period);

	/**
	 * 指定期間の所定労働日数を取得する(大塚用)
	 * @param period 期間
	 * @param workTypeMap 勤務種類マップ　（Object型=WorkType）
	 * @return 所定労働日数
	 */
	public double byPeriod(DatePeriod period, Map<String, Object> workTypeMap);
}
