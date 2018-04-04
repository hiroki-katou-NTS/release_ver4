package nts.uk.ctx.at.record.app.find.workrule.specific;

import lombok.Data;

/**
 * Specific work rule DTO
 * @author HoangNDH
 *
 */
@Data
public class SpecificWorkRuleDto {
	// 総拘束時間の計算.計算方法
	private int constraintCalcMethod;
	// 総労働時間の上限値制御.設定
	private int upperLimitSet;
	
	// 時間休暇相殺優先順位.
	// 代休
	private int substituteHoliday;
	// 60H超休
	private int sixtyHourVacation;
	// 特別休暇
	private int specialHoliday;
	// 年休
	private int annualHoliday;
}
