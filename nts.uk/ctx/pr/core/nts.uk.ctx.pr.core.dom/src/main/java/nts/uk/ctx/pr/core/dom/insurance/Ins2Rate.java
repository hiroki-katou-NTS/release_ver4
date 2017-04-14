/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class InsuranceAmount.
 */
@DecimalRange(min = "0", max = "999.99")
public class Ins2Rate extends DecimalPrimitiveValue<Ins2Rate> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new insurance amount.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public Ins2Rate(BigDecimal rawValue) {
		super(rawValue);
	}

}
