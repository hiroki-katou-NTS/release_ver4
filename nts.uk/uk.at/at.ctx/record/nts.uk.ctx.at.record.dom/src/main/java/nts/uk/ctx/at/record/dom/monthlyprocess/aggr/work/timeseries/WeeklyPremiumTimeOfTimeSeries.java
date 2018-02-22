package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

/**
 * 時系列の週割増時間
 * @author shuichu_ishida
 */
@Getter
public class WeeklyPremiumTimeOfTimeSeries {

	/** 年月日 */
	private GeneralDate ymd;
	/** 週割増時間 */
	private AttendanceTimeMonth weeklyPremiumTime;
	
	/**
	 * コンストラクタ
	 * @param ymd 年月日
	 */
	public WeeklyPremiumTimeOfTimeSeries(GeneralDate ymd){
		
		this.ymd = ymd;
		this.weeklyPremiumTime = new AttendanceTimeMonth(0);
	}
}
