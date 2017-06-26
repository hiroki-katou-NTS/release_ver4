/**
 * 
 */
package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;

import lombok.Value;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyHeader;

/**
 * @author hungnm
 *
 */
@Value
public class FormulaEasyHeadDto {
	
	private String companyCode;
	
	private String formulaCode;
	
	private String historyId;
	
	private BigDecimal conditionAtr;

	private BigDecimal referenceMasterNo;
	
	public static FormulaEasyHeadDto fromDomain(FormulaEasyHeader domain) {
		return new FormulaEasyHeadDto(domain.getCompanyCode(),
				domain.getFormulaCode().v(),
				domain.getHistoryId(),
				new BigDecimal(domain.getConditionAtr().value),
				new BigDecimal(domain.getReferenceMasterNo().value));
	}
}
