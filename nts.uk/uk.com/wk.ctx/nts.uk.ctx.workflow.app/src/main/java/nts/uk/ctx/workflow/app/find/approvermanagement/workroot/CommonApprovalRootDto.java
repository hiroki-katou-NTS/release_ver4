package nts.uk.ctx.workflow.app.find.approvermanagement.workroot;

import java.util.List;

import lombok.Value;
@Value
public class CommonApprovalRootDto {
	/**会社名*/
	private String companyName;
	private List<CompanyAppRootDto> lstCompanyRoot;
	private List<WorkPlaceAppRootDto> lstWorkplaceRoot;
	private List<PersonAppRootDto> lstPersonRoot;
	private List<ObjDate> result;
}
