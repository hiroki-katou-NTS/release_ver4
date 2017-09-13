package nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MasterApproverRootOutput {
	
	CompanyApprovalInfor companyRootInfor;
	/**
	 * workplaceId, approver of workplace
	 */
	Map<String, WorkplaceApproverOutput> worplaceRootInfor;
}
