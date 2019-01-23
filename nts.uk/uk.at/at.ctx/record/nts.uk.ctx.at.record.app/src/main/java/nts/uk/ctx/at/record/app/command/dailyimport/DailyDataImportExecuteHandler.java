package nts.uk.ctx.at.record.app.command.dailyimport;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.uk.ctx.at.record.app.service.dailyimport.DailyDataImportService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DailyDataImportExecuteHandler extends AsyncCommandHandler<DailyDataImportExexuteCommand> {

	@Inject
	private DailyDataImportService importService;
	
	@Override
	protected void handle(CommandHandlerContext<DailyDataImportExexuteCommand> context) {
		TaskDataSetter dataSetter = context.asAsync().getDataSetter();
		importService.importAt(new DatePeriod(context.getCommand().getStartDate(), context.getCommand().getEndDate()), dataSetter, () -> {
			return context.asAsync().hasBeenRequestedToCancel();
		});
	}

}
