package nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.ConfirmMsgOutput;

/**
 * Refactor5
 * @author huylq
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckBeforeOutputMulti {

	/**
	 * List＜ビジネスネーム, 確認メッセージリスト＞
	 */
	private Map<String, List<ConfirmMsgOutput>> confirmMsgOutputMap;
	
	/**
	 * List＜社員ID, 承認ルートの内容＞
	 */
	private Map<String, ApprovalRootContentImport_New> approvalRootContentMap;
	
	/**
	 * エラー対象者のビジネスネーム
	 */
	private String errorEmpBusinessName;
}
