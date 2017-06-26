package nts.uk.ctx.pr.core.dom.itemmaster.itemsalaryperiod;

public enum PeriodAtr {
	// 有効期限利用区分
	NotUse(0),
	// 利用しない
	Use(1);
	// 利用する

	public final int value;

	PeriodAtr(int value) {
		this.value = value;

	}

}
