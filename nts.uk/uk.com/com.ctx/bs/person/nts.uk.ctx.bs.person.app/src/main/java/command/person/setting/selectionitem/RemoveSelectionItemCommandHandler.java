package command.person.setting.selectionitem;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.IPerInfoSelectionItemRepository;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.PerInfoHistorySelection;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.PerInfoHistorySelectionRepository;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.SelectionItemOrderRepository;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.SelectionRepository;

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
	
	@Override
	protected void handle(CommandHandlerContext<RemoveSelectionItemCommand> context) {
		RemoveSelectionItemCommand command = context.getCommand();
		String getSelectionItemId = command.getSelectionItemId();
		
		//Todo: ドメインモデル「選択項目.ReferenceTypeState.コード名称参照条件」に削除対象の選択項目IDが登録されているかチェックする
		//	  : ※削除対象の個人情報の選択項目が使用されていないかのチェック

		// ToDo: ドメインモデル「選択肢」を削除する
		//this.selectionRepo.remove(getSelectionItemId);

		// ToDo: ドメインモデル「選択肢の並び順と既定値」を削除する
		//this.selectionOrderRepo.remove(getSelectionItemId);

		// ドメインモデル「個人情報の選択項目」を削除する
		this.perInfoSelectionItemRepo.remove(getSelectionItemId);

		// 選択項目ID：選択している選択項目ID
		List<PerInfoHistorySelection> historyList = this.historySelectionRepository
				.historySelection(getSelectionItemId);

		// ドメインモデル「選択肢履歴」を削除する
		for (PerInfoHistorySelection h : historyList) {
			this.historySelectionRepository.remove(h.getHistId());
		}
	}
}
