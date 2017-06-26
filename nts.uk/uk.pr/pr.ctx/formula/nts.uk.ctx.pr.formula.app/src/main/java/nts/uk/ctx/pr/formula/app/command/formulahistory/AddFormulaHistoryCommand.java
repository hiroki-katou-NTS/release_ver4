package nts.uk.ctx.pr.formula.app.command.formulahistory;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nampt
 *
 */
@Data
@NoArgsConstructor
public class AddFormulaHistoryCommand {
	
	private String formulaCode;
	
	private int startDate;
	
	private int difficultyAtr;
	
	private int conditionAtr;

	private int referenceMasterNo;

}
