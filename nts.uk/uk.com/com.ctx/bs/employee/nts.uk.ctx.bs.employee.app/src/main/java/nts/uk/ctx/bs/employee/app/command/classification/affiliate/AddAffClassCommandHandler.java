/**
 * 
 */
package nts.uk.ctx.bs.employee.app.command.classification.affiliate;

import java.util.ArrayList;
import java.util.Optional;

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
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

/**
 * @author danpv
 * @author hop.nt
 *
 */
@Stateless
public class AddAffClassCommandHandler
		extends CommandHandlerWithResult<AddAffClassificationCommand, PeregAddCommandResult>
		implements PeregAddCommandHandler<AddAffClassificationCommand> {

	@Inject
	private AffClassHistoryRepository affClassHistoryRepo;

	@Inject
	private AffClassHistoryRepositoryService affClassHistoryRepositoryService;

	@Override
	public String targetCategoryCd() {
		return "CS00004";
	}

	@Override
	public Class<AddAffClassificationCommand> commandClass() {
		return AddAffClassificationCommand.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddAffClassificationCommand> context) {
		AddAffClassificationCommand command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		// add history
		String newHistoryId = IdentifierUtil.randomUniqueId();
		
		Optional<AffClassHistory> historyOption = affClassHistoryRepo.getByEmployeeId(companyId,command.getEmployeeId());
		
		AffClassHistory history = new AffClassHistory(companyId, command.getEmployeeId(), new ArrayList<>());
		if (historyOption.isPresent()) {
			history = historyOption.get();
		}
		DateHistoryItem dateItem = new DateHistoryItem(newHistoryId, new DatePeriod(command.getStartDate() != null ? command.getStartDate() : ConstantUtils.minDate(), command.getEndDate()!= null? command.getEndDate():  ConstantUtils.maxDate()));
		history.add(dateItem);
		affClassHistoryRepositoryService.addToMerge(history, command.getClassificationCode());

		return new PeregAddCommandResult(newHistoryId);
	}
}
