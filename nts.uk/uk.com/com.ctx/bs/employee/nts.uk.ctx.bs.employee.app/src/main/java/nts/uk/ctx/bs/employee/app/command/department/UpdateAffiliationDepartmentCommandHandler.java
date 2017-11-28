package nts.uk.ctx.bs.employee.app.command.department;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistory;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistoryItem;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistoryRepository;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregUpdateCommandHandler;

@Stateless
public class UpdateAffiliationDepartmentCommandHandler extends CommandHandler<UpdateAffiliationDepartmentCommand>
	implements PeregUpdateCommandHandler<UpdateAffiliationDepartmentCommand>{

	@Inject
	private AffDepartmentHistoryRepository affDepartmentHistoryRepository;
	
	@Inject
	private AffDepartmentHistoryItemRepository affDepartmentHistoryItemRepository;
	
	
	@Override
	public String targetCategoryCd() {
		return "CS00011";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateAffiliationDepartmentCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<UpdateAffiliationDepartmentCommand> context) {
		val command = context.getCommand();
		
		Optional<AffDepartmentHistory> itemHist = affDepartmentHistoryRepository.getAffDepartmentHistorytByEmployeeId(command.getEmployeeId());
		if (!itemHist.isPresent()){
			throw new RuntimeException("Invalid AffDepartmentHistory");
		}
		Optional<DateHistoryItem> itemToBeChanged = itemHist.get().getHistoryItems().stream()
				.filter(d->d.identifier().equals(command.getHistoryId())).findFirst();
		
		if (!itemToBeChanged.isPresent()){
			throw new RuntimeException("Invalid AffDepartmentHistory");
		}
		
		itemHist.get().changeSpan(itemToBeChanged.get(), new DatePeriod(command.getStartDate(), command.getEndDate()));
		
		affDepartmentHistoryRepository.updateAffDepartment(itemHist.get(), itemToBeChanged.get());
		
		
		AffDepartmentHistoryItem domain = AffDepartmentHistoryItem.createFromJavaType(command.getHistoryId(), command.getEmployeeId(), command.getDepartmentCode(), command.getAffHistoryTranfsType(), command.getDistributionRatio());
		affDepartmentHistoryItemRepository.updateAffDepartment(domain);
		
		
	}

}
