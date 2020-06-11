package nts.uk.ctx.workflow.pub.spr.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class ApprovalRootStateSprExport {
	
	private String rootStateID;
	
	private GeneralDate approvalRecordDate;
	
	private String employeeID;
}
