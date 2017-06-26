package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input;

import lombok.Getter;

@Getter
public class ResidenceTax {
	private int month;
	private MonthlyResidenceTax value;
	public ResidenceTax(int month, MonthlyResidenceTax value) {
		super();
		this.month = month;
		this.value = value;
	}
}
