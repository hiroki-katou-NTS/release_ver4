package nts.uk.ctx.basic.app.command.organization.workplace;

import lombok.Data;

@Data
public class RemoveWorkPlaceCommand {

	private String workplaceCode;

	private String historyId;
	
	private String hierarchyCode;

}

