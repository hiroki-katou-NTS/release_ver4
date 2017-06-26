package nts.uk.ctx.pr.formula.app.command.formulahistory;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nampt
 *
 */
@Data
@NoArgsConstructor
public class UpdateFormulaHistoryCommand {

	private String formulaCode;

	private String historyId;
	
	private int startDate;

}
