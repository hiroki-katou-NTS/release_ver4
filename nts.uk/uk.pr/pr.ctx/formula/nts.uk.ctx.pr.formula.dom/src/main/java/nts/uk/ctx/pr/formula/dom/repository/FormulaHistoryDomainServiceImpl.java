package nts.uk.ctx.pr.formula.dom.repository;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyHeader;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;

/**
 * @author Nam-PT K screen activity 33
 *
 */
@Stateless
public class FormulaHistoryDomainServiceImpl implements FormulaHistoryDomainService {

	@Inject
	private FormulaHistoryRepository formulaHistoryRepository;

	@Inject
	private FormulaEasyHeaderRepository formulaEasyHeaderRepository;

	@Inject
	private FormulaManualRepository formulaManualRepository;

	@Inject
	private FormulaEasyConditionRepository formulaEasyConditionRepository;

	@Inject
	private FormulaEasyDetailRepository formulaEasyDetailRepository;

	@Inject
	private FormulaEasyStandardItemRepository formulaEasyStandardItemRepository;

	@Inject
	private FormulaMasterRepository formulaMasterRepository;

	public void add(int difficultyAtr, FormulaHistory formulaHistoryAdd, FormulaEasyHeader formulaEasyHead,
			FormulaHistory previousFormulaHistoryUpdate) {

		try {
			this.formulaHistoryRepository.add(formulaHistoryAdd);
			if (difficultyAtr == 0) {
				this.formulaEasyHeaderRepository.add(formulaEasyHead);
			}
			if (previousFormulaHistoryUpdate != null) {
				this.formulaHistoryRepository.update(previousFormulaHistoryUpdate);
			}
		} catch (Exception e) {
			throw new BusinessException(new RawErrorMessage("OKボタンクリックで次の処理へ"));
		}
	}

	public void remove(int difficultyAtr, String companyCode, String formulaCode, String historyId, int startDate) {

		try {
			this.formulaHistoryRepository.remove(companyCode, new FormulaCode(formulaCode), historyId);

			if (difficultyAtr == 1) {
				this.formulaManualRepository.remove(companyCode, new FormulaCode(formulaCode), historyId);
			} else if (difficultyAtr == 0) {
				this.formulaEasyHeaderRepository.remove(companyCode, new FormulaCode(formulaCode), historyId);

				this.formulaEasyConditionRepository.remove(companyCode, new FormulaCode(formulaCode), historyId);

				this.formulaEasyDetailRepository.remove(companyCode, new FormulaCode(formulaCode), historyId);

				this.formulaEasyStandardItemRepository.remove(companyCode, new FormulaCode(formulaCode), historyId);
			}

			if (!this.formulaHistoryRepository.isNewestHistory(companyCode, new FormulaCode(formulaCode),
					new YearMonth(startDate))) {
				this.formulaMasterRepository.remove(companyCode, new FormulaCode(formulaCode));
			} else if (this.formulaHistoryRepository.isNewestHistory(companyCode, new FormulaCode(formulaCode),
					new YearMonth(startDate))) {
				// select last history
				Optional<FormulaHistory> lastFormulaHistory = this.formulaHistoryRepository.findLastHistory(companyCode,
						new FormulaCode(formulaCode));
				// update history by historyId of last history, startDate = startDate input
				if (lastFormulaHistory.isPresent()) {
					this.formulaHistoryRepository.update(new FormulaHistory(companyCode, new FormulaCode(formulaCode),
							lastFormulaHistory.get().getHistoryId(), lastFormulaHistory.get().getStartDate(),
							new YearMonth(GeneralDate.max().year() * 100 + GeneralDate.max().month())));
				}
			}
		} catch (Exception e) {
			throw new BusinessException(new RawErrorMessage("OKボタンクリックで次の処理へ"));
		}
	}

}
