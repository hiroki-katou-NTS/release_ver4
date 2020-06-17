package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
/**
 * 承認フェーズ中間データ
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
@Builder
public class AppPhaseInstance {
	
	/**
	 * 順序
	 */
	private Integer phaseOrder;
	
	/**
	 * 承認形態
	 */
	private ApprovalForm approvalForm;
	
	/**
	 * 承認枠
	 */
	private List<AppFrameInstance> listAppFrame;
}
