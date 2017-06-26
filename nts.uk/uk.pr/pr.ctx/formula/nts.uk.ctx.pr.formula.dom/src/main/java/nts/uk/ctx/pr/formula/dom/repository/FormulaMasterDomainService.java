package nts.uk.ctx.pr.formula.dom.repository;

import java.util.List;

import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyCondition;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyDetail;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyHeader;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyStandardItem;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;
import nts.uk.ctx.pr.formula.dom.formula.FormulaManual;
import nts.uk.ctx.pr.formula.dom.formula.FormulaMaster;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaName;

public interface FormulaMasterDomainService {
	void add(FormulaMaster formulaMaster, FormulaHistory formulaHistory, FormulaEasyHeader formulaEasyHeader);

	void update(int difficultyAtr, String companyCode, FormulaCode formulaCode, FormulaName formulaName, String historyId,
			List<FormulaEasyCondition> listFormulaEasyCondition, List<FormulaEasyDetail> listFormulaEasyDetail,
			List<FormulaEasyStandardItem> listFormulaEasyStandardItem, FormulaManual formulaManual);
}
