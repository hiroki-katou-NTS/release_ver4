package nts.uk.ctx.workflow.pub.service.export;

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
public class ApprovalFrameExport {
	
	private Integer phaseOrder;
	
	private Integer frameOrder;
	
	private ApprovalBehaviorAtrExport approvalAtr;
	
	private List<ApproverStateExport> listApprover;
	
	private String approverID;
	
	private String approverName;
	
	private String representerID;
	
	private String representerName;
	
	private String approvalReason;
	
	private int confirmAtr;
	
	private GeneralDate approvalDate;
}
