package nts.uk.ctx.at.record.dom.daily;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;

/**
 * 計算乖離付き時間(マイナス付き)
 * @author keisuke_hoshina
 *
 */
@Getter
public class TimeDivergenceWithCalculationMinusExist {
	@Setter
	private AttendanceTimeOfExistMinus time;
	private AttendanceTimeOfExistMinus calcTime;
	private AttendanceTimeOfExistMinus divergenceTime;
	
	private TimeDivergenceWithCalculationMinusExist(AttendanceTimeOfExistMinus time,AttendanceTimeOfExistMinus calcTime) {
		this.time = time;
		this.calcTime = calcTime;
		this.divergenceTime = this.time.minusMinutes(this.calcTime.valueAsMinutes());
	}
	
	/**
	 * 時間、計算時間が同じ計算付き時間帯を作成する
	 * @return
	 */
	public static TimeDivergenceWithCalculationMinusExist sameTime(AttendanceTimeOfExistMinus time) {
		return new TimeDivergenceWithCalculationMinusExist(time,time);
	}
	
	
	/**
	 * 指定された時間で計算付き時間を作成する
	 * @return
	 */
	public static TimeDivergenceWithCalculationMinusExist createTimeWithCalculation(AttendanceTimeOfExistMinus time,AttendanceTimeOfExistMinus calcTime) {
		return new TimeDivergenceWithCalculationMinusExist(time,calcTime);
		
	}
	
	/**
	 * 自身の乖離時間を計算する
	 * @return
	 */
	public TimeDivergenceWithCalculationMinusExist calcDiverGenceTime() {
		return new TimeDivergenceWithCalculationMinusExist(this.time,this.calcTime);
	}
	
}
