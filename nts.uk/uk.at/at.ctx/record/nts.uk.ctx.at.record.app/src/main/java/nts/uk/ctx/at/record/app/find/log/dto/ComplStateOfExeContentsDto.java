package nts.uk.ctx.at.record.app.find.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ComplStateOfExeContents;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplStateOfExeContentsDto {
	
	/** 実行内容 */
	private int executionContent;
	
	/** 従業員の実行状況 */
	private int status;
	
	private String statusName;
	
	public static ComplStateOfExeContentsDto fromDomain(ComplStateOfExeContents domain) {
		return new ComplStateOfExeContentsDto(domain.getExecutionContent().value, domain.getStatus().value, domain.getStatus().nameId);
	}
}
