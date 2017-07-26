/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.budget.external.actualresult;

import nts.arc.primitive.TimeClockPrimitiveValue;
import nts.arc.primitive.constraint.TimeRange;

/**
 * The Class ExtBudgetTime.
 * 外部予算実績時間
 */
@TimeRange(min = "00:00", max = "999:59")
public class ExtBudgetTime extends TimeClockPrimitiveValue<ExtBudgetTime> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new ext budget time.
     *
     * @param rawValue the raw value
     */
    public ExtBudgetTime(Long rawValue) {
        super(rawValue);
    }

}
