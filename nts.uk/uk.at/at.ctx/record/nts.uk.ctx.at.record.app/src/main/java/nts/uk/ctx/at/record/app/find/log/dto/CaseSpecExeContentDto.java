package nts.uk.ctx.at.record.app.find.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CaseSpecExeContent;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseSpecExeContentDto {
	/**
	 * ID (運用ケース)
	 */
	private String caseSpecExeContentID;

	/**
	 * 並び順
	 */
	private int orderNumber;

	/**
	 * 運用ケース名
	 */
	private String useCaseName;

	private SetInforReflAprResultDto reflectApprovalSetInfo;

	/**
	 * 日別作成の設定情報
	 */

	private SettingInforForDailyCreationDto dailyCreationSetInfo;
	/**
	 * 日別計算の設定情報
	 */

	private CalExeSettingInforDto dailyCalSetInfo;
	/**
	 * 月別集計の設定情報
	 */

	private CalExeSettingInforDto monlyAggregationSetInfo;

	public static CaseSpecExeContentDto fromDomain(CaseSpecExeContent domain) {

		return new CaseSpecExeContentDto(domain.getCaseSpecExeContentID(), domain.getOrderNumber(),
				domain.getUseCaseName().v(),
				domain.getReflectApprovalSetInfo().isPresent()
						? SetInforReflAprResultDto.fromDomain(domain.getReflectApprovalSetInfo().get()) : null,
				domain.getDailyCreationSetInfo().isPresent()
						? SettingInforForDailyCreationDto.fromDomain(domain.getDailyCreationSetInfo().get()) : null,
				domain.getDailyCalSetInfo().isPresent()
						? CalExeSettingInforDto.fromDomain(domain.getDailyCalSetInfo().get()) : null,
				domain.getMonlyAggregationSetInfo().isPresent()
						? CalExeSettingInforDto.fromDomain(domain.getMonlyAggregationSetInfo().get()) : null);
	}

}
