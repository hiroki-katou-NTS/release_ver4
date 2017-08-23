/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.shift.total.times.setting;

import nts.arc.primitive.TimeDurationPrimitiveValue;
import nts.arc.primitive.constraint.TimeRange;

/**
 * The Class ConditionThresholdLimit.
 */
@TimeRange(max = "24:00", min = "00:00")
public class ConditionThresholdLimit extends TimeDurationPrimitiveValue<ConditionThresholdLimit> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new attendence time.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public ConditionThresholdLimit(Long rawValue) {
		super(rawValue);
	}
}
