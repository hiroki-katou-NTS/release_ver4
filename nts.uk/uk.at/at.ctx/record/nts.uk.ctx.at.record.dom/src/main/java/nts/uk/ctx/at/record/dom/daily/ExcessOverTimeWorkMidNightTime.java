package nts.uk.ctx.at.record.dom.daily;

import lombok.Value;

/**
 * 法定外残業深夜時間
 * @author keisuke_hoshina
 *
 */
@Value
public class ExcessOverTimeWorkMidNightTime {
	private TimeDivergenceWithCalculation time;
	
	/**
	 * 実績超過乖離時間の計算
	 * @return
	 */
	public int calcOverLimitDivergenceTime() {
		return this.getTime().getDivergenceTime().valueAsMinutes() 
				 + this.getTime().getDivergenceTime().valueAsMinutes();
	}

	/**
	 * 実績超過乖離時間が発生しているか判定する
	 * @return 乖離時間が発生している
	 */
	public boolean isOverLimitDivergenceTime() {
		return this.calcOverLimitDivergenceTime() > 0 ? true:false;
	}
}
