package nts.uk.ctx.at.record.app.command.workrecord.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLogRepository;

@Stateless
public class UpdateExecutionTimeCommandHandler extends CommandHandler<UpdateExecutionTimeCommand> {

	@Inject
	ExecutionLogRepository executionLogRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateExecutionTimeCommand> context) {
		UpdateExecutionTimeCommand command = context.getCommand();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		GeneralDateTime startDate = GeneralDateTime
				.localDateTime(LocalDateTime.parse(command.getExecutionStartDate(), formatter));
		GeneralDateTime endDate = GeneralDateTime
				.localDateTime(LocalDateTime.parse(command.getExecutionEndDate(), formatter));

		// daily create
		GeneralDateTime dailyCreateStartTime = !command.getDailyCreateStartTime().equals("0")
				? GeneralDateTime.localDateTime(LocalDateTime.parse(command.getDailyCreateStartTime(), formatter))
				: null;

		GeneralDateTime dailyCreateEndTime = !command.getDailyCreateEndTime().equals("0")
				? null: endDate;

		// daily calculate
		GeneralDateTime dailyCalculateStartTime = !command.getDailyCalculateStartTime().equals("0")
				? GeneralDateTime.localDateTime(LocalDateTime.parse(command.getDailyCalculateStartTime(), formatter))
				: null;
		GeneralDateTime dailyCalculateEndTime = !command.getDailyCalculateEndTime().equals("0")
				? null: endDate;

		// approval
		GeneralDateTime reflectApprovalStartTime = !command.getReflectApprovalStartTime().equals("0")
				? GeneralDateTime.localDateTime(LocalDateTime.parse(command.getReflectApprovalStartTime(), formatter))
				: null;
		GeneralDateTime reflectApprovalEndTime = !command.getReflectApprovalEndTime().equals("0")
				? null: endDate;

		// monthly
		GeneralDateTime monthlyAggregateStartTime = !command.getMonthlyAggregateStartTime().equals("0")
				? GeneralDateTime.localDateTime(LocalDateTime.parse(command.getMonthlyAggregateStartTime(), formatter))
				: null;
		GeneralDateTime monthlyAggregateEndTime = !command.getMonthlyAggregateEndTime().equals("0")
				? null: endDate;

		this.executionLogRepository.updateExecutionDate(command.getEmpCalAndSumExecLogID(), startDate, endDate,
				dailyCreateStartTime, dailyCreateEndTime, dailyCalculateStartTime, dailyCalculateEndTime,
				reflectApprovalStartTime, reflectApprovalEndTime, monthlyAggregateStartTime, monthlyAggregateEndTime, command.getStopped());
	}

}
