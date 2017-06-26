package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EasyFormulaFindDto {
	
	private String easyFormulaCode;
	
	private BigDecimal value;
	
	/**
	 * B_SEL_003
	 * REF_MASTER_CD
	 */
	private String refMasterNo;	
	
	/**
	 * C_SEL_002
	 * FIX_FORMULA_ATR
	 */
	private int fixFormulaAtr;
	
	/**
	 * formula detail
	 */
	
	private FormulaEasyFinderDto formulaEasyDetail;
	
}
