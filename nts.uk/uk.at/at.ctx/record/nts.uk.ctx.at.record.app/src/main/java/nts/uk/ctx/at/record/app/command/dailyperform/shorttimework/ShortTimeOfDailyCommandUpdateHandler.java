package nts.uk.ctx.at.record.app.command.dailyperform.shorttimework;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.daily.DailyRecordAdUpService;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class ShortTimeOfDailyCommandUpdateHandler extends CommandFacade<ShortTimeOfDailyCommand> {

	@Inject
	private DailyRecordAdUpService adUpRepo;

	@Override
	protected void handle(CommandHandlerContext<ShortTimeOfDailyCommand> context) {
		ShortTimeOfDailyCommand command = context.getCommand();
		if(command.getData().isPresent()){
			adUpRepo.adUpShortTime(command.toDomain());
		}
	}

}
