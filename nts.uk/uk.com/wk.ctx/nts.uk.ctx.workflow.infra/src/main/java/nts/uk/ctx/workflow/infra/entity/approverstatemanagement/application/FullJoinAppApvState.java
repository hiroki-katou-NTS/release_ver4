package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;

@AllArgsConstructor
@Getter
public class FullJoinAppApvState {
	private String companyID;
	private String appID;
	private String employeeID;
	private GeneralDate appDate;
	private Integer phaseOrder;
	private Integer appPhaseAtr;
	private Integer approvalForm;
	private Integer frameOrder;
	private Integer appFrameAtr;
	private Integer confirmAtr;
	private String approverID;
	private String representerID;
	private GeneralDate approvalDate;
	private String approvalReason;
	private String approverChildID;
}
