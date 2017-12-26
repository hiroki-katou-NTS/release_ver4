package nts.uk.ctx.at.record.dom.daily;

import lombok.Value;

/**
 * 控除合計時間
 * @author keisuke_hoshina
 *
 */
@Value
public class DeductionTotalTime {
	private TimeWithCalculation totalTime;
	private TimeWithCalculation withinStatutoryTotalTime;
	private TimeWithCalculation excessOfStatutoryTotalTime;
	
	/**
	 * Constructor
	 */
	private DeductionTotalTime(TimeWithCalculation totalTime,TimeWithCalculation withinStatutoryTotalTime,TimeWithCalculation excessOfStatutoryTotalTime) {
		this.totalTime                  = totalTime;
		this.withinStatutoryTotalTime   = withinStatutoryTotalTime;
		this.excessOfStatutoryTotalTime = excessOfStatutoryTotalTime;
	}
	
	/**
	 * 控除合計時間の再作成
	 * @return
	 */
	public static DeductionTotalTime of(TimeWithCalculation totalTime,TimeWithCalculation withinStatutoryTotalTime,TimeWithCalculation excessOfStatutoryTotalTime) {
		return new DeductionTotalTime(
									 totalTime
									,withinStatutoryTotalTime
									,excessOfStatutoryTotalTime);
	}
}
