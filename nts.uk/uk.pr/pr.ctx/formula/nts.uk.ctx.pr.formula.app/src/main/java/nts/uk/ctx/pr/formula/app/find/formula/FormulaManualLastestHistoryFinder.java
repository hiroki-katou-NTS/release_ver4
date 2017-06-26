package nts.uk.ctx.pr.formula.app.find.formula;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaHistoryRepository;
import nts.uk.ctx.pr.formula.dom.repository.FormulaManualRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

@Stateless
public class FormulaManualLastestHistoryFinder {
	@Inject
	FormulaManualRepository formulaManualRepository;

	@Inject
	private FormulaHistoryRepository formulaHistoryRepository;

	public FormulaManualDto find(String formulaCode) {
		LoginUserContext login = AppContexts.user();
		FormulaHistory lastestHistory = formulaHistoryRepository
				.findLastHistory(login.companyCode(), new FormulaCode(formulaCode)).get();
		FormulaManualDto formulaManual = formulaManualRepository
				.findByPriKey(login.companyCode(), new FormulaCode(formulaCode), lastestHistory.getHistoryId())
				.map(domain -> FormulaManualDto.fromDomain(domain)).get();
		return formulaManual;
	}

}
