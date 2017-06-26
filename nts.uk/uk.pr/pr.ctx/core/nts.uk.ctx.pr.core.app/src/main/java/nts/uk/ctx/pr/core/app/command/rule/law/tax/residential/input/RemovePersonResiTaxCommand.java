package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class RemovePersonResiTaxCommand {
	private int yearKey;
	private List<String> personId;

}
