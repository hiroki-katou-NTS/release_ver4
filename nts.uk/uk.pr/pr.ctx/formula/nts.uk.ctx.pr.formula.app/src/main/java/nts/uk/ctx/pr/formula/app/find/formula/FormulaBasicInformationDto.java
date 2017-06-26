package nts.uk.ctx.pr.formula.app.find.formula;

import lombok.Data;

@Data
public class FormulaBasicInformationDto {
	
	private String formulaCode;
		
	private String formulaName;
	
	/**
	 * B_SEL_001
	 */
	private int difficultyAtr;
	
	/* Formula History */
	private int startDate;

	private int endDate;	

	/* Formula header */
	/**
	 * B_SEL_002
	 * CONDITION_ATR
	 */
	private int conditionAtr;

	/**
	 * B_SEL_003
	 * REF_MASTER_CD
	 */
	private int refMasterNo;

}
