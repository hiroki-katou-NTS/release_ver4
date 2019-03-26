/**
 * 9:03:33 AM Mar 12, 2018
 */
package nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基準社員の承認対象者
 * @author hungnm
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRootOfEmployeeImport {
	/**
	 * 基準社員
	 */
	private String employeeStandard;
	
	/**
	 * ルート状況 - 対象者承認状況リスト
	 */
	private List<ApprovalRootSituation> approvalRootSituations;
}
