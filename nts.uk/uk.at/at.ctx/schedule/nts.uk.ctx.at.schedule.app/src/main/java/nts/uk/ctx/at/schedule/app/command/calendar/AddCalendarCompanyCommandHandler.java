package nts.uk.ctx.at.schedule.app.command.calendar;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.schedule.dom.calendar.CalendarCompany;
import nts.uk.ctx.at.schedule.dom.calendar.CalendarRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AddCalendarCompanyCommandHandler extends CommandHandler<AddCalendarCompanyCommand> {
	@Inject
	private CalendarRepository calendarCompanyRepo;

	@Override
	protected void handle(CommandHandlerContext<AddCalendarCompanyCommand> context) {
		String companyId = AppContexts.user().companyId();
		CalendarCompany calendarCompany = CalendarCompany.createFromJavaType(companyId, 
				context.getCommand().getDateId(),
				context.getCommand().getWorkingDayAtr());
		Optional<CalendarCompany> calendarCom =calendarCompanyRepo.findCalendarCompanyByDate(companyId,
				context.getCommand().getDateId());
		if (calendarCom.isPresent()) {
			//do something
		} else {
			calendarCompanyRepo.addCalendarCompany(calendarCompany);
		}
	}

}
