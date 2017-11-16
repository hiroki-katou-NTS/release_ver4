package nts.uk.ctx.workflow.app.find.approvermanagement.workroot;

import java.util.List;

import lombok.Value;
@Value
public class DataFullDto {
	private String workplaceId;
	private List<DataDisplayComDto> lstCompany;
	private List<DataDisplayWpDto> lstWorkplace;
	private List<DataDisplayPsDto> lstPerson;

}
