package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ResidenceTaxCommand {
	public int month;
	public BigDecimal value;
}
