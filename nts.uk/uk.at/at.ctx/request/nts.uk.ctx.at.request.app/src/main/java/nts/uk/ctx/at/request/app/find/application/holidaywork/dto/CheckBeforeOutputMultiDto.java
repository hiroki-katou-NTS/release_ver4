package nts.uk.ctx.at.request.app.find.application.holidaywork.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApprovalRootStateImportDto;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.ConfirmMsgOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.CheckBeforeOutputMulti;

/**
 * Refactor5
 * @author huylq
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckBeforeOutputMultiDto {
	
	/**
	 * List＜ビジネスネーム, 確認メッセージリスト＞
	 */
	private Map<String, List<ConfirmMsgOutput>> confirmMsgOutputMap;
	
	/**
	 * List＜社員ID, 承認ルートの内容＞
	 */
	private Map<String, ApprovalRootStateImportDto> approvalRootContentMap;
	
	/**
	 * エラー対象者のビジネスネーム
	 */
	private String errorEmpBusinessName;

	public static CheckBeforeOutputMultiDto fromDomain(CheckBeforeOutputMulti domain) {
		Map<String, ApprovalRootStateImportDto> approvalRootContentMap = new HashMap<String, ApprovalRootStateImportDto>();
		domain.getApprovalRootContentMap().entrySet().forEach(entry -> {
			approvalRootContentMap.put(entry.getKey(), ApprovalRootStateImportDto.fromDomain(entry.getValue().getApprovalRootState()));
		});
		return new CheckBeforeOutputMultiDto(domain.getConfirmMsgOutputMap(), 
				approvalRootContentMap, 
				domain.getErrorEmpBusinessName());
	}
}
