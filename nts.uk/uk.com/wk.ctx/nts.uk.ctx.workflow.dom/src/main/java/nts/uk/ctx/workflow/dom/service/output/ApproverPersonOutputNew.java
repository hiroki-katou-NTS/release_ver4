package nts.uk.ctx.workflow.dom.service.output;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;

@Value
@AllArgsConstructor
public class ApproverPersonOutputNew {
	/*承認できるフラグ：(true, false)
    true：承認できる
    false：承認できない*/
	private Boolean authorFlag;
	
	/*指定する社員の承認区分：（未承認、承認済、否認）*/
	private ApprovalBehaviorAtr approvalAtr;
	
	/*代行期限切れフラグ：(true, false)
    true：代行期限切れ
    false：代行期限内*/
	private Boolean expirationAgentFlag; 
	
	/* 承認中フェーズの承認区分  */
	private ApprovalBehaviorAtr approvalPhaseAtr;
}
