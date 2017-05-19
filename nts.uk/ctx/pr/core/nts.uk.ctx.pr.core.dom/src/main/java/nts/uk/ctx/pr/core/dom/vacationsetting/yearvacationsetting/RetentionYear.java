/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.vacationsetting.yearvacationsetting;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * The Class RetentionYear.
 */
@IntegerRange(min = 0, max = 99)
public class RetentionYear extends IntegerPrimitiveValue<RetentionYear> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8879850157645403702L;

    /**
     * Instantiates a new retention year.
     *
     * @param rawValue the raw value
     */
    public RetentionYear(Integer rawValue) {
        super(rawValue);
    }
}
