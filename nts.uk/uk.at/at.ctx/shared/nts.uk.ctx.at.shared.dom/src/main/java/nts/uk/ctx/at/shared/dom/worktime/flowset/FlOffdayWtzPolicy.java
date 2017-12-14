/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;

/**
 * The Interface FlOffdayWtzPolicy.
 */
public interface FlOffdayWtzPolicy {

	/**
	 * Validate.
	 *
	 * @param predTime the pred time
	 * @param flowOff the flow off
	 */
	void validate(PredetemineTimeSetting predTime, FlOffdayWtz flowOff);
}
