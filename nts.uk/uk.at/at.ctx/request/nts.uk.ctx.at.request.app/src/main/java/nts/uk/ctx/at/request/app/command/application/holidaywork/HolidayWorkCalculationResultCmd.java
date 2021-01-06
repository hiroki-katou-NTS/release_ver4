package nts.uk.ctx.at.request.app.command.application.holidaywork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.app.command.application.overtime.ApplicationTimeCommand;
import nts.uk.ctx.at.request.app.command.application.overtime.OverStateOutputCommand;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.CalculatedFlag;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.HolidayWorkCalculationResult;

/**
 * Refactor5
 * @author huylq
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HolidayWorkCalculationResultCmd {
	
	/**
	 * 事前申請・実績の超過状態
	 */
	private OverStateOutputCommand actualOvertimeStatus;
	
	/**
	 * 申請時間
	 */
	private ApplicationTimeCommand applicationTime;
	
	/**
	 * 計算済フラグ
	 */
	private int calculatedFlag;
	
	public HolidayWorkCalculationResult toDomain() {
		return new HolidayWorkCalculationResult(
				this.actualOvertimeStatus != null ? this.actualOvertimeStatus.toDomain() : null,
				this.applicationTime != null ? this.applicationTime.toDomain() : null,
				this.calculatedFlag == 0 ? CalculatedFlag.CALCULATED : CalculatedFlag.UNCALCULATED
						);
	}
}
