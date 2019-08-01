package nts.uk.ctx.at.record.app.command.dailyperform.workrecord;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.repo.AttendanceTimeByWorkOfDailyRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class AttendanceTimeByWorkOfDailyCommandUpdateHandler extends CommandFacade<AttendanceTimeByWorkOfDailyCommand> {

	// TODO: create table and write repo
	@Inject
	private AttendanceTimeByWorkOfDailyRepository repo;

	@Override
	protected void handle(CommandHandlerContext<AttendanceTimeByWorkOfDailyCommand> context) {
		AttendanceTimeByWorkOfDailyCommand command = context.getCommand();
		if(command.getData().isPresent()){
			repo.update(command.toDomain().get());
		}
	}
}
