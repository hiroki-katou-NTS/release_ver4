package nts.uk.ctx.pr.formula.app.find.formulahistory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaHistoryRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * @author nampt
 *	activity 22 , 23
 *	J + K screen start
 */
@Stateless
public class FormulaHistoryFinder {

	@Inject
	private FormulaHistoryRepository formulaHistoryRepository;

	public List<FormulaHistoryDto> find() {

		LoginUserContext login = AppContexts.user();

		return this.formulaHistoryRepository.findAll(login.companyCode()).stream().map(f -> {
			return FormulaHistoryDto.fromDomain(f);
		}).collect(Collectors.toList());

	}

	/**
	 * Add history 
	 * J + K screen
	 * 
	 * @ CCD = login company code
	 * @ FORMULA_CD = calculation formula code of the item selected on the caller screen
     * @ HIST_ID = History ID of the latest history of the item selected on the caller screen
	 * 
	 * @param formulaCode
	 * @param historyId
	 * @return
	 */
	public Optional<FormulaHistoryDto> findByPriKey(String formulaCode, String historyId) {

		LoginUserContext login = AppContexts.user();

		return formulaHistoryRepository.findByPriKey(login.companyCode(), new FormulaCode(formulaCode), historyId)
				.map(f -> FormulaHistoryDto.fromDomain(f));
	}

	public List<FormulaHistoryDto> findDataDifFormulaCode(String formulaCode, int baseDate) {
		LoginUserContext login = AppContexts.user();

		return formulaHistoryRepository.findDataDifFormulaCode(login.companyCode(), new FormulaCode(formulaCode), new YearMonth(baseDate))
				.stream().map(f -> FormulaHistoryDto.fromDomain(f)).collect(Collectors.toList());
	}

}
