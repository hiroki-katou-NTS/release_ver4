/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * The Class AnnualNumberDay.
 */
@IntegerRange(min = 0, max = 99)
public class AnnualNumberDay extends IntegerPrimitiveValue<AnnualNumberDay> {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new annual number day.
     *
     * @param rawValue the raw value
     */
    public AnnualNumberDay(Integer rawValue) {
        super(rawValue);
    }
}
