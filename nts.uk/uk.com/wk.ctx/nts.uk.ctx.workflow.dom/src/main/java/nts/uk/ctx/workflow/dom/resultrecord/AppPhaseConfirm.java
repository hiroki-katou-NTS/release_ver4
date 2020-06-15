package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
/**
 * 承認済フェーズ
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
@Builder
public class AppPhaseConfirm {
	
	/**
	 * 順序
	 */
	private Integer phaseOrder;
	
	/**
	 * 承認区分
	 */
	@Setter
	private ApprovalBehaviorAtr appPhaseAtr;
	
	/**
	 * 承認済承認者
	 */
	private List<AppFrameConfirm> listAppFrame;
	
}
