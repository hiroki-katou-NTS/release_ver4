/**
 * 
 */
package nts.uk.ctx.pr.formula.dom.repository;

import java.util.List;

import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyCondition;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;

/**
 * @author hungnm
 *
 */
public interface FormulaEasyConditionRepository {
	
	/**
	 * @ CCD = login company code
	 * @ FORMULA_CD = [B_INP_ 001]
	 * History ID of the history selected with @HIST_ID = [A_LST_001]
	 * 
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 * @return a formula easy condition
	 */
	List<FormulaEasyCondition> find(String companyCode, FormulaCode formulaCode, String historyId);
	
	void add(List<FormulaEasyCondition> formulaEasyCondition);
	
	void remove(String companyCode, FormulaCode formulaCode, String historyId);
}
