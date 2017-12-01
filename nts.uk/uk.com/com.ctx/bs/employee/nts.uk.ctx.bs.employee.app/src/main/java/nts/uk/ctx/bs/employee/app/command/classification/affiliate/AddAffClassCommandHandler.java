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
import nts.uk.ctx.bs.employee.dom.classification.affiliate_ver1.AffClassHistItemRepository_ver1;
import nts.uk.ctx.bs.employee.dom.classification.affiliate_ver1.AffClassHistItem_ver1;
import nts.uk.ctx.bs.employee.dom.classification.affiliate_ver1.AffClassHistoryRepository_ver1;
import nts.uk.ctx.bs.employee.dom.classification.affiliate_ver1.AffClassHistory_ver1;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

/**
 * @author danpv
 *
 */
@Stateless
public class AddAffClassCommandHandler
		extends CommandHandlerWithResult<AddAffClassificationCommand, PeregAddCommandResult>
		implements PeregAddCommandHandler<AddAffClassificationCommand> {

	@Inject
	private AffClassHistoryRepository_ver1 affClassHistoryRepo;

	@Inject
	private AffClassHistItemRepository_ver1 affClassHistItemRepo;

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

		// add history
		String newHistoryId = IdentifierUtil.randomUniqueId();
		DateHistoryItem dateItem = new DateHistoryItem(newHistoryId,
				new DatePeriod(command.getStartDate(), command.getEndDate()));
		Optional<AffClassHistory_ver1> historyOption = affClassHistoryRepo.getByEmployeeId(command.getEmployeeId());
		AffClassHistory_ver1 history = null;
		if (historyOption.isPresent()) {
			history = historyOption.get();
		} else {
			history = new AffClassHistory_ver1(command.getEmployeeId(), new ArrayList<>());
		}
		history.add(dateItem);
		affClassHistoryRepo.add(history);
		// add history item
		AffClassHistItem_ver1 histItem = AffClassHistItem_ver1.createFromJavaType(command.getEmployeeId(), newHistoryId,
				command.getClassificationCode());
		affClassHistItemRepo.add(histItem);

		return new PeregAddCommandResult(newHistoryId);
	}
}
