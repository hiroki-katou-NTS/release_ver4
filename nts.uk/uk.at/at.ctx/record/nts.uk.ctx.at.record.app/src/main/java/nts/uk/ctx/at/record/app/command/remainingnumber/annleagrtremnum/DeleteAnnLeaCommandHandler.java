package nts.uk.ctx.at.record.app.command.remainingnumber.annleagrtremnum;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;

@Stateless
public class DeleteAnnLeaCommandHandler extends AsyncCommandHandler<DeleteLeaGrantRemnNumCommand>{
	
	@Inject
	private AnnLeaGrantRemDataRepository annLeaRepo;

	@Override
	protected void handle(CommandHandlerContext<DeleteLeaGrantRemnNumCommand> context) {
		DeleteLeaGrantRemnNumCommand command = context.getCommand();
		
		annLeaRepo.delete(command.getAnnLeavId());
		
	}

}
