package nts.uk.ctx.pr.core.app.find.rule.law.tax.commutelimit;

import java.math.BigDecimal;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuteNoTaxLimit;

@Value
public class CommuteNoTaxLimitDto {

	private String companyCode;
	private String commuNoTaxLimitCode;
	private String commuNoTaxLimitName;
	private BigDecimal commuNoTaxLimitValue;

	public static CommuteNoTaxLimitDto getValuesFromDomain(CommuteNoTaxLimit commuteNoTaxLimit) {
		return new CommuteNoTaxLimitDto(commuteNoTaxLimit.getCompanyCode(),
				commuteNoTaxLimit.getCommuNoTaxLimitCode().v(), commuteNoTaxLimit.getCommuNoTaxLimitName().v(),
				commuteNoTaxLimit.getCommuNoTaxLimitValue().v());
	}

}
