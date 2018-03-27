package nts.uk.ctx.at.record.app.find.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.PartResetClassification;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartResetClassificationDto {
	//マスタ再設定
		private Boolean masterReconfiguration;

		//休業再設定
		private Boolean closedHolidays;

		// 就業時間帯再設定
		private Boolean resettingWorkingHours;

		// 打刻のみ再度反映
		private Boolean reflectsTheNumberOfFingerprintChecks;

		// 特定日区分再設定
		private Boolean specificDateClassificationResetting;

		// 申し送り時間再設定
		private Boolean resetTimeAssignment;

		// 育児・介護短時間再設定
		private Boolean resetTimeChildOrNurseCare;

		// 計算区分再設定
		private Boolean calculationClassificationResetting;
		
		public static PartResetClassificationDto fromDomain(PartResetClassification domain){
			return new PartResetClassificationDto(
					domain.getMasterReconfiguration(),
					domain.getClosedHolidays(),
					domain.getResettingWorkingHours(),
					domain.getReflectsTheNumberOfFingerprintChecks(),
					domain.getSpecificDateClassificationResetting(),
					domain.getResetTimeAssignment(),
					domain.getResetTimeChildOrNurseCare(),
					domain.getCalculationClassificationResetting()
					);
			
		}
}
