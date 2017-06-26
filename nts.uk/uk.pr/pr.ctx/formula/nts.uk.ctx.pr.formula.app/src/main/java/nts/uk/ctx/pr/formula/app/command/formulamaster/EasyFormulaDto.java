package nts.uk.ctx.pr.formula.app.command.formulamaster;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EasyFormulaDto {
	
	private String easyFormulaCode;
	
	private BigDecimal value;

	/*B_SEL_003 */
	private String referenceMasterNo;
	
	/* Formula Detail */	
	private EasyDetailDto formulaDetail;	
	
	private int fixFormulaAtr;
	
	
	
}
