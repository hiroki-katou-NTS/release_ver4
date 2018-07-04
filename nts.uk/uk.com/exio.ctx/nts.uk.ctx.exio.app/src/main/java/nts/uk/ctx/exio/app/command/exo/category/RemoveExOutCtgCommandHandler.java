package nts.uk.ctx.exio.app.command.exo.category;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.exio.dom.exo.category.ExOutCtgRepository;

@Stateless
@Transactional
public class RemoveExOutCtgCommandHandler extends CommandHandler<ExOutCtgCommand> {

	@Inject
	private ExOutCtgRepository repository;

	@Override
	protected void handle(CommandHandlerContext<ExOutCtgCommand> context) {
		int categoryId = context.getCommand().getCategoryId();
		repository.remove(categoryId);
	}
}
