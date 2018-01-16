package nts.uk.ctx.at.record.dom.monthly.calc;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

/**
 * 集計総拘束時間
 * @author shuichi_ishida
 */
@Getter
public class AggregateTotalTimeSpentAtWork {

	/** 拘束残業時間 */
	private AttendanceTimeMonth overTimeSpentAtWork;
	/** 拘束深夜時間 */
	private AttendanceTimeMonth midnightTimeSpentAtWork;
	/** 拘束休出時間 */
	private AttendanceTimeMonth holidayTimeSpentAtWork;
	/** 拘束差異時間 */
	private AttendanceTimeMonth varienceTimeSpentAtWork;
	/** 総拘束時間 */
	private AttendanceTimeMonth totalTimeSpentAtWork;

	/**
	 * コンストラクタ
	 */
	public AggregateTotalTimeSpentAtWork(){
		
		this.overTimeSpentAtWork = new AttendanceTimeMonth(0);
		this.midnightTimeSpentAtWork = new AttendanceTimeMonth(0);
		this.holidayTimeSpentAtWork = new AttendanceTimeMonth(0);
		this.varienceTimeSpentAtWork = new AttendanceTimeMonth(0);
		this.totalTimeSpentAtWork = new AttendanceTimeMonth(0);
	}
	
	/**
	 * ファクトリー
	 * @param overTimeSpentAtWork 拘束残業時間
	 * @param midnightTimeSpentAtWork 拘束深夜時間
	 * @param holidayTimeSpentAtWork 拘束休出時間
	 * @param varienceTimeSpentAtWork 拘束差異時間
	 * @param totalTimeSpentAtWork 総拘束時間
	 * @return 集計総拘束時間
	 */
	public static AggregateTotalTimeSpentAtWork of(
			AttendanceTimeMonth overTimeSpentAtWork,
			AttendanceTimeMonth midnightTimeSpentAtWork,
			AttendanceTimeMonth holidayTimeSpentAtWork,
			AttendanceTimeMonth varienceTimeSpentAtWork,
			AttendanceTimeMonth totalTimeSpentAtWork){

		val domain = new AggregateTotalTimeSpentAtWork();
		domain.overTimeSpentAtWork = overTimeSpentAtWork;
		domain.midnightTimeSpentAtWork = midnightTimeSpentAtWork;
		domain.holidayTimeSpentAtWork = holidayTimeSpentAtWork;
		domain.varienceTimeSpentAtWork = varienceTimeSpentAtWork;
		domain.totalTimeSpentAtWork = totalTimeSpentAtWork;
		return domain;
	}
}
