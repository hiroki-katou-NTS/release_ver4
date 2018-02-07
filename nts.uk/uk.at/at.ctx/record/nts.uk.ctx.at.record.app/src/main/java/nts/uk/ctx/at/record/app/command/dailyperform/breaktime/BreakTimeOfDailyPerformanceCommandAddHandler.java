package nts.uk.ctx.at.record.app.command.dailyperform.breaktime;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class BreakTimeOfDailyPerformanceCommandAddHandler extends CommandFacade<BreakTimeOfDailyPerformanceCommand> {

	@Inject
	private BreakTimeOfDailyPerformanceRepository repo;

	@Override
	protected void handle(CommandHandlerContext<BreakTimeOfDailyPerformanceCommand> context) {
		BreakTimeOfDailyPerformanceCommand command = context.getCommand();
		if (!command.getData().isEmpty()) {
			repo.insert(command.getData());
		}
	}

}
