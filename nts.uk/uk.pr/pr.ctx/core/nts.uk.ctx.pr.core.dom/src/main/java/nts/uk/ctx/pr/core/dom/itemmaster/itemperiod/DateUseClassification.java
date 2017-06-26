package nts.uk.ctx.pr.core.dom.itemmaster.itemperiod;

public enum DateUseClassification {
	// 0:利用しない
	NotUsed(0),
	// 1:利用する
	Use(1);

	public final int value;

	DateUseClassification(int value) {
		this.value = value;
	}
}
