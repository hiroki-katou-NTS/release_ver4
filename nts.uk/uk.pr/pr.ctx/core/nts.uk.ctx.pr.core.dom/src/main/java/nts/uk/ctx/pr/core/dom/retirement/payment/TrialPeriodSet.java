package nts.uk.ctx.pr.core.dom.retirement.payment;

/**
 * 試用期間の有無設定
 * @author Doan Duy Hung
 *
 */

public enum TrialPeriodSet {
	
	/**
	 * 0 - 試用期間なし
	 */
	NO_TRIAL_PERIOD(0),
	
	/**
	 * 1 - 試用期間あり
	 */
	TRIAL_PERIOD_AVAIABLE(1);
	
	public final int value;
	
	TrialPeriodSet(int value) {
		this.value = value;
	}
}
