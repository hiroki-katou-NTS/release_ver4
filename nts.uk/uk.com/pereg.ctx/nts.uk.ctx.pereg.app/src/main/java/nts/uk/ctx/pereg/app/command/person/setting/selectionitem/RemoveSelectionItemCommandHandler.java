package nts.uk.ctx.pereg.app.command.person.setting.selectionitem;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pereg.app.find.person.info.item.PerInfoItemDefFinder;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.IPerInfoSelectionItemRepository;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.PerInfoHistorySelectionRepository;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.SelectionItemOrderRepository;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.SelectionRepository;

@Stateless
@Transactional
public class RemoveSelectionItemCommandHandler extends CommandHandler<RemoveSelectionItemCommand> {
	@Inject
	private IPerInfoSelectionItemRepository perInfoSelectionItemRepo;

	@Inject
	private PerInfoHistorySelectionRepository historySelectionRepository;

	@Inject
	private SelectionRepository selectionRepo;

	@Inject
	private SelectionItemOrderRepository selectionOrderRepo;
	
	@Inject
	private PerInfoItemDefFinder itemDefinitionFinder;

	@Override
	protected void handle(CommandHandlerContext<RemoveSelectionItemCommand> context) {
		RemoveSelectionItemCommand command = context.getCommand();
		String selectionItemId = command.getSelectionItemId();

		// check
		checkSelectionItemId(selectionItemId);

		// remove selections
		selectionRepo.removeInSelectionItemId(selectionItemId);

		// remove selection orders
		selectionOrderRepo.removeInSelectionItemId(selectionItemId);
		
		// remove histories
		historySelectionRepository.removeInSelectionItemId(selectionItemId);
		
		// remove selection item
		this.perInfoSelectionItemRepo.remove(selectionItemId);
	}
	
	private void checkSelectionItemId(String id) {
		if (itemDefinitionFinder.checkExistedSelectionItemId(id)) {
			throw new BusinessException("Msg_521");
		}
	}
}
