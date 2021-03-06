package nts.uk.ctx.at.record.app.command.dailyperform.attendanceleavinggate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class PCLogInfoOfDailyCommandAddHandler extends CommandFacade<PCLogInfoOfDailyCommand> {

	@Inject
	private PCLogOnInfoOfDailyRepo repo;

	@Override
	protected void handle(CommandHandlerContext<PCLogInfoOfDailyCommand> context) {
		if(context.getCommand().getData().isPresent()){
			repo.add(context.getCommand().toDomain().get());
		}
	}

}
