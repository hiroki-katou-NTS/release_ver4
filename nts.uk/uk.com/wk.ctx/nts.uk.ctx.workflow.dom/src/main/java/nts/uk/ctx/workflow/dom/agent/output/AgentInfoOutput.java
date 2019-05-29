package nts.uk.ctx.workflow.dom.agent.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;
/**
 * 承認者、期間から承認代行情報を取得する
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class AgentInfoOutput {
	/**
	 * 承認者社員ID
	 */
	private String approverID;
	
	/**
	 * 代行者社員ID
	 */
	private String agentID;
	
	/**
	 * 開始日
	 */
	private GeneralDate startDate;
	
	/**
	 * 終了日
	 */
	private GeneralDate endDate;
}
