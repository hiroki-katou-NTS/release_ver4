package nts.uk.ctx.pr.formula.dom.repository;

import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;

public interface FormulaHistoryRepository {
	
	/**
	 * @param companyCode
	 * @return list of formula history in a company
	 */
	List<FormulaHistory> findAll(String companyCode);
	
	Optional<FormulaHistory> findByPriKey(String companyCode, FormulaCode formulaCode, String historyId);
	
	List<FormulaHistory> findDataDifFormulaCode(String companyCode, FormulaCode formulaCode, YearMonth baseDate);
	
	void add (FormulaHistory formulaHistory);
	
	void remove (String companyCode, FormulaCode formulaCode, String historyId);
	
	void update (FormulaHistory formulaHistory);
	
	void updateByKey(String companyCode, FormulaCode formulaCode, String historyId,YearMonth startDate);
	
	boolean isExistedHistory(String companyCode);
	
	boolean isExistedHistoryForUpdate(String companyCode, FormulaCode formulaCode, String historyId);
	
	Optional<FormulaHistory> findLastHistory(String companyCode, FormulaCode formulaCode);
	
	boolean isNewestHistory(String companyCode, FormulaCode formulaCode, YearMonth startDate);
	
	Optional<FormulaHistory> findPreviousHistory(String companyCode, FormulaCode formulaCode, YearMonth startDate);
	
}
