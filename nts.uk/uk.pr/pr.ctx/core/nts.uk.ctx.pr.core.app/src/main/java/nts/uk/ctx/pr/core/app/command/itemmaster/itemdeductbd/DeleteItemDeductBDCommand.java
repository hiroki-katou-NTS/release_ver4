package nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sonnlb
 *
 */
@Getter
@Setter
public class DeleteItemDeductBDCommand {
	private String itemCode;
	private String itemBreakdownCode;
}
