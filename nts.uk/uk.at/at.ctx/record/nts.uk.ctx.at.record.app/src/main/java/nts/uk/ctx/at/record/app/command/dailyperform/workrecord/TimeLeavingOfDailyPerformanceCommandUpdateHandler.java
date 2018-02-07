package nts.uk.ctx.at.record.app.command.dailyperform.workrecord;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class TimeLeavingOfDailyPerformanceCommandUpdateHandler extends CommandFacade<TimeLeavingOfDailyPerformanceCommand> {

	@Inject
	private TimeLeavingOfDailyPerformanceRepository repo;

	@Override
	protected void handle(CommandHandlerContext<TimeLeavingOfDailyPerformanceCommand> context) {
		TimeLeavingOfDailyPerformanceCommand command = context.getCommand();
		if(command.getData().isPresent()){
			repo.update(command.getData().get());
		}
	}
}
