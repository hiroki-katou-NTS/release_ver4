package nts.uk.ctx.pr.formula.app.find.formula;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.formula.app.find.formulahistory.FormulaHistoryDto;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaEasyHeaderRepository;
import nts.uk.ctx.pr.formula.dom.repository.FormulaHistoryRepository;
import nts.uk.ctx.pr.formula.dom.repository.FormulaMasterRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * @author Nam-PT
 * activity 7
 * B screen
 *
 */
@Stateless
public class FormulaBasicInformationFinder {

	@Inject
	private FormulaMasterRepository formulaMasterRepository;

	@Inject
	private FormulaHistoryRepository formulaHistoryRepository;

	@Inject
	private FormulaEasyHeaderRepository formulaEasyHeaderRepository;

	public FormulaBasicInformationDto init(String fomulaCode, String historyId) {
		LoginUserContext login = AppContexts.user();

		Optional<FormulaDto> formulaDto = formulaMasterRepository
				.findByCompanyCodeAndFormulaCode(login.companyCode(), new FormulaCode(fomulaCode))
				.map(f -> FormulaDto.fromDomain(f));

		Optional<FormulaHistoryDto> formulaHistoryDto = formulaHistoryRepository
				.findByPriKey(login.companyCode(), new FormulaCode(fomulaCode), historyId)
				.map(f -> FormulaHistoryDto.fromDomain(f));

		FormulaBasicInformationDto formulaBasicInformationDto = new FormulaBasicInformationDto();

		formulaBasicInformationDto.setFormulaCode(formulaDto.isPresent() ? formulaDto.get().getFormulaCode() : null);
		formulaBasicInformationDto.setFormulaName(formulaDto.isPresent() ? formulaDto.get().getFormulaName() : null);
		formulaBasicInformationDto.setDifficultyAtr(formulaDto.isPresent() ? formulaDto.get().getDifficultyAtr() : null);
		formulaBasicInformationDto.setStartDate(formulaHistoryDto.isPresent() ? formulaHistoryDto.get().getStartDate() : null);
		formulaBasicInformationDto.setEndDate(formulaHistoryDto.isPresent() ? formulaHistoryDto.get().getEndDate() : null);

		if (formulaDto.get().getDifficultyAtr() == 0) {
			Optional<FormulaEasyHeadDto> formulaEasyHeader = formulaEasyHeaderRepository
					.findByPriKey(login.companyCode(), new FormulaCode(fomulaCode), historyId)
					.map(f -> FormulaEasyHeadDto.fromDomain(f));

			formulaBasicInformationDto.setConditionAtr(formulaEasyHeader.isPresent() ? formulaEasyHeader.get().getConditionAtr().intValue() : null);
			formulaBasicInformationDto.setRefMasterNo(formulaEasyHeader.isPresent() ? formulaEasyHeader.get().getReferenceMasterNo().intValue() : null);
			
			//master
		}

		return formulaBasicInformationDto;
	}
}
