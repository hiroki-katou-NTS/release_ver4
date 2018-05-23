package nts.uk.ctx.at.record.app.command.remainingnumber.paymana;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.SubstitutionOfHDManaDataService;

@Stateless
public class PayoutSubofHDManagementCommandHandler extends CommandHandler<PayoutSubofHDManagementCommand> {

	@Inject
	private SubstitutionOfHDManaDataService SubstitutionOfHDManaDataService;
	
	@Override
	protected void handle(CommandHandlerContext<PayoutSubofHDManagementCommand> context) {
		val command = context.getCommand();
		SubstitutionOfHDManaDataService.insertSubOfHDMan(command.getSid(),command.getPayoutID(),command.getRemainNumber(), command.getSubofHD());
	}

}
 