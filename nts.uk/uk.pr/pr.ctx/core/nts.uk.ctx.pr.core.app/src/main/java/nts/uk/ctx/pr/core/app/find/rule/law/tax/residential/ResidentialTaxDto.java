package nts.uk.ctx.pr.core.app.find.rule.law.tax.residential;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.ResidentialTax;

/**
 * 
 * @author lanlt
 *
 */
@Value
public class ResidentialTaxDto {
	private String resiTaxCode;
	private String resiTaxAutonomy;
	private String prefectureCode;

	public static ResidentialTaxDto fromDomain(ResidentialTax domain) {
		return new ResidentialTaxDto(
				domain.getResiTaxCode().v(),
				domain.getResiTaxAutonomy().v(),
				domain.getPrefectureCode().v());
	}
}
