package nts.uk.ctx.at.shared.app.command.workingcondition;

import java.util.ArrayList;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

@Stateless
public class AddWorkingConditionCommandHandler extends CommandHandlerWithResult<AddWorkingConditionCommand, PeregAddCommandResult>
	implements PeregAddCommandHandler<AddWorkingConditionCommand>{
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
		return AddWorkingConditionCommand.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddWorkingConditionCommand> context) {
		val command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		String histId = IdentifierUtil.randomUniqueId();
		
		WorkingCondition workingCond = new WorkingCondition(companyId,command.getEmployeeId(),new ArrayList<DateHistoryItem>());
		
		Optional<WorkingCondition> listHistBySid =  workingConditionRepository.getBySid(companyId, command.getEmployeeId());
		if (listHistBySid.isPresent()){
			workingCond = listHistBySid.get();
		}
		GeneralDate endDate = command.getEndDate() !=null? command.getEndDate() : GeneralDate.max();
		DateHistoryItem itemToBeAdded = new DateHistoryItem(histId, new DatePeriod(command.getStartDate(), endDate));
		workingCond.add(itemToBeAdded);
		
		workingConditionRepository.save(workingCond);
		
		WorkingConditionItem  workingCondItem = addWorkingConditionCommandAssembler.fromDTO(histId,command);
		
		workingConditionItemRepository.add(workingCondItem);
		
		return new PeregAddCommandResult(histId);
	}

}
