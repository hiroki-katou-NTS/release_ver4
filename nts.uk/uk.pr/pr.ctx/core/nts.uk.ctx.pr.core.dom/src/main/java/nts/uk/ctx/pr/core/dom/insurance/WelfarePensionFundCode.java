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
@DecimalRange(min = "0", max = "9999")
public class WelfarePensionFundCode extends DecimalPrimitiveValue<WelfarePensionFundCode> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new welfare pension fund code.
	 *
	 * @param rawValue the raw value
	 */
	public WelfarePensionFundCode(BigDecimal rawValue) {
		super(rawValue);
	}

}