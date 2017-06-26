package nts.uk.ctx.pr.core.dom.itemmaster.itemattend;

public enum WorkDaysScopeAtr {
	// 労働日数対象区分
	NotApplicable(0),
	// 対象外
	Object(1);
	// 対象
	public final int value;

	WorkDaysScopeAtr(int value) {
		this.value = value;
	}
}
