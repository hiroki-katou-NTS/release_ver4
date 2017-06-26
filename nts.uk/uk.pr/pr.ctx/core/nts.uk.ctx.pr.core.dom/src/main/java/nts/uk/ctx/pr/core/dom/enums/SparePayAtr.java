package nts.uk.ctx.pr.core.dom.enums;

/**
 * 予備月区分
 * 
 * @author vunv
 *
 */
public enum SparePayAtr {
	/**
	 * 0:通常
	 */
	NORMAL(0),

	/**
	 * 1:予備
	 */
	PRELIMINARY(1);

	public int value;

	private SparePayAtr(int value) {
		this.value = value;
	}

}
