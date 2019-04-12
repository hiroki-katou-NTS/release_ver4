package nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApproverPersonImportNew {
	/*承認できるフラグ：(true, false)
    true：承認できる
    false：承認できない*/
	private Boolean authorFlag;
	
	/*指定する社員の承認区分：（未承認、承認済、否認）*/
	private ApprovalBehaviorAtrImport_New approvalAtr;
	
	/*代行期限切れフラグ：(true, false)
    true：代行期限切れ
    false：代行期限内*/
	private Boolean expirationAgentFlag; 
	
	/* 承認中フェーズの承認区分  */
	private ApprovalBehaviorAtrImport_New approvalPhaseAtr;
}
