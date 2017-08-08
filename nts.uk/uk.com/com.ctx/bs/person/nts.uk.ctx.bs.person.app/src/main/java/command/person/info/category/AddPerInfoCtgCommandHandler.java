package command.person.info.category;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonInfoCategory;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
@Transactional
public class AddPerInfoCtgCommandHandler extends CommandHandler<AddPerInfoCtgCommand> {

	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRep;

	@Override
	protected void handle(CommandHandlerContext<AddPerInfoCtgCommand> context) {
		AddPerInfoCtgCommand perInfoCtgCommand = context.getCommand();
		String categoryCode = null;
		PersonInfoCategory perInfoCtg = PersonInfoCategory.createFromJavaType(AppContexts.user().companyId(),
				categoryCode, perInfoCtgCommand.getCategoryName().v(), perInfoCtgCommand.getCategoryType().value);
		this.perInfoCtgRep.addPerInfoCtg(perInfoCtg, AppContexts.user().companyId());
	}

}
