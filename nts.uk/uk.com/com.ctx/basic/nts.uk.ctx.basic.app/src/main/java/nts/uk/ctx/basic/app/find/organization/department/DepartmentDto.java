package nts.uk.ctx.basic.app.find.organization.department;

import java.util.ArrayList;

import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
public class DepartmentDto extends OrganizationTreeDto<DepartmentDto>{

	private String companyCode;

	private String departmentCode;

	private String historyId;

	private GeneralDate endDate;

	private String externalCode;

	private String fullName;

	private String hierarchyCode;

	private String name;
	
	private String display;

	private GeneralDate startDate;

	public DepartmentDto(String departmentCode, String externalCode, String fullName, String hierarchyCode,
			String name, String historyId) {
		this.departmentCode = departmentCode;
		this.externalCode = externalCode;
		this.fullName = fullName;
		this.hierarchyCode = hierarchyCode;
		this.name = name;
		this.display =  departmentCode +" "+ name;
		this.children = new ArrayList<>();
		this.historyId = historyId;
	}
	
	
}
