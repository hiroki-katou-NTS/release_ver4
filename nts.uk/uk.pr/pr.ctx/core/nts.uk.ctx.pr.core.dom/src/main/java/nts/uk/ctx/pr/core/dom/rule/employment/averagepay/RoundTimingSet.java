package nts.uk.ctx.pr.core.dom.rule.employment.averagepay;

/**
 * 日数端数処理区分
 * Round Timing Setting
 * @author Doan Duy Hung
 *
 */

public enum RoundTimingSet {
	/**
	 * 0 - 足した後
	 */
	AFTER_ADDING(0),
	
	/**
	 * 1 - 足す前
	 */
	BEFORE_ADDING(1); 
	
	public final int value;
	
	RoundTimingSet(int value) {
		this.value = value;
	}
}
