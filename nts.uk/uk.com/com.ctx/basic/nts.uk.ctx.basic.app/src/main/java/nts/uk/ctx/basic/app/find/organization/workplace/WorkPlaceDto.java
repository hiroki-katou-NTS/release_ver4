package nts.uk.ctx.basic.app.find.organization.workplace;

import java.math.BigDecimal;
import java.util.ArrayList;

import lombok.Data;
import nts.arc.time.GeneralDate;


@Data
public class WorkPlaceDto extends OrganizationTreeDto<WorkPlaceDto> {

	private String companyCode;

	private String departmentCode;

	private String historyId;

	private GeneralDate endDate;

	private String externalCode;

	private String fullName;

	private String hierarchyCode;

	private String name;
	
	private String display;

	private String parentChildAttribute1;

	private String parentChildAttribute2;

	private String parentWorkCode1;

	private String parentWorkCode2;

	private GeneralDate startDate;

	public WorkPlaceDto(String workPlaceCode, GeneralDate endDate,String historyId, String externalCode, String genericName, String hierarchyCode,
			String name, String parentChildAttribute1, String parentChildAttribute2, String parentWorkCode1,
			String parentWorkCode2, GeneralDate startDate) {
		super();
		this.departmentCode = workPlaceCode;
		this.endDate = endDate;
		this.historyId = historyId;
		this.externalCode = externalCode;
		this.fullName = genericName;
		this.hierarchyCode = hierarchyCode;
		this.name = name;
		this.display = workPlaceCode + " " + name;
		this.parentChildAttribute1 = parentChildAttribute1;
		this.parentChildAttribute2 = parentChildAttribute2;
		this.parentWorkCode1 = parentWorkCode1;
		this.parentWorkCode2 = parentWorkCode2;
		this.startDate = startDate;
		this.children = new ArrayList<>();
	}

	public WorkPlaceDto(String workPlaceCode, String externalCode, String genericName, String hierarchyCode, String name) {
		super();
		this.departmentCode = workPlaceCode;
		this.externalCode = externalCode;
		this.fullName = genericName;
		this.hierarchyCode = hierarchyCode;
		this.name = name;
		this.children = new ArrayList<>();
	}
}
