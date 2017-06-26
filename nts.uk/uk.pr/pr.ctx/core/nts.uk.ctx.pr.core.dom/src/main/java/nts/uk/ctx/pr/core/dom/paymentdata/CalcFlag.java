package nts.uk.ctx.pr.core.dom.paymentdata;

/**
 * 計算フラグ
 * 
 * @author vunv
 *
 */
public enum CalcFlag {
	/**
	 * 0:未計算
	 */
	UN_CALCULATION(0),

	/**
	 * 1:計算済み
	 */
	CALCULATED(1);

	public int value;

	private CalcFlag(int value) {
		this.value = value;
	}

}
