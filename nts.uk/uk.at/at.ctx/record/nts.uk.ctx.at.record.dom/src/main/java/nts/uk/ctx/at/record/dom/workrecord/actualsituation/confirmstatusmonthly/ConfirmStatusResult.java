package nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
/**
 * 確認状況 : 
 * @author tutk
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmStatusResult {
	
	/**対象社員*/
	private String employeeId;
	
	/**対象年月*/
	private YearMonth yearMonth;
	
	/**対象締め*/
	private ClosureId closureId;
	
	/**確認状況*/
	private boolean confirmStatus;
	
	/**実施可否*/
	private AvailabilityAtr implementaPropriety;

	/**解除可否*/
	private ReleasedAtr whetherToRelease;

	public ConfirmStatusResult(String employeeId, YearMonth yearMonth, boolean confirmStatus, AvailabilityAtr implementaPropriety,
			ReleasedAtr whetherToRelease) {
		super();
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.confirmStatus = confirmStatus;
		this.implementaPropriety = implementaPropriety;
		this.whetherToRelease = whetherToRelease;
	}
	
	
	
}
