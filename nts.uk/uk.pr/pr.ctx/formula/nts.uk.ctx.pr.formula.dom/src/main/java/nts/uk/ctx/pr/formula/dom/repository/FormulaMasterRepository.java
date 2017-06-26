package nts.uk.ctx.pr.formula.dom.repository;

import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.formula.dom.formula.FormulaMaster;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaName;

/**
 * @author hungnm
 *
 */
public interface FormulaMasterRepository {
	
	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param baseYearMonth
	 * @return a formula master
	 */
	Optional<FormulaMaster> findByBaseYearMonth(String companyCode, FormulaCode formulaCode, YearMonth baseYM);
	
	/**
	 * @param companyCode
	 * @return list of formulas in a company
	 */
	List<FormulaMaster> findAll(String companyCode);
	
	Optional<FormulaMaster> findByCompanyCodeAndFormulaCode(String companyCode, FormulaCode formulaCode);
	
	List<FormulaMaster> findByCompanyCodeAndFormulaCodes(String companyCode, List<FormulaCode> formulaCode);
	
	void add(FormulaMaster formulaMaster);
	
	void update(String companyCode, FormulaCode formulaCode, FormulaName formulaName);
	
	void remove(String companyCode, FormulaCode formulaCode);
	
	boolean isExistedFormulaByCompanyCode(String companyCode);
	
	boolean isExistedFormula(String companyCode, FormulaCode formulaCode);
}
