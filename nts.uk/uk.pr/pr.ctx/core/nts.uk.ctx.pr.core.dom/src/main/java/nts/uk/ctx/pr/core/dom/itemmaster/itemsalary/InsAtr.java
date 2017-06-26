package nts.uk.ctx.pr.core.dom.itemmaster.itemsalary;

public enum InsAtr {
	//社会保険対象区分
	
	NotApplicable(0),
	//対象外

	Object(1);
	//対象

	public final int value;

	InsAtr(int value) {
		this.value = value;
	}

}
