package nts.uk.ctx.sys.portal.app.command.titlemenu;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.sys.portal.dom.titlemenu.service.TitleMenuService;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author hieult
 */
@Stateless
@Transactional
public class CopyTitleMenuCommandHandler extends CommandHandler<CopyTitleMenuCommand> {
	
	@Inject
	private TitleMenuService titleMenuService;

	@Override
	protected void handle(CommandHandlerContext<CopyTitleMenuCommand> context) {
		String companyID = AppContexts.user().companyID();
		CopyTitleMenuCommand command = context.getCommand();
		
		titleMenuService.copyTitleMenu(companyID, command.getSourceTitleMenuCD(), command.getTargetTitleMenuCD(), command.getOverwrite());
	}

	
}
