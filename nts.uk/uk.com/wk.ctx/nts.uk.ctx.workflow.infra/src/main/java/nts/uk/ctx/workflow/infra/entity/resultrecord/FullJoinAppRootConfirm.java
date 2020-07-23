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
public class FullJoinAppRootConfirm {
	private final String rootID;
	private final String companyID;
	private final String employeeID;
	private final GeneralDate recordDate;
	private final Integer rootType;
	private final Integer yearMonth;
	private final Integer closureID;
	private final Integer closureDay;
	private final Integer lastDayFlg;
	private final Integer phaseOrder;
	private final Integer appPhaseAtr;
	private final Integer frameOrder;
	private final String approverID;
	private final String representerID;
	private final GeneralDate approvalDate;
	
	public static FullJoinAppRootConfirm dailyRoot(
			String rootID, String companyID, String employeeID, GeneralDate recordDate) {
		
		return new FullJoinAppRootConfirm(
				rootID, companyID, employeeID, recordDate,
				1,
				null, null, null, null, null, null, null, null, null, null);
	}
	
	public FullJoinAppRootConfirm phase(
			Integer phaseOrder, Integer appPhaseAtr) {
		
		return new FullJoinAppRootConfirm(
				rootID, companyID, employeeID, recordDate, rootType, yearMonth, closureID, closureDay, lastDayFlg,
				phaseOrder, appPhaseAtr,
				frameOrder, approverID, representerID, approvalDate);
	}
	
	public FullJoinAppRootConfirm frame(
			Integer frameOrder, String approverID, String representerID, GeneralDate approvalDate) {
		
		return new FullJoinAppRootConfirm(
				rootID, companyID, employeeID, recordDate, rootType, yearMonth, closureID, closureDay, lastDayFlg,
				phaseOrder, appPhaseAtr,
				frameOrder, approverID, representerID, approvalDate);
	}
}
