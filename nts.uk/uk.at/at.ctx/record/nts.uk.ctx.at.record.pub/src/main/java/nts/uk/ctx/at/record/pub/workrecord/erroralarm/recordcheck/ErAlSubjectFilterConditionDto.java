package nts.uk.ctx.at.record.pub.workrecord.erroralarm.recordcheck;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErAlSubjectFilterConditionDto {
	
	// 勤務種別でしぼり込む
	private Boolean filterByBusinessType;

	// 職位でしぼり込む
	private Boolean filterByJobTitle;

	// 雇用でしぼり込む
	private Boolean filterByEmployment;

	// 分類でしぼり込む
	private Boolean filterByClassification;

	// 対象勤務種別
	private List<String> lstBusinessTypeCode;

	// 対象職位
	private List<String> lstJobTitleId;

	// 対象雇用
	private List<String> lstEmploymentCode;

	// 対象分類
	private List<String> lstClassificationCode;
}
