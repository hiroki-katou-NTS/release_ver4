package nts.uk.ctx.sys.portal.app.toppage.command;

import javax.ejb.Stateless;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;

@Stateless
public class DeleteTopPageCommandHandler extends CommandHandler<TopPageBaseCommand>{

	@Override
	protected void handle(CommandHandlerContext<TopPageBaseCommand> context) {
		// TODO Auto-generated method stub
		
	}

}
