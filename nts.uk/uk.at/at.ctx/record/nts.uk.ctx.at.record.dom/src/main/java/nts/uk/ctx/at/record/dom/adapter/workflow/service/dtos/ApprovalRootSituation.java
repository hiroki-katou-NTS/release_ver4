/**
 * 9:04:09 AM Mar 12, 2018
 */
package nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApproverEmployeeState;

/**
 * @author hungnm
 *
 */
@Data
@AllArgsConstructor
public class ApprovalRootSituation {
	/**
	 * インスタンスID
	 */
	private String appRootID;
	/**
	 * ルートの状況 - 基準社員のルート状況
	 */
	private ApproverEmployeeState approvalAtr;
	/**
	 * 対象日
	 */
	private GeneralDate appDate;
	/**
	 * 対象者
	 */
	private String targetID;
	/**
	 * 承認状況
	 */
	private ApprovalStatus approvalStatus; 
}
