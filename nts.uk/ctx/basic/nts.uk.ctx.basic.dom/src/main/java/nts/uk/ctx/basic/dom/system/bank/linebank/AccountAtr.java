package nts.uk.ctx.basic.dom.system.bank.linebank;
/**
 * Account attribute
 * @author sonnh1
 *
 */
public enum AccountAtr {
	/**
	 * 0 - ����
	 */
	NORMAL(0),
	/**
	 * 1- ����
	 */
	CURRENTLY(1);
	
	public final int value;
	AccountAtr(int value){
		this.value = value;
	}
	
}
