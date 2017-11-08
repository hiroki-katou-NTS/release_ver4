/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.joggingworkset;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.StampReflectTimezone;

/**
 * The Class TimeDiffWorkStampReflectTimezone.
 */
// 時差勤務打刻反映時間帯
@Getter
public class TimeDiffWorkStampReflectTimezone {

	/** The stamp reflect timezone. */
	// 打刻反映時間帯
	private StampReflectTimezone stampReflectTimezone;

	/** The is update start time. */
	// 開始時刻に合わせて時刻を変動させる
	private boolean isUpdateStartTime;
}
