package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class FormulaEasyFinderDto {
	
	String easyFormulaName;

	BigDecimal easyFormulaTypeAtr;
	
	BigDecimal baseFixedAmount;

	BigDecimal baseAmountDevision;

	BigDecimal baseFixedValue;

	BigDecimal baseValueDevision;

	BigDecimal premiumRate;

	BigDecimal roundProcessingDevision;

	String coefficientDivision;

	BigDecimal coefficientFixedValue;

	BigDecimal adjustmentDevision;

	BigDecimal totalRounding;
	
	BigDecimal maxLimitValue;
	
	BigDecimal minLimitValue;
	
	List<String> referenceItemCodes;
}
