package nts.uk.ctx.pr.proto.dom.enums;

/**
 * ���^�ܗ^�敪
 * 
 * @author vunv
 *
 */
public enum PayBonusAttribute {
	/**
	 * ���^
	 */
	SALARY(0),

	/**
	 * �ܗ^
	 */
	BONUSES(1);

	public int value;

	private PayBonusAttribute(int value) {
		this.value = value;
	}

}
