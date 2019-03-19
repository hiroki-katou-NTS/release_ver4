package nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalstatusmonthly;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 月の実績の承認状況
 * @author tutk
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class ApprovalStatusMonth {
	/**承認状況*/
	private List<ApprovalStatusResult> approvalStatusResult;

	public ApprovalStatusMonth(List<ApprovalStatusResult> approvalStatusResult) {
		super();
		this.approvalStatusResult = approvalStatusResult;
	}
	
}
