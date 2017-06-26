package nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RemovePersonalUnitPriceCommand {
	private String personalUnitPriceCode;
}
