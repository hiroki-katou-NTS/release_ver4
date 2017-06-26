package nts.uk.ctx.basic.app.command.organization.workplace;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class UpdateWorkPlaceCommand {

	private String workPlaceCode;

	private String historyId;

	private String endDate;

	private String externalCode;

	private String fullName;

	private String hierarchyCode;

	private String name;

	private String shortName;

	private String startDate;
	
	private String memo;
	
	private String parentChildAttribute1;

	private String parentChildAttribute2;

	private String parentWorkCode1;

	private String parentWorkCode2;

}
