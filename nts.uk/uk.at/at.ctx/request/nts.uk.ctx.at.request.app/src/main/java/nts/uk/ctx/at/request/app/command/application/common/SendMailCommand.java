package nts.uk.ctx.at.request.app.command.application.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SendMailCommand {
	private String mailContent;
	private List<DetailSendMailCommand> sendMailOption;
	private ApplicationCommand_New application;
}
