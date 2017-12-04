package nts.uk.ctx.bs.employee.app.command.workplace.affiliate;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryDomainService;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository_v1;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository_v1;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory_ver1;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.pereg.app.command.PeregDeleteCommandHandler;

@Stateless
public class DeleteAffWorkplaceHistoryCommandHandler extends CommandHandler<DeleteAffWorkplaceHistoryCommand>
	implements PeregDeleteCommandHandler<DeleteAffWorkplaceHistoryCommand>{
	
	@Inject
	private AffWorkplaceHistoryRepository_v1 affWorkplaceHistoryRepository;
	
	@Inject
	private AffWorkplaceHistoryItemRepository_v1 affWorkplaceHistoryItemRepository;
	
	@Inject 
	private AffWorkplaceHistoryDomainService affWorkplaceHistoryDomainService;
	
	@Override
	public String targetCategoryCd() {
		return "CS00017";
	}

	@Override
	public Class<?> commandClass() {
		return DeleteAffWorkplaceHistoryCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<DeleteAffWorkplaceHistoryCommand> context) {
		val command = context.getCommand();
		
		Optional<AffWorkplaceHistory_ver1> existHist = affWorkplaceHistoryRepository.getAffWorkplaceHistByEmployeeId(command.getEmployeeId());
		
		if (!existHist.isPresent()){
			throw new RuntimeException("invalid AffWorkplaceHistory"); 
		}
			
		Optional<DateHistoryItem> itemToBeDeleted = existHist.get().getHistoryItems().stream()
                .filter(h -> h.identifier().equals(command.getHistoryId()))
                .findFirst();
		
		if (!itemToBeDeleted.isPresent()){
			throw new RuntimeException("invalid AffWorkplaceHistory");
		}
		existHist.get().remove(itemToBeDeleted.get());
		
		affWorkplaceHistoryDomainService.delete(existHist.get(), itemToBeDeleted.get());
		
		affWorkplaceHistoryItemRepository.delete(command.getHistoryId());
	}
	
}
