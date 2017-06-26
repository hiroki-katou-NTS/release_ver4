package nts.uk.ctx.basic.app.command.organization.department;

import lombok.Getter;

@Getter
public class RemoveDepartmentCommand {

	private String departmentCode;

	private String historyId;
	
	private String hierarchyCode;

}
