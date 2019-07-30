package nts.uk.ctx.at.record.app.command.dailyperform.goout;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.daily.DailyRecordAdUpService;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class OutingTimeOfDailyPerformanceCommandUpdateHandler extends CommandFacade<OutingTimeOfDailyPerformanceCommand> {

	@Inject
	private DailyRecordAdUpService adUpRepo;

	@Override
	protected void handle(CommandHandlerContext<OutingTimeOfDailyPerformanceCommand> context) {
		OutingTimeOfDailyPerformanceCommand command = context.getCommand();
		if(command.getData().isPresent()){
			adUpRepo.adUpOutTime(command.toDomain());
		}
	}
}
