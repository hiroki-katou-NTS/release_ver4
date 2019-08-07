package nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalstatusmonthly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly.AvailabilityAtr;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly.ReleasedAtr;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
/**
 * 承認状況
 * @author tutk
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalStatusResult {
	/**対象社員*/
	private String employeeId;
	
	/**対象年月*/
	private YearMonth yearMonth;
	
	/**対象締め*/
	private ClosureId closureId;
	
	/**承認状態*/
	private boolean approvalStatus;
	
	/**承認状況*/
	private ApprovalStatusForEmployee normalStatus;
	
	/**実施可否*/
	private AvailabilityAtr implementaPropriety;
	
	/**解除可否*/
	private ReleasedAtr whetherToRelease;

	public ApprovalStatusResult(String employeeId, YearMonth yearMonth, boolean approvalStatus, ApprovalStatusForEmployee normalStatus,
			AvailabilityAtr implementaPropriety, ReleasedAtr whetherToRelease) {
		super();
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.approvalStatus = approvalStatus;
		this.normalStatus = normalStatus;
		this.implementaPropriety = implementaPropriety;
		this.whetherToRelease = whetherToRelease;
	}
	
	
}
