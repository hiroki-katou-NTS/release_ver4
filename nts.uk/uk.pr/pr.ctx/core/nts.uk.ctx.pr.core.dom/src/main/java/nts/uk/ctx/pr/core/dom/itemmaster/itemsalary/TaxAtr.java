package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

public enum TaxAtr {
	// 課税区分
	Taxation(0),
	// 課税
	TaxLimited(1),
	// 非課税（限度あり）
	TaxNolimit(2),
	// 非課税（限度無し）
	CommutingManually(3),
	// 通勤費（手入力）
	CommutingCommuter(4);
	// 通勤費（定期券利用）

	public final int value;

	TaxAtr(int value) {
		this.value = value;

	}
}
