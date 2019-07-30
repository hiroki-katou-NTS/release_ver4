package nts.uk.ctx.at.record.app.command.dailyperform.editstate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.daily.DailyRecordAdUpService;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class EditStateOfDailyPerformCommandUpdateHandler extends CommandFacade<EditStateOfDailyPerformCommand> {

	@Inject
	private DailyRecordAdUpService adUpRepo;

	@Override
	protected void handle(CommandHandlerContext<EditStateOfDailyPerformCommand> context) {
		EditStateOfDailyPerformCommand command = context.getCommand();
		
		if(!command.getData().isEmpty()){
			adUpRepo.adUpEditState(command.toDomain());
		}else {
			//repo.delete(command.getEmployeeId(), command.getWorkDate());
		}
	}

}
