package nts.uk.ctx.basic.dom.system.bank.linebank;

/**
 * Account attribute
 * 
 * @author sonnh1
 *
 */
public enum AccountAtr {
	/**
	 * 0 - 普通
	 */
	NORMAL(0),
	/**
	 * 1- 当座
	 */
	CURRENTLY(1);

	public final int value;

	AccountAtr(int value) {
		this.value = value;
	}

}
