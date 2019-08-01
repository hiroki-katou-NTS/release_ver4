package nts.uk.ctx.at.record.app.command.dailyperform.temporarytime;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class TemporaryTimeOfDailyPerformanceCommandUpdateHandler
		extends CommandFacade<TemporaryTimeOfDailyPerformanceCommand> {

	@Inject
	private TemporaryTimeOfDailyPerformanceRepository repo;

	@Override
	protected void handle(CommandHandlerContext<TemporaryTimeOfDailyPerformanceCommand> context) {
		TemporaryTimeOfDailyPerformanceCommand command = context.getCommand();
		if(command.getData().isPresent()){
			repo.update(command.toDomain().get());
		}
	}
}
