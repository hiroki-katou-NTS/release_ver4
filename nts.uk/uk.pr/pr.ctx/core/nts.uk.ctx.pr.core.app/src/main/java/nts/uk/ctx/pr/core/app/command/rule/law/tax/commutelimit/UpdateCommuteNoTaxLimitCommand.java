package nts.uk.ctx.pr.core.app.command.rule.law.tax.commutelimit;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateCommuteNoTaxLimitCommand {

	private String commuNoTaxLimitCode;
	private String commuNoTaxLimitName;
	private BigDecimal commuNoTaxLimitValue;
}
