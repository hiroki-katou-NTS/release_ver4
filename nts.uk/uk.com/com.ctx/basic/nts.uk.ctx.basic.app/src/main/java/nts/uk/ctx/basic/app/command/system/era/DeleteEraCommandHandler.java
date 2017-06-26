package nts.uk.ctx.basic.app.command.system.era;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.system.era.Era;
import nts.uk.ctx.basic.dom.system.era.EraName;
import nts.uk.ctx.basic.dom.system.era.EraRepository;

@RequestScoped
public class DeleteEraCommandHandler extends CommandHandler<DeleteEraCommand>{
	@Inject
	private EraRepository eraRepository;

	@Override
	protected void handle(CommandHandlerContext<DeleteEraCommand> context) {
		// TODO Auto-generated method stub
		//String eraName = context.getCommand().toDomain().getEraName().v();
//		GeneralDate startDate = context.getCommand().toDomain().getStartDate();
//		eraRepository.delete(startDate);
		String histId = context.getCommand().getEraHist();
		eraRepository.delete(histId);
	}
	
	
}
