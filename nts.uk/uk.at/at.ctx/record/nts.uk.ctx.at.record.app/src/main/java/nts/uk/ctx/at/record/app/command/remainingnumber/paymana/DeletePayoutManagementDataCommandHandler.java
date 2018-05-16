package nts.uk.ctx.at.record.app.command.remainingnumber.paymana;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.PayoutManagementDataRepository;


@Stateless
public class DeletePayoutManagementDataCommandHandler extends CommandHandler<DeletePayoutManagementDataCommand> {
    
	
	@Inject
	private PayoutManagementDataRepository payoutMNDTRepo;
	
	@Override
	protected void handle(CommandHandlerContext<DeletePayoutManagementDataCommand> context) {
		DeletePayoutManagementDataCommand command = context.getCommand();
		payoutMNDTRepo.delete(command.getPayoutId());
	}

}
