package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
/**
 * 承認済フェーズ
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class AppPhaseInstance {
	
	/**
	 * 順序
	 */
	private Integer phaseOrder;
	
	/**
	 * 承認区分
	 */
	private ApprovalBehaviorAtr appPhaseAtr;
	
	/**
	 * 承認済承認者
	 */
	private List<AppFrameInstance> listAppFrame;
	
}
