package nts.uk.ctx.at.record.app.command.dailyperform.attendanceleavinggate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class PCLogInfoOfDailyCommandUpdateHandler extends CommandFacade<PCLogInfoOfDailyCommand> {

	@Inject
	private PCLogOnInfoOfDailyRepo repo;

	@Override
	protected void handle(CommandHandlerContext<PCLogInfoOfDailyCommand> context) {
		PCLogInfoOfDailyCommand command = context.getCommand();
		if(command.getData().isPresent()){
			repo.update(command.toDomain().get());
		}
	}
}
