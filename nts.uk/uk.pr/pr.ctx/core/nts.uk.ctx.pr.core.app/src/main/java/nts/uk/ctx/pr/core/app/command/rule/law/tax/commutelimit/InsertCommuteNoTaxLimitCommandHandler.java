package nts.uk.ctx.pr.core.app.command.rule.law.tax.commutelimit;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuNoTaxLimitCode;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuNoTaxLimitName;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuNoTaxLimitValue;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuteNoTaxLimit;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuteNoTaxLimitRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
@Transactional
public class InsertCommuteNoTaxLimitCommandHandler extends CommandHandler<InsertCommuteNoTaxLimitCommand> {

	@Inject
	private CommuteNoTaxLimitRepository repository;

	@Override
	protected void handle(CommandHandlerContext<InsertCommuteNoTaxLimitCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		InsertCommuteNoTaxLimitCommand insertCommand = context.getCommand();

		Optional<CommuteNoTaxLimit> exitCommuteNoTaxLimit = this.repository.getCommuteNoTaxLimit(companyCode,
				insertCommand.getCommuNoTaxLimitCode());
		
		if (exitCommuteNoTaxLimit.isPresent()) {
			throw new BusinessException("3");
		}
		CommuteNoTaxLimit commuteNoTaxLimit = new CommuteNoTaxLimit(companyCode,
				new CommuNoTaxLimitCode(insertCommand.getCommuNoTaxLimitCode()),
				new CommuNoTaxLimitName(insertCommand.getCommuNoTaxLimitName()),
				new CommuNoTaxLimitValue(insertCommand.getCommuNoTaxLimitValue()));

		this.repository.add(commuteNoTaxLimit);
	}

}
