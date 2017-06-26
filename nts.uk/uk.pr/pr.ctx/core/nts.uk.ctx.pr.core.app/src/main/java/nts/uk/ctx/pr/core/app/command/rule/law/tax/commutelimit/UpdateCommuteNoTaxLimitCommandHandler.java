package nts.uk.ctx.pr.core.app.command.rule.law.tax.commutelimit;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuNoTaxLimitName;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuNoTaxLimitValue;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuteNoTaxLimit;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuteNoTaxLimitRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
@Transactional
public class UpdateCommuteNoTaxLimitCommandHandler extends CommandHandler<UpdateCommuteNoTaxLimitCommand> {

	@Inject
	private CommuteNoTaxLimitRepository repository;

	@Override
	protected void handle(CommandHandlerContext<UpdateCommuteNoTaxLimitCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		UpdateCommuteNoTaxLimitCommand ic = context.getCommand();

		Optional<CommuteNoTaxLimit> commuteNoTaxLimitUpdate = this.repository.getCommuteNoTaxLimit(companyCode,
				ic.getCommuNoTaxLimitCode());

		if (!commuteNoTaxLimitUpdate.isPresent()) {
			throw new BusinessException("4");
		}
		commuteNoTaxLimitUpdate.get().setCommuNoTaxLimitName(new CommuNoTaxLimitName(ic.getCommuNoTaxLimitName()));
		commuteNoTaxLimitUpdate.get().setCommuNoTaxLimitValue(new CommuNoTaxLimitValue(ic.getCommuNoTaxLimitValue()));
		this.repository.update(commuteNoTaxLimitUpdate.get());

	}

}
