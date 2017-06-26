package nts.uk.ctx.pr.core.dom.itemmaster;

public enum ItemDisplayAtr {
	/**
	 * 0:ゼロ円の場合明細書から項目名も出さない
	 */
	NO(0),
	/**
	 * 1:出す
	 */
	YES(1);
	
	public final int value;
	
	ItemDisplayAtr(int value) {
		this.value = value;
	}
}
