/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fluidworkset;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

@Getter
// 流動休憩設定
public class FluRestTimeSetting {

	// 流動時間設定
	private FluidTimeSetting fluidTimeSetting;

	// 流動休憩時間
	private AttendanceTime fluidRestTime;

	// 流動経過時間
	private AttendanceTime fluidElapsedTime;
}
