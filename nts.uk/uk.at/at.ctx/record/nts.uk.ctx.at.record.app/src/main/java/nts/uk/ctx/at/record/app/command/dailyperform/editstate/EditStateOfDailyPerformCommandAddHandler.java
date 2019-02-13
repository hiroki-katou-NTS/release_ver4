package nts.uk.ctx.at.record.app.command.dailyperform.editstate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class EditStateOfDailyPerformCommandAddHandler extends CommandFacade<EditStateOfDailyPerformCommand> {

	@Inject
	private EditStateOfDailyPerformanceRepository repo;

	@Override
	protected void handle(CommandHandlerContext<EditStateOfDailyPerformCommand> context) {
		EditStateOfDailyPerformCommand command = context.getCommand();
		
		repo.delete(command.getEmployeeId(), command.getWorkDate());
		
		if(!command.getData().isEmpty()){
			repo.add(command.toDomain());
		}
	}

}
