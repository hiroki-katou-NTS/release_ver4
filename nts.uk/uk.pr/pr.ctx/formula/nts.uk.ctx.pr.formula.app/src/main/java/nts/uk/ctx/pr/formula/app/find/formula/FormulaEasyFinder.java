package nts.uk.ctx.pr.formula.app.find.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.formula.dom.primitive.EasyFormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaEasyDetailRepository;
import nts.uk.ctx.pr.formula.dom.repository.FormulaEasyStandardItemRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * @author Nam-PT L screen activity 24
 */
@Stateless
public class FormulaEasyFinder {

	@Inject
	private FormulaEasyStandardItemRepository formulaEasyStandardItemRepository;

	@Inject
	private FormulaEasyDetailRepository formulaEasyDetailRepository;

	public FormulaEasyFinderDto init(String formulaCode, String historyId, String easyFormulaCode) {
		LoginUserContext login = AppContexts.user();
		String companyCode = login.companyCode();

		Optional<FormulaEasyDetailDto> formulaEasyDetailDto = formulaEasyDetailRepository.findByPriKey(companyCode,
				new FormulaCode(formulaCode), historyId, new EasyFormulaCode(easyFormulaCode))
				.map(f -> FormulaEasyDetailDto.fromDomain(f));
		if (!formulaEasyDetailDto.isPresent()) {
			return null;
		}

		FormulaEasyFinderDto formulaEasyFinderDto = new FormulaEasyFinderDto();
		formulaEasyFinderDto.setEasyFormulaCode(formulaEasyDetailDto.get().getEasyFormulaCode());
		formulaEasyFinderDto.setEasyFormulaName(formulaEasyDetailDto.get().getEasyFormulaName());
		formulaEasyFinderDto.setEasyFormulaTypeAtr(formulaEasyDetailDto.get().getEasyFormulaTypeAtr());
		formulaEasyFinderDto.setBaseFixedAmount(formulaEasyDetailDto.get().getBaseFixedAmount());
		formulaEasyFinderDto.setBaseAmountDevision(formulaEasyDetailDto.get().getBaseAmountDevision());
		formulaEasyFinderDto.setBaseFixedValue(formulaEasyDetailDto.get().getBaseFixedValue());
		formulaEasyFinderDto.setBaseValueDevision(formulaEasyDetailDto.get().getBaseValueDevision());
		formulaEasyFinderDto.setPremiumRate(formulaEasyDetailDto.get().getPremiumRate());
		formulaEasyFinderDto.setRoundProcessingDevision(formulaEasyDetailDto.get().getRoundProcessingDevision());
		formulaEasyFinderDto.setCoefficientDivision(formulaEasyDetailDto.get().getCoefficientDivision());
		formulaEasyFinderDto.setCoefficientFixedValue(formulaEasyDetailDto.get().getCoefficientFixedValue());
		formulaEasyFinderDto.setAdjustmentDevision(formulaEasyDetailDto.get().getAdjustmentDevision());
		formulaEasyFinderDto.setTotalRounding(formulaEasyDetailDto.get().getTotalRounding());
		List<String> referenceItemCodes = new ArrayList<>();
		/* baseAmount = 基準金額 */
		if (formulaEasyDetailDto.get().getBaseAmountDevision().intValue() == 1
				|| formulaEasyDetailDto.get().getBaseAmountDevision().intValue() == 2
				|| formulaEasyDetailDto.get().getBaseAmountDevision().intValue() == 3
				|| formulaEasyDetailDto.get().getBaseAmountDevision().intValue() == 4) {
			List<FormulaEasyStandardItemDto> formulaEasyStandardItemDtos = formulaEasyStandardItemRepository
					.findAll(companyCode, new FormulaCode(formulaCode), historyId, new EasyFormulaCode(easyFormulaCode))
					.stream().map(f -> FormulaEasyStandardItemDto.fromDomain(f)).collect(Collectors.toList());
			
			formulaEasyStandardItemDtos.stream().forEach(f -> {
				referenceItemCodes.add(f.getReferenceItemCode());
			});
			
		}
		formulaEasyFinderDto.setReferenceItemCodes(referenceItemCodes);
		return formulaEasyFinderDto;
	}

}
