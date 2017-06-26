package nts.uk.ctx.at.record.app.command.divergencetime;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTimeRepository;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class DeleteDivergenceReasonCommandHandler extends CommandHandler<DeleteDivergenceReasonCommand>{
	@Inject
	private DivergenceTimeRepository divTimeRepo;

	@Override
	protected void handle(CommandHandlerContext<DeleteDivergenceReasonCommand> context) {
		String companyId = AppContexts.user().companyId();
		divTimeRepo.deleteDivReason(companyId, context.getCommand().getDivTimeId(), context.getCommand().getDivReasonCode());
	}
	

}
