/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import lombok.Getter;

/**
 * @author danpv
 *
 */
@Getter
public class PartResetClassification {

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

	public PartResetClassification(Boolean masterReconfiguration, Boolean closedHolidays, Boolean resettingWorkingHours,
			Boolean reflectsTheNumberOfFingerprintChecks, Boolean specificDateClassificationResetting,
			Boolean resetTimeAssignment, Boolean resetTimeChildOrNurseCare,
			Boolean calculationClassificationResetting) {
		super();
		this.masterReconfiguration = masterReconfiguration;
		this.closedHolidays = closedHolidays;
		this.resettingWorkingHours = resettingWorkingHours;
		this.reflectsTheNumberOfFingerprintChecks = reflectsTheNumberOfFingerprintChecks;
		this.specificDateClassificationResetting = specificDateClassificationResetting;
		this.resetTimeAssignment = resetTimeAssignment;
		this.resetTimeChildOrNurseCare = resetTimeChildOrNurseCare;
		this.calculationClassificationResetting = calculationClassificationResetting;
	}


	
}