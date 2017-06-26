package nts.uk.ctx.pr.core.app.command.itemmaster;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sonnlb
 *
 */
@Getter
@Setter
public class DeleteItemMasterCommand {

	private int categoryAtr;
	private String itemCode;

}
