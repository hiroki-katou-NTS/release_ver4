package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

public enum RangeAtr {
	// 平均賃金対象区分
	NotApplicable(0),
	// 対象外
	Object(1);
	// 対象

	public final int value;

	RangeAtr(int value) {
		this.value = value;
	}
}
