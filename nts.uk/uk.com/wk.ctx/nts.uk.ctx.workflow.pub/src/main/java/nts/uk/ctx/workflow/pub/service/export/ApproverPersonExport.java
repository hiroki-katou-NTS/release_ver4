package nts.uk.ctx.workflow.pub.service.export;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Getter
@AllArgsConstructor
public class ApproverPersonExport {
	
	/*承認できるフラグ：(true, false)
    true：承認できる
    false：承認できない*/
	private Boolean authorFlag;
	
	/*指定する社員の承認区分：（未承認、承認済、否認）*/
	private ApprovalBehaviorAtrExport approvalAtr;
	
	/*代行期限切れフラグ：(true, false)
    true：代行期限切れ
    false：代行期限内*/
	private Boolean expirationAgentFlag; 
}
