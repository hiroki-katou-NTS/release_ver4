package nts.uk.ctx.pr.formula.app.command.formulamaster;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.formula.dom.enums.AdjustmentAtr;
import nts.uk.ctx.pr.formula.dom.enums.BaseMoneyAtr;
import nts.uk.ctx.pr.formula.dom.enums.DivideValueSet;
import nts.uk.ctx.pr.formula.dom.enums.EasyFormulaTypeAtr;
import nts.uk.ctx.pr.formula.dom.enums.FixFormulaAtr;
import nts.uk.ctx.pr.formula.dom.enums.ReferenceMonthAtr;
import nts.uk.ctx.pr.formula.dom.enums.RoundAtr;
import nts.uk.ctx.pr.formula.dom.enums.RoundDigit;
import nts.uk.ctx.pr.formula.dom.enums.RoundMethod;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyCondition;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyDetail;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyStandardItem;
import nts.uk.ctx.pr.formula.dom.formula.FormulaManual;
import nts.uk.ctx.pr.formula.dom.primitive.DivideValue;
import nts.uk.ctx.pr.formula.dom.primitive.EasyFormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.EasyFormulaName;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaContent;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaName;
import nts.uk.ctx.pr.formula.dom.primitive.Money;
import nts.uk.ctx.pr.formula.dom.primitive.PremiumRate;
import nts.uk.ctx.pr.formula.dom.primitive.ReferenceItemCode;
import nts.uk.ctx.pr.formula.dom.primitive.ReferenceMasterCode;
import nts.uk.ctx.pr.formula.dom.primitive.WorkItemCode;
import nts.uk.ctx.pr.formula.dom.primitive.WorkValue;
import nts.uk.ctx.pr.formula.dom.repository.FormulaMasterDomainService;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author nampt activity 3
 */
@Stateless
public class UpdateFormulaMasterCommandHandler extends CommandHandler<UpdateFormulaMasterCommand> {

	@Inject
	private FormulaMasterDomainService formulaMasterDomainService;

	@Override
	protected void handle(CommandHandlerContext<UpdateFormulaMasterCommand> context) {
		UpdateFormulaMasterCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		
		List<FormulaEasyCondition> listFormulaEasyCondition = new ArrayList<>();
		List<FormulaEasyDetail> listFormulaEasyDetail = new ArrayList<>();
		List<FormulaEasyStandardItem> listFormulaEasyStandardItem = new ArrayList<>();
		FormulaManual formulaManual = new FormulaManual(companyCode, new FormulaCode(command.getFormulaCode()),
				command.getHistoryId());
		if (command.getDifficultyAtr() == 0) {
			// [かんたん計算_条件.INS-1]を実施する
			listFormulaEasyCondition = command.getEasyFormulaDto().stream().map(f -> {
				FormulaEasyCondition formulaEasyCondition = new FormulaEasyCondition(companyCode,
						new FormulaCode(command.getFormulaCode()), command.getHistoryId(),
						new EasyFormulaCode(f.getEasyFormulaCode()),
						EnumAdaptor.valueOf(f.getFixFormulaAtr(), FixFormulaAtr.class), new Money(f.getValue()),
						new ReferenceMasterCode(f.getReferenceMasterNo()));
				return formulaEasyCondition;
			}).collect(Collectors.toList());

			command.getEasyFormulaDto().forEach(easyFormulaDto -> {
				if (easyFormulaDto.getFixFormulaAtr() == 1 &&  easyFormulaDto.getFormulaDetail().getBaseAmountDevision() != null) {
					FormulaEasyDetail formulaEasyDetail = new FormulaEasyDetail(companyCode,
							new FormulaCode(command.getFormulaCode()), command.getHistoryId(),
							new EasyFormulaCode(easyFormulaDto.getEasyFormulaCode()),
							new EasyFormulaName(easyFormulaDto.getFormulaDetail().getEasyFormulaName()),
							EnumAdaptor.valueOf(easyFormulaDto.getFormulaDetail().getEasyFormulaTypeAtr().intValue(),
									EasyFormulaTypeAtr.class),
							new Money(easyFormulaDto.getFormulaDetail().getBaseFixedAmount()),
							EnumAdaptor.valueOf(easyFormulaDto.getFormulaDetail().getBaseAmountDevision().intValue(),
									BaseMoneyAtr.class),
							new DivideValue(easyFormulaDto.getFormulaDetail().getBaseFixedValue()),
							EnumAdaptor.valueOf(easyFormulaDto.getFormulaDetail().getBaseValueDevision().intValue(),
									DivideValueSet.class),
							new PremiumRate(easyFormulaDto.getFormulaDetail().getPremiumRate()),
							EnumAdaptor.valueOf(
									easyFormulaDto.getFormulaDetail().getRoundProcessingDevision().intValue(),
									RoundAtr.class),
							new WorkItemCode(easyFormulaDto.getFormulaDetail().getCoefficientDivision()),
							new WorkValue(easyFormulaDto.getFormulaDetail().getCoefficientFixedValue()),
							EnumAdaptor.valueOf(easyFormulaDto.getFormulaDetail().getAdjustmentDevision().intValue(),
									AdjustmentAtr.class),
							EnumAdaptor.valueOf(easyFormulaDto.getFormulaDetail().getTotalRounding().intValue(),
									RoundAtr.class));
					
					formulaEasyDetail.validate();	
					
					listFormulaEasyDetail.add(formulaEasyDetail);
				}
			});

			command.getEasyFormulaDto().forEach(setFormulaEasyStandard -> {
				if (setFormulaEasyStandard.getFixFormulaAtr() == 1) {
					setFormulaEasyStandard.getFormulaDetail().getReferenceItemCodes().forEach(item -> {
						listFormulaEasyStandardItem.add(new FormulaEasyStandardItem(companyCode,
								new FormulaCode(command.getFormulaCode()), command.getHistoryId(),
								new EasyFormulaCode(setFormulaEasyStandard.getEasyFormulaCode()),
								new ReferenceItemCode(item)));
					});
				}
			});
		} else {
			formulaManual = new FormulaManual(companyCode, new FormulaCode(command.getFormulaCode()),
					command.getHistoryId(), new FormulaContent(command.getFormulaContent()),
					EnumAdaptor.valueOf(command.getReferenceMonthAtr(), ReferenceMonthAtr.class),
					EnumAdaptor.valueOf(command.getRoundAtr(), RoundMethod.class),
					EnumAdaptor.valueOf(command.getRoundDigit(), RoundDigit.class));
		}
		formulaMasterDomainService.update(command.getDifficultyAtr(), companyCode,
				new FormulaCode(command.getFormulaCode()), new FormulaName(command.getFormulaName()) , command.getHistoryId(), listFormulaEasyCondition,
				listFormulaEasyDetail, listFormulaEasyStandardItem, formulaManual);
	}

}
