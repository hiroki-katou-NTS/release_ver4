package nts.uk.ctx.pr.proto.dom.enums;

/**
 * �\�����敪
 * 
 * @author vunv
 *
 */
public enum SparePayAttribute {
	/**
	 * �ʏ�
	 */
	NORMAL(0),

	/**
	 * �\��
	 */
	PRELIMINARY(1);

	public int value;

	private SparePayAttribute(int value) {
		this.value = value;
	}

}
