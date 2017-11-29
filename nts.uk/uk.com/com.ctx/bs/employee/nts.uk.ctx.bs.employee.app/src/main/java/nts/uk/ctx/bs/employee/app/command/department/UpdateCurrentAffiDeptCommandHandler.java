package nts.uk.ctx.bs.employee.app.command.department;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.department.CurrentAffiDept;
import nts.uk.ctx.bs.employee.dom.department.CurrentAffiDeptRepository;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregUpdateCommandHandler;

@Stateless
public class UpdateCurrentAffiDeptCommandHandler extends CommandHandler<UpdateCurrentAffiDeptCommand>
	implements PeregUpdateCommandHandler<UpdateCurrentAffiDeptCommand>{

	@Inject
	private CurrentAffiDeptRepository currentAffiDeptRepository;
	
	@Override
	public String targetCategoryCd() {
		return "CS00012";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateCurrentAffiDeptCommand.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handle(CommandHandlerContext<UpdateCurrentAffiDeptCommand> context) {
		val command = context.getCommand();
		
//		List<DateHistoryItem> dateHistoryItem = new ArrayList<DateHistoryItem>();
		DateHistoryItem histItem =new DateHistoryItem(command.getHistoryId(), new DatePeriod(command.getStartDate(), command.getEndDate()));
//		dateHistoryItem.add(histItem);
		
		CurrentAffiDept domain = new CurrentAffiDept(command.getEmployeeId(), command.getAffiDeptId(), command.getDepartmentId(), new ArrayList<>());
		domain.add(histItem);
		
		currentAffiDeptRepository.updateCurrentAffiDept(domain);
	}

}
