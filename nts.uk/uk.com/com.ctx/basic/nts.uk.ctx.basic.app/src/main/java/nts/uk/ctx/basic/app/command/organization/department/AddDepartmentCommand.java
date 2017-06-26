package nts.uk.ctx.basic.app.command.organization.department;


import lombok.Getter;

@Getter
public class AddDepartmentCommand {

	private String departmentCode;

	private String historyId;

	private String externalCode;

	private String fullName;

	private String hierarchyCode;

	private String name;
	
	private String startDate;
	
	private String endDate;
	
	private String memo;
	
	

}
