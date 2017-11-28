package nts.uk.ctx.bs.employee.app.command.workplace.affiliate;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItem;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository_v1;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository_v1;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory_ver1;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregUpdateCommandHandler;

@Stateless
public class UpdateAffWorkplaceHistoryCommandHandler extends CommandHandler<UpdateAffWorkplaceHistoryCommand>
	implements PeregUpdateCommandHandler<UpdateAffWorkplaceHistoryCommand>{
	
	@Inject
	private AffWorkplaceHistoryRepository_v1 affWorkplaceHistoryRepository;
	
	@Inject
	private AffWorkplaceHistoryItemRepository_v1 affWorkplaceHistoryItemRepository;
	
	
	@Override
	public String targetCategoryCd() {
		return "CS00010";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateAffWorkplaceHistoryCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<UpdateAffWorkplaceHistoryCommand> context) {
		val command = context.getCommand();
		
		Optional<AffWorkplaceHistory_ver1> existHist = affWorkplaceHistoryRepository.getAffWorkplaceHistByEmployeeId(command.getEmployeeId());
		
		if (!existHist.isPresent()){
			throw new RuntimeException("invalid AffWorkplaceHistory"); 
		}
			
		Optional<DateHistoryItem> itemToBeUpdate = existHist.get().getHistoryItems().stream()
                .filter(h -> h.identifier().equals(command.getHistoryId()))
                .findFirst();
		
		if (!itemToBeUpdate.isPresent()){
			throw new RuntimeException("invalid AffWorkplaceHistory");
		}
		existHist.get().changeSpan(itemToBeUpdate.get(), new DatePeriod(command.getStartDate(), command.getEndDate()));
		
		affWorkplaceHistoryRepository.updateAffWorkplaceHistory(existHist.get(), itemToBeUpdate.get());
		
		AffWorkplaceHistoryItem domain = AffWorkplaceHistoryItem.createFromJavaType(command.getHistoryId(), command.getEmployeeId(), command.getWorkplaceCode(), command.getNormalWorkplaceCode(), command.getLocationCode());
		affWorkplaceHistoryItemRepository.updateAffWorkplaceHistory(domain);
	}
	
}
