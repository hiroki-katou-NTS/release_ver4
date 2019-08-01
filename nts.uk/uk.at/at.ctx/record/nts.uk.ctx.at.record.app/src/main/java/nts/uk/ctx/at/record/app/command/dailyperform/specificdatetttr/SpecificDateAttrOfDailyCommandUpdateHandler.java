package nts.uk.ctx.at.record.app.command.dailyperform.specificdatetttr;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class SpecificDateAttrOfDailyCommandUpdateHandler extends CommandFacade<SpecificDateAttrOfDailyCommand> {

	@Inject
	private SpecificDateAttrOfDailyPerforRepo repo;

	@Override
	protected void handle(CommandHandlerContext<SpecificDateAttrOfDailyCommand> context) {
		SpecificDateAttrOfDailyCommand command = context.getCommand();
		if(command.getData().isPresent()){
			repo.update(command.toDomain().get());
		}
	}

}
