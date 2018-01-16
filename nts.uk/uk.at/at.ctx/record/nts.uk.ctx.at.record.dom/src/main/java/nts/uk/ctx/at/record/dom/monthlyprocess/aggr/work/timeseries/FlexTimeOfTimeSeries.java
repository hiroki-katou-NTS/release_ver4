package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculationMinusExist;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;

/**
 * 時系列のフレックス時間
 * @author shuichi_ishida
 */
@Getter
public class FlexTimeOfTimeSeries {

	/** 年月日 */
	private final GeneralDate ymd;
	
	/** フレックス時間（日別実績） */
	@Setter
	private FlexTime flexTime;
	
	/**
	 * コンストラクタ
	 * @param ymd 年月日
	 */
	public FlexTimeOfTimeSeries(GeneralDate ymd){
		
		this.ymd = ymd;
		this.flexTime = new FlexTime(
				TimeWithCalculationMinusExist.sameTime(new AttendanceTimeOfExistMinus(0)),
				new AttendanceTime(0));
	}
	
	/**
	 * ファクトリー
	 * @param ymd 年月日
	 * @param flexTime フレックス時間（日別実績）
	 * @return 時系列のフレックス時間
	 */
	public static FlexTimeOfTimeSeries of(GeneralDate ymd, FlexTime flexTime){
		
		val domain = new FlexTimeOfTimeSeries(ymd);
		domain.flexTime = flexTime;
		return domain;
	}
}
