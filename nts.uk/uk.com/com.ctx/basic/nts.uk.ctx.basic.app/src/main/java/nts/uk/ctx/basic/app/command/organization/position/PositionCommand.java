package nts.uk.ctx.basic.app.command.organization.position;

import lombok.Value;

@Value
public class PositionCommand {

	private String jobName;
	private String memo;
	private String hiterarchyOrderCode;
	private int presenceCheckScopeSet;
	private String jobOutCode;
}
