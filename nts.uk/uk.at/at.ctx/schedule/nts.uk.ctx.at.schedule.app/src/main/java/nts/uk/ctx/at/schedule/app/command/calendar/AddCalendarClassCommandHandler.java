package nts.uk.ctx.at.schedule.app.command.calendar;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.schedule.dom.calendar.CalendarClass;
import nts.uk.ctx.at.schedule.dom.calendar.CalendarClassRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AddCalendarClassCommandHandler extends CommandHandler<List<AddCalendarClassCommand>> {
	
	@Inject
	private CalendarClassRepository calendarClassRepo;

	@Override
	protected void handle(CommandHandlerContext<List<AddCalendarClassCommand>> context) {
		String companyId = AppContexts.user().companyId();
		List<AddCalendarClassCommand> calendarClassCommands = context.getCommand();
		for(AddCalendarClassCommand calendarClassCommand : calendarClassCommands){
			CalendarClass clendarClass = CalendarClass.createFromJavaType(companyId,
					calendarClassCommand.getClassId(), 
					calendarClassCommand.getDateId(),
					calendarClassCommand.getWorkingDayAtr());
			Optional<CalendarClass> clendarCla =calendarClassRepo.findCalendarClassByDate(companyId,
					calendarClassCommand.getClassId(),
					calendarClassCommand.getDateId());
			if (clendarCla.isPresent()) {
				//do something
			} else {
				calendarClassRepo.addCalendarClass(clendarClass);
			}
		}
	}

}
