package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

public enum ApplyFor {

	NotApplicable(0),
	// 対象外
	Target(1);
	// 対象
	public final int value;

	ApplyFor(int value) {
		this.value = value;
	}
}
