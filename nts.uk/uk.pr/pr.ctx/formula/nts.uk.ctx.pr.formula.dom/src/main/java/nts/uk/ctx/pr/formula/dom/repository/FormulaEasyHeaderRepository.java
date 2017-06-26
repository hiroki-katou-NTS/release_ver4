/**
 * 
 */
package nts.uk.ctx.pr.formula.dom.repository;

import java.util.Optional;

import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyHeader;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;

/**
 * @author hungnm
 *
 */
public interface FormulaEasyHeaderRepository {
	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 * @param referenceMasterNo
	 * @return a formulaEasyHead
	 */
	Optional<FormulaEasyHeader> findByPriKey(String companyCode, FormulaCode formulaCode, String historyId);
	
	void add(FormulaEasyHeader formulaEasyHead);
	
	void remove(String companyCode, FormulaCode formulaCode, String historyId);
}
