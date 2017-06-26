/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable;

import nts.arc.primitive.LongPrimitiveValue;
import nts.arc.primitive.constraint.LongMaxValue;
import nts.arc.primitive.constraint.LongMinValue;

/**
 * The Class WtAmount.
 */
@LongMinValue(value = -1000000000)
@LongMaxValue(value = 1000000000)
public class WtValue extends LongPrimitiveValue<WtValue> {

	/**
	 * Instantiates a new wage table code.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public WtValue(Long rawValue) {
		super(rawValue);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

}
