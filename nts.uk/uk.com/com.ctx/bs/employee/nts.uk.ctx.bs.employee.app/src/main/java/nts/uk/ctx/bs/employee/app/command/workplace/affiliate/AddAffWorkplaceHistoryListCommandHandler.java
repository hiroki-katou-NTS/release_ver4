package nts.uk.ctx.bs.employee.app.command.workplace.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItem;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryService;
import nts.uk.ctx.bs.person.dom.person.common.ConstantUtils;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;
import nts.uk.shr.pereg.app.command.PeregAddListCommandHandler;
@Stateless
public class AddAffWorkplaceHistoryListCommandHandler extends CommandHandlerWithResult<List<AddAffWorkplaceHistoryCommand>, List<PeregAddCommandResult>>
implements PeregAddListCommandHandler<AddAffWorkplaceHistoryCommand>{
	
	@Inject
	private AffWorkplaceHistoryRepository affWorkplaceHistoryRepository;
	
	@Inject
	private AffWorkplaceHistoryItemRepository affWorkplaceHistoryItemRepository;
	
	@Inject 
	private AffWorkplaceHistoryService affWorkplaceHistoryService;
	@Override
	public String targetCategoryCd() {
		return "CS00017";
	}

	@Override
	public Class<?> commandClass() {
		return AddAffWorkplaceHistoryCommand.class;
	}

	@Override
	protected List<PeregAddCommandResult> handle(CommandHandlerContext<List<AddAffWorkplaceHistoryCommand>> context) {
		List<AddAffWorkplaceHistoryCommand> command = context.getCommand();
		String cid = AppContexts.user().companyId();
		List<String> sids = command.parallelStream().map(c -> c.getEmployeeId()).collect(Collectors.toList());
		List<AffWorkplaceHistoryItem> histItems = new ArrayList<>();
		List<AffWorkplaceHistory> affJobTitleHistoryLst = new ArrayList<>();
		List<PeregAddCommandResult> result = new ArrayList<>();
		Map<String, List<AffWorkplaceHistory>> existHistMap = affWorkplaceHistoryRepository.getBySidsAndCid(cid, sids)
				.parallelStream().collect(Collectors.groupingBy(c -> c.getEmployeeId()));
		command.parallelStream().forEach(c -> {
			String histId = IdentifierUtil.randomUniqueId();
			List<AffWorkplaceHistory> affJobTitleHistory = existHistMap.get(c.getEmployeeId());
			AffWorkplaceHistory itemtoBeAdded = new AffWorkplaceHistory(cid, c.getEmployeeId(), new ArrayList<>());
			DateHistoryItem dateItem = new DateHistoryItem(histId,
					new DatePeriod(c.getStartDate() != null ? c.getStartDate() : ConstantUtils.minDate(),
							c.getEndDate() != null ? c.getEndDate() : ConstantUtils.maxDate()));
			if (affJobTitleHistory != null) {
				itemtoBeAdded = affJobTitleHistory.get(0);
			}
			itemtoBeAdded.add(dateItem);
			affJobTitleHistoryLst.add(itemtoBeAdded);
			AffWorkplaceHistoryItem histItem =AffWorkplaceHistoryItem.createFromJavaType(histId,  c.getEmployeeId(), c.getWorkplaceId(), c.getNormalWorkplaceId());
			histItems.add(histItem);
			result.add(new PeregAddCommandResult(histId));
		});
		
		if(!affJobTitleHistoryLst.isEmpty()) {
			affWorkplaceHistoryService.addAll(affJobTitleHistoryLst);
		}
		
		if(!histItems.isEmpty()) {
			affWorkplaceHistoryItemRepository.addAll(histItems);
		}
		return result;
	}

}
