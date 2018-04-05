package nts.uk.ctx.at.request.app.command.application.common;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.auth.x500.X500Principal;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.dom.application.common.service.mail.after.CheckTransmission;

@Stateless
public class SendMailCommandHandler extends CommandHandlerWithResult<SendMailCommand, List<Integer>>{
	
	@Inject
	private CheckTransmission checkTranmission;
	protected List<Integer> handle(CommandHandlerContext<SendMailCommand> context)  {
		List<String> employeeIdList = new ArrayList<String>();
		context.getCommand().getSendMailOption().forEach(x -> {
			employeeIdList.add(x.getEmployeeID());
		});
		ApplicationCommand_New app = context.getCommand().getApplication();
		return checkTranmission.doCheckTranmission(app.getApplicationID(), app.getApplicationType(), app.getPrePostAtr(), employeeIdList, null, context.getCommand().getMailContent(), null);
	}
}
