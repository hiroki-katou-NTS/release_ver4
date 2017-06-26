package nts.uk.ctx.pr.formula.app.command.formulahistory;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nampt
 *
 */
@Data
@NoArgsConstructor
public class RemoveFormulaHistoryCommand {
	
	private String formulaCode;
	
	private String historyId;
	
	private int startDate;
	
	/* Formula header */
	/**
	 * B_SEL_001
	 * DIFFICULTY_ATR
	 */
	private int difficultyAtr;
}
