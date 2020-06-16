package nts.uk.ctx.bs.employee.app.command.jobtitle.affiliate;

import java.util.ArrayList;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistory;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryItem;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryService;
import nts.uk.ctx.bs.person.dom.person.common.ConstantUtils;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

@Stateless
public class AddAffJobTitleMainCommandHandler extends CommandHandlerWithResult<AddAffJobTitleMainCommand, PeregAddCommandResult>
	implements PeregAddCommandHandler<AddAffJobTitleMainCommand>{

	@Inject
	private AffJobTitleHistoryRepository affJobTitleHistoryRepository;
	
	@Inject 
	private AffJobTitleHistoryService affJobTitleHistoryService;
	
	@Override
	public String targetCategoryCd() {
		return "CS00016";
	}

	@Override
	public Class<?> commandClass() {
		return AddAffJobTitleMainCommand.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddAffJobTitleMainCommand> context) {
		val command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		String histId = IdentifierUtil.randomUniqueId();
		DateHistoryItem dateItem = new DateHistoryItem(histId, new DatePeriod(command.getStartDate()!=null?command.getStartDate():ConstantUtils.minDate(), command.getEndDate()!= null? command.getEndDate():  ConstantUtils.maxDate()));
		
		Optional<AffJobTitleHistory> existHist = affJobTitleHistoryRepository.getListBySid(companyId, command.getSid());
		
		AffJobTitleHistory itemtoBeAdded = new AffJobTitleHistory(companyId, command.getSid(),new ArrayList<>());
		// In case of exist history of this employee
		if (existHist.isPresent()){
			itemtoBeAdded = existHist.get();
		}
		
		itemtoBeAdded.add(dateItem);
		
		AffJobTitleHistoryItem histItem = AffJobTitleHistoryItem.createFromJavaType(histId, command.getSid(), command.getJobTitleId(), command.getNote());
		
		affJobTitleHistoryService.addToMerge(itemtoBeAdded, histItem);
		
		return new PeregAddCommandResult(histId);
	}

}
