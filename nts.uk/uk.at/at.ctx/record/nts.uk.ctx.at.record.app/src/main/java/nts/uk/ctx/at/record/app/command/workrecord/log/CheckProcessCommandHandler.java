package nts.uk.ctx.at.record.app.command.workrecord.log;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ProcessFlowOfDailyCreationDomainService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@Transactional
public class CheckProcessCommandHandler extends AsyncCommandHandler<CheckProcessCommand> {
		
	@Inject
	private ProcessFlowOfDailyCreationDomainService processFlowOfDailyCreationDomainService;

	@Override
	protected void handle(CommandHandlerContext<CheckProcessCommand> context) {
		val asyncContext = context.asAsync();
		val dataSetter = asyncContext.getDataSetter();
		val command = context.getCommand();

		DatePeriod periodTime = new DatePeriod(
				GeneralDate.fromString(command.getPeriodStartDate(), "yyyy/MM/dd"),
				GeneralDate.fromString(command.getPeriodEndDate(), "yyyy/MM/dd"));
		processFlowOfDailyCreationDomainService.processFlowOfDailyCreation(asyncContext, ExecutionAttr.MANUAL, periodTime, command.getEmpCalAndSumExecLogID());
		
		dataSetter.setData("complete", true);
	}
}