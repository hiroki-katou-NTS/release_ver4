package nts.uk.ctx.bs.employee.app.command.department;

import java.util.ArrayList;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistory;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistoryService;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistoryItem;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistoryRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

@Stateless
public class AddAffiliationDepartmentCommandHandler extends CommandHandlerWithResult<AddAffiliationDepartmentCommand,PeregAddCommandResult>
	implements PeregAddCommandHandler<AddAffiliationDepartmentCommand>{

	@Inject
	private AffDepartmentHistoryRepository affDepartmentHistoryRepository;
	
	@Inject
	private AffDepartmentHistoryItemRepository affDepartmentHistoryItemRepository;
	
	@Inject
	private AffDepartmentHistoryService affDepartmentHistoryService;
	
	@Override
	public String targetCategoryCd() {
		return "CS00015";
	}

	@Override
	public Class<?> commandClass() {
		return AddAffiliationDepartmentCommand.class;
	}

	@Override
	protected PeregAddCommandResult  handle(CommandHandlerContext<AddAffiliationDepartmentCommand> context) {
		val command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		
		String newHistId = IdentifierUtil.randomUniqueId();
		
		DateHistoryItem dateItem = new DateHistoryItem(newHistId, new DatePeriod(command.getStartDate(), command.getEndDate()));
		
		AffDepartmentHistory itemToBeAdded = null;
		
		Optional<AffDepartmentHistory> itemHist = affDepartmentHistoryRepository.getAffDepartmentHistorytByEmployeeId(command.getEmployeeId());
		if (itemHist.isPresent()){
			itemToBeAdded = itemHist.get();
		} else {
			itemToBeAdded = new AffDepartmentHistory(companyId, command.getEmployeeId(),new ArrayList<>());
		}
		itemToBeAdded.add(dateItem);
		
		affDepartmentHistoryService.add(itemToBeAdded);
		
		AffDepartmentHistoryItem histItem = AffDepartmentHistoryItem.createFromJavaType(newHistId, command.getEmployeeId(), command.getDepartmentId(), command.getAffHistoryTranfsType(), command.getDistributionRatio());
		affDepartmentHistoryItemRepository.add(histItem);
		
		return new PeregAddCommandResult(newHistId);
	}

}
