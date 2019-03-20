package nts.uk.ctx.at.shared.dom.adapter.dailyperformance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalStatusImport {
	/**
	 * 解除可否区分
	 */
	private Integer releaseAtr;
	
	/**
	 * 基準社員の承認アクション
	 */
	private Integer approvalAction;
}
