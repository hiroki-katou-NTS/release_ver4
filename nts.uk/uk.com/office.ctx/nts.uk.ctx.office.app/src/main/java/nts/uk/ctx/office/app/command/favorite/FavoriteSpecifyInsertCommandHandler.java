package nts.uk.ctx.office.app.command.favorite;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.office.dom.favorite.FavoriteSpecify;
import nts.uk.ctx.office.dom.favorite.FavoriteSpecifyRepository;

/*
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.オフィス支援.在席照会.お気に入り.App.お気に入りを新規登録する.お気に入りを新規登録する
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class FavoriteSpecifyInsertCommandHandler extends CommandHandler<FavoriteSpecifyCommand> {

	@Inject
	private FavoriteSpecifyRepository favoriteSpecifyRepository;

	@Override
	protected void handle(CommandHandlerContext<FavoriteSpecifyCommand> context) {
		FavoriteSpecifyCommand command = context.getCommand();
		FavoriteSpecify domain = FavoriteSpecify.createFromMemento(command);
		favoriteSpecifyRepository.insert(domain);
	}
}
