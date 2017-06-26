/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

/**
 * The Class Money.
 */
@DecimalRange(min = "-99999999.99", max = "99999999.99")
public class Money extends DecimalPrimitiveValue<Money> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new money.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public Money(BigDecimal rawValue) {
		super(rawValue);
	}

}
