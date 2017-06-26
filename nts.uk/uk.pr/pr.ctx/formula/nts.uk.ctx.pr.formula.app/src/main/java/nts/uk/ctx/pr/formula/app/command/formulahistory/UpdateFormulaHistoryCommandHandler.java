package nts.uk.ctx.pr.formula.app.command.formulahistory;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaHistoryRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author nampt K screen - update history activity 35
 *
 */
@Stateless
public class UpdateFormulaHistoryCommandHandler extends CommandHandler<UpdateFormulaHistoryCommand> {

	@Inject
	private FormulaHistoryRepository formulaHistoryrepository;

	/**
	 * @ CCD = login company code @ FORMULA CD = [K _ LBL _ 002] History ID of
	 * the history selected with @HIST_ID = [A_LST_001]
	 */
	@Override
	protected void handle(CommandHandlerContext<UpdateFormulaHistoryCommand> context) {
		UpdateFormulaHistoryCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();

		// select previous history with startDate
		Optional<FormulaHistory> previousFormulaHistory = this.formulaHistoryrepository.findPreviousHistory(companyCode,
				new FormulaCode(command.getFormulaCode()), new YearMonth(command.getStartDate()));
		if (previousFormulaHistory.isPresent()
				&& (previousFormulaHistory.get().getStartDate().v() >= command.getStartDate()
						|| command.getStartDate() > previousFormulaHistory.get().getEndDate().v())) {
			throw new BusinessException("ER023");
		} else if (!previousFormulaHistory.isPresent()
				&& (command.getStartDate() < 190001 || 999912 < command.getStartDate())) {
			throw new BusinessException("ER023");
		}
		try {
			this.formulaHistoryrepository.updateByKey(companyCode, new FormulaCode(command.getFormulaCode()),
					command.getHistoryId(), new YearMonth(command.getStartDate()));
			if (this.formulaHistoryrepository.isNewestHistory(companyCode, new FormulaCode(command.getFormulaCode()),
					new YearMonth(command.getStartDate()))) {

				if (previousFormulaHistory.isPresent()) {
					this.formulaHistoryrepository.update(new FormulaHistory(companyCode,
							new FormulaCode(command.getFormulaCode()), previousFormulaHistory.get().getHistoryId(),
							new YearMonth(previousFormulaHistory.get().getStartDate().v()),
							new YearMonth(command.getStartDate()).addMonths(-1)));

				}
			}
		} catch (Exception e) {
			throw new BusinessException(new RawErrorMessage("OKボタンクリックで次の処理へ"));
		}

	}

}
