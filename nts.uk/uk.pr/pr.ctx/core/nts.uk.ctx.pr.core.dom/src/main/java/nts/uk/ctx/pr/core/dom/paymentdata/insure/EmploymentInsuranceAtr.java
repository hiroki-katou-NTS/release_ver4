package nts.uk.ctx.pr.core.dom.paymentdata.insure;

/** 雇用保険計算区分 */
public enum EmploymentInsuranceAtr {
	/**
	 * 0:A(保険料率パターンＡ)
	 */
	A(0),
	/**
	 * 1:B(保険料率パターンＢ一般)
	 */
	B(1),

	/**
	 * 2:Bその他(保険料率パターンＢその他)
	 */
	B_OTHER(2),

	/**
	 * 3:対象外
	 */
	NOT_APPLY(3);

	public final int value;

	/**
	 * Constructor.
	 * 
	 * @param value
	 */
	private EmploymentInsuranceAtr(int value) {
		this.value = value;
	}
}
