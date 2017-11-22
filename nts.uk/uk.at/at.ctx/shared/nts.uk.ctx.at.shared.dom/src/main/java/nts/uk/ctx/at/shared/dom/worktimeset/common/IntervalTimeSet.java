/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktimeset.common;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class IntervalTimeSet.
 */
//インターバル時間設定
@Getter
public class IntervalTimeSet extends DomainObject {

	/** The use interval exemption time. */
	// インターバル免除時間を使用する
	private boolean useIntervalExemptionTime;

	/** The interval exemption time round. */
	// インターバル免除時間丸め
	private TimeRounding intervalExemptionTimeRound;

	/** The interval time. */
	// インターバル時間
	private IntervalTime intervalTime;

	/** The use interval time. */
	// インターバル時間を使用する
	private boolean useIntervalTime;
}
