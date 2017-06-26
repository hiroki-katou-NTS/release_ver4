package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;

import lombok.Value;
import nts.uk.ctx.pr.formula.dom.formula.FormulaManual;

/**
 * @author hungnm
 *
 */
@Value
public class FormulaManualDto {

	private String companyCode;

	private String formulaCode;

	private String historyId;

	private String formulaContent;

	private BigDecimal referenceMonthAtr;

	private BigDecimal roundAtr;

	private BigDecimal roundDigit;

	public static FormulaManualDto fromDomain(FormulaManual domain) {
		return new FormulaManualDto(domain.getCompanyCode(), domain.getFormulaCode().v(), domain.getHistoryId(),
				domain.getFormulaContent().v(), new BigDecimal(domain.getReferenceMonthAtr().value),new BigDecimal(domain.getRoundAtr().value),
				new BigDecimal(domain.getRoundDigit().value));
	}
}
