/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.EmployeeExecutionStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;

/**
 * @author danpv
 *
 */
@Getter
@AllArgsConstructor
public class ComplStateOfExeContents {

	/** 実行内容 */
	private ExecutionContent executionContent;
	
	/** 従業員の実行状況 */
	private EmployeeExecutionStatus status;

	/** Check Status is Complete */
	public boolean isComplete() {
		return this.status == EmployeeExecutionStatus.COMPLETE;
	}
	
}