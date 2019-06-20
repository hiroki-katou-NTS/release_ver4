package nts.uk.ctx.bs.employee.app.command.classification.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistItem;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistItemRepository;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistory;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistoryRepository;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistoryRepositoryService;
import nts.uk.ctx.bs.person.dom.person.common.ConstantUtils;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;
import nts.uk.shr.pereg.app.command.PeregAddListCommandHandler;
/**
 * 
 * @author lanlt
 *
 */
@Stateless
public class AddAffClassListCommandHandler extends CommandHandlerWithResult<List<AddAffClassificationCommand>, List<PeregAddCommandResult>>
implements PeregAddListCommandHandler<AddAffClassificationCommand>{
	@Inject
	private AffClassHistoryRepository affClassHistoryRepo;

	@Inject
	private AffClassHistItemRepository affClassHistItemRepo;

	@Inject
	private AffClassHistoryRepositoryService affClassHistoryRepositoryService;
	@Override
	public String targetCategoryCd() {
		return "CS00004";
	}

	@Override
	public Class<?> commandClass() {
		return AddAffClassificationCommand.class;
	}

	@Override
	protected List<PeregAddCommandResult> handle(CommandHandlerContext<List<AddAffClassificationCommand>> context) {
		List<AddAffClassificationCommand> command = context.getCommand();
		String cid = AppContexts.user().companyId();
		List<PeregAddCommandResult> result = new ArrayList<>();
		List<AffClassHistItem> histItemDomains = new ArrayList<>();
		List<AffClassHistory> affClassHistDomains = new ArrayList<>();
		List<String> sids = command.stream().map(c -> c.getEmployeeId()).collect(Collectors.toList());
		
		Map<String, List<AffClassHistory>> historiesMap = affClassHistoryRepo.getBySidsWithCid(cid, sids).stream().collect(Collectors.groupingBy(c -> c.getEmployeeId()));
		
		command.stream().forEach(c ->{
			// add history
			String newHistoryId = IdentifierUtil.randomUniqueId();
			AffClassHistory history = new AffClassHistory(cid, c.getEmployeeId(), new ArrayList<>());
			if(historiesMap.containsKey(c.getEmployeeId())) {
				List<AffClassHistory> affClassHistLst = historiesMap.get(c.getEmployeeId());
				if(affClassHistLst.size() > 0) {
					history = affClassHistLst.get(0);
				}
				
			}
			DateHistoryItem dateItem = new DateHistoryItem(newHistoryId, new DatePeriod(c.getStartDate() != null ? c.getStartDate() : ConstantUtils.minDate(), c.getEndDate()!= null? c.getEndDate():  ConstantUtils.maxDate()));
			history.add(dateItem);
			affClassHistDomains.add(history);
			// add history item
			AffClassHistItem histItem = AffClassHistItem.createFromJavaType(c.getEmployeeId(), newHistoryId,
					c.getClassificationCode());
			histItemDomains.add(histItem);
			result.add(new PeregAddCommandResult(newHistoryId));
		});
		affClassHistoryRepositoryService.addAll(affClassHistDomains);
		affClassHistItemRepo.addAll(histItemDomains);
		
		return result;
	}

}
