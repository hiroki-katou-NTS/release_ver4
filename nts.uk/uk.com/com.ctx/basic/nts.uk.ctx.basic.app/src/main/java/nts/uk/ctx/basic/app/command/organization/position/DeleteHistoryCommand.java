package nts.uk.ctx.basic.app.command.organization.position;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class DeleteHistoryCommand {


	private String historyId;
	private String startDate;
	private String oldStartDate;
	private String jobCode;
	private String authCode;
}
