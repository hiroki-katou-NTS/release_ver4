package nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.arc.time.GeneralDateTime;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Value
public class ApproverStateImport_New {
	
	private String approverID;
	
	private ApprovalBehaviorAtrImport_New approvalAtr;
	
	private String agentID;
	
	private String approverName;
	
	private String agentName;
	
	private String representerID;
	
	private String representerName;
	
	private GeneralDateTime approvalDate;
	
	private String approvalReason;
	
	private String approverEmail;
	
	private String agentMail;
	
	private String representerEmail;
	
	private Integer approverInListOrder;
	
	public static ApproverStateImport_New createSimpleFromInstance(String approverID) {
		return new ApproverStateImport_New(approverID, null, null, null, null, null, null, null, null, null, null, null, null);
	}
}
