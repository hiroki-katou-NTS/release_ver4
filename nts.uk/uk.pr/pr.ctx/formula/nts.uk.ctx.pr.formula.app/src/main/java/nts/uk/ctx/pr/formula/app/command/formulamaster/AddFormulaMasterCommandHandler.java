package nts.uk.ctx.pr.formula.app.command.formulamaster;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.formula.dom.enums.ConditionAtr;
import nts.uk.ctx.pr.formula.dom.enums.DifficultyAtr;
import nts.uk.ctx.pr.formula.dom.enums.ReferenceMasterNo;
import nts.uk.ctx.pr.formula.dom.formula.FormulaEasyHeader;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;
import nts.uk.ctx.pr.formula.dom.formula.FormulaMaster;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaName;
import nts.uk.ctx.pr.formula.dom.repository.FormulaMasterDomainService;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author nampt
 *	activity 3
 */
@Stateless
public class AddFormulaMasterCommandHandler extends CommandHandler<AddFormulaMasterCommand> {

	@Inject
	private FormulaMasterDomainService formulaMasterDomainService;

	@Override
	protected void handle(CommandHandlerContext<AddFormulaMasterCommand> context) {

		AddFormulaMasterCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		String historyId = IdentifierUtil.randomUniqueId();
		
		FormulaMaster formulaMasterDomain = context.getCommand().toDomainMaster(companyCode);
		FormulaHistory formulaHistoryDomain = context.getCommand().toDomainHis(companyCode, historyId);
		
		formulaMasterDomain.validate();
		formulaHistoryDomain.validate();

		FormulaMaster formulaMaster = new FormulaMaster(companyCode, new FormulaCode(command.getFormulaCode()),
				EnumAdaptor.valueOf(command.getDifficultyAtr().intValue(), DifficultyAtr.class),
				new FormulaName(command.getFormulaName()));

		FormulaHistory formulaHistory = new FormulaHistory(companyCode, new FormulaCode(command.getFormulaCode()),
				historyId, new YearMonth(command.getStartDate()), new YearMonth(GeneralDate.max().year()*100 + GeneralDate.max().month()));

		FormulaEasyHeader formulaEasyHeader = new FormulaEasyHeader(companyCode,
				new FormulaCode(command.getFormulaCode()), historyId,
				EnumAdaptor.valueOf(command.getConditionAtr(), ConditionAtr.class),
				EnumAdaptor.valueOf(command.getRefMasterNo(), ReferenceMasterNo.class));

		formulaMasterDomainService.add(formulaMaster, formulaHistory, formulaEasyHeader);
	}

}
