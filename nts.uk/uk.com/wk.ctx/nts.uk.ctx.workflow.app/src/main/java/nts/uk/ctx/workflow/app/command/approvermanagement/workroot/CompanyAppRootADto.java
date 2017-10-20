package nts.uk.ctx.workflow.app.command.approvermanagement.workroot;

import lombok.Value;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.ApprovalPhaseDto;
@Value
public class CompanyAppRootADto {

	private boolean color;
	private boolean common;
	private Integer appTypeValue;
	private String appTypeName;
	private String approvalId;
	private String historyId;
	private String branchId;
	private ApprovalPhaseDto appPhase1;
	private ApprovalPhaseDto appPhase2;
	private ApprovalPhaseDto appPhase3;
	private ApprovalPhaseDto appPhase4;
	private ApprovalPhaseDto appPhase5;
}
