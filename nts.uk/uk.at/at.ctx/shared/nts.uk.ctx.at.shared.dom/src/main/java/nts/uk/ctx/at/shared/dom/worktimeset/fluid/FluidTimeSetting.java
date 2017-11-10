/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktimeset.fluid;

import lombok.Getter;

/**
 * The Class FluidTimeSetting.
 */
@Getter
// 流動時間設定
public class FluidTimeSetting {

	/** The elapsed time. */
	// 経過時間
	private AttendanceTime elapsedTime;

	/** The rounding. */
	// 丸め
	private TimeRoundingSetting rounding;
}
