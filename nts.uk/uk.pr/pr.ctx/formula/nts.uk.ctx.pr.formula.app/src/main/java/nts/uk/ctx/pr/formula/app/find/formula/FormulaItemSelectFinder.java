package nts.uk.ctx.pr.formula.app.find.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.formula.app.find.formulahistory.FormulaHistoryDto;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaHistoryRepository;
import nts.uk.ctx.pr.formula.dom.repository.FormulaMasterRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * 
 * @author Nam-PT activity 18 H screen
 *
 */
@Stateless
public class FormulaItemSelectFinder {

	@Inject
	private FormulaHistoryRepository formulaHistoryRepository;

	@Inject
	private FormulaMasterRepository formulaMasterRepository;

	public List<FormulaItemSelectDto> init(String formulaCode, int baseDate) {

		LoginUserContext login = AppContexts.user();
		String companyCode = login.companyCode();

		List<FormulaHistoryDto> lstFormulaHistoryDto = formulaHistoryRepository
				.findDataDifFormulaCode(companyCode, new FormulaCode(formulaCode), new YearMonth(baseDate)).stream()
				.map(f -> FormulaHistoryDto.fromDomain(f)).collect(Collectors.toList());
		List<FormulaItemSelectDto> lstFormulaDto = new ArrayList<>();
		if (lstFormulaHistoryDto.size() > 0) {
			List<FormulaCode> lstFormulaCode = lstFormulaHistoryDto.stream().map(
					history -> {
						return new FormulaCode(history.getFormulaCode());
					}
			).collect(Collectors.toList());
			lstFormulaDto = formulaMasterRepository
					.findByCompanyCodeAndFormulaCodes(companyCode, lstFormulaCode)
					.stream()
					.filter(formula -> formula.getDifficultyAtr().value == 1)
					.map(formula -> FormulaItemSelectDto.fromDomain(formula))
					.collect(Collectors.toList());
		}
		return lstFormulaDto;
	}

}
