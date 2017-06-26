package nts.uk.ctx.pr.core.dom.itemmaster.itemattend;

public enum ItemAtr {
	// 項目属性
	Time(0),
	// 時間
	NumberOfTimes(1);
	// 回数
	public final int value;

	ItemAtr(int value) {
		this.value = value;

	}

}
