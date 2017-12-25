package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries;

import lombok.Getter;
import nts.arc.time.GeneralDate;

/**
 * 時系列の代休使用時間
 * @author shuichi_ishida
 */
@Getter
public class CompensatoryLeaveUseTimeOfTimeSeries {

	/** 年月日 */
	private GeneralDate ymd;
	/** 代休使用時間 */
	//private CompensatoryLeaveOfDaily compensatoryLeaveUseTime;

	/**
	 * コンストラクタ
	 */
	public CompensatoryLeaveUseTimeOfTimeSeries(){
		
		//this.compensatoryLeaveUseTime = new CompensatoryLeaveOfDaily();
	}

	/**
	 * ファクトリー
	 * @param ymd 年月日
	 * @return 時系列の代休使用時間
	 */
	public static CompensatoryLeaveUseTimeOfTimeSeries of(GeneralDate ymd){
		
		CompensatoryLeaveUseTimeOfTimeSeries domain = new CompensatoryLeaveUseTimeOfTimeSeries();
		domain.ymd = ymd;
		return domain;
	}
}