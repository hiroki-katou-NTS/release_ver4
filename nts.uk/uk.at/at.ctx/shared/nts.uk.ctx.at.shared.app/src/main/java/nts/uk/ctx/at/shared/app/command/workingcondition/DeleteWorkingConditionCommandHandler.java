package nts.uk.ctx.at.shared.app.command.workingcondition;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.pereg.app.command.PeregDeleteCommandHandler;

@Stateless
public class DeleteWorkingConditionCommandHandler extends CommandHandler<DeleteWorkingConditionCommand>
	implements PeregDeleteCommandHandler<DeleteWorkingConditionCommand>{
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private WorkingConditionRepository workingConditionRepository;
	
	@Override
	public String targetCategoryCd() {
		return "CS00020";
	}

	@Override
	public Class<?> commandClass() {
		return DeleteWorkingConditionCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<DeleteWorkingConditionCommand> context) {
		val command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		
		Optional<WorkingCondition> listHistBySid =  workingConditionRepository.getBySid(companyId, command.getEmployeeId());
		
		if (!listHistBySid.isPresent()){
			throw new RuntimeException("Invalid item to be deleted");
		}
		WorkingCondition workingCond = listHistBySid.get();
		Optional<DateHistoryItem> itemToBeDeleted = workingCond.getDateHistoryItem().stream().filter(hist->hist.identifier().equals(command.getHistId())).findFirst();
		if (!itemToBeDeleted.isPresent()){
			throw new RuntimeException("Invalid item to be deleted");
		}
		workingCond.remove(itemToBeDeleted.get());
		
		workingConditionRepository.update(workingCond);
		
		workingConditionItemRepository.delete(command.getHistId());
		
	}

}
