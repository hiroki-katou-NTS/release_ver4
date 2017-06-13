package nts.uk.ctx.workflow.app.command.agent;

import lombok.Value;
import nts.arc.time.GeneralDate;

@Value
public class AddAgentCommand {
	private String companyId;
	
	private String employeeId;
	
	private GeneralDate startDate;
	
	private GeneralDate endDate;
	
	private String agentSid1;

	private int agentAppType1;

	private String agentSid2;

	private int agentAppType2;

	private String agentSid3;

	private int agentAppType3;

	private String agentSid4;

	private int agentAppType4;
	
}
