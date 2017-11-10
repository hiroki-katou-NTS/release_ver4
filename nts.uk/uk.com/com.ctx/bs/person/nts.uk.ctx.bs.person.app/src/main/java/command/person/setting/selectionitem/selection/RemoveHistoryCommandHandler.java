package command.person.setting.selectionitem.selection;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.PerInfoHistorySelection;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.PerInfoHistorySelectionRepository;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.Selection;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.SelectionItemOrder;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.SelectionItemOrderRepository;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.SelectionRepository;

/**
 * 
 * @author tuannv
 *
 */
@Stateless
@Transactional
public class RemoveHistoryCommandHandler extends CommandHandler<RemoveHistoryCommand> {

	@Inject
	private PerInfoHistorySelectionRepository perInfoHistSeleRepo;

	@Inject
	private SelectionRepository selectionRepo;

	@Inject
	private SelectionItemOrderRepository selectionOrderRepo;

	@Override
	protected void handle(CommandHandlerContext<RemoveHistoryCommand> context) {
		RemoveHistoryCommand command = context.getCommand();
		String getHistId = command.getHistId();

		// liem tra domain: co 1history ko the xoa dc:履歴が1件のみの場合削除できない#Msg_57
		Optional<PerInfoHistorySelection> optCheckExitByHistId = this.perInfoHistSeleRepo
				.getAllHistoryByHistId(command.getHistId());
		if (!optCheckExitByHistId.isPresent()) {
			throw new BusinessException(new RawErrorMessage("Msg_57"));
		}

		// Xoa domain: history
		this.perInfoHistSeleRepo.remove(getHistId);

		// Xoa Selection:
		List<Selection> selectionList = this.selectionRepo.getAllSelectByHistId(getHistId);
		selectionList.stream().forEach(x -> this.selectionRepo.remove(x.getSelectionID()));
		
		// Xoa domain: Selection Order
		List<SelectionItemOrder> orderList = this.selectionOrderRepo.getAllOrderSelectionByHistId(getHistId);
		orderList.stream().forEach(x -> this.selectionOrderRepo.remove(x.getSelectionID()));

		
		
	}

}
