package nts.uk.ctx.pr.core.dom.itemmaster;

public enum FixAtr {
	// システム規定値区分
	NotRepeatable(0),
	// 廃止不可
	Abolishable(1);
	// 廃止可能
	public final int value;

	FixAtr(int value) {
		this.value = value;
	}
}
