package nts.uk.ctx.pr.formula.app.find.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.formula.app.find.formulahistory.FormulaHistoryDto;
import nts.uk.ctx.pr.formula.dom.repository.FormulaHistoryRepository;
import nts.uk.ctx.pr.formula.dom.repository.FormulaMasterRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * @author Nam-PT A screen - init activity 1
 */
@Stateless
public class FormulaFinder {

	@Inject
	private FormulaMasterRepository repository;

	@Inject
	private FormulaHistoryRepository historyRepository;

	public List<FormulaFinderDto> init() {

		LoginUserContext login = AppContexts.user();
		boolean isExistedFormula = this.repository.isExistedFormulaByCompanyCode(login.companyCode());

		if (!isExistedFormula) {
			return null;
		}

		Map<String, FormulaDto> formulaDtos = repository.findAll(login.companyCode()).stream().map(f -> {
			return FormulaDto.fromDomain(f);
		}).collect(Collectors.toMap(c -> c.getFormulaCode(), c -> c));

		Map<String, List<FormulaHistoryDto>> formulaHistories = historyRepository.findAll(login.companyCode()).stream()
				.map(f -> {
					return FormulaHistoryDto.fromDomain(f);
				}).collect(Collectors.groupingBy(c -> c.getFormulaCode(), Collectors.toList()));

		List<FormulaFinderDto> result = new ArrayList<>();

		formulaDtos.entrySet().stream().forEach(c -> {
			List<FormulaHistoryDto> currentFormulaHistories = formulaHistories.get(c.getKey());

			if (currentFormulaHistories != null && !currentFormulaHistories.isEmpty()) {
				result.addAll(currentFormulaHistories.stream().map(x -> {
					FormulaFinderDto history = new FormulaFinderDto();
					history.setCcd(c.getValue().getCompanyCode());
					history.setFormulaCode(c.getValue().getFormulaCode());
					history.setFormulaName(c.getValue().getFormulaName());
					history.setDifficultyAtr(c.getValue().getDifficultyAtr());
					history.setHistoryId(x.getHistoryId());
					history.setStartDate(x.getStartDate());
					history.setEndDate(x.getEndDate());
					return history;
				}).collect(Collectors.toList()));
			}
		});

		return result;

	}

}
