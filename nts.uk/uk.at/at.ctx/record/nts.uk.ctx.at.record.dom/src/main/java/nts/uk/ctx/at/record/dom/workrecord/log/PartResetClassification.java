/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.log;

import lombok.Getter;

/**
 * @author danpv
 *
 */
@Getter
public class PartResetClassification {

	//マスタ再設定
	private boolean masterReconfiguration;

	//休業再設定
	private boolean closedHolidays;

	// 就業時間帯再設定
	private boolean resettingWorkingHours;

	// 打刻のみ再度反映
	private boolean reflectsTheNumberOfFingerprintChecks;

	// 特定日区分再設定
	private boolean specificDateClassificationResetting;

	// 申し送り時間再設定
	private boolean resetTimeAssignment;

	// 育児・介護短時間再設定
	private boolean resetTimeChildOrNurseCare;

	// 計算区分再設定
	private boolean calculationClassificationResetting;

	public PartResetClassification(boolean masterReconfiguration, boolean closedHolidays, boolean resettingWorkingHours,
			boolean reflectsTheNumberOfFingerprintChecks, boolean specificDateClassificationResetting,
			boolean resetTimeAssignment, boolean resetTimeChildOrNurseCare,
			boolean calculationClassificationResetting) {
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