package nts.uk.ctx.at.record.app.command.dailyperform.remark;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerformRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class RemarkOfDailyCommandAddHandler extends CommandFacade<RemarkOfDailyCommand> {

	/** TODO: no repo*/
	@Inject
	private RemarksOfDailyPerformRepo repo;

	@Override
	protected void handle(CommandHandlerContext<RemarkOfDailyCommand> context) {
		RemarkOfDailyCommand command = context.getCommand();
		if (!command.getData().isEmpty()) {
			repo.add(command.getData());
		}
	}
}
