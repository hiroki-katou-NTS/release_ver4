package nts.uk.ctx.bs.employee.app.command.workplace.affiliate;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryService;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItem;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository_v1;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository_v1;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory_ver1;
import nts.uk.ctx.bs.person.dom.person.common.ConstantUtils;
import nts.uk.shr.com.context.AppContexts;
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
	
	@Inject 
	private AffWorkplaceHistoryService affWorkplaceHistoryService;
	
	@Override
	public String targetCategoryCd() {
		return "CS00017";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateAffWorkplaceHistoryCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<UpdateAffWorkplaceHistoryCommand> context) {
		val command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		Optional<AffWorkplaceHistory_ver1> existHist = affWorkplaceHistoryRepository.getAffWorkplaceHistByEmployeeId(companyId, command.getEmployeeId());
		
		if (!existHist.isPresent()){
			throw new RuntimeException("invalid AffWorkplaceHistory"); 
		}
			
		Optional<DateHistoryItem> itemToBeUpdate = existHist.get().getHistoryItems().stream()
                .filter(h -> h.identifier().equals(command.getHistoryId()))
                .findFirst();
		
		if (!itemToBeUpdate.isPresent()){
			throw new RuntimeException("invalid AffWorkplaceHistory");
		}
		existHist.get().changeSpan(itemToBeUpdate.get(), new DatePeriod(command.getStartDate(), command.getEndDate()!= null? command.getEndDate():  GeneralDate.fromString(ConstantUtils.MAX_DATE, ConstantUtils.FORMAT_DATE_YYYYMMDD)));
		
		affWorkplaceHistoryService.update(existHist.get(), itemToBeUpdate.get());
		
		AffWorkplaceHistoryItem histItem = AffWorkplaceHistoryItem.createFromJavaType(command.getHistoryId(), command.getEmployeeId(), command.getWorkplaceId(), command.getNormalWorkplaceId());
		affWorkplaceHistoryItemRepository.update(histItem);
	}
	
}
