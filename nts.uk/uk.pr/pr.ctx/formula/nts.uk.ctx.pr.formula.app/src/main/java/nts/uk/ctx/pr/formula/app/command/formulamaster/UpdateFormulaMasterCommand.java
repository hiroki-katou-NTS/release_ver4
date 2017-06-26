package nts.uk.ctx.pr.formula.app.command.formulamaster;

import java.util.List;

import lombok.Data;

/**
 * @author nampt
 *
 */
@Data
public class UpdateFormulaMasterCommand {

	private String formulaCode;

	private String formulaName;
	
	private int difficultyAtr;
	
	private String historyId;

	private List<EasyFormulaDto> easyFormulaDto;
	
	/* Formula Manual */
	private String formulaContent;

	private int referenceMonthAtr;

	private int roundAtr;

	private int roundDigit;
	
	
}
