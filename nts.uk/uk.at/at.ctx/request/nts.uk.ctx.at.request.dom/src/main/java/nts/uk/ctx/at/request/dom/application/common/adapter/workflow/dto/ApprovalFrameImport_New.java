package nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.arc.time.GeneralDate;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Value
public class ApprovalFrameImport_New {
	
	private Integer phaseOrder;
	
	private Integer frameOrder;
	
	private ApprovalBehaviorAtrImport_New approvalAtr;
	
	private List<ApproverStateImport_New> listApprover;
	
	private String approverID;
	
	private String approverName;
	
	private String approverMail;
	
	private String representerID;
	
	private String representerName;
	
	private String representerMail;
	
	private String approvalReason;
	
	private int confirmAtr;
	
	private GeneralDate approvalDate;
}
