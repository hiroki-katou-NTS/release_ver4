package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class FormulaSettingDto {

	
	private List<EasyFormulaFindDto> easyFormula;
	
	/**
	 * formula manual
	 */
	private String formulaContent;

	private BigDecimal referenceMonthAtr;

	private BigDecimal roundAtr;

	private BigDecimal roundDigit;

}
