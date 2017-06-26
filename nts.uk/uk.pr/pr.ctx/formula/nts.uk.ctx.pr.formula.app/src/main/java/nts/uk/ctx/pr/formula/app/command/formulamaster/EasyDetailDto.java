package nts.uk.ctx.pr.formula.app.command.formulamaster;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class EasyDetailDto {
	
	private String easyFormulaCode;
	
	private String easyFormulaName;

	private BigDecimal easyFormulaTypeAtr;
	
	private BigDecimal baseFixedAmount;

	private BigDecimal baseAmountDevision;

	private BigDecimal baseFixedValue;

	private BigDecimal baseValueDevision;

	private BigDecimal premiumRate;

	private BigDecimal roundProcessingDevision;

	private String coefficientDivision;

	private BigDecimal coefficientFixedValue;

	private BigDecimal adjustmentDevision;

	private BigDecimal totalRounding;
	
	private BigDecimal maxLimitValue;
	
	private BigDecimal minLimitValue;
	
	private List<String> referenceItemCodes;
}
