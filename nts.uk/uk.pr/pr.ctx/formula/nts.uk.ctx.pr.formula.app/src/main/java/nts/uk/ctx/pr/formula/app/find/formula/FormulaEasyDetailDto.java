/**
 * 
 */
package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;

import lombok.Value;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyDetail;

/**
 * @author hungnm
 *
 */
@Value
public class FormulaEasyDetailDto {
	
	private String companyCode;
	
	private String formulaCode;
	
	private String historyId;
	
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

	public static FormulaEasyDetailDto fromDomain(FormulaEasyDetail domain) {
		return new FormulaEasyDetailDto(
				domain.getCompanyCode(), 
				domain.getFormulaCode().v(),
				domain.getHistoryId(), 
				domain.getEasyFormulaCode().v(), 
				domain.getEasyFormulaName().v(),
				new BigDecimal(domain.getEasyFormulaTypeAtr().value),
				domain.getBaseFixedAmount().v(),
				new BigDecimal(domain.getBaseAmountDevision().value),
				domain.getBaseFixedValue().v(),
				new BigDecimal(domain.getBaseValueDevision().value),
				domain.getPremiumRate().v(),
				new BigDecimal(domain.getRoundProcessingDevision().value),
				domain.getCoefficientDivision().v(),
				domain.getCoefficientFixedValue().v(),
				new BigDecimal(domain.getAdjustmentDevision().value),
				new BigDecimal(domain.getTotalRounding().value));
	}

}
