package nts.uk.ctx.core.app.command.socialinsurance.healthinsurance;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.core.app.command.socialinsurance.healthinsurance.command.AddHealthInsuranceCommand;
import nts.uk.ctx.core.dom.socialinsurance.healthinsurance.service.HealthInsuranceService;
@Stateless
@Transactional
public class UpdateHealthInsuranceFeeRateHistoryCommandHandler  extends CommandHandler<AddHealthInsuranceCommand>{

	@Inject
	private HealthInsuranceService healthInsuranceService;
	
	@Override
	protected void handle(CommandHandlerContext<AddHealthInsuranceCommand> context) {
		healthInsuranceService.updateHistory(context.getCommand().getOfficeCode(), context.getCommand().getYearMonthHistoryItem().fromCommandToDomain());
	}

}
