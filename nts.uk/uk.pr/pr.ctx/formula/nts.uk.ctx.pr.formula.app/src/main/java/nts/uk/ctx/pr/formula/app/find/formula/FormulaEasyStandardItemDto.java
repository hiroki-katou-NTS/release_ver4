/**
 * 
 */
package nts.uk.ctx.pr.formula.app.find.formula;

import lombok.Value;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyStandardItem;

/**
 * @author hungnm, nampt
 *
 */
@Value
public class FormulaEasyStandardItemDto {

	private String companyCode;

	private String formulaCode;

	private String historyId;

	private String easyFormulaCode;

	private String referenceItemCode;

	public static FormulaEasyStandardItemDto fromDomain(FormulaEasyStandardItem domain) {
		return new FormulaEasyStandardItemDto(domain.getCompanyCode(), domain.getFormulaCode().v(),
				domain.getHistoryId(), domain.getEasyFormulaCode().v(), domain.getReferenceItemCode().v());
	}
}
