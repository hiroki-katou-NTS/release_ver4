package nts.uk.ctx.bs.employee.app.command.jobtitle.affiliate;

import java.util.ArrayList;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.bs.employee.dom.jobtile.affiliate.AffJobTitleHistoryItem;
import nts.uk.ctx.bs.employee.dom.jobtile.affiliate.AffJobTitleHistoryItemRepository_v1;
import nts.uk.ctx.bs.employee.dom.jobtile.affiliate.AffJobTitleHistoryRepository_ver1;
import nts.uk.ctx.bs.employee.dom.jobtile.affiliate.AffJobTitleHistory_ver1;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;
@Stateless
public class AddAffJobTitleMainCommandHandler extends CommandHandlerWithResult<AddAffJobTitleMainCommand, PeregAddCommandResult>
	implements PeregAddCommandHandler<AddAffJobTitleMainCommand>{

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
		return AddAffJobTitleMainCommand.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddAffJobTitleMainCommand> context) {
		val command = context.getCommand();
		
		String histId = IdentifierUtil.randomUniqueId();
		DateHistoryItem dateItem = new DateHistoryItem(histId, new DatePeriod(command.getStartDate(), command.getEndDate()));
		
		AffJobTitleHistory_ver1 itemtoBeAdded = null;
		
		Optional<AffJobTitleHistory_ver1> existHist = affJobTitleHistoryRepository_ver1.getListBySid(command.getSid());
		
		// In case of exist history of this employee
		if (existHist.isPresent()){
			existHist.get().add(dateItem);
			itemtoBeAdded = existHist.get();
		} else {
			// In case of non - exist history of this employee
			itemtoBeAdded = new AffJobTitleHistory_ver1(command.getSid(),new ArrayList<>());
			itemtoBeAdded.add(dateItem);
		}
		
		affJobTitleHistoryRepository_ver1.addJobTitleMain(itemtoBeAdded);
		
		AffJobTitleHistoryItem domain = AffJobTitleHistoryItem.createFromJavaType(histId, command.getSid(), command.getJobTitleCode(), command.getNote());
		affJobTitleHistoryItemRepository_v1.addJobTitleMain(domain);
		
		return new PeregAddCommandResult(histId);
	}

}
