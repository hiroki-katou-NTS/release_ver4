package nts.uk.ctx.pr.formula.app.find.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.formula.dom.primitive.EasyFormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaEasyConditionRepository;
import nts.uk.ctx.pr.formula.dom.repository.FormulaEasyDetailRepository;
import nts.uk.ctx.pr.formula.dom.repository.FormulaEasyStandardItemRepository;
import nts.uk.ctx.pr.formula.dom.repository.FormulaManualRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * @author Nam-PT activity 8 C screen
 *
 */
@Stateless
public class FormulaSettingFinder {

	@Inject
	FormulaEasyStandardItemRepository formulaEasyStandardItemRepository;

	@Inject
	FormulaEasyConditionRepository formulaEasyConditionRepository;

	@Inject
	FormulaEasyDetailRepository formulaEasyDetailRepository;

	@Inject
	FormulaManualRepository formulaManualRepository;

	public FormulaSettingDto init(String formulaCode, String historyId, int difficultyAtr) {

		LoginUserContext login = AppContexts.user();
		FormulaSettingDto formulaSettingDto = new FormulaSettingDto();

		if (difficultyAtr == 0) {
			// かんたん計算_条件.SEL-1
			List<FormulaEasyConditionDto> formulaEasyConditions = formulaEasyConditionRepository
					.find(login.companyCode(), new FormulaCode(formulaCode), historyId).stream().map(f -> {
						FormulaEasyConditionDto formulaEasyConditionDto = new FormulaEasyConditionDto();
						formulaEasyConditionDto.setEasyFormulaCode(f.getEasyFormulaCode().v());
						formulaEasyConditionDto.setFixFormulaAtr(f.getFixFormulaAtr().value);
						formulaEasyConditionDto.setFixMoney(f.getFixMoney().v());
						formulaEasyConditionDto.setReferenceMasterCode(f.getReferenceMasterCode().v());
						return formulaEasyConditionDto;
					}).collect(Collectors.toList());
			if (!formulaEasyConditions.isEmpty()) {
				List<EasyFormulaFindDto> easyFormula = new ArrayList<>();
				List<EasyFormulaCode> easyFormulaCodes = formulaEasyConditions.stream().map(easyConditions -> {
					return new EasyFormulaCode(easyConditions.getEasyFormulaCode());
				}).collect(Collectors.toList());
				Map<String, FormulaEasyDetailDto> formulaEasyDetailDtos = formulaEasyDetailRepository
						.findByPriKeys(login.companyCode(), new FormulaCode(formulaCode), historyId, easyFormulaCodes)
						.stream().map(formulaEasyDetail -> FormulaEasyDetailDto.fromDomain(formulaEasyDetail))
						.collect(Collectors.toList()).stream()
						.collect(Collectors.toMap(FormulaEasyDetailDto::getEasyFormulaCode, x -> x));
				formulaEasyConditions.forEach(f -> {
					EasyFormulaFindDto easyFormulaFindDto = new EasyFormulaFindDto();
					easyFormulaFindDto.setEasyFormulaCode(f.getEasyFormulaCode());
					easyFormulaFindDto.setValue(f.getFixMoney());
					easyFormulaFindDto.setRefMasterNo(f.getReferenceMasterCode());
					easyFormulaFindDto.setFixFormulaAtr(f.getFixFormulaAtr());

					FormulaEasyFinderDto formulaEasyFinderDto = new FormulaEasyFinderDto();
					FormulaEasyDetailDto detailDto = formulaEasyDetailDtos.get(easyFormulaFindDto.getEasyFormulaCode());
					if (detailDto != null) {
						formulaEasyFinderDto.setEasyFormulaCode(detailDto.getEasyFormulaCode());
						formulaEasyFinderDto.setEasyFormulaName(detailDto.getEasyFormulaName());
						// formulaEasyFinderDto.setBaseFixedAmount(detailDto.getBaseFixedAmount());
						// formulaEasyFinderDto.setBaseAmountDevision(detailDto.getBaseAmountDevision());
						// formulaEasyFinderDto.setBaseFixedAmount(detailDto.getBaseFixedAmount());
						// formulaEasyFinderDto.setBaseValueDevision(detailDto.getBaseValueDevision());
						// formulaEasyFinderDto.setPremiumRate(detailDto.getPremiumRate());
						// formulaEasyFinderDto.setRoundProcessingDevision(detailDto.getRoundProcessingDevision());
						// formulaEasyFinderDto.setCoefficientDivision(detailDto.getCoefficientDivision());
						// formulaEasyFinderDto.setCoefficientFixedValue(detailDto.getCoefficientFixedValue());
						// formulaEasyFinderDto.setAdjustmentDevision(detailDto.getAdjustmentDevision());
						// formulaEasyFinderDto.setTotalRounding(detailDto.getTotalRounding());
						// formulaEasyFinderDto.setMaxLimitValue(detailDto.getMaxLimitValue());
						// formulaEasyFinderDto.setMinLimitValue(detailDto.getMinLimitValue());
						// formulaEasyFinderDto.setBaseFixedValue(detailDto.getBaseFixedValue());
						// formulaEasyFinderDto.setEasyFormulaTypeAtr(detailDto.getEasyFormulaTypeAtr());
					}

					// // Select reference Code from FormulaEasyStandardItem
					// List<FormulaEasyStandardItemDto>
					// formulaEasyStandardItemDtos =
					// formulaEasyStandardItemRepository
					// .findAll(login.companyCode(), new
					// FormulaCode(formulaCode), historyId,
					// new EasyFormulaCode(f.getEasyFormulaCode()))
					// .stream().map(dto ->
					// FormulaEasyStandardItemDto.fromDomain(dto))
					// .collect(Collectors.toList());
					//
					// // set reference Code in List<String> referenceItemCodes
					// formulaEasyFinderDto
					// .setReferenceItemCodes(formulaEasyStandardItemDtos.stream().map(easyStandard
					// -> {
					// return easyStandard.getReferenceItemCode();
					// }).collect(Collectors.toList()));

					easyFormulaFindDto.setFormulaEasyDetail(formulaEasyFinderDto);
					easyFormula.add(easyFormulaFindDto);
				});
				formulaSettingDto.setEasyFormula(easyFormula);
			}

		} else if (difficultyAtr == 1) {
			Optional<FormulaManualDto> formulaManualDto = formulaManualRepository
					.findByPriKey(login.companyCode(), new FormulaCode(formulaCode), historyId)
					.map(f -> FormulaManualDto.fromDomain(f));
			if (formulaManualDto.isPresent()) {
				formulaSettingDto.setFormulaContent(formulaManualDto.get().getFormulaContent());
				formulaSettingDto.setReferenceMonthAtr(formulaManualDto.get().getReferenceMonthAtr());
				formulaSettingDto.setRoundAtr(formulaManualDto.get().getRoundAtr());
				formulaSettingDto.setRoundDigit(formulaManualDto.get().getRoundDigit());
			}
		}

		return formulaSettingDto;
	}

}
