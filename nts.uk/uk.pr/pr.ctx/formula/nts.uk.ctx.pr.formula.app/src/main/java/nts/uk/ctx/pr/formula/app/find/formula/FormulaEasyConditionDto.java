/**
 * 
 */
package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author hungnm
 *
 */
@Data
public class FormulaEasyConditionDto {
	
	private int fixFormulaAtr;
	
	private String easyFormulaCode;

	private BigDecimal fixMoney;

	private String referenceMasterCode;
	
}
