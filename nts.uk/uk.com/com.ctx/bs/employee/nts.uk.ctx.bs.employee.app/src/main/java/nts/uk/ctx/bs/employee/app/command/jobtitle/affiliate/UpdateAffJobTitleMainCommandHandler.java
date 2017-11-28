package nts.uk.ctx.bs.employee.app.command.jobtitle.affiliate;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryItem;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryItemRepository_v1;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryRepository_ver1;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistory_ver1;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregUpdateCommandHandler;
@Stateless
public class UpdateAffJobTitleMainCommandHandler extends CommandHandler<UpdateAffJobTitleMainCommand>
	implements PeregUpdateCommandHandler<UpdateAffJobTitleMainCommand>{

	@Inject
	private AffJobTitleHistoryRepository_ver1 affJobTitleHistoryRepository_ver1;
	
	@Inject
	private AffJobTitleHistoryItemRepository_v1 affJobTitleHistoryItemRepository_v1;
	
	@Override
	public String targetCategoryCd() {
		return "CS00009";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateAffJobTitleMainCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<UpdateAffJobTitleMainCommand> context) {
		val command = context.getCommand();
		
		Optional<AffJobTitleHistory_ver1> existHist = affJobTitleHistoryRepository_ver1.getListBySid(command.getSid());
		
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
		
		affJobTitleHistoryRepository_ver1.updateJobTitleMain(existHist.get(), itemToBeUpdate.get());
	
		AffJobTitleHistoryItem domain = AffJobTitleHistoryItem.createFromJavaType(command.getHistoryId(), command.getSid(), command.getJobTitleCode(), command.getNote());
		affJobTitleHistoryItemRepository_v1.updateJobTitleMain(domain);
	}

}
