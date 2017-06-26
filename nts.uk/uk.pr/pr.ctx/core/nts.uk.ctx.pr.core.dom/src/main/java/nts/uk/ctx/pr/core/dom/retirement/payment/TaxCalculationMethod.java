package nts.uk.ctx.pr.core.dom.retirement.payment;

/**
 * 税計算方法区分
 * @author Doan Duy Hung
 *
 */

public enum TaxCalculationMethod {
	
	/**
	 * 0 - 計算しない
	 */
	DO_NOT_CALCULATE(0),
	
	/**
	 * 1 - 計算する
	 */
	CALCULATE(1);
	
	public final int value;
	
	TaxCalculationMethod(int value) {
		this.value = value;
	}
}
