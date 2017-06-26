/**
 * 
 */
package nts.uk.ctx.pr.formula.dom.repository;

import java.util.Optional;

import nts.uk.ctx.pr.formula.dom.formula.FormulaManual;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;

/**
 * @author hungnm
 *
 */
public interface FormulaManualRepository {
	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 * @return a formula manual
	 */
	Optional<FormulaManual> findByPriKey(String companyCode, FormulaCode formulaCode, String historyId);
	
	void add(FormulaManual formulaManual);
	
	void update(FormulaManual formulaManual);
	
	/**
	 * @ CCD = login company code
	 * @ FORMULA CD = [K _ LBL _ 002]
	 * History ID of the history selected with @HIST_ID = [A_LST_001]
	 * @param formulaManual
	 */
	void remove(String companyCode, FormulaCode formulaCode, String historyId);
}
