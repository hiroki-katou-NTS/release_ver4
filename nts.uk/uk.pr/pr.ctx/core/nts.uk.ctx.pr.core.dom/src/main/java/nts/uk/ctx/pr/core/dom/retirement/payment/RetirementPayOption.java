package nts.uk.ctx.pr.core.dom.retirement.payment;

/**
 * 退職所得の受給に関する申告書区分
 * @author Doan Duy Hung
 *
 */

public enum RetirementPayOption {
	
	/**
	 * 0 - あり（他からの退職金の支払いなし）
	 */
	YES_NO_PAYMENT(0),
	
	/**
	 * 1 - あり（他からの退職金の支払いあり）
	 */
	YES_PAYMENT(1),
	
	/**
	 * 2 - 申告書なし
	 */
	NO_DECLARATION(2);
	
	public final int value;
	
	RetirementPayOption(int value) {
		this.value = value;
	}
}
