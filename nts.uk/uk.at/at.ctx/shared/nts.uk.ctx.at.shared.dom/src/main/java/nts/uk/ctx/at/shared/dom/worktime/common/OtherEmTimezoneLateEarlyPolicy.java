/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;

/**
 * The Interface OtherEmTimezoneLateEarlyPolicy.
 */
public interface OtherEmTimezoneLateEarlyPolicy {

	/**
	 * Valid late time.
	 *
	 * @param predTime the pred time
	 * @param otSet the ot set
	 */
	// 猶予時間設定 <= 所定時間設定．１日の範囲時間    ~ #Msg_517
	void validLateTime(PredetemineTimeSetting predTime, OtherEmTimezoneLateEarlySet otSet);
}
