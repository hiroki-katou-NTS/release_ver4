/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

/**
 * The Class InsuranceAmount.
 */
@DecimalRange(min = "0.00", max = "9999999999.99")
public class InsuranceAmount extends DecimalPrimitiveValue<InsuranceAmount> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new insurance amount.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public InsuranceAmount(BigDecimal rawValue) {
		super(rawValue);
	}

}
