package nts.uk.ctx.at.record.app.command.dailyperform.breaktime;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.daily.DailyRecordAdUpService;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class BreakTimeOfDailyPerformanceCommandUpdateHandler extends CommandFacade<BreakTimeOfDailyPerformanceCommand> {

	@Inject
	private BreakTimeOfDailyPerformanceRepository repo;
	
	@Inject
	private DailyRecordAdUpService adUpRepo;

	@Override
	protected void handle(CommandHandlerContext<BreakTimeOfDailyPerformanceCommand> context) {
		BreakTimeOfDailyPerformanceCommand command = context.getCommand();
		
		if(command.shouldDelete() || command.getData().getTimeZone().getBreakTimeSheets().isEmpty()){
			repo.delete(command.getEmployeeId(), command.getWorkDate());
			return;
		}
		adUpRepo.adUpBreakTime(command.getData().get());
	}

}
