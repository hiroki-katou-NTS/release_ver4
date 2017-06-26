package nts.uk.ctx.pr.core.dom.itemmaster.itemdeduct;

public enum DeductAtr {
	// 控除項目種類
	OptionalDeduction(0),
	// 任意控除
	TaxDeduction(1),
	// 社保控除
	IncomeTaxDeduction(2),
	// 所得税控除
	InhabitantTaxDeduction(3);
	// 住民税控除

	public final int value;

	DeductAtr(int value) {
		this.value = value;
	}

}
