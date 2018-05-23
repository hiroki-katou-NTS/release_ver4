package nts.uk.ctx.at.record.app.command.dailyperform.affiliationInfor;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class BusinessTypeOfDailyPerformCommandAddHandler extends CommandFacade<BusinessTypeOfDailyPerformCommand> {

	@Inject
	private WorkTypeOfDailyPerforRepository repo;

	@Override
	protected void handle(CommandHandlerContext<BusinessTypeOfDailyPerformCommand> context) {
		repo.add(context.getCommand().getData());
	}
}
