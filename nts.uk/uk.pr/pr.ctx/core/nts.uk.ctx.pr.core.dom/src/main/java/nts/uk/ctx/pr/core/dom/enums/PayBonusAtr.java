package nts.uk.ctx.pr.core.dom.enums;

/**
 * 給与賞与区分
 * 
 * @author vunv
 *
 */
public enum PayBonusAtr {
	/**
	 * 0:給与
	 */
	SALARY(0),

	/**
	 * 1:賞与
	 */
	BONUSES(1);

	public int value;

	private PayBonusAtr(int value) {
		this.value = value;
	}

}
