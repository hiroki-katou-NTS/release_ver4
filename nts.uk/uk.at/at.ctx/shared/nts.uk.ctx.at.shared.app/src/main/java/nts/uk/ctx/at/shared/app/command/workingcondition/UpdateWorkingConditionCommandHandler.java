package nts.uk.ctx.at.shared.app.command.workingcondition;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregUpdateCommandHandler;

@Stateless
public class UpdateWorkingConditionCommandHandler extends CommandHandler<UpdateWorkingConditionCommand>
	implements PeregUpdateCommandHandler<UpdateWorkingConditionCommand>{
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private WorkingConditionRepository workingConditionRepository;
	
	@Inject
	private AddWorkingConditionCommandAssembler addWorkingConditionCommandAssembler;
	
	@Override
	public String targetCategoryCd() {
		return "CS00020";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateWorkingConditionCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<UpdateWorkingConditionCommand> context) {
		val command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		
		Optional<WorkingCondition> listHistBySid =  workingConditionRepository.getBySid(companyId, command.getEmployeeId());
		
		if (!listHistBySid.isPresent()){
			throw new RuntimeException("Invalid item to be updated");
		}
		WorkingCondition workingCond = listHistBySid.get();
		Optional<DateHistoryItem> itemToBeUpdated = workingCond.getDateHistoryItem().stream().filter(hist->hist.identifier().equals(command.getHistId())).findFirst();
		if (!itemToBeUpdated.isPresent()){
			throw new RuntimeException("Invalid item to be updated");
		}
		workingCond.changeSpan(itemToBeUpdated.get(), new DatePeriod(command.getStartDate(), command.getEndDate()));
		
		workingConditionRepository.update(workingCond);
		
		WorkingConditionItem  workingCondItem = addWorkingConditionCommandAssembler.fromDTO(command);
		
		workingConditionItemRepository.add(workingCondItem);
		
	}

}
