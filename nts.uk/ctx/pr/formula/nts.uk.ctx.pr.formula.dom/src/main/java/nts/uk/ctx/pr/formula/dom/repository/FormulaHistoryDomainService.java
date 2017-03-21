package nts.uk.ctx.pr.formula.dom.repository;

import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyHeader;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;

public interface FormulaHistoryDomainService {

	void add(FormulaHistory formulaHistoryAdd, FormulaEasyHeader formulaEasyHead, FormulaHistory formulaHistoryUpdate);

	void remove(int conditionAtr, String companyCode, String formulaCode, String historyId, int startDate);
}
