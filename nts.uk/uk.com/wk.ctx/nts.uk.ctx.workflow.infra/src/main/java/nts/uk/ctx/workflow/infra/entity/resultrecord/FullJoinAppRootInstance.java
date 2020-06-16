package nts.uk.ctx.workflow.infra.entity.resultrecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nts.arc.time.GeneralDate;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
@Builder
public class FullJoinAppRootInstance {
	private String rootID;
	private String companyID;
	private String employeeID;
	private GeneralDate startDate;
	private GeneralDate endDate;
	private Integer rootType;
	private Integer phaseOrder;
	private Integer approvalForm;
	private Integer frameOrder;
	private Integer confirmAtr;
	private String approverChildID;
}
