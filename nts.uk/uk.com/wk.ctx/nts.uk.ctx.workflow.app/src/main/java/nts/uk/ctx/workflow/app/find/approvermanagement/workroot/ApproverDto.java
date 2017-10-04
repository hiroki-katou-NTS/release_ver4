package nts.uk.ctx.workflow.app.find.approvermanagement.workroot;

import lombok.Value;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
@Value
public class ApproverDto {
	/**承認者ID*/
	private String approverId;
	/**職位ID*/
	private String jobTitleId;
	/**社員ID*/
	private String employeeId;
	/**Name*/
	private String name;
	/**順序*/
	private int orderNumber;
	/**区分*/
	private int approvalAtr;
	/**確定者*/
	private int confirmPerson;
	public static ApproverDto fromDomain(Approver domain,String name ){
		return new ApproverDto(domain.getApproverId(),
					domain.getJobTitleId(),
					domain.getEmployeeId(),
					name,
					domain.getOrderNumber(),
					domain.getApprovalAtr().value,
					domain.getConfirmPerson().value);
	}
}
