package nts.uk.ctx.pr.core.dom.rule.employment.averagepay;


/**
 * 端数桁設定
 * Round Digit Setting
 * @author Doan Duy Hung
 *
 */

public enum RoundDigitSet {
	/**
	 * 0 - 切り捨てない
	 */
	DO_NOT_TRUNCATE(0),
	
	/**
	 * 1 - 切り捨てる
	 */
	TRUNCATE(1);
	
	public final int value;
	
	RoundDigitSet(int value) {
		this.value = value;
		// TODO Auto-generated constructor stub
	}
	
}
