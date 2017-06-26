package nts.uk.ctx.pr.formula.app.find.formula;

import lombok.Data;

@Data
public class FormulaFinderDto {
	
	private String ccd;

	private String formulaCode;

	private String formulaName;

	/**
	 * B_SEL_001
	 */
	private int difficultyAtr;

	/* Formula History */
	private String historyId;

	private int startDate;

	private int endDate;

}
