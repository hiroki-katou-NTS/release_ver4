package nts.uk.ctx.basic.app.command.organization.position;

import java.util.List;

import lombok.Value;

@Value
public class RegistryPositionCommand {
	//
	private String historyId;
	private String startDate;
	private boolean chkCopy;
	private String jobCode;
	private boolean chkInsert;
	//
	private PositionCommand positionCommand;
	private List<AddJobTitleRefCommand> refCommand;
}
